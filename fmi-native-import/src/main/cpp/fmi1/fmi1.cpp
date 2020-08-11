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

#include "fmu_instance.hpp"

#include <jni.h>
#include <cstdlib>
#include <vector>
#include <string>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_load(JNIEnv* env, jobject, jstring dir, jstring libName, jstring modelIdentifier)
{
    const char* _dir = env->GetStringUTFChars(dir, nullptr);
    const char* _libName = env->GetStringUTFChars(libName, nullptr);
    const char* _modelIdentifier = env->GetStringUTFChars(modelIdentifier, nullptr);
    auto* fmu = new FmuInstance(_dir, _libName, _modelIdentifier);
    env->ReleaseStringUTFChars(dir, _dir);
    env->ReleaseStringUTFChars(libName, _libName);
    env->ReleaseStringUTFChars(modelIdentifier, _modelIdentifier);
    return (jlong)fmu;
}

JNIEXPORT jstring JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getVersion(JNIEnv* env, jobject, jlong p)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiGetVersionTYPE* fmiGetVersion = fmu->fmiGetVersion_;
    const char* version = (*fmiGetVersion)();
    return env->NewStringUTF(version);
}

JNIEXPORT jstring JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getTypesPlatform(JNIEnv* env, jobject, jlong p)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiGetTypesPlatformTYPE* fmiGetTypesPlatform = fmu->fmiGetTypesPlatform_;
    const char* platform = (*fmiGetTypesPlatform)();
    return env->NewStringUTF(platform);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setDebugLogging(JNIEnv* env, jobject, jlong p, jlong c, jboolean loggingOn)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiSetDebugLoggingTYPE* fmiSetDebugLogging = fmu->fmiSetDebugLogging_;
    fmiStatus status = (*fmiSetDebugLogging)((void*)c, (fmiBoolean)loggingOn);
    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getInteger(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jintArray ref)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    fmiValueReference* __vr = (fmiValueReference*)malloc(sizeof(fmiValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmiValueReference)_vr[i];
    }

    fmiGetIntegerTYPE* fmiGetInteger = fmu->fmiGetInteger_;

    fmiInteger* _ref = (fmiInteger*)malloc(sizeof(fmiInteger) * size);
    fmiStatus status = (*fmiGetInteger)((void*)c, __vr, size, _ref);

    env->SetIntArrayRegion(ref, 0, size, (jint*)_ref);

    free(_ref);
    free(__vr);
    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getReal(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jdoubleArray ref)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    fmiValueReference* __vr = (fmiValueReference*)malloc(sizeof(fmiValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmiValueReference)_vr[i];
    }

    fmiGetRealTYPE* fmiGetReal = fmu->fmiGetReal_;

    fmiReal* _ref = (fmiReal*)malloc(sizeof(fmiReal) * size);
    fmiStatus status = (*fmiGetReal)((void*)c, __vr, size, _ref);

    env->SetDoubleArrayRegion(ref, 0, size, _ref);

    free(_ref);
    free(__vr);
    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getString(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jobjectArray ref)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    fmiValueReference* __vr = (fmiValueReference*)malloc(sizeof(fmiValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmiValueReference)_vr[i];
    }

    fmiGetStringTYPE* fmiGetString = fmu->fmiGetString_;

    std::vector<const char*> _ref(size);
    for (int i = 0; i < size; i++) {
        jstring str = (jstring)env->GetObjectArrayElement(ref, i);
        _ref[i] = env->GetStringUTFChars(str, nullptr);
    }

    fmiStatus status = (*fmiGetString)((void*)c, __vr, size, _ref.data());

    for (int i = 0; i < size; i++) {
        jstring value = env->NewStringUTF(_ref[i]);
        env->SetObjectArrayElement(ref, i, value);
    }

    env->ReleaseLongArrayElements(vr, _vr, 0);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_getBoolean(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jbooleanArray ref)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    fmiValueReference* __vr = (fmiValueReference*)malloc(sizeof(fmiValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmiValueReference)_vr[i];
    }

    fmiBoolean* _ref = (fmiBoolean*)malloc(sizeof(fmiBoolean*) * size);

    fmiGetBooleanTYPE* fmiGetBoolean = fmu->fmiGetBoolean_;
    fmiStatus status = (*fmiGetBoolean)((void*)c, __vr, size, _ref);

    for (int i = 0; i < size; i++) {
        jboolean value = (jboolean)_ref[i];
        env->SetBooleanArrayRegion(ref, i, 1, &value);
    }

    free(_ref);
    free(__vr);
    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setInteger(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jintArray values)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);
    jint* _values = env->GetIntArrayElements(values, nullptr);

    fmiValueReference* __vr = (fmiValueReference*)malloc(sizeof(fmiValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmiValueReference)_vr[i];
    }

    fmiSetIntegerTYPE* fmiSetInteger = fmu->fmiSetInteger_;
    fmiStatus status = (*fmiSetInteger)((void*)c, __vr, size, (fmiInteger*)_values);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseIntArrayElements(values, _values, 0);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setReal(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jdoubleArray values)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);
    jdouble* _values = env->GetDoubleArrayElements(values, nullptr);

    fmiValueReference* __vr = (fmiValueReference*)malloc(sizeof(fmiValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmiValueReference)_vr[i];
    }

    fmiSetRealTYPE* fmiSetReal = fmu->fmiSetReal_;
    fmiStatus status = (*fmiSetReal)((void*)c, __vr, size, _values);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseDoubleArrayElements(values, _values, 0);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setString(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jobjectArray values)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    fmiValueReference* __vr = (fmiValueReference*)malloc(sizeof(fmiValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmiValueReference)_vr[i];
    }

    std::vector<const char*> _values(size);
    for (int i = 0; i < size; i++) {
        jstring str = (jstring)env->GetObjectArrayElement(values, i);
        _values[i] = env->GetStringUTFChars(str, nullptr);
    }

    fmiSetStringTYPE* fmiSetString = fmu->fmiSetString_;
    fmiStatus status = (*fmiSetString)((void*)c, __vr, size, _values.data());

    env->ReleaseLongArrayElements(vr, _vr, 0);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_setBoolean(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jbooleanArray values)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, 0);
    jboolean* _values = env->GetBooleanArrayElements(values, 0);

    fmiValueReference* __vr = (fmiValueReference*)malloc(sizeof(fmiValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmiValueReference)_vr[i];
    }

    fmiSetBooleanTYPE* fmiSetBoolean = fmu->fmiSetBoolean_;
    fmiStatus status = (*fmiSetBoolean)((void*)c, __vr, size, (fmiBoolean*)_values);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseBooleanArrayElements(values, _values, 0);

    free(__vr);

    return status;
}

