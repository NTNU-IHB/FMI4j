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
#define DLL_HANDLE HANDLE
#else
#define DLL_HANDLE void*
#include <dlfcn.h>
#endif

#ifdef WIN32
#define function_ptr FARPROC
#else
typedef void* function_ptr; 
#endif

DLL_HANDLE handle;

function_ptr* load_function(const char* function_name) {
#ifdef WIN32
    return (function_ptr) GetProcAddress(handle, function_name);
#else
    return dlsym(handle, function_name);
#endif
}

static const char* status_to_string(fmi2Status status) {
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

static void logger(void* fmi2ComponentEnvironment, fmi2String instance_name, fmi2Status status, fmi2String category, fmi2String message, ...) {
    printf("status = %s, instanceName = %s, category = %s: %s\n", status_to_string(status), instance_name, category, message);
}

fmi2CallbackFunctions callback = {
    .logger = logger,
    .allocateMemory = calloc,
    .freeMemory = free,
    .stepFinished = NULL,
    .componentEnvironment = NULL
};

JNIEXPORT jboolean JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_load(JNIEnv *env, jobject obj, jstring lib_name) {

    const char* _lib_name = (*env)->GetStringUTFChars(env, lib_name, 0);
    #ifdef WIN32
    	handle = LoadLibrary(_lib_name);
    #else
    	handle = dlopen(_lib_name, RTLD_NOW|RTLD_GLOBAL);
    #endif
    (*env)->ReleaseStringUTFChars(env, lib_name, _lib_name);

    if (!handle) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }

}

JNIEXPORT jstring JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_getVersion(JNIEnv *env, jobject obj) {
    const char* (*fmi2GetVersion)(void);
    fmi2GetVersion = load_function("fmi2GetVersion");
    const char* version = (*fmi2GetVersion)();
    return (*env)->NewStringUTF(env, version);
}

JNIEXPORT jstring JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_getTypesPlatform(JNIEnv *env, jobject obj) {
    const char* (*fmi2GetTypesPlatform)(void);
    fmi2GetTypesPlatform = load_function("fmi2GetTypesPlatform");
    const char* platform = (*fmi2GetTypesPlatform)();
    return (*env)->NewStringUTF(env, platform);
}

