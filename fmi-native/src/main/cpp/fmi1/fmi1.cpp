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

#include "fmu_instance.hpp"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_FmiLibrary_load(JNIEnv *env, jobject obj, jstring lib_name) {
    const char* _lib_name = env->GetStringUTFChars(lib_name, 0);
    FmuInstance* fmu = new FmuInstance(_lib_name);
    env->ReleaseStringUTFChars(lib_name, _lib_name);
    return (jlong) fmu;
}

JNIEXPORT jstring JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_FmiLibrary_getVersion(JNIEnv *env, jobject obj, jlong p) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiGetVersionTYPE* fmiGetVersion = fmu->fmiGetVersion_;
    const char* version = (*fmiGetVersion)();
    return env->NewStringUTF(version);
}

JNIEXPORT jstring JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_FmiLibrary_getTypesPlatform(JNIEnv *env, jobject obj, jlong p) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiGetTypesPlatformTYPE* fmiGetTypesPlatform = fmu->fmiGetTypesPlatform_;
    const char* platform = (*fmiGetTypesPlatform)();
    return env->NewStringUTF(platform);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_terminate(JNIEnv *env, jobject obj, jlong p, jlong c) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiTerminateSlaveTYPE* fmiTerminate = fmu->fmiTerminateSlave_;
    return (*fmiTerminate)((void*) c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_reset(JNIEnv *env, jobject obj, jlong p, jlong c) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiResetSlaveTYPE* fmiReset = fmu->fmiResetSlave_;
    return (*fmiReset)((void*) c);
}

JNIEXPORT void JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_freeInstance(JNIEnv *env, jobject obj, jlong p, jlong c) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiFreeSlaveInstanceTYPE* fmiFreeInstance = fmu->fmiFreeSlaveInstance_;
    (*fmiFreeInstance)((void*) c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_getInteger(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jintArray ref) {

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

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_getReal(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jdoubleArray ref) {

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

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_getString(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jobjectArray ref) {

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

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_getBoolean(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jbooleanArray ref) {

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

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_setInteger(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jintArray values) {

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

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_setReal(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jdoubleArray values) {

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

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_setString(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jobjectArray values) {

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

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_setBoolean(JNIEnv *env, jobject obj, jlong p, jlong c, jlongArray vr, jbooleanArray values) {

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

JNIEXPORT jboolean JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1Library_free(JNIEnv *env, jobject obj, jlong p) {

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

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1CoSimulationLibrary_step(JNIEnv *env, jobject obj, jlong p, jlong c, jdouble currentCommunicationPoint, jdouble communicationStepSize, jboolean newStep) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiDoStepTYPE* fmiDoStep = fmu->fmiDoStep_;
    return (*fmiDoStep)((void*) c, currentCommunicationPoint, communicationStepSize, newStep);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_jni_Fmi1CoSimulationLibrary_cancelStep(JNIEnv *env, jobject obj, jlong p, jlong c) {
    FmuInstance* fmu = (FmuInstance*) p;
    fmiCancelStepTYPE* fmiCancelStep = fmu->fmiCancelStep_;
    return (*fmiCancelStep)((void*) c);
}

#ifdef __cplusplus
}
#endif