JNIEXPORT jboolean JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1Library_free(JNIEnv* env, jobject, jlong p)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    jboolean status = 0;
    if (fmu->handle_) {
#ifdef WIN32
        status = FreeLibrary(fmu->handle_);
#else
        status = dlclose(fmu->handle_) == 0;
#endif
        fmu->handle_ = nullptr;
    }
    free(fmu);

    return status;
}


/***************************************************
Functions for FMI for Model Exchange
****************************************************/
JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_instantiateModel(JNIEnv* env, jobject, jlong p, jstring instanceName, jstring guid, jboolean loggingOn)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const char* _instanceName = env->GetStringUTFChars(instanceName, nullptr);
    const char* _guid = env->GetStringUTFChars(guid, nullptr);

    fmiInstantiateModelTYPE* fmiInstantiate = fmu->fmiInstantiateModel_;
    fmiComponent c = (*fmiInstantiate)(_instanceName, _guid, fmu->meCallback_, (fmiBoolean)loggingOn);

    env->ReleaseStringUTFChars(instanceName, _instanceName);
    env->ReleaseStringUTFChars(guid, _guid);

    return (jlong)c;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_setTime(JNIEnv* env, jobject, jlong p, jlong c, jdouble time)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiSetTimeTYPE* fmiSetTime = fmu->fmiSetTime_;
    return (*fmiSetTime)((void*)c, time);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_setContinuousStates(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray x)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(x);
    jdouble* _x = env->GetDoubleArrayElements(x, nullptr);

    fmiSetContinuousStatesTYPE* fmiSetContinuousStates = fmu->fmiSetContinuousStates_;
    fmiStatus status = (*fmiSetContinuousStates)((void*)c, _x, size);

    env->ReleaseDoubleArrayElements(x, _x, 0);

    return status;
}


JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_getDerivatives(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray derivatives)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(derivatives);
    fmiReal* _derivatives = (fmiReal*)malloc(sizeof(fmiReal) * size);

    fmiGetDerivativesTYPE* fmiGetDerivatives = fmu->fmiGetDerivatives_;
    fmiStatus status = (*fmiGetDerivatives)((void*)c, _derivatives, size);

    env->SetDoubleArrayRegion(derivatives, 0, size, _derivatives);
    free(_derivatives);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi_jni_Fmi1ModelExchangeLibrary_getEventIndicators(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray eventIndicators)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(eventIndicators);
    fmiReal* _eventIndicators = (fmiReal*)malloc(sizeof(fmiReal) * size);

    fmiGetEventIndicatorsTYPE* fmiGetEventIndicators = fmu->fmiGetEventIndicators_;
    fmiStatus status = (*fmiGetEventIndicators)((void*)c, _eventIndicators, size);

    env->SetDoubleArrayRegion(eventIndicators, 0, size, _eventIndicators);
    free(_eventIndicators);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_getContinuousStates(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray x)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(x);
    fmiReal* _x = (fmiReal*)malloc(sizeof(fmiReal) * size);

    fmiGetContinuousStatesTYPE* fmiGetContinuousStates = fmu->fmiGetContinuousStates_;
    fmiStatus status = (*fmiGetContinuousStates)((void*)c, _x, size);

    env->SetDoubleArrayRegion(x, 0, size, _x);
    free(_x);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_getNominalContinuousStates(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray xNominal)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(xNominal);
    fmiReal* _xNominal = (fmiReal*)malloc(sizeof(fmiReal) * size);

    fmiGetNominalContinuousStatesTYPE* fmiGetNominalContinuousStates = fmu->fmiGetNominalContinuousStates_;
    fmiStatus status = (*fmiGetNominalContinuousStates)((void*)c, _xNominal, size);

    env->SetDoubleArrayRegion(xNominal, 0, size, _xNominal);
    free(_xNominal);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_getStateValueReferences(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vrx)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vrx);
    jlong* _vrx = (jlong*)malloc(sizeof(jlong) * size);

    fmiGetStateValueReferencesTYPE* fmiGetStateValueReferences = fmu->fmiGetStateValueReferences_;
    fmiStatus status = (*fmiGetStateValueReferences)((void*)c, (fmiValueReference*)_vrx, size);

    env->SetLongArrayRegion(vrx, 0, size, _vrx);
    free(_vrx);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_terminate(JNIEnv* env, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiTerminateTYPE* fmiTerminate = fmu->fmiTerminate_;
    return (*fmiTerminate)((void*)c);
}

JNIEXPORT void JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1ModelExchangeLibrary_freeModelInstance(JNIEnv* env, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiFreeModelInstanceTYPE* fmiFreeInstance = fmu->fmiFreeModelInstance_;
    (*fmiFreeInstance)((void*)c);
}


/***************************************************
Functions for FMI for Co-Simulation
****************************************************/

JNIEXPORT jlong JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_instantiateSlave(JNIEnv* env, jobject, jlong p, jstring instanceName, jstring guid, jstring fmuLocation, jboolean loggingOn)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const char* _guid = env->GetStringUTFChars(guid, nullptr);
    const char* _fmuLocation = env->GetStringUTFChars(fmuLocation, nullptr);
    const char* _instanceName = env->GetStringUTFChars(instanceName, nullptr);

    std::cout << &fmu->csCallback_ << std::endl;

    fmiInstantiateSlaveTYPE* fmiInstantiate = fmu->fmiInstantiateSlave_;
    fmiComponent c = (*fmiInstantiate)(_instanceName, _guid, _fmuLocation, "application/x-fmu-sharedlibrary", 0, 0, 0, fmu->csCallback_, (fmiBoolean)loggingOn);

    std::cout << "no crash" << std::endl;

    env->ReleaseStringUTFChars(guid, _guid);
    env->ReleaseStringUTFChars(fmuLocation, _fmuLocation);
    env->ReleaseStringUTFChars(instanceName, _instanceName);

    return (jlong)c;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_initializeSlave(JNIEnv* env, jobject, jlong p, jlong c, jdouble startTime, jdouble stopTime)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiBoolean stopTimeDefined = stopTime > startTime;
    fmiInitializeSlaveTYPE* fmiSetup = fmu->fmiInitializeSlave_;
    return (*fmiSetup)((void*)c, startTime, stopTimeDefined, stopTime);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_doStep(JNIEnv* env, jobject, jlong p, jlong c, jdouble currentCommunicationPoint, jdouble communicationStepSize, jboolean newStep)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiDoStepTYPE* fmiDoStep = fmu->fmiDoStep_;
    return (*fmiDoStep)((void*)c, currentCommunicationPoint, communicationStepSize, newStep);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_resetSlave(JNIEnv* env, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiResetSlaveTYPE* fmiReset = fmu->fmiResetSlave_;
    return (*fmiReset)((void*)c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_terminateSlave(JNIEnv* env, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiTerminateSlaveTYPE* fmiTerminate = fmu->fmiTerminateSlave_;
    return (*fmiTerminate)((void*)c);
}

JNIEXPORT void JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi1_jni_Fmi1CoSimulationLibrary_freeSlaveInstance(JNIEnv* env, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmiFreeSlaveInstanceTYPE* fmiFreeInstance = fmu->fmiFreeSlaveInstance_;
    (*fmiFreeInstance)((void*)c);
}


#ifdef __cplusplus
}
#endif
