/*
 * The MIT License
 *
 * Copyright 2017-2018 Norwegian University of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "fmi2FunctionTypes.h"

#if defined(_MSC_VER) || defined(WIN32) || defined(__MINGW32__)
#include <windows.h>
#define DLL_HANDLE HMODULE
#else
#define DLL_HANDLE void*
#include <dlfcn.h>
#endif

#ifdef WIN32
#define function_ptr FARPROC
#else
typedef void* function_ptr; 
#endif

struct fmu_t {

    DLL_HANDLE handle;

//    fmi2GetVersionTYPE* fmi2GetVersion;
//    fmi2GetTypesPlatformTYPE* fmi2GetTypesPlatform;
//
//    fmi2InstantiateTYPE* fmi2Instantiate;
//    fmi2SetDebugLoggingTYPE* fmi2SetDebugLogging;
//    fmi2SetupExperimentTYPE* fmi2SetupExperiment;
//    fmi2EnterInitializationModeTYPE* fmi2EnterInitializationMode;
//    fmi2ExitInitializationModeTYPE* fmi2ExitInitializationMode;
//
//    fmi2TerminateTYPE* fmi2Terminate;
//    fmi2ResetTYPE* fmi2Reset;
//
//    fmi2GetIntegerTYPE* fmi2GetInteger;
//    fmi2GetRealTYPE* fmi2GetReal;
//    fmi2GetStringTYPE* fmi2GetString;
//    fmi2GetBooleanTYPE* fmi2GetBoolean;
//
//    fmi2SetIntegerTYPE* fmi2SetInteger;
//    fmi2SetRealTYPE* fmi2SetReal;
//    fmi2SetStringTYPE* fmi2SetString;
//    fmi2SetBooleanTYPE* fmi2SetBoolean;
//
//    fmi2GetFMUstateTYPE* fmi2GetFMUstate;
//    fmi2SetFMUstateTYPE* fmi2SetFMUstate;
//    fmi2FreeFMUstateTYPE* fmi2FreeFMUstate;
//
//    fmi2SerializedFMUstateSizeTYPE* fmi2SerializedFMUstateSize;
//    fmi2SerializeFMUstateTYPE* fmi2SerializeFMUstate;
//    fmi2DeSerializeFMUstateTYPE* fmi2DeSerializeFMUstate;
//
//    fmi2GetDirectionalDerivativeTYPE* fmi2GetDirectionalDerivative;
//
    fmi2DoStepTYPE* fmi2DoStep;
//    fmi2CancelStepTYPE* fmi2CancelStep;
//    fmi2GetRealOutputDerivativesTYPE* fmi2GetRealOutputDerivatives;
//    fmi2SetRealInputDerivativesTYPE* fmi2SetRealInputDerivatives;
//
//    fmi2EnterEventModeTYPE* fmi2EnterEventMode;
//    fmi2EnterContinuousTimeModeTYPE* fmi2EnterContinuousTimeMode;
//    fmi2SetTimeTYPE* fmi2SetTime;
//    fmi2SetContinuousStatesTYPE* fmi2SetContinuousStates;
//    fmi2GetDerivativesTYPE* fmi2GetDerivatives;
//    fmi2GetEventIndicatorsTYPE* fmi2GetEventIndicators;
//    fmi2GetContinuousStatesTYPE* fmi2GetContinuousStates;
//    fmi2GetNominalsOfContinuousStatesTYPE* fmi2GetNominalsOfContinuousStates;
//    fmi2CompletedIntegratorStepTYPE* fmi2CompletedIntegratorStep;
//    fmi2NewDiscreteStatesTYPE* fmi2NewDiscreteStates;

};

static function_ptr* load_function(DLL_HANDLE handle, const char* function_name) {
#ifdef WIN32
    return (function_ptr*) GetProcAddress(handle, function_name);
#else
    return dlsym(handle, function_name);
#endif
}

const char* status_to_string(fmi2Status status) {
    switch (status){
        case 0: return "OK";
        case 1: return "Warning";
        case 2: return "Discard";
        case 3: return "Error";
        case 4: return "Fatal";
        case 5: return "Pending";
        default: return "Unknown";
    }
}

void logger(void* fmi2ComponentEnvironment, fmi2String instance_name, fmi2Status status, fmi2String category, fmi2String message, ...) {
    printf("status = %s, instanceName = %s, category = %s: %s\n", status_to_string(status), instance_name, category, message);
}

fmi2CallbackFunctions callback = {
    .logger = logger,
    .allocateMemory = calloc,
    .freeMemory = free,
    .stepFinished = NULL,
    .componentEnvironment = NULL
};

JNIEXPORT jlong JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_load(JNIEnv *env, jobject obj, jstring lib_name) {

    struct fmu_t* fmu = malloc(sizeof(struct fmu_t));

    const char* _lib_name = (*env)->GetStringUTFChars(env, lib_name, 0);
    #ifdef WIN32
    	fmu->handle = LoadLibrary(_lib_name);
    #else
    	fmu->handle = dlopen(_lib_name, RTLD_NOW|RTLD_LOCAL);
    #endif

    (*env)->ReleaseStringUTFChars(env, lib_name, _lib_name);

    return (jlong) fmu;
}

JNIEXPORT jstring JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_getVersion(JNIEnv *env, jobject obj, jlong p) {
    struct fmu_t* fmu = (struct  fmu_t*) p;
    fmi2GetVersionTYPE* fmi2GetVersion = load_function(fmu->handle, "fmi2GetVersion");
    const char* version = (*fmi2GetVersion)();
    return (*env)->NewStringUTF(env, version);
}

JNIEXPORT jstring JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_getTypesPlatform(JNIEnv *env, jobject obj, jlong p) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2GetTypesPlatformTYPE* fmi2GetTypesPlatform = load_function(fmu->handle, "fmi2GetTypesPlatform");
    const char* platform = (*fmi2GetTypesPlatform)();
    return (*env)->NewStringUTF(env, platform);
}

JNIEXPORT jlong JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_instantiate(JNIEnv *env, jobject obj, jlong p, jstring instanceName, jint type, jstring guid,  jstring resourceLocation, jboolean visible, jboolean loggingOn) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const char* _instanceName = (*env)->GetStringUTFChars(env, instanceName, 0);
    const char* _guid = (*env)->GetStringUTFChars(env, guid, 0);
    const char* _resourceLocation = (*env)->GetStringUTFChars(env, resourceLocation, 0);

    fmi2InstantiateTYPE* fmi2Instantiate = load_function(fmu->handle, "fmi2Instantiate");
    fmi2Component c = (*fmi2Instantiate)(_instanceName, type, _guid, _resourceLocation, &callback, (fmi2Boolean) visible, (fmi2Boolean) loggingOn);

    (*env)->ReleaseStringUTFChars(env, instanceName, _instanceName);
    (*env)->ReleaseStringUTFChars(env, guid, _guid);
    (*env)->ReleaseStringUTFChars(env, resourceLocation, _resourceLocation);

    return (jlong) c;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_setDebugLogging(JNIEnv *env, jobject obj, jlong p, jlong c, jboolean loggingOn, jobjectArray categories) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize nCategories = (*env)->GetArrayLength(env, categories);
    char* _categories = malloc(sizeof(char) * nCategories);

    for (int i = 0; i < nCategories; i++) {
        jstring str = (jstring) (*env)->GetObjectArrayElement(env, categories, i);
        _categories[i] = (*env)->GetStringUTFChars(env, str, NULL);
    }

    fmi2SetDebugLoggingTYPE* fmi2SetDebugLogging = load_function(fmu->handle, "fmi2SetDebugLogging");
    fmi2Status status = (*fmi2SetDebugLogging)((void*) c, (fmi2Boolean) loggingOn, nCategories, _categories);

    free(_categories);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_setupExperiment(JNIEnv *env, jobject obj, jlong p, jlong c, jboolean toleranceDefined, jdouble tolerance, jdouble startTime, jdouble stopTime) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2Boolean stopTimeDefined = stopTime > startTime ? 1 : 0;
    fmi2SetupExperimentTYPE* fmi2SetupExperiment = load_function(fmu->handle, "fmi2SetupExperiment");
    return (*fmi2SetupExperiment)((void*) c, (fmi2Boolean) toleranceDefined, tolerance, startTime, stopTimeDefined, stopTime);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_enterInitializationMode(JNIEnv *env, jobject obj, jlong p, jlong c) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2EnterInitializationModeTYPE* fmi2EnterInitializationMode = load_function(fmu->handle, "fmi2EnterInitializationMode");
    return (*fmi2EnterInitializationMode)((void*) c);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_exitInitializationMode(JNIEnv *env, jobject obj, jlong p, jlong c) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2ExitInitializationModeTYPE* fmi2ExitInitializationMode = load_function(fmu->handle, "fmi2ExitInitializationMode");
    return (*fmi2ExitInitializationMode)((void*) c);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_terminate(JNIEnv *env, jobject obj, jlong p, jlong c) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2TerminateTYPE* fmi2Terminate = load_function(fmu->handle, "fmi2Terminate");
    return (*fmi2Terminate)((void*) c);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_reset(JNIEnv *env, jobject obj, jlong p, jlong c) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2ResetTYPE* fmi2Reset = load_function(fmu->handle, "fmi2Reset");
    return (*fmi2Reset)((void*) c);
}

fmi2FreeInstanceTYPE* fmi2FreeInstance;
JNIEXPORT void JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_freeInstance(JNIEnv *env, jobject obj, jlong p, jlong c) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2FreeInstanceTYPE* fmi2FreeInstance = load_function(fmu->handle, "fmi2FreeInstance");
    (*fmi2FreeInstance)((void*) c);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_getInteger(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jintArray ref) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2GetIntegerTYPE* fmi2GetInteger = load_function(fmu->handle, "fmi2GetInteger");

    fmi2Integer* _ref = malloc(sizeof(fmi2Integer) * size);
    fmi2Status status = (*fmi2GetInteger)((void*) c, _vr, size, _ref);

    (*env)->SetIntArrayRegion(env, ref, 0, size, _ref);
    free(_ref);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_getReal(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jdoubleArray ref) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2GetRealTYPE* fmi2GetReal = load_function(fmu->handle, "fmi2GetReal");

    fmi2Real* _ref = malloc(sizeof(fmi2Real) * size);
    fmi2Status status = (*fmi2GetReal)((void*) c, _vr, size, _ref);

    (*env)->SetDoubleArrayRegion(env, ref, 0, size, _ref);

    free(_ref);
    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_getString(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jobjectArray ref) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2GetStringTYPE* fmi2GetString = load_function(fmu->handle, "fmi2GetString");

    char* _ref = malloc(sizeof(char) * size);
    for (int i = 0; i < size; i++) {
        jstring str = (jstring) (*env)->GetObjectArrayElement(env, ref, i);
        _ref[i] = (*env)->GetStringUTFChars(env, str, NULL);
    }

    fmi2Status status = (*fmi2GetString)((void*) c, _vr, size, _ref);

    for (int i = 0; i < size; i++) {
        jstring value = (*env)->NewStringUTF(env, _ref[i]);
        (*env)->SetObjectArrayElement(env, ref, i, value);
    }

    free(_ref);
    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_getBoolean(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jbooleanArray ref) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2Boolean* _ref = malloc(sizeof(fmi2Boolean) * size);

    fmi2GetBooleanTYPE* fmi2GetBoolean = load_function(fmu->handle, "fmi2GetBoolean");
    fmi2Status status = (*fmi2GetBoolean)((void*) c, _vr, size, _ref);

    (*env)->SetBooleanArrayRegion(env, ref, 0, size, _ref);

    free(_ref);
    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_setInteger(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jintArray values) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jint *_values = (*env)->GetIntArrayElements(env, values, 0);

    fmi2SetIntegerTYPE* fmi2SetInteger = load_function(fmu->handle, "fmi2SetInteger");
    fmi2Status status = (*fmi2SetInteger)((void*) c, _vr, size, _values);

    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);
    (*env)->ReleaseIntArrayElements(env, values, _values, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_setReal(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jdoubleArray values) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jdouble *_values = (*env)->GetDoubleArrayElements(env, values, 0);

    fmi2SetRealTYPE* fmi2SetReal = load_function(fmu->handle, "fmi2SetReal");
    fmi2Status status = (*fmi2SetReal)((void*) c, _vr, size, _values);

    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);
    (*env)->ReleaseDoubleArrayElements(env, values, _values, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_setString(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jobjectArray values) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    char* _values = malloc(sizeof(char) * size);
    for (int i = 0; i < size; i++) {
       jstring str = (jstring) (*env)->GetObjectArrayElement(env, values, i);
       _values[i] = (*env)->GetStringUTFChars(env, str, NULL);
    }

    fmi2SetStringTYPE* fmi2SetString = load_function(fmu->handle, "fmi2SetString");
    fmi2Status status = (*fmi2SetString)((void*) c, _vr, size, _values);

    free(_values);
    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_setBoolean(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jbooleanArray values) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jboolean *_values = (*env)->GetBooleanArrayElements(env, values, 0);

    fmi2SetBooleanTYPE* fmi2SetBoolean = load_function(fmu->handle, "fmi2SetBoolean");
    fmi2Status status = (*fmi2SetBoolean)((void*) c, _vr, size, _values);

    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);
    (*env)->ReleaseBooleanArrayElements(env, values, _values, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_getFMUstate(JNIEnv *env, jobject obj, jlong p, jlong c, jobject state) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    jclass cls = (*env)->FindClass(env, "no/mechatronics/sfi/fmi4j/importer/jni/FmuState");
    jfieldID id = (*env)->GetFieldID(env, cls, "pointer", "J");

    fmi2FMUstate _state;
    fmi2GetFMUstateTYPE* fmi2GetFMUstate = load_function(fmu->handle, "fmi2GetFMUstate");
    fmi2Status status = (*fmi2GetFMUstate)((void*) c, &_state);

    (*env)->SetLongField(env, state, id, (jlong) _state);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_setFMUstate(JNIEnv *env, jobject obj, jlong p, jlong c, jlong state) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2SetFMUstateTYPE* fmi2SetFMUstate = load_function(fmu->handle, "fmi2SetFMUstate");
    return (*fmi2SetFMUstate)((void*) c, (fmi2FMUstate) state);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_freeFMUstate(JNIEnv *env, jobject obj, jlong p, jlong c, jlong state) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2FreeFMUstateTYPE* fmi2FreeFMUstate = load_function(fmu->handle, "fmi2FreeFMUstate");
    fmi2FMUstate _state = (fmi2FMUstate) state;
    return (*fmi2FreeFMUstate)((void*) c, &_state);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_serializedFMUstateSize(JNIEnv *env, jobject obj, jlong p, jlong c, jlong state, jobject size) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    jclass size_cls = (*env)->FindClass(env, "no/mechatronics/sfi/fmi4j/importer/jni/IntByReference");
    jfieldID size_id = (*env)->GetFieldID(env, size_cls, "value", "I");

    size_t _size;
    fmi2SerializedFMUstateSizeTYPE* fmi2SerializedFMUstateSize = load_function(fmu->handle, "fmi2SerializedFMUstateSize");
    fmi2Status status = (*fmi2SerializedFMUstateSize)((void*) c, (fmi2FMUstate) state, &_size);

    (*env)->SetIntField(env, size, size_id, (jint) _size);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_serializeFMUstate(JNIEnv *env, jobject obj, jlong p, jlong c, jlong state, jbyteArray serializedState) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, serializedState);
    fmi2Byte *_serializedState = malloc( sizeof(fmi2Byte) * size );

    fmi2SerializeFMUstateTYPE* fmi2SerializeFMUstate = load_function(fmu->handle, "fmi2SerializeFMUstate");
    fmi2Status status = (*fmi2SerializeFMUstate)((void*) c, (fmi2FMUstate) state, _serializedState, size);

    (*env)->SetByteArrayRegion(env, serializedState, 0, size, _serializedState);
    free(_serializedState);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_deSerializeFMUstate(JNIEnv *env, jobject obj, jlong p, jlong c, jobject state, const jbyteArray serializedState) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    jclass cls = (*env)->FindClass(env, "no/mechatronics/sfi/fmi4j/importer/jni/FmuState");
    jfieldID id = (*env)->GetFieldID(env, cls, "pointer", "J");

    const jsize size = (*env)->GetArrayLength(env, serializedState);
    jbyte *_serializedState = (*env)->GetByteArrayElements(env, serializedState, 0);

    fmi2DeSerializeFMUstateTYPE* fmi2DeSerializeFMUstate = load_function(fmu->handle, "fmi2DeSerializeFMUstate");

    fmi2FMUstate _state = (fmi2FMUstate) (*env)->GetLongField(env, state, id);
    fmi2Status status = (*fmi2DeSerializeFMUstate)((void*) c, _serializedState, size, _state);

    (*env)->SetLongField(env, state, id, (jlong) _state);

    (*env)->ReleaseByteArrayElements(env, serializedState, _serializedState, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_getDirectionalDerivative(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vUnknown_ref, jintArray vKnown_ref, jdoubleArray dvKnown_ref, jdoubleArray dvUnknown_ref) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize nUknown = (*env)->GetArrayLength(env, vUnknown_ref);
    const jsize nKnown = (*env)->GetArrayLength(env, vUnknown_ref);

    const jint *_vUnknown_ref = (*env)->GetIntArrayElements(env, vUnknown_ref, 0);
    const jint *_vKnown_ref = (*env)->GetIntArrayElements(env, vKnown_ref, 0);
    const jdouble *_dvKnown_ref = (*env)->GetDoubleArrayElements(env, vKnown_ref, 0);
    const jdouble *_dvUnknown_ref = (*env)->GetDoubleArrayElements(env, vUnknown_ref, 0);

    fmi2GetDirectionalDerivativeTYPE* fmi2GetDirectionalDerivative = load_function(fmu->handle, "fmi2GetDirectionalDerivative");
    fmi2Status status = (*fmi2GetDirectionalDerivative)((void*) c, _vUnknown_ref, nUknown, _vKnown_ref, nKnown, _dvKnown_ref, _dvUnknown_ref);

    (*env)->ReleaseIntArrayElements(env, vUnknown_ref, _vUnknown_ref, NULL);
    (*env)->ReleaseIntArrayElements(env, vKnown_ref, _vKnown_ref, NULL);

    (*env)->ReleaseDoubleArrayElements(env, dvKnown_ref, _dvKnown_ref, NULL);
    (*env)->ReleaseDoubleArrayElements(env, dvUnknown_ref, _dvUnknown_ref, NULL);

    return status;
}

JNIEXPORT jboolean JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2Library_free(JNIEnv *env, jobject obj, jlong p) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    jboolean status;
    if (fmu->handle) {
        #ifdef WIN32
            status = FreeLibrary(fmu->handle);
        #else
            status = dlclose(fmu->handle) == 0;
        #endif
        fmu->handle = NULL;
        fmu->fmi2DoStep = NULL;
    }
    free(fmu);

    return status;
}

/***************************************************
Functions for FMI2 for Co-Simulation
****************************************************/
JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2CoSimulationLibrary_step(JNIEnv *env, jobject obj, jlong p, jlong c, jdouble currentCommunicationPoint, jdouble communicationStepSize, jboolean noSetFMUStatePriorToCurrentPoint) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2DoStepTYPE* fmi2DoStep = load_function(fmu->handle, "fmi2DoStep");
    return (*fmi2DoStep)((void*) c, currentCommunicationPoint, communicationStepSize, noSetFMUStatePriorToCurrentPoint);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2CoSimulationLibrary_cancelStep(JNIEnv *env, jobject obj, jlong p, jlong c) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2CancelStepTYPE* fmi2CancelStep = load_function(fmu->handle, "fmi2CancelStep");
    return (*fmi2CancelStep)((void*) c);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2CoSimulationLibrary_setRealInputDerivatives(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jintArray order, jdoubleArray value) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize nvr = (*env)->GetArrayLength(env, vr);

    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jint *_order = (*env)->GetIntArrayElements(env, order, 0);
    const jdouble *_value = (*env)->GetDoubleArrayElements(env, value, 0);

    fmi2SetRealInputDerivativesTYPE* fmi2SetRealInputDerivatives = load_function(fmu->handle, "fmi2SetRealInputDerivatives");
    fmi2Status status = (*fmi2SetRealInputDerivatives)((void*) c, _vr, nvr, _order, _value);

    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);
    (*env)->ReleaseIntArrayElements(env, order, _order, NULL);

    (*env)->ReleaseDoubleArrayElements(env, value, _value, NULL);

    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2CoSimulationLibrary_getRealOutputDerivatives(JNIEnv *env, jobject obj, jlong p, jlong c, jintArray vr, jintArray order, jdoubleArray value) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize nvr = (*env)->GetArrayLength(env, vr);

    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jint *_order = (*env)->GetIntArrayElements(env, order, 0);
    fmi2Real *_value = malloc(sizeof(fmi2Real) * nvr);

    fmi2GetRealOutputDerivativesTYPE* fmi2GetRealOutputDerivatives = load_function(fmu->handle, "fmi2GetRealOutputDerivatives");
    fmi2Status status = (*fmi2GetRealOutputDerivatives)((void*) c, _vr, nvr, _order, _value);

    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);
    (*env)->ReleaseIntArrayElements(env, order, _order, NULL);

    (*env)->SetDoubleArrayRegion(env, value, 0, nvr, _value);
    free(_value);

    return status;
}

