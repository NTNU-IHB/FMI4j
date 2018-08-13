
#include <jni.h>
#include <stdio.h>
#include <dlfcn.h>
#include <stdlib.h>

#include "fmi2FunctionTypes.h"

void* lib;

void logger(void* fmi2ComponentEnvironment, fmi2String instanceName, fmi2Status status, fmi2String category, fmi2String message, ...) {
     printf("instanceName = %s, category = %s: %s\n", instanceName, category, message);
}

fmi2CallbackFunctions callback = {
    .logger = logger,
    .allocateMemory = calloc,
    .freeMemory = free,
    .stepFinished = NULL,
    .componentEnvironment = NULL
};

JNIEXPORT jboolean JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_load(JNIEnv *env, jobject obj, jstring libName) {

    const char *_libName = (*env)->GetStringUTFChars(env, libName, 0);
    lib = dlopen(_libName, RTLD_LAZY);
    (*env)->ReleaseStringUTFChars(env, libName, _libName);

    if (!lib) {
        fputs (dlerror(), stderr);
        return JNI_FALSE;
    }

    return JNI_TRUE;

}

JNIEXPORT void JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_close(JNIEnv *env, jobject obj) {
    if (lib) {
        dlclose(lib);
    }
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_setDebugLogging(JNIEnv *env, jobject obj, jlong c, jboolean loggingOn, jobjectArray categories) {

    jsize nCategories = (*env)->GetArrayLength(env, categories);
    const char* _categories[nCategories];

    for (int i = 0; i < nCategories; i++) {
        jstring str = (jstring) (*env)->GetObjectArrayElement(env, categories, i);
        _categories[i] = (*env)->GetStringUTFChars(env, str, NULL);
    }

    int (*fmi2SetDebugLogging)(void*, int, int, const char* []);
    fmi2SetDebugLogging = dlsym(lib, "fmi2SetDebugLogging");
    int status = (*fmi2SetDebugLogging)((void*) c, loggingOn == JNI_FALSE ? 0 : 1, nCategories, _categories);
    return status;
}

JNIEXPORT jstring JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_getFmiVersion(JNIEnv *env, jobject obj) {
    const char* (*fmi2GetVersion)(void);
    fmi2GetVersion = dlsym(lib, "fmi2GetVersion");
    const char* version = (*fmi2GetVersion)();
    return (*env)->NewStringUTF(env, version);
}

JNIEXPORT jstring JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_getTypesPlatform(JNIEnv *env, jobject obj) {
    const char* (*fmi2GetTypesPlatform)(void);
    fmi2GetTypesPlatform = dlsym(lib, "fmi2GetTypesPlatform");
    const char* platform = (*fmi2GetTypesPlatform)();
    return (*env)->NewStringUTF(env, platform);
}

JNIEXPORT jlong JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_instantiate(JNIEnv *env, jobject obj, jstring instanceName, jint type, jstring guid,  jstring resourceLocation, jboolean visible, jboolean loggingOn) {
    void* (*fmi2Instantiate)(fmi2String, fmi2Type, fmi2String, fmi2String, const fmi2CallbackFunctions*, fmi2Boolean, fmi2Boolean);

    const char* _instanceName = (*env)->GetStringUTFChars(env, instanceName, 0);
    const char* _guid = (*env)->GetStringUTFChars(env, guid, 0);
    const char* _resourceLocation = (*env)->GetStringUTFChars(env, resourceLocation, 0);

    fmi2Instantiate = dlsym(lib, "fmi2Instantiate");
    fmi2Component c = (*fmi2Instantiate)(_instanceName, type, _guid, _resourceLocation, &callback, visible == JNI_FALSE ? 0 : 1, loggingOn == JNI_FALSE ? 0 : 1);

    (*env)->ReleaseStringUTFChars(env, instanceName, _instanceName);
    (*env)->ReleaseStringUTFChars(env, guid, _guid);
    (*env)->ReleaseStringUTFChars(env, resourceLocation, _resourceLocation);

    return (jlong) c;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_setupExperiment(JNIEnv *env, jobject obj, jlong c, jboolean toleranceDefined, jdouble tolerance, jdouble startTime, jboolean stopTimeDefined, jdouble stopTime) {
    int (*fmi2SetupExperiment)(fmi2Component, fmi2Boolean, fmi2Real, fmi2Real, fmi2Boolean, fmi2Real);
    fmi2SetupExperiment = dlsym(lib, "fmi2SetupExperiment");
    int status = (*fmi2SetupExperiment)((void*) c, toleranceDefined == JNI_FALSE ? 0 : 1, tolerance, startTime, stopTimeDefined == JNI_FALSE ? 0 : 1, stopTime);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_enterInitializationMode(JNIEnv *env, jobject obj, jlong c) {
    int (*fmi2EnterInitializationMode)(fmi2Component);
    fmi2EnterInitializationMode = dlsym(lib, "fmi2EnterInitializationMode");
    int status = (*fmi2EnterInitializationMode)((void*) c);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_exitInitializationMode(JNIEnv *env, jobject obj, jlong c) {
    int (*fmi2ExitInitializationMode)(fmi2Component);
    fmi2ExitInitializationMode = dlsym(lib, "fmi2ExitInitializationMode");
    int status = (*fmi2ExitInitializationMode)((void*) c);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_step(JNIEnv *env, jobject obj, jlong c, jdouble currentCommunicationPoint, jdouble communicationStepSize, jboolean noSetFMUStatePriorToCurrentPoint) {
    int (*fmi2DoStep)(fmi2Component, fmi2Real, fmi2Real, fmi2Boolean);
    fmi2DoStep = dlsym(lib, "fmi2DoStep");
    int status = (*fmi2DoStep)((void*) c, currentCommunicationPoint, communicationStepSize, noSetFMUStatePriorToCurrentPoint == JNI_FALSE ? 0 : 1);
    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_terminate(JNIEnv *env, jobject obj, jlong c) {
    int (*fmi2Terminate)(fmi2Component);
    fmi2Terminate = dlsym(lib, "fmi2Terminate");
    int status = (*fmi2Terminate)((void*) c);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_reset(JNIEnv *env, jobject obj, jlong c) {
    int (*fmi2Reset)(fmi2Component);
    fmi2Reset = dlsym(lib, "fmi2Reset");
    int status = (*fmi2Reset)((void*) c);
    return status;
}

JNIEXPORT void JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_freeInstance(JNIEnv *env, jobject obj, jlong c) {
    void (*fmi2FreeInstance)(fmi2Component);
    fmi2FreeInstance = dlsym(lib, "fmi2FreeInstance");
    (*fmi2FreeInstance)((void*) c);
    return;
}

JNIEXPORT int JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_getInteger(JNIEnv *env, jobject obj, jlong c, jintArray vr, jintArray ref) {

    jsize size = (*env)->GetArrayLength(env, vr);
    jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2Status (*fmi2GetInteger)(fmi2Component, const fmi2ValueReference[], size_t, fmi2Integer   []);
    fmi2GetInteger = dlsym(lib, "fmi2GetInteger");

    jint _ref [size];
    int status = (*fmi2GetInteger)((void*) c, _vr, size, _ref);

    (*env)->SetIntArrayRegion(env, ref, 0, size, _ref);

    return status;
}

JNIEXPORT int JNICALL Java_no_mechatronics_sfi_fmi4j_jni_FmiLibrary_getReal(JNIEnv *env, jobject obj, jlong c, jintArray vr, jdoubleArray ref) {

    jsize size = (*env)->GetArrayLength(env, vr);
    jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2Status (*fmi2GetReal)(fmi2Component, const fmi2ValueReference[], size_t, fmi2Real   []);
    fmi2GetReal = dlsym(lib, "fmi2GetReal");

    jdouble _ref [size];
    int status = (*fmi2GetReal)((void*) c, _vr, size, _ref);

    (*env)->SetDoubleArrayRegion(env, ref, 0, size, _ref);

    return status;
}