/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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
#include <vector>
#include <string>

#include "fmu_instance.hpp"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_load(JNIEnv *env, jobject obj, jstring libName, jstring modelIdentifier) {
    const char* _libName = env->GetStringUTFChars(libName, 0);
    const char* _modelIdentifier = env->GetStringUTFChars(modelIdentifier, 0);
    FmuInstance* fmu = new FmuInstance(_libName, _modelIdentifier);
    env->ReleaseStringUTFChars(libName, _libName);
    env->ReleaseStringUTFChars(modelIdentifier, _modelIdentifier);
    return (jlong) fmu;
}

JNIEXPORT jstring JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getVersion(JNIEnv *env, jobject obj, jlong p) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiGetVersionTYPE* fmiGetVersion = fmu->fmiGetVersion_;
    const char* version = (*fmiGetVersion)();
    return env->NewStringUTF(version);
}

JNIEXPORT jstring JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getTypesPlatform(JNIEnv *env, jobject obj, jlong p) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiGetTypesPlatformTYPE* fmiGetTypesPlatform = fmu->fmiGetTypesPlatform_;
    const char* platform = (*fmiGetTypesPlatform)();
    return env->NewStringUTF(platform);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setDebugLogging(JNIEnv *env, jobject obj, jlong p, jlong c, jboolean loggingOn) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiSetDebugLoggingTYPE* fmiSetDebugLogging = fmu->fmiSetDebugLogging_;
    fmiStatus status = (*fmiSetDebugLogging)((void*) c, (fmiBoolean) loggingOn);
    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getInteger(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jintArray ref) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, 0);

    fmiGetIntegerTYPE* fmiGetInteger = fmu->fmiGetInteger_;

    fmiInteger* _ref = (fmiInteger*) malloc(sizeof(fmiInteger) * size);
    fmiStatus status = (*fmiGetInteger)((void*) c, (fmiValueReference*)_vr, size, _ref);

    env->SetIntArrayRegion(ref, 0, size, (jint*)_ref);
    free(_ref);
    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getReal(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jdoubleArray ref) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(vr);
    jlong *_vr = env->GetLongArrayElements(vr, 0);

    fmiGetRealTYPE* fmiGetReal = fmu->fmiGetReal_;

    fmiReal* _ref = (fmiReal*) malloc(sizeof(fmiReal) * size);
    fmiStatus status = (*fmiGetReal)((void*) c, (fmiValueReference*)_vr, size, _ref);

    env->SetDoubleArrayRegion(ref, 0, size, _ref);

    free(_ref);
    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getString(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jobjectArray ref) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(vr);
    jlong *_vr = env->GetLongArrayElements(vr, 0);

    fmiGetStringTYPE* fmiGetString = fmu->fmiGetString_;

    std::vector<const char*> _ref(size);
    for (int i = 0; i < size; i++) {
        jstring str = (jstring) env->GetObjectArrayElement(ref, i);
        _ref[i] = env->GetStringUTFChars(str, 0);
    }

    fmiStatus status = (*fmiGetString)((void*) c, (fmiValueReference*)_vr, size, _ref.data());

    for (int i = 0; i < size; i++) {
        jstring value = env->NewStringUTF(_ref[i]);
        env->SetObjectArrayElement(ref, i, value);
    }

    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getBoolean(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jbooleanArray ref) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(vr);
    jlong *_vr = env->GetLongArrayElements(vr, 0);

    fmiBoolean* _ref = (fmiBoolean*) malloc(sizeof(fmiBoolean*) * size);

    fmiGetBooleanTYPE* fmiGetBoolean = fmu->fmiGetBoolean_;
    fmiStatus status = (*fmiGetBoolean)((void*) c, (fmiValueReference*)_vr, size, _ref);

    for (int i = 0; i < size; i++) {
        jboolean value = (jboolean) _ref[i];
        env->SetBooleanArrayRegion(ref, i, 1, &value);
    }

    free(_ref);
    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setInteger(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jintArray values) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(vr);
    jlong *_vr = env->GetLongArrayElements(vr, 0);
    jint *_values = env->GetIntArrayElements(values, 0);

    fmiSetIntegerTYPE* fmiSetInteger = fmu->fmiSetInteger_;
    fmiStatus status = (*fmiSetInteger)((void*) c, (fmiValueReference*)_vr, size, (fmiInteger*)_values);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseIntArrayElements(values, _values, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setReal(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jdoubleArray values) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(vr);
    jlong *_vr = env->GetLongArrayElements(vr, 0);
    jdouble *_values = env->GetDoubleArrayElements(values, 0);

    fmiSetRealTYPE* fmiSetReal = fmu->fmiSetReal_;
    fmiStatus status = (*fmiSetReal)((void*) c, (fmiValueReference*)_vr, size, _values);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseDoubleArrayElements(values, _values, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setString(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jobjectArray values) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(vr);
    jlong *_vr = env->GetLongArrayElements(vr, 0);

    std::vector<const char*> _values(size);
    for (int i = 0; i < size; i++) {
       jstring str = (jstring) env->GetObjectArrayElement(values, i);
       _values[i] = env->GetStringUTFChars(str, 0);
    }

    fmiSetStringTYPE* fmiSetString = fmu->fmiSetString_;
    fmiStatus status = (*fmiSetString)((void*) c, (fmiValueReference*)_vr, size, _values.data());

    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setBoolean(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jbooleanArray values) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(vr);
    jlong *_vr = env->GetLongArrayElements(vr, 0);
    jboolean *_values = env->GetBooleanArrayElements(values, 0);

    fmiSetBooleanTYPE* fmiSetBoolean = fmu->fmiSetBoolean_;
    fmiStatus status = (*fmiSetBoolean)((void*) c, (fmiValueReference*)_vr, size, (fmiBoolean*)_values);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseBooleanArrayElements(values, _values, 0);

    return status;
}

JNIEXPORT jboolean JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_free(JNIEnv *env, jobject obj, jlong p) {

    FmuInstance* fmu = (FmuInstance*) p;

    jboolean status;
    if (fmu->handle_) {
        #ifdef WIN32
            status = FreeLibrary(fmu->handle_);
        #else
            status = dlclose(fmu->handle_) == 0;
        #endif
        fmu->handle_ = NULL;
    }
    free(fmu);

    return status;
}


/***************************************************
Functions for FMI for Model Exchange
****************************************************/
JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_instantiateModel(JNIEnv *env, jobject obj, jlong p, jstring instanceName, jstring guid, jboolean loggingOn) {
    FmuInstance* fmu = (FmuInstance*) p;

    const char* _instanceName = env->GetStringUTFChars(instanceName, 0);
    const char* _guid = env->GetStringUTFChars(guid, 0);

    fmiInstantiateModelTYPE* fmiInstantiate = fmu->fmiInstantiateModel_;
    fmiComponent c = (*fmiInstantiate)(_instanceName, _guid, fmu->meCallback_, (fmiBoolean) loggingOn);

    env->ReleaseStringUTFChars(instanceName, _instanceName);
    env->ReleaseStringUTFChars(guid, _guid);

    return (jlong) c;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_setTime(JNIEnv *env, jobject obj, jlong p, jlong c, jdouble time) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiSetTimeTYPE* fmiSetTime = fmu->fmiSetTime_;
    return (*fmiSetTime)((void*) c, time);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_setContinuousStates(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray x) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(x);
    jdouble* _x = env->GetDoubleArrayElements(x, 0);

    fmiSetContinuousStatesTYPE* fmiSetContinuousStates = fmu->fmiSetContinuousStates_;
    fmiStatus status = (*fmiSetContinuousStates)((void*) c, _x, size);

    env->ReleaseDoubleArrayElements(x, _x, 0);

    return status;
}


JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_getDerivatives(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray derivatives) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(derivatives);
    fmiReal* _derivatives = (fmiReal*) malloc(sizeof(fmiReal) * size);

    fmiGetDerivativesTYPE* fmiGetDerivatives = fmu->fmiGetDerivatives_;
    fmiStatus status = (*fmiGetDerivatives)((void*) c, _derivatives, size);

    env->SetDoubleArrayRegion(derivatives, 0, size, _derivatives);
    free(_derivatives);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi_jni_Fmi1ModelExchangeLibrary_getEventIndicators(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray eventIndicators) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(eventIndicators);
    fmiReal* _eventIndicators = (fmiReal*) malloc(sizeof(fmiReal) * size);

    fmiGetEventIndicatorsTYPE* fmiGetEventIndicators = fmu->fmiGetEventIndicators_;
    fmiStatus status = (*fmiGetEventIndicators)((void*) c, _eventIndicators, size);

    env->SetDoubleArrayRegion(eventIndicators, 0, size, _eventIndicators);
    free(_eventIndicators);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_getContinuousStates(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray x) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(x);
    fmiReal* _x = (fmiReal*) malloc(sizeof(fmiReal) * size);

    fmiGetContinuousStatesTYPE* fmiGetContinuousStates = fmu->fmiGetContinuousStates_;
    fmiStatus status = (*fmiGetContinuousStates)((void*) c, _x, size);

    env->SetDoubleArrayRegion(x, 0, size, _x);
    free(_x);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_getNominalContinuousStates(JNIEnv *env, jobject obj, jlong p, jlong c, jdoubleArray xNominal) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(xNominal);
    fmiReal* _xNominal = (fmiReal*) malloc(sizeof(fmiReal) * size);

    fmiGetNominalContinuousStatesTYPE* fmiGetNominalContinuousStates = fmu->fmiGetNominalContinuousStates_;
    fmiStatus status = (*fmiGetNominalContinuousStates)((void*) c, _xNominal, size);

    env->SetDoubleArrayRegion(xNominal, 0, size, _xNominal);
    free(_xNominal);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_getStateValueReferences(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vrx) {

    FmuInstance* fmu = (FmuInstance*) p;

    const jsize size = env->GetArrayLength(vrx);
    jlong* _vrx = (jlong*) malloc(sizeof(jlong) * size);

    fmiGetStateValueReferencesTYPE* fmiGetStateValueReferences = fmu->fmiGetStateValueReferences_;
    fmiStatus status = (*fmiGetStateValueReferences)((void*) c, (fmiValueReference*)_vrx, size);

    env->SetLongArrayRegion(vrx, 0, size, _vrx);
    free(_vrx);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_terminate(JNIEnv *env, jobject obj, jlong p, jlong c) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiTerminateTYPE* fmiTerminate = fmu->fmiTerminate_;
    return (*fmiTerminate)((void*) c);
}

JNIEXPORT void JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_freeModelInstance(JNIEnv *env, jobject obj, jlong p, jlong c) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiFreeModelInstanceTYPE* fmiFreeInstance = fmu->fmiFreeModelInstance_;
    (*fmiFreeInstance)((void*) c);
}