/***************************************************
Functions for FMI2 for Model Exchange
****************************************************/
JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_enterEventMode(JNIEnv *env, jobject obj, jlong p, jlong c) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2EnterEventModeTYPE* fmi2EnterEventMode = load_function(fmu->handle, "fmi2EnterEventMode");
    return (*fmi2EnterEventMode)((void*) c);
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_enterContinuousTimeMode(JNIEnv *env, jobject obj, jlong p, jlong c) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2EnterContinuousTimeModeTYPE* fmi2EnterContinuousTimeMode = load_function(fmu->handle, "fmi2EnterContinuousTimeMode");
    return (*fmi2EnterContinuousTimeMode)((void*) c);
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_setTime(JNIEnv *env, jobject obj, jlong p, jlong c, jdouble time) {
    struct fmu_t* fmu = (struct fmu_t*) p;
    fmi2SetTimeTYPE* fmi2SetTime = load_function(fmu->handle, "fmi2SetTime");
    return (*fmi2SetTime)((void*) c, time);
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_setContinuousStates(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray x) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, x);
    const jdouble* _x = (*env)->GetDoubleArrayElements(env, x, 0);

    fmi2SetContinuousStatesTYPE* fmi2SetContinuousStates = load_function(fmu->handle, "fmi2SetContinuousStates");
    fmi2Status status = (*fmi2SetContinuousStates)((void*) c, _x, size);

    (*env)->ReleaseDoubleArrayElements(env, x, _x, NULL);

    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_getDerivatives(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray derivatives) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, derivatives);
    fmi2Real* _derivatives = malloc(sizeof(fmi2Real) * size);

    fmi2GetDerivativesTYPE* fmi2GetDerivatives = load_function(fmu->handle, "fmi2GetDerivatives");
    fmi2Status status = (*fmi2GetDerivatives)((void*) c, _derivatives, size);

    (*env)->SetDoubleArrayRegion(env, derivatives, 0, size, _derivatives);
    free(_derivatives);

    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_getEventIndicators(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray eventIndicators) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, eventIndicators);
    fmi2Real* _eventIndicators = malloc(sizeof(fmi2Real) * size);

    fmi2GetEventIndicatorsTYPE* fmi2GetEventIndicators = load_function(fmu->handle, "fmi2GetEventIndicators");
    fmi2Status status = (*fmi2GetEventIndicators)((void*) c, _eventIndicators, size);

    (*env)->SetDoubleArrayRegion(env, eventIndicators, 0, size, _eventIndicators);
    free(_eventIndicators);

    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_getContinuousStates(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray x) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, x);
    fmi2Real* _x = malloc(sizeof(fmi2Real) * size);

    fmi2GetContinuousStatesTYPE* fmi2GetContinuousStates = load_function(fmu->handle, "fmi2GetContinuousStates");
    fmi2Status status = (*fmi2GetContinuousStates)((void*) c, _x, size);

    (*env)->SetDoubleArrayRegion(env, x, 0, size, _x);
    free(_x);

    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_getNominalsOfContinuousStates(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray x_nominal) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    const jsize size = (*env)->GetArrayLength(env, x_nominal);
    fmi2Real *_x_nominal = malloc(sizeof(fmi2Real) * size);

    fmi2GetNominalsOfContinuousStatesTYPE* fmi2GetNominalsOfContinuousStates = load_function(fmu->handle, "fmi2GetNominalsOfContinuousStates");
    fmi2Status status = (*fmi2GetNominalsOfContinuousStates)((void*) c, _x_nominal, size);

    (*env)->SetDoubleArrayRegion(env, x_nominal, 0, size, _x_nominal);
    free(_x_nominal);

    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_completedIntegratorStep(JNIEnv *env, jobject obj, jlong p, jlong c, jboolean noSetFMUStatePriorToCurrentPoint, jobject enterEventMode, jobject terminateSimulation) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    fmi2Boolean _enterEventMode;
    fmi2Boolean _terminateSimulation;

    jclass cls = (*env)->FindClass(env, "no/mechatronics/sfi/fmi4j/importer/jni/BooleanByReference");
    jfieldID id = (*env)->GetFieldID(env, cls, "value", "Z");

    fmi2CompletedIntegratorStepTYPE* fmi2CompletedIntegratorStep = load_function(fmu->handle, "fmi2CompletedIntegratorStep");
    fmi2Status status = (*fmi2CompletedIntegratorStep)((void*) c, noSetFMUStatePriorToCurrentPoint, &_enterEventMode, &_terminateSimulation);

    (*env)->SetBooleanField(env, enterEventMode, id, _enterEventMode);
    (*env)->SetBooleanField(env, terminateSimulation, id, _terminateSimulation);

    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_Fmi2ModelExchangeLibrary_newDiscreteStates(JNIEnv *env, jobject obj, jlong p, jlong c, jobject states) {

    struct fmu_t* fmu = (struct fmu_t*) p;

    jclass cls = (*env)->FindClass(env, "no/mechatronics/sfi/fmi4j/importer/jni/EventInfo");

    jfieldID newDiscreteStatesNeeded_id = (*env)->GetFieldID(env, cls, "newDiscreteStatesNeeded", "Z");
    jfieldID terminateSimulation_id = (*env)->GetFieldID(env, cls, "terminateSimulation", "Z");
    jfieldID nominalsOfContinuousStatesChanged_id = (*env)->GetFieldID(env, cls, "nominalsOfContinuousStatesChanged", "Z");
    jfieldID valuesOfContinuousStatesChanged_id = (*env)->GetFieldID(env, cls, "valuesOfContinuousStatesChanged", "Z");
    jfieldID nextEventTimeDefined_id = (*env)->GetFieldID(env, cls, "nextEventTimeDefined", "Z");
    jfieldID nextEventTime_id = (*env)->GetFieldID(env, cls, "nextEventTime", "D");

    fmi2EventInfo _states = {
        .newDiscreteStatesNeeded = (*env)->GetBooleanField(env, states, newDiscreteStatesNeeded_id),
        .terminateSimulation = (*env)->GetBooleanField(env, states, terminateSimulation_id),
        .nominalsOfContinuousStatesChanged = (*env)->GetBooleanField(env, states, nominalsOfContinuousStatesChanged_id),
        .valuesOfContinuousStatesChanged = (*env)->GetBooleanField(env, states, valuesOfContinuousStatesChanged_id),
        .nextEventTimeDefined = (*env)->GetBooleanField(env, states, nextEventTimeDefined_id),
        .nextEventTime = (*env)->GetDoubleField(env, states, nextEventTime_id),
    };

    fmi2NewDiscreteStatesTYPE* fmi2NewDiscreteStates = load_function(fmu->handle, "fmi2NewDiscreteStates");
    fmi2Status status = (*fmi2NewDiscreteStates)((void*) c, &_states);

    (*env)->SetBooleanField(env, states, newDiscreteStatesNeeded_id, _states.newDiscreteStatesNeeded);
    (*env)->SetBooleanField(env, states, terminateSimulation_id, _states.terminateSimulation);
    (*env)->SetBooleanField(env, states, nominalsOfContinuousStatesChanged_id, _states.nominalsOfContinuousStatesChanged);
    (*env)->SetBooleanField(env, states, valuesOfContinuousStatesChanged_id, _states.valuesOfContinuousStatesChanged);
    (*env)->SetBooleanField(env, states, nextEventTimeDefined_id, _states.nextEventTimeDefined);
    (*env)->SetDoubleField(env, states, nextEventTime_id, _states.nextEventTime);

    return status;
}