JNIEXPORT jlong JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_instantiate(JNIEnv *env, jobject obj, jstring instanceName, jint type, jstring guid,  jstring resourceLocation, jboolean visible, jboolean loggingOn) {

    const char* _instanceName = (*env)->GetStringUTFChars(env, instanceName, 0);
    const char* _guid = (*env)->GetStringUTFChars(env, guid, 0);
    const char* _resourceLocation = (*env)->GetStringUTFChars(env, resourceLocation, 0);

    void* (*fmi2Instantiate)(fmi2String, fmi2Type, fmi2String, fmi2String, const fmi2CallbackFunctions*, fmi2Boolean, fmi2Boolean);
    fmi2Instantiate = load_function("fmi2Instantiate");
    fmi2Component c = (*fmi2Instantiate)(_instanceName, type, _guid, _resourceLocation, &callback, (fmi2Boolean) visible, (fmi2Boolean) loggingOn);

    (*env)->ReleaseStringUTFChars(env, instanceName, _instanceName);
    (*env)->ReleaseStringUTFChars(env, guid, _guid);
    (*env)->ReleaseStringUTFChars(env, resourceLocation, _resourceLocation);

    return (jlong) c;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_setDebugLogging(JNIEnv *env, jobject obj, jlong c, jboolean loggingOn, jobjectArray categories) {

    const jsize nCategories = (*env)->GetArrayLength(env, categories);
    char* _categories = malloc(sizeof(char) * nCategories);

    for (int i = 0; i < nCategories; i++) {
        jstring str = (jstring) (*env)->GetObjectArrayElement(env, categories, i);
        _categories[i] = (*env)->GetStringUTFChars(env, str, NULL);
    }

    int (*fmi2SetDebugLogging)(void*, int, int, const char* []);
    fmi2SetDebugLogging = load_function("fmi2SetDebugLogging");
    int status = (*fmi2SetDebugLogging)((void*) c, (fmi2Boolean) loggingOn, nCategories, _categories);

    free(_categories);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_setupExperiment(JNIEnv *env, jobject obj, jlong c, jboolean toleranceDefined, jdouble tolerance, jdouble startTime, jdouble stopTime) {
    fmi2Boolean stopTimeDefined = stopTime > startTime ? 1: 0;
    int (*fmi2SetupExperiment)(fmi2Component, fmi2Boolean, fmi2Real, fmi2Real, fmi2Boolean, fmi2Real);
    fmi2SetupExperiment = load_function("fmi2SetupExperiment");
    int status = (*fmi2SetupExperiment)((void*) c, (fmi2Boolean) toleranceDefined, tolerance, startTime, stopTimeDefined, stopTime);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_enterInitializationMode(JNIEnv *env, jobject obj, jlong c) {
    int (*fmi2EnterInitializationMode)(fmi2Component);
    fmi2EnterInitializationMode = load_function("fmi2EnterInitializationMode");
    int status = (*fmi2EnterInitializationMode)((void*) c);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_exitInitializationMode(JNIEnv *env, jobject obj, jlong c) {
    int (*fmi2ExitInitializationMode)(fmi2Component);
    fmi2ExitInitializationMode = load_function("fmi2ExitInitializationMode");
    int status = (*fmi2ExitInitializationMode)((void*) c);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_terminate(JNIEnv *env, jobject obj, jlong c) {
    int (*fmi2Terminate)(fmi2Component);
    fmi2Terminate = load_function("fmi2Terminate");
    int status = (*fmi2Terminate)((void*) c);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_reset(JNIEnv *env, jobject obj, jlong c) {
    int (*fmi2Reset)(fmi2Component);
    fmi2Reset = load_function("fmi2Reset");
    int status = (*fmi2Reset)((void*) c);
    return status;
}

JNIEXPORT void JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_freeInstance(JNIEnv *env, jobject obj, jlong c) {
    void (*fmi2FreeInstance)(fmi2Component);
    fmi2FreeInstance = load_function("fmi2FreeInstance");
    (*fmi2FreeInstance)((void*) c);
    return;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_getInteger(JNIEnv *env, jobject obj, jlong c, jintArray vr, jintArray ref) {

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2Status (*fmi2GetInteger)(fmi2Component, const fmi2ValueReference[], size_t, fmi2Integer[]);
    fmi2GetInteger = load_function("fmi2GetInteger");

    fmi2Integer* _ref = malloc(sizeof(fmi2Integer) * size);
    fmi2Status status = (*fmi2GetInteger)((void*) c, _vr, size, _ref);

    (*env)->SetIntArrayRegion(env, ref, 0, size, _ref);
    free(_ref);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_getReal(JNIEnv *env, jobject obj, jlong c, jintArray vr, jdoubleArray ref) {

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2Status (*fmi2GetReal)(fmi2Component, const fmi2ValueReference[], size_t, fmi2Real[]);
    fmi2GetReal = load_function("fmi2GetReal");

    fmi2Real* _ref = malloc(sizeof(fmi2Real) * size);
    fmi2Status status = (*fmi2GetReal)((void*) c, _vr, size, _ref);

    (*env)->SetDoubleArrayRegion(env, ref, 0, size, _ref);

    free(_ref);
    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_getString(JNIEnv *env, jobject obj, jlong c, jintArray vr, jobjectArray ref) {

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2Status (*fmi2GetString)(fmi2Component, const fmi2ValueReference[], size_t, fmi2String[]);
    fmi2GetString = load_function("fmi2GetString");

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

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_getBoolean(JNIEnv *env, jobject obj, jlong c, jintArray vr, jbooleanArray ref) {

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    fmi2Boolean* _ref = malloc(sizeof(fmi2Boolean) * size);

    fmi2Status (*fmi2GetBoolean)(fmi2Component, const fmi2ValueReference[], size_t, fmi2Boolean[]);
    fmi2GetBoolean = load_function("fmi2GetBoolean");
    fmi2Status status = (*fmi2GetBoolean)((void*) c, _vr, size, _ref);

    (*env)->SetBooleanArrayRegion(env, ref, 0, size, _ref);

    free(_ref);
    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_setInteger(JNIEnv *env, jobject obj, jlong c, jintArray vr, jintArray values) {

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jint *_values = (*env)->GetIntArrayElements(env, values, 0);

    fmi2Status (*fmi2SetInteger)(fmi2Component, const fmi2ValueReference[], size_t, fmi2Integer[]);
    fmi2SetInteger = load_function("fmi2SetInteger");

    int status = (*fmi2SetInteger)((void*) c, _vr, size, _values);

    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);
    (*env)->ReleaseIntArrayElements(env, values, _values, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_setReal(JNIEnv *env, jobject obj, jlong c, jintArray vr, jdoubleArray values) {

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jdouble *_values = (*env)->GetDoubleArrayElements(env, values, 0);

    fmi2Status (*fmi2SetReal)(fmi2Component, const fmi2ValueReference[], size_t, fmi2Real[]);
    fmi2SetReal = load_function("fmi2SetReal");
    fmi2Status status = (*fmi2SetReal)((void*) c, _vr, size, _values);

    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);
    (*env)->ReleaseDoubleArrayElements(env, values, _values, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_setString(JNIEnv *env, jobject obj, jlong c, jintArray vr, jobjectArray values) {

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);

    char* _values = malloc(sizeof(char) * size);
    for (int i = 0; i < size; i++) {
       jstring str = (jstring) (*env)->GetObjectArrayElement(env, values, i);
       _values[i] = (*env)->GetStringUTFChars(env, str, NULL);
    }

    fmi2Status (*fmi2SetString)(fmi2Component, const fmi2ValueReference[], size_t, fmi2String[]);
    fmi2SetString = load_function("fmi2SetString");
    fmi2Status status = (*fmi2SetString)((void*) c, _vr, size, _values);

    free(_values);
    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_setBoolean(JNIEnv *env, jobject obj, jlong c, jintArray vr, jbooleanArray values) {

    const jsize size = (*env)->GetArrayLength(env, vr);
    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jboolean *_values = (*env)->GetBooleanArrayElements(env, values, 0);

    fmi2Status (*fmi2SetBoolean)(fmi2Component, const fmi2ValueReference[], size_t, fmi2Boolean[]);
    fmi2SetBoolean = load_function("fmi2SetBoolean");
    fmi2Status status = (*fmi2SetBoolean)((void*) c, _vr, size, _values);

    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);
    (*env)->ReleaseBooleanArrayElements(env, values, _values, NULL);

    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_getFMUstate(JNIEnv *env, jobject obj, jlong c, jobject state) {

    jclass cls = (*env)->FindClass(env, "no/mechatronics/sfi/fmi4j/importer/jni/FmuState");
    jfieldID id = (*env)->GetFieldID(env, cls, "pointer", "J");

    fmi2Status (*fmi2GetFMUstate)(fmi2Component, fmi2FMUstate*);
    fmi2GetFMUstate = load_function("fmi2GetFMUstate");

    fmi2FMUstate _state;
    fmi2Status status = (*fmi2GetFMUstate)((void*) c, &_state);

    (*env)->SetLongField(env, state, id, (jlong) _state);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_setFMUstate(JNIEnv *env, jobject obj, jlong c, jlong state) {

    fmi2Status (*fmi2SetFMUstate)(fmi2Component, fmi2FMUstate);
    fmi2SetFMUstate = load_function("fmi2SetFMUstate");
    fmi2Status status = (*fmi2SetFMUstate)((void*) c, (fmi2FMUstate) state);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_freeFMUstate(JNIEnv *env, jobject obj, jlong c, jlong state) {

    fmi2Status (*fmi2FreeFMUstate)(fmi2Component, fmi2FMUstate*);
    fmi2FreeFMUstate = load_function("fmi2FreeFMUstate");
    fmi2FMUstate _state = (fmi2FMUstate) state;
    fmi2Status status = (*fmi2FreeFMUstate)((void*) c, &_state);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_serializedFMUstateSize(JNIEnv *env, jobject obj, jlong c, jlong state, jobject size) {

    jclass size_cls = (*env)->FindClass(env, "no/mechatronics/sfi/fmi4j/importer/jni/IntByReference");
    jfieldID size_id = (*env)->GetFieldID(env, size_cls, "value", "I");

    fmi2Status (*fmi2SerializedFMUstateSize)(fmi2Component, fmi2FMUstate, size_t*);
    fmi2SerializedFMUstateSize = load_function("fmi2SerializedFMUstateSize");

    size_t _size;
    fmi2Status status = (*fmi2SerializedFMUstateSize)((void*) c, (fmi2FMUstate) state, &_size);

    (*env)->SetIntField(env, size, size_id, (jint) _size);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_serializeFMUstate(JNIEnv *env, jobject obj, jlong c, jlong state, jbyteArray serializedState) {

    const jsize size = (*env)->GetArrayLength(env, serializedState);
    fmi2Byte *_serializedState = malloc( sizeof(fmi2Byte) * size );

    fmi2Status (*fmi2SerializeFMUstate)(fmi2Component, fmi2FMUstate, fmi2Byte[], size_t);
    fmi2SerializeFMUstate = load_function("fmi2SerializeFMUstate");

    fmi2Status status = (*fmi2SerializeFMUstate)((void*) c, (fmi2FMUstate) state, _serializedState, size);

    (*env)->SetByteArrayRegion(env, serializedState, 0, size, _serializedState);
    free(_serializedState);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_deSerializeFMUstate(JNIEnv *env, jobject obj, jlong c, jobject state, const jbyteArray serializedState) {

    jclass cls = (*env)->FindClass(env, "no/mechatronics/sfi/fmi4j/importer/jni/FmuState");
    jfieldID id = (*env)->GetFieldID(env, cls, "pointer", "J");

    const jsize size = (*env)->GetArrayLength(env, serializedState);
    const jbyte *_serializedState = (*env)->GetByteArrayElements(env, serializedState, 0);

    fmi2Status (*fmi2DeserializeFMUstate)(fmi2Component, const fmi2Byte[], size_t, fmi2FMUstate*);
    fmi2DeserializeFMUstate = load_function("fmi2DeserializeFMUstate");

    fmi2FMUstate _state = (fmi2FMUstate) (*env)->GetLongField(env, state, id);
    fmi2Status status = (*fmi2DeserializeFMUstate)((void*) c, _serializedState, size, _state);

    (*env)->SetLongField(env, state, id, (jlong) _state);

    (*env)->ReleaseByteArrayElements(env, serializedState, _serializedState, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_getDirectionalDerivative(JNIEnv *env, jobject obj, jlong c, jintArray vUnknown_ref, jintArray vKnown_ref, jdoubleArray dvKnown_ref, jdoubleArray dvUnknown_ref) {

    const jsize nUknown = (*env)->GetArrayLength(env, vUnknown_ref);
    const jsize nKnown = (*env)->GetArrayLength(env, vUnknown_ref);

    const jint *_vUnknown_ref = (*env)->GetIntArrayElements(env, vUnknown_ref, 0);
    const jint *_vKnown_ref = (*env)->GetIntArrayElements(env, vKnown_ref, 0);
    const jdouble *_dvKnown_ref = (*env)->GetDoubleArrayElements(env, vKnown_ref, 0);
    const jdouble *_dvUnknown_ref = (*env)->GetDoubleArrayElements(env, vUnknown_ref, 0);

    fmi2Status (*fmi2GetDirectionalDerivative)(fmi2Component, const fmi2ValueReference[], size_t, const fmi2ValueReference[], size_t, const fmi2Real[], fmi2Real[]);
    fmi2GetDirectionalDerivative = load_function("fmi2GetDirectionalDerivative");
    fmi2Status status = (*fmi2GetDirectionalDerivative)((void*) c, _vUnknown_ref, nUknown, _vKnown_ref, nKnown, _dvKnown_ref, _dvUnknown_ref);

    (*env)->ReleaseIntArrayElements(env, vUnknown_ref, _vUnknown_ref, NULL);
    (*env)->ReleaseIntArrayElements(env, vKnown_ref, _vKnown_ref, NULL);

    (*env)->ReleaseDoubleArrayElements(env, dvKnown_ref, _dvKnown_ref, NULL);
    (*env)->ReleaseDoubleArrayElements(env, dvUnknown_ref, _dvUnknown_ref, NULL);

    return status;
}

JNIEXPORT jboolean JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiLibrary_free(JNIEnv *env, jobject obj) {
    if (handle) {
        jboolean status;
        #ifdef WIN32
            status = FreeLibrary(handle);
        #else
            status = dlclose(handle) == 0;
        #endif
        handle = NULL;
        return status;
    } else {
        return JNI_FALSE;
    }
}

/***************************************************
Functions for FMI2 for Co-Simulation
****************************************************/

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiCoSimulationLibrary_step(JNIEnv *env, jobject obj, jlong c, jdouble currentCommunicationPoint, jdouble communicationStepSize, jboolean noSetFMUStatePriorToCurrentPoint) {
    fmi2Status (*fmi2DoStep)(fmi2Component, fmi2Real, fmi2Real, fmi2Boolean);
    fmi2DoStep = load_function("fmi2DoStep");
    fmi2Status status = (*fmi2DoStep)((void*) c, currentCommunicationPoint, communicationStepSize, noSetFMUStatePriorToCurrentPoint);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiCoSimulationLibrary_cancelStep(JNIEnv *env, jobject obj, jlong c) {
    fmi2Status (*fmi2CancelStep)(fmi2Component);
    fmi2CancelStep = load_function("fmi2CancelStep");
    fmi2Status status = (*fmi2CancelStep)((void*) c);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiCoSimulationLibrary_setRealInputDerivatives(JNIEnv *env, jobject obj, jlong c, jintArray vr, jintArray order, jdoubleArray value) {

    const jsize nvr = (*env)->GetArrayLength(env, vr);

    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jint *_order = (*env)->GetIntArrayElements(env, order, 0);
    const jdouble *_value = (*env)->GetDoubleArrayElements(env, value, 0);

    fmi2Status (*fmi2SetRealInputDerivatives)(fmi2Component, const fmi2ValueReference [], size_t, const fmi2Integer [], const fmi2Real []);
    fmi2SetRealInputDerivatives = load_function("fmi2SetRealInputDerivatives");
    fmi2Status status = (*fmi2SetRealInputDerivatives)((void*) c, _vr, nvr, _order, _value);

    (*env)->ReleaseIntArrayElements(env, vr, _vr, NULL);
    (*env)->ReleaseIntArrayElements(env, order, _order, NULL);

    (*env)->ReleaseDoubleArrayElements(env, value, _value, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiCoSimulationLibrary_getRealOutputDerivatives(JNIEnv *env, jobject obj, jlong c, jintArray vr, jintArray order, jdoubleArray value) {

    const jsize nvr = (*env)->GetArrayLength(env, vr);

    const jint *_vr = (*env)->GetIntArrayElements(env, vr, 0);
    const jint *_order = (*env)->GetIntArrayElements(env, order, 0);
    fmi2Real *_value = malloc(sizeof(fmi2Real) * nvr);

    fmi2Status (*fmi2GetRealOutputDerivatives)(fmi2Component, const fmi2ValueReference [], size_t, const fmi2Integer [], fmi2Real []);
    fmi2GetRealOutputDerivatives = load_function("fmi2GetRealOutputDerivatives");
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

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_enterEventMode(JNIEnv *env, jobject obj, jlong c) {
    fmi2Status (*fmi2EnterEventMode)(fmi2Component);
    fmi2EnterEventMode = load_function("fmi2EnterEventMode");
    fmi2Status status = (*fmi2EnterEventMode)((void*) c);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_enterContinuousTimeMode(JNIEnv *env, jobject obj, jlong c) {
    fmi2Status (*fmi2EnterContinuousTimeMode)(fmi2Component);
    fmi2EnterContinuousTimeMode = load_function("fmi2EnterContinuousTimeMode");
    fmi2Status status = (*fmi2EnterContinuousTimeMode)((void*) c);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_setTime(JNIEnv *env, jobject obj, jlong c, jdouble time) {
    fmi2Status (*fmi2SetTime)(fmi2Component, fmi2Real);
    fmi2SetTime = load_function("fmi2SetTime");
    fmi2Status status = (*fmi2SetTime)((void*) c, time);
    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_setContinuousStates(JNIEnv *env, jobject obj, jlong c, jdoubleArray x) {

    const jsize size = (*env)->GetArrayLength(env, x);
    const jdouble* _x = (*env)->GetDoubleArrayElements(env, x, 0);

    fmi2Status (*fmi2SetContinuousStates)(fmi2Component, const fmi2Real[], size_t);
    fmi2SetContinuousStates = load_function("fmi2SetContinuousStates");
    fmi2Status status = (*fmi2SetContinuousStates)((void*) c, _x, size);

    (*env)->ReleaseDoubleArrayElements(env, x, _x, NULL);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_getDerivatives(JNIEnv *env, jobject obj, jlong c, jdoubleArray derivatives) {

    const jsize size = (*env)->GetArrayLength(env, derivatives);
    fmi2Real* _derivatives = malloc(sizeof(fmi2Real) * size);

    fmi2Status (*fmi2GetDerivatives)(fmi2Component, fmi2Real[], size_t);
    fmi2GetDerivatives = load_function("fmi2GetDerivatives");
    fmi2Status status = (*fmi2GetDerivatives)((void*) c, _derivatives, size);

    (*env)->SetDoubleArrayRegion(env, derivatives, 0, size, _derivatives);
    free(_derivatives);

    return status;
}


JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_getEventIndicators(JNIEnv *env, jobject obj, jlong c, jdoubleArray eventIndicators) {

    const jsize size = (*env)->GetArrayLength(env, eventIndicators);
    fmi2Real* _eventIndicators = malloc(sizeof(fmi2Real) * size);

    fmi2Status (*fmi2GetEventIndicators)(fmi2Component, fmi2Real[], size_t);
    fmi2GetEventIndicators = load_function("fmi2GetEventIndicators");
    fmi2Status status = (*fmi2GetEventIndicators)((void*) c, _eventIndicators, size);

    (*env)->SetDoubleArrayRegion(env, eventIndicators, 0, size, _eventIndicators);
    free(_eventIndicators);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_getContinuousStates(JNIEnv *env, jobject obj, jlong c, jdoubleArray x) {

    const jsize size = (*env)->GetArrayLength(env, x);
    fmi2Real* _x = malloc(sizeof(fmi2Real) * size);

    fmi2Status (*fmi2GetContinuousStates)(fmi2Component, fmi2Real[], size_t);
    fmi2GetContinuousStates = load_function("fmi2GetContinuousStates");
    fmi2Status status = (*fmi2GetContinuousStates)((void*) c, _x, size);

    (*env)->SetDoubleArrayRegion(env, x, 0, size, _x);
    free(_x);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_getNominalsOfContinuousStates(JNIEnv *env, jobject obj, jlong c, jdoubleArray x_nominal) {

    const jsize size = (*env)->GetArrayLength(env, x_nominal);
    fmi2Real *_x_nominal = malloc(sizeof(fmi2Real) * size);

    fmi2Status (*fmi2GetNominalsOfContinuousStates)(fmi2Component, fmi2Real[], size_t);
    fmi2GetNominalsOfContinuousStates = load_function("fmi2GetNominalsOfContinuousStates");
    fmi2Status status = (*fmi2GetNominalsOfContinuousStates)((void*) c, _x_nominal, size);

    (*env)->SetDoubleArrayRegion(env, x_nominal, 0, size, _x_nominal);
    free(_x_nominal);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_completedIntegratorStep(JNIEnv *env, jobject obj, jlong c, jboolean noSetFMUStatePriorToCurrentPoint, jobject enterEventMode, jobject terminateSimulation) {

    fmi2Boolean _enterEventMode;
    fmi2Boolean _terminateSimulation;

    jclass cls = (*env)->FindClass(env, "no/mechatronics/sfi/fmi4j/importer/jni/BooleanByReference");
    jfieldID id = (*env)->GetFieldID(env, cls, "value", "Z");

    fmi2Status (*fmi2CompletedIntegratorStep)(fmi2Component, fmi2Boolean, fmi2Boolean*, fmi2Boolean*);
    fmi2CompletedIntegratorStep = load_function("fmi2CompletedIntegratorStep");
    fmi2Status status = (*fmi2CompletedIntegratorStep)((void*) c, noSetFMUStatePriorToCurrentPoint, &_enterEventMode, &_terminateSimulation);

    (*env)->SetBooleanField(env, enterEventMode, id, _enterEventMode);
    (*env)->SetBooleanField(env, terminateSimulation, id, _terminateSimulation);

    return status;
}

JNIEXPORT jint JNICALL Java_no_mechatronics_sfi_fmi4j_importer_jni_FmiModelExchangeLibrary_newDiscreteStates(JNIEnv *env, jobject obj, jlong c, jobject states) {

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

    fmi2Status (*fmi2NewDiscreteStates)(fmi2Component, fmi2EventInfo*);
    fmi2NewDiscreteStates = load_function("fmi2NewDiscreteStates");
    fmi2Status status = (*fmi2NewDiscreteStates)((void*) c, &_states);

    (*env)->SetBooleanField(env, states, newDiscreteStatesNeeded_id, _states.newDiscreteStatesNeeded);
    (*env)->SetBooleanField(env, states, terminateSimulation_id, _states.terminateSimulation);
    (*env)->SetBooleanField(env, states, nominalsOfContinuousStatesChanged_id, _states.nominalsOfContinuousStatesChanged);
    (*env)->SetBooleanField(env, states, valuesOfContinuousStatesChanged_id, _states.valuesOfContinuousStatesChanged);
    (*env)->SetBooleanField(env, states, nextEventTimeDefined_id, _states.nextEventTimeDefined);
    (*env)->SetDoubleField(env, states, nextEventTime_id, _states.nextEventTime);

    return status;
}