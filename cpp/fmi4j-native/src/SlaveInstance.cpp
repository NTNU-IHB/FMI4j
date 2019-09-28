
#include <SlaveInstance.hpp>
#include <cppfmu_cs.hpp>
#include <cstring>
#include <fstream>
#include <iostream>
#include <jni.h>
#include <sstream>
#include <string>

namespace
{

void jvm_invoke(JavaVM* jvm, const std::function<void(JNIEnv*)>& f)
{
    JNIEnv* env;
    bool attach = false;
    int getEnvStat = jvm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_8);
    if (getEnvStat == JNI_EDETACHED) {
        attach = true;
        jvm->AttachCurrentThread(reinterpret_cast<void**>(&env), nullptr);
    }

    f(env);

    if (attach) {
        jvm->DetachCurrentThread();
    }
}

JNIEnv* create_jvm(JavaVM** jvm, const std::string& classpath)
{
    JNIEnv* env;

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

    JavaVMInitArgs args;
    JavaVMOption options[1];
    options[0].optionString = _strdup(std::string("-Djava.class.path=" + classpath + "/model.jar").c_str());
    args.version = JNI_VERSION_1_8;
    args.nOptions = 1;
    args.options = options;
    args.ignoreUnrecognized = false;

    rc = JNI_CreateJavaVM(jvm, (void**)&env, &args);
    if (rc == JNI_OK) {
        std::cout << "Creating a new JVM. classpath=" << classpath << std::endl;
    } else {
        std::cout << "Unable to Launch JVM: " << rc << std::endl;
    }
    return env;
}

inline jmethodID GetMethodID(JNIEnv* env, jclass cls, const char* name, const char* sig)
{
    auto id = env->GetMethodID(cls, name, sig);
    if (id == nullptr) {
        std::string msg = "Unable to locate method '" + std::string(name) + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }
}

inline jmethodID GetStaticMethodID(JNIEnv* env, jclass cls, const char* name, const char* sig)
{
    auto id = env->GetStaticMethodID(cls, name, sig);
    if (id == nullptr) {
        std::string msg = "Unable to locate method static '" + std::string(name) + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }
}

} // namespace


