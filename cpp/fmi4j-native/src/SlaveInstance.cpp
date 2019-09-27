
#include <SlaveInstance.hpp>
#include <cppfmu_cs.hpp>
#include <cstring>
#include <iostream>
#include <jni.h>

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
    JavaVMInitArgs args;
    JavaVMOption options[1];
    options[0].optionString = _strdup(std::string("-Djava.class.path=" + classpath).c_str());
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
        std::cout << "Creating a new JVM. classpath=" << classpath << std::endl;
    } else {
        std::cout << "Unable to Launch JVM: " << rc << std::endl;
    }
    return env;
}

inline jmethodID GetMethodID(JNIEnv* env, jclass cls, const char* name, const char* sig) {
    auto id = env->GetMethodID(cls, name, sig);
    if (id == nullptr) {
        std::string msg = "Unable to locate method '" + std::string(name) + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }
}

} // namespace


namespace fmi4j
{

SlaveInstance::SlaveInstance(const cppfmu::Memory& memory, JNIEnv* env, const char* slaveClass)
{

    env->GetJavaVM(&jvm_);

    jclass cls = env->FindClass(slaveClass);
    if (cls == nullptr) {
        std::string msg = "Unable to locate slave class '" + std::string(slaveClass) + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }
//    jclass superCls = env->GetSuperclass(cls);
//    if (superCls == nullptr) {
//        std::string msg = "Unable to get super class of'" + std::string(slaveClass) + "'!";
//        throw cppfmu::FatalError(msg.c_str());
//    }

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

    setupExperimentId_ = GetMethodID(env, cls, "setupExperiment", "(D)Z");
    enterInitialisationModeId_ = GetMethodID(env, cls, "enterInitialisationMode", "()Z");
    exitInitializationModeId_ = GetMethodID(env, cls, "exitInitialisationMode", "()Z");

    doStepId_ = GetMethodID(env, cls, "doStep", "(DD)Z");
    resetId_ = GetMethodID(env, cls, "reset", "()Z");
    terminateId_ = GetMethodID(env, cls, "terminate", "()Z");

    getRealId_ = GetMethodID(env, cls, "getReal", "([J)[D");
    setRealId_ = GetMethodID(env, cls, "setReal", "([J[D)V");

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
        std::cout << "GetReal" << std::endl;
        for (int i = 0; i < nvr; i++) {
            value[i] = valueArrayElements[i];
        }

        free(vrArrayElements);
        env->ReleaseDoubleArrayElements(valueArray, valueArrayElements, 0);

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

    const char* mainClass = "no/ntnu/ihb/fmi4j/TestSlave";
    return cppfmu::AllocateUnique<fmi4j::SlaveInstance>(memory, memory, env, mainClass);
}