/***************************************************
Functions for FMI for Co-Simulation
****************************************************/

JNIEXPORT jlong JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_instantiateSlave(JNIEnv *env, jobject obj, jlong p, jstring instanceName, jstring guid, jstring fmuLocation, jboolean visible, jboolean interactive, jboolean loggingOn) {

    FmuInstance* fmu = (FmuInstance*) p;

    const char* _instanceName = env->GetStringUTFChars(instanceName, 0);
    const char* _guid = env->GetStringUTFChars(guid, 0);
    const char* _fmuLocation = env->GetStringUTFChars(fmuLocation, 0);

    fmiInstantiateSlaveTYPE* fmiInstantiate = fmu->fmiInstantiateSlave_;
    fmiComponent c = (*fmiInstantiate)(_instanceName, _guid, _fmuLocation, "application/x-fmu-sharedlibrary", 0, (fmiBoolean) visible, (fmiBoolean) interactive, fmu->csCallback_, (fmiBoolean) loggingOn);

    env->ReleaseStringUTFChars(instanceName, _instanceName);
    env->ReleaseStringUTFChars(guid, _guid);
    env->ReleaseStringUTFChars(fmuLocation, _fmuLocation);

    return (jlong) c;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_initializeSlave(JNIEnv *env, jobject obj, jlong p, jlong c, jdouble startTime, jdouble stopTime) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiBoolean stopTimeDefined = stopTime > startTime;
    fmiInitializeSlaveTYPE* fmiSetup = fmu->fmiInitializeSlave_;
    return (*fmiSetup)((void*) c, startTime, stopTimeDefined, stopTime);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_doStep(JNIEnv *env, jobject obj, jlong p, jlong c, jdouble currentCommunicationPoint, jdouble communicationStepSize, jboolean newStep) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiDoStepTYPE* fmiDoStep = fmu->fmiDoStep_;
    return (*fmiDoStep)((void*) c, currentCommunicationPoint, communicationStepSize, newStep);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_resetSlave(JNIEnv *env, jobject obj, jlong p, jlong c) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiResetSlaveTYPE* fmiReset = fmu->fmiResetSlave_;
    return (*fmiReset)((void*) c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_terminateSlave(JNIEnv *env, jobject obj, jlong p, jlong c) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiTerminateSlaveTYPE* fmiTerminate = fmu->fmiTerminateSlave_;
    return (*fmiTerminate)((void*) c);
}

JNIEXPORT void JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_freeSlaveInstance(JNIEnv *env, jobject obj, jlong p, jlong c) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiFreeSlaveInstanceTYPE* fmiFreeInstance = fmu->fmiFreeSlaveInstance_;
    (*fmiFreeInstance)((void*) c);
}



#ifdef __cplusplus
}
#endif