namespace fmi4j
{

SlaveInstance::SlaveInstance(const cppfmu::Memory& memory, JNIEnv* env, std::string slaveClass)
{

    env->GetJavaVM(&jvm_);

    jclass cls = env->FindClass(slaveClass.c_str());
    if (cls == nullptr) {
        std::string msg = "Unable to locate slave class '" + std::string(slaveClass) + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }

    jmethodID mid = env->GetMethodID(cls, "<init>", "()V");
    if (mid == nullptr) {
        std::string msg = "Unable to locate noargs constructor for slave class '" + std::string(slaveClass) + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }

    slave_ = env->NewObject(cls, mid);
    if (slave_ == nullptr) {
        std::string msg = "Unable to instantiate a new instance of '" + std::string(slaveClass) + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }

    env->CallObjectMethod(slave_, GetMethodID(env, cls, "define", "()Lno/ntnu/ihb/fmi4j/Fmi2Slave;"));

    setupExperimentId_ = GetMethodID(env, cls, "setupExperiment", "(D)Z");
    enterInitialisationModeId_ = GetMethodID(env, cls, "enterInitialisationMode", "()Z");
    exitInitializationModeId_ = GetMethodID(env, cls, "exitInitialisationMode", "()Z");

    doStepId_ = GetMethodID(env, cls, "doStep", "(DD)Z");
    resetId_ = GetMethodID(env, cls, "reset", "()Z");
    terminateId_ = GetMethodID(env, cls, "terminate", "()Z");

    getRealId_ = GetMethodID(env, cls, "getReal", "([J)[D");
    setRealId_ = GetMethodID(env, cls, "setReal", "([J[D)V");

    getIntegerId_ = GetMethodID(env, cls, "getInteger", "([J)[I");
    setIntegerId_ = GetMethodID(env, cls, "setInteger", "([J[I)V");

    getBooleanId_ = GetMethodID(env, cls, "getBoolean", "([J)[Z");
    setBooleanId_ = GetMethodID(env, cls, "setBoolean", "([J[Z)V");

    getStringId_ = GetMethodID(env, cls, "getString", "([J)[Ljava/lang/String;");
    setStringId_ = GetMethodID(env, cls, "setString", "([J[Ljava/lang/String;)V");
}

void SlaveInstance::SetupExperiment(cppfmu::FMIBoolean toleranceDefined, cppfmu::FMIReal tolerance, cppfmu::FMIReal tStart, cppfmu::FMIBoolean stopTimeDefined, cppfmu::FMIReal tStop)
{
    jvm_invoke(jvm_, [this, tStart](JNIEnv* env) {
        env->CallBooleanMethod(slave_, setupExperimentId_, tStart);
    });
}

void SlaveInstance::EnterInitializationMode()
{
    jvm_invoke(jvm_, [this](JNIEnv* env) {
        env->CallBooleanMethod(slave_, enterInitialisationModeId_);
    });
}

void SlaveInstance::ExitInitializationMode()
{
    jvm_invoke(jvm_, [this](JNIEnv* env) {
        env->CallBooleanMethod(slave_, exitInitializationModeId_);
    });
}

bool SlaveInstance::DoStep(cppfmu::FMIReal currentCommunicationPoint, cppfmu::FMIReal communicationStepSize, cppfmu::FMIBoolean newStep, cppfmu::FMIReal& endOfStep)
{
    bool status;
    jvm_invoke(jvm_, [this, &status, currentCommunicationPoint, communicationStepSize](JNIEnv* env) {
        status = env->CallBooleanMethod(slave_, doStepId_, currentCommunicationPoint, communicationStepSize);
    });
    return status;
}


void SlaveInstance::Reset()
{
    jvm_invoke(jvm_, [this](JNIEnv* env) {
        env->CallBooleanMethod(slave_, resetId_);
    });
}


void SlaveInstance::Terminate()
{
    jvm_invoke(jvm_, [this](JNIEnv* env) {
        env->CallBooleanMethod(slave_, terminateId_);
    });
}

void SlaveInstance::SetReal(const cppfmu::FMIValueReference* vr, std::size_t nvr, const cppfmu::FMIReal* value)
{
    jvm_invoke(jvm_, [this, vr, nvr, value](JNIEnv* env) {
        auto vrArray = env->NewLongArray(nvr);
        auto vrArrayElements = reinterpret_cast<jlong*>(malloc(sizeof(jlong) * nvr));

        auto valueArray = env->NewDoubleArray(nvr);
        auto valueArrayElements = reinterpret_cast<jdouble*>(malloc(sizeof(jdouble) * nvr));

        for (int i = 0; i < nvr; i++) {
            vrArrayElements[i] = static_cast<jlong>(vr[i]);
            valueArrayElements[i] = value[i];
        }

        env->SetLongArrayRegion(vrArray, 0, nvr, vrArrayElements);
        env->SetDoubleArrayRegion(valueArray, 0, nvr, valueArrayElements);

        env->CallVoidMethod(slave_, setRealId_, vrArray, valueArray);

        free(vrArrayElements);
        free(valueArrayElements);
    });
}

void SlaveInstance::SetInteger(const cppfmu::FMIValueReference* vr, std::size_t nvr, const cppfmu::FMIInteger* value)
{
    jvm_invoke(jvm_, [this, vr, nvr, value](JNIEnv* env) {
        auto vrArray = env->NewLongArray(nvr);
        auto vrArrayElements = reinterpret_cast<jlong*>(malloc(sizeof(jlong) * nvr));

        auto valueArray = env->NewIntArray(nvr);
        auto valueArrayElements = reinterpret_cast<jint*>(malloc(sizeof(jint) * nvr));

        for (int i = 0; i < nvr; i++) {
            vrArrayElements[i] = static_cast<jlong>(vr[i]);
            valueArrayElements[i] = static_cast<jint>(value[i]);
        }

        env->SetLongArrayRegion(vrArray, 0, nvr, vrArrayElements);
        env->SetIntArrayRegion(valueArray, 0, nvr, valueArrayElements);

        env->CallVoidMethod(slave_, setIntegerId_, vrArray, valueArray);

        free(vrArrayElements);
        free(valueArrayElements);
    });
}

void SlaveInstance::SetBoolean(const cppfmu::FMIValueReference* vr, std::size_t nvr, const cppfmu::FMIBoolean* value)
{
    jvm_invoke(jvm_, [this, vr, nvr, value](JNIEnv* env) {
        auto vrArray = env->NewLongArray(nvr);
        auto vrArrayElements = reinterpret_cast<jlong*>(malloc(sizeof(jlong) * nvr));

        auto valueArray = env->NewBooleanArray(nvr);
        auto valueArrayElements = reinterpret_cast<jboolean*>(malloc(sizeof(jboolean) * nvr));

        for (int i = 0; i < nvr; i++) {
            vrArrayElements[i] = static_cast<jlong>(vr[i]);
            valueArrayElements[i] = static_cast<jboolean>(value[i]);
        }

        env->SetLongArrayRegion(vrArray, 0, nvr, vrArrayElements);
        env->SetBooleanArrayRegion(valueArray, 0, nvr, valueArrayElements);

        env->CallVoidMethod(slave_, setBooleanId_, vrArray, valueArray);

        free(vrArrayElements);
        free(valueArrayElements);
    });
}

void SlaveInstance::SetString(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIString const* value)
{
    jvm_invoke(jvm_, [this, vr, nvr, value](JNIEnv* env) {
        auto vrArray = env->NewLongArray(nvr);
        auto vrArrayElements = reinterpret_cast<jlong*>(malloc(sizeof(jlong) * nvr));

        auto valueArray = env->NewObjectArray(nvr, env->FindClass("java/lang/String"), nullptr);

        for (int i = 0; i < nvr; i++) {
            vrArrayElements[i] = static_cast<jlong>(vr[i]);
            env->SetObjectArrayElement(valueArray, i, env->NewStringUTF(value[i]));
        }

        env->SetLongArrayRegion(vrArray, 0, nvr, vrArrayElements);

        env->CallVoidMethod(slave_, setStringId_, vrArray, valueArray);

        free(vrArrayElements);
    });
}

void SlaveInstance::GetReal(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIReal* value) const
{
    jvm_invoke(jvm_, [this, vr, nvr, value](JNIEnv* env) {
        auto vrArray = env->NewLongArray(nvr);
        auto vrArrayElements = reinterpret_cast<jlong*>(malloc(sizeof(jlong) * nvr));

        for (int i = 0; i < nvr; i++) {
            vrArrayElements[i] = static_cast<jlong>(vr[i]);
        }

        env->SetLongArrayRegion(vrArray, 0, nvr, vrArrayElements);

        auto valueArray = reinterpret_cast<jdoubleArray>(env->CallObjectMethod(slave_, getRealId_, vrArray));
        auto valueArrayElements = env->GetDoubleArrayElements(valueArray, nullptr);

        for (int i = 0; i < nvr; i++) {
            value[i] = valueArrayElements[i];
        }

        free(vrArrayElements);
        env->ReleaseDoubleArrayElements(valueArray, valueArrayElements, 0);
    });
}


void SlaveInstance::GetInteger(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIInteger* value) const
{
    jvm_invoke(jvm_, [this, vr, nvr, value](JNIEnv* env) {
        auto vrArray = env->NewLongArray(nvr);
        auto vrArrayElements = reinterpret_cast<jlong*>(malloc(sizeof(jlong) * nvr));

        for (int i = 0; i < nvr; i++) {
            vrArrayElements[i] = static_cast<jlong>(vr[i]);
        }

        env->SetLongArrayRegion(vrArray, 0, nvr, vrArrayElements);

        auto valueArray = reinterpret_cast<jintArray>(env->CallObjectMethod(slave_, getIntegerId_, vrArray));
        auto valueArrayElements = env->GetIntArrayElements(valueArray, nullptr);

        for (int i = 0; i < nvr; i++) {
            value[i] = static_cast<fmi2Integer>(valueArrayElements[i]);
        }

        free(vrArrayElements);
        env->ReleaseIntArrayElements(valueArray, valueArrayElements, 0);
    });
}

void SlaveInstance::GetBoolean(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIBoolean* value) const
{
    jvm_invoke(jvm_, [this, vr, nvr, value](JNIEnv* env) {
        auto vrArray = env->NewLongArray(nvr);
        auto vrArrayElements = reinterpret_cast<jlong*>(malloc(sizeof(jlong) * nvr));

        for (int i = 0; i < nvr; i++) {
            vrArrayElements[i] = static_cast<jlong>(vr[i]);
        }

        env->SetLongArrayRegion(vrArray, 0, nvr, vrArrayElements);

        auto valueArray = reinterpret_cast<jbooleanArray>(env->CallObjectMethod(slave_, getBooleanId_, vrArray));
        auto valueArrayElements = env->GetBooleanArrayElements(valueArray, nullptr);

        for (int i = 0; i < nvr; i++) {
            value[i] = static_cast<fmi2Boolean>(valueArrayElements[i]);
        }

        free(vrArrayElements);
        env->ReleaseBooleanArrayElements(valueArray, valueArrayElements, 0);
    });
}

void SlaveInstance::GetString(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIString* value) const
{

    jvm_invoke(jvm_, [this, vr, nvr, value](JNIEnv* env) {
        auto vrArray = env->NewLongArray(nvr);
        auto vrArrayElements = reinterpret_cast<jlong*>(malloc(sizeof(jlong) * nvr));

        for (int i = 0; i < nvr; i++) {
            vrArrayElements[i] = static_cast<jlong>(vr[i]);
        }

        env->SetLongArrayRegion(vrArray, 0, nvr, vrArrayElements);

        auto valueArray = reinterpret_cast<jobjectArray>(env->CallObjectMethod(slave_, getStringId_, vrArray));

        for (int i = 0; i < nvr; i++) {
            auto jstr = reinterpret_cast<jstring>(env->GetObjectArrayElement(valueArray, i));
            auto cStr = env->GetStringUTFChars(jstr, nullptr);
            value[i] = cStr;
            env->ReleaseStringUTFChars(jstr, cStr);
        }

        free(vrArrayElements);
    });
}

SlaveInstance::~SlaveInstance()
{
    jvm_invoke(jvm_, [this](JNIEnv* env) {
        env->DeleteGlobalRef(slave_);
    });
}

} // namespace fmi4j

cppfmu::UniquePtr<cppfmu::SlaveInstance> CppfmuInstantiateSlave(
    cppfmu::FMIString instanceName,
    cppfmu::FMIString fmuGUID,
    cppfmu::FMIString fmuResourceLocation,
    cppfmu::FMIString mimeType,
    cppfmu::FMIReal timeout,
    cppfmu::FMIBoolean visible,
    cppfmu::FMIBoolean interactive,
    cppfmu::Memory memory,
    cppfmu::Logger logger)
{

    JNIEnv* env;
    JavaVM* jvm;
    env = create_jvm(&jvm, std::string(fmuResourceLocation));

    if (env == nullptr) {
        throw cppfmu::FatalError("Unable to setup the JVM!");
    }

    std::string mainClass;
    std::ifstream infile(std::string(fmuResourceLocation) + "/mainclass.txt");
    std::getline(infile, mainClass);

    std::replace(mainClass.begin(), mainClass.end(), '.', '/');

    return cppfmu::AllocateUnique<fmi4j::SlaveInstance>(memory, memory, env, mainClass);
}
