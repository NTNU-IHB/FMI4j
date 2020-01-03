
#include <fmi4j/SlaveInstance.hpp>
#include <fmi4j/jni_helper.hpp>

#include <cppfmu/cppfmu_cs.hpp>

#include <fstream>
#include <iostream>
#include <jni.h>
#include <string>

namespace fmi4j
{

#ifdef _MSC_VER
#    pragma warning(push)
#    pragma warning(disable : 4267) //conversion from 'size_t' to 'jsize', possible loss of data
#endif

SlaveInstance::SlaveInstance(
    const cppfmu::Memory& memory,
    JNIEnv* env,
    const std::string& instanceName,
    const std::string& resources)
{

    env->GetJavaVM(&jvm_);

    std::string slaveName;
    std::ifstream infile(resources + "/mainclass.txt");
    std::getline(infile, slaveName);

    std::string classpath = "file:/" + resources + "/model.jar";
    classLoader_ = env->NewGlobalRef(create_classloader(env, classpath));

    jclass slaveCls = FindClass(env, classLoader_, slaveName.c_str());
    if (slaveCls == nullptr) {
        std::string msg = "Unable to locate slave class '" + slaveName + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }

    jmethodID mid = env->GetMethodID(slaveCls, "<init>", "(Ljava/lang/String;)V");
    if (mid == nullptr) {
        std::string msg = "Unable to locate 1 arg constructor that takes a String for slave class '" + slaveName + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }

    slave_ = env->NewGlobalRef(env->NewObject(slaveCls, mid, env->NewStringUTF(instanceName.c_str())));
    if (slave_ == nullptr) {
        std::string msg = "Unable to instantiate a new instance of '" + slaveName + "'!";
        throw cppfmu::FatalError(msg.c_str());
    }

    env->SetObjectField(slave_, GetFieldID(env, slaveCls, "resourceLocation", "Ljava/lang/String;"), env->NewStringUTF(resources.c_str()));
    env->CallObjectMethod(slave_, GetMethodID(env, slaveCls, "define", "()Lno/ntnu/ihb/fmi4j/export/fmi2/Fmi2Slave;"));

    setupExperimentId_ = GetMethodID(env, slaveCls, "setupExperiment", "(D)V");
    enterInitialisationModeId_ = GetMethodID(env, slaveCls, "enterInitialisationMode", "()V");
    exitInitializationModeId_ = GetMethodID(env, slaveCls, "exitInitialisationMode", "()V");

    doStepId_ = GetMethodID(env, slaveCls, "doStep", "(DD)V");
    resetId_ = GetMethodID(env, slaveCls, "reset", "()V");
    terminateId_ = GetMethodID(env, slaveCls, "terminate", "()V");

    getRealId_ = GetMethodID(env, slaveCls, "getReal", "([J)[D");
    setRealId_ = GetMethodID(env, slaveCls, "setReal", "([J[D)V");

    getIntegerId_ = GetMethodID(env, slaveCls, "getInteger", "([J)[I");
    setIntegerId_ = GetMethodID(env, slaveCls, "setInteger", "([J[I)V");

    getBooleanId_ = GetMethodID(env, slaveCls, "getBoolean", "([J)[Z");
    setBooleanId_ = GetMethodID(env, slaveCls, "setBoolean", "([J[Z)V");

    getStringId_ = GetMethodID(env, slaveCls, "getString", "([J)[Ljava/lang/String;");
    setStringId_ = GetMethodID(env, slaveCls, "setString", "([J[Ljava/lang/String;)V");
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

        jclass URLClassLoader = env->FindClass("java/net/URLClassLoader");
        jmethodID closeId = env->GetMethodID(URLClassLoader, "close", "()V");
        env->CallVoidMethod(classLoader_, closeId);

        env->DeleteGlobalRef(classLoader_);
    });
}

#ifdef _MSC_VER
#    pragma warning(pop)
#endif

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

    std::string resources = std::string(fmuResourceLocation);
    auto find = resources.find("file:///");
    if (find != std::string::npos) {
        resources.replace(find, 8, "");
    }

    JNIEnv* env;
    JavaVM* jvm;
    env = get_or_create_jvm(&jvm);

    if (env == nullptr) {
        throw cppfmu::FatalError("Unable to setup the JVM!");
    }

    return cppfmu::AllocateUnique<fmi4j::SlaveInstance>(memory, memory, env, instanceName, resources);
}
