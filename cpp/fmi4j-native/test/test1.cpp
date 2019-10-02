
#include <iostream>
#include <jni.h>

#include <cstring>

namespace {

JNIEnv* create_jvm(JavaVM** jvm, const std::string& classpath)
{
    JNIEnv* env;
    JavaVMInitArgs args;
    JavaVMOption options[1];
    options[0].optionString = _strdup(std::string( "-Djava.class.path=" + classpath).c_str());
    args.version = JNI_VERSION_1_8;
    args.nOptions = 1;
    args.options = options;
    args.ignoreUnrecognized = false;

    jint rc;
    jsize nVms;
    rc = JNI_GetCreatedJavaVMs(jvm, 1, &nVms);
    if (rc == JNI_OK && nVms == 1) {
        rc = (*jvm)->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_8);
        if (rc == JNI_OK) {
            std::cout << "Reusing already created JMV" << std::endl;
            return env;
        }
    }
    rc = JNI_CreateJavaVM(jvm, (void**)&env, &args);
    if (rc == JNI_OK) {
        std::cout << "Creating a new JVM." << std::endl;
    } else {
        std::cout << "Unable to Launch JVM: " << rc << std::endl;
    }
    return env;
}

}

int main(int argc, char** argv)
{

    JavaVM* jvm; /* denotes a Java VM */
    JNIEnv* env; /* pointer to native method interface */
    env = create_jvm(&jvm, R"(D:\Development\FMI4j\java\fmi4j\fmu-slaves\build\libs\fmu-slaves.jar)");

    /* invoke the Main.test method using the JNI */
    jclass cls = env->FindClass("no/ntnu/ihb/fmi4j/JavaTestSlave");
    std::cout << env->GetMethodID(cls, "setupExperiment", "(D)Z") << std::endl;

    jvm->DestroyJavaVM();

    return 0;
}
