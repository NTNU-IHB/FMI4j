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

#ifdef __cplusplus
}
#endif
