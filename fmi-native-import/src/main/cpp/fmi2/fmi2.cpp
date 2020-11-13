#include "fmu_instance.hpp"

#include <cstdlib>
#include <jni.h>
#include <vector>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_load(JNIEnv* env, jobject, jstring dir, jstring lib_name)
{
    const char* _dir = env->GetStringUTFChars(dir, nullptr);
    const char* _lib_name = env->GetStringUTFChars(lib_name, nullptr);
    auto* fmu = new FmuInstance(_dir, _lib_name);
    env->ReleaseStringUTFChars(dir, _dir);
    env->ReleaseStringUTFChars(lib_name, _lib_name);
    return (jlong)fmu;
}

JNIEXPORT jstring JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getVersion(JNIEnv* env, jobject, jlong p)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2GetVersionTYPE* fmi2GetVersion = fmu->fmi2GetVersion_;
    const char* version = (*fmi2GetVersion)();
    return env->NewStringUTF(version);
}

JNIEXPORT jstring JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getTypesPlatform(JNIEnv* env, jobject, jlong p)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2GetTypesPlatformTYPE* fmi2GetTypesPlatform = fmu->fmi2GetTypesPlatform_;
    const char* platform = (*fmi2GetTypesPlatform)();
    return env->NewStringUTF(platform);
}

JNIEXPORT jlong JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_instantiate(JNIEnv* env, jobject, jlong p, jstring instanceName, jint type, jstring guid, jstring resourceLocation, jboolean visible, jboolean loggingOn)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const char* _guid = env->GetStringUTFChars(guid, nullptr);
    const char* _instanceName = env->GetStringUTFChars(instanceName, nullptr);
    const char* _resourceLocation = env->GetStringUTFChars(resourceLocation, nullptr);

    fmi2InstantiateTYPE* fmi2Instantiate = fmu->fmi2Instantiate_;
    fmi2Component c = (*fmi2Instantiate)(_instanceName, (fmi2Type)type, _guid, _resourceLocation, &fmu->callback_, (fmi2Boolean)visible, (fmi2Boolean)loggingOn);

    env->ReleaseStringUTFChars(guid, _guid);
    env->ReleaseStringUTFChars(instanceName, _instanceName);
    env->ReleaseStringUTFChars(resourceLocation, _resourceLocation);

    return (jlong)c;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setDebugLogging(JNIEnv* env, jobject, jlong p, jlong c, jboolean loggingOn, jobjectArray categories)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize nCategories = env->GetArrayLength(categories);
    std::vector<const char*> _categories;

    for (int i = 0; i < nCategories; i++) {
        auto category = (jstring)env->GetObjectArrayElement(categories, i);
        _categories[i] = env->GetStringUTFChars(category, nullptr);
        env->ReleaseStringUTFChars(category, _categories[i]);
    }

    fmi2SetDebugLoggingTYPE* fmi2SetDebugLogging = fmu->fmi2SetDebugLogging_;
    fmi2Status status = (*fmi2SetDebugLogging)((void*)c, (fmi2Boolean)loggingOn, nCategories, _categories.data());

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setupExperiment(JNIEnv*, jobject, jlong p, jlong c, jdouble tolerance, jdouble startTime, jdouble stopTime)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2Boolean toleranceDefined = tolerance > 0;
    fmi2Boolean stopTimeDefined = stopTime > startTime;
    fmi2SetupExperimentTYPE* fmi2Setup = fmu->fmi2SetupExperiment_;
    return (*fmi2Setup)((void*)c, toleranceDefined, tolerance, startTime, stopTimeDefined, stopTime);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_enterInitializationMode(JNIEnv*, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2EnterInitializationModeTYPE* fmi2EnterInitializationMode = fmu->fmi2EnterInitializationMode_;
    return (*fmi2EnterInitializationMode)((void*)c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_exitInitializationMode(JNIEnv*, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2ExitInitializationModeTYPE* fmi2ExitInitializationMode = fmu->fmi2ExitInitializationMode_;
    return (*fmi2ExitInitializationMode)((void*)c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_terminate(JNIEnv*, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2TerminateTYPE* fmi2Terminate = fmu->fmi2Terminate_;
    return (*fmi2Terminate)((void*)c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_reset(JNIEnv*, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2ResetTYPE* fmi2Reset = fmu->fmi2Reset_;
    return (*fmi2Reset)((void*)c);
}

JNIEXPORT void JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_freeInstance(JNIEnv*, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2FreeInstanceTYPE* fmi2FreeInstance = fmu->fmi2FreeInstance_;
    (*fmi2FreeInstance)((void*)c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getInteger(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jintArray ref)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = static_cast<fmi2ValueReference>(_vr[i]);
    }

    fmi2GetIntegerTYPE* fmi2GetInteger = fmu->fmi2GetInteger_;

    auto _ref = (fmi2Integer*)malloc(sizeof(fmi2Integer) * size);
    fmi2Status status = (*fmi2GetInteger)((void*)c, __vr, size, _ref);

    auto __ref = (jint*)malloc(sizeof(jint) * size);
    for (auto i = 0; i < size; ++i) {
        __ref[i] = static_cast<int>(_ref[i]);
    }

    env->SetIntArrayRegion(ref, 0, size, __ref);

    free(_ref);
    free(__ref);
    free(__vr);
    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getReal(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jdoubleArray ref)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    fmi2GetRealTYPE* fmi2GetReal = fmu->fmi2GetReal_;

    auto _ref = (fmi2Real*)malloc(sizeof(fmi2Real) * size);
    fmi2Status status = (*fmi2GetReal)((void*)c, __vr, size, _ref);

    env->SetDoubleArrayRegion(ref, 0, size, _ref);

    free(_ref);
    free(__vr);
    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getRealDirect(JNIEnv* env, jobject, jlong p, jlong c, jint size, jobject vr, jdoubleArray ref)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    auto _vr = static_cast<jlong*>(env->GetDirectBufferAddress(vr));
    auto _ref = static_cast<fmi2Real*>(env->GetDirectBufferAddress(ref));

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    fmi2GetRealTYPE* fmi2GetReal = fmu->fmi2GetReal_;
    fmi2Status status = (*fmi2GetReal)((void*)c, __vr, size, _ref);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getString(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jobjectArray ref)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    fmi2GetStringTYPE* fmi2GetString = fmu->fmi2GetString_;

    std::vector<fmi2String> _ref(size);
    fmi2Status status = (*fmi2GetString)((void*)c, __vr, size, _ref.data());

    for (int i = 0; i < size; i++) {
        jstring value = env->NewStringUTF(_ref[i]);
        env->SetObjectArrayElement(ref, i, value);
    }

    env->ReleaseLongArrayElements(vr, _vr, 0);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getBoolean(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jbooleanArray ref)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    auto _ref = (fmi2Boolean*)malloc(sizeof(fmi2Boolean*) * size);

    fmi2GetBooleanTYPE* fmi2GetBoolean = fmu->fmi2GetBoolean_;
    fmi2Status status = (*fmi2GetBoolean)((void*)c, __vr, size, _ref);

    for (auto i = 0; i < size; i++) {
        auto value = (jboolean)_ref[i];
        env->SetBooleanArrayRegion(ref, i, 1, &value);
    }

    free(_ref);
    free(__vr);
    env->ReleaseLongArrayElements(vr, _vr, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getAll(JNIEnv* env, jobject obj, jlong p, jlong c, jlongArray intVr, jintArray intRef, jlongArray realVr, jdoubleArray realRef, jlongArray strVr, jobjectArray strRef, jlongArray boolVr, jbooleanArray boolRef)
{
    jint intStatus = Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getInteger(env, obj, p, c, intVr, intRef);
    jint realStatus = Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getReal(env, obj, p, c, realVr, realRef);
    jint strStatus = Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getString(env, obj, p, c, strVr, strRef);
    jint boolStatus = Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getBoolean(env, obj, p, c, boolVr, boolRef);

    return (intStatus + realStatus + strStatus + boolStatus == 4) ? JNI_TRUE : JNI_FALSE;
}


JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setInteger(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jintArray values)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);
    jint* _values = env->GetIntArrayElements(values, nullptr);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    fmi2SetIntegerTYPE* fmi2SetInteger = fmu->fmi2SetInteger_;
    fmi2Status status = (*fmi2SetInteger)((void*)c, __vr, size, (fmi2Integer*)_values);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseIntArrayElements(values, _values, 0);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setReal(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jdoubleArray values)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);
    jdouble* _values = env->GetDoubleArrayElements(values, nullptr);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    fmi2SetRealTYPE* fmi2SetReal = fmu->fmi2SetReal_;
    fmi2Status status = (*fmi2SetReal)((void*)c, __vr, size, _values);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseDoubleArrayElements(values, _values, 0);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setRealDirect(JNIEnv* env, jobject, jlong p, jlong c, jint size, jobject vr, jobject values)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    auto _vr = static_cast<jlong*>(env->GetDirectBufferAddress(vr));
    auto _values = static_cast<fmi2Real*>(env->GetDirectBufferAddress(values));

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    fmi2SetRealTYPE* fmi2SetReal = fmu->fmi2SetReal_;
    fmi2Status status = (*fmi2SetReal)((void*)c, __vr, size, _values);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setString(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jobjectArray values)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    std::vector<const char*> _values(size);
    for (auto i = 0; i < size; i++) {
        auto str = (jstring)env->GetObjectArrayElement(values, i);
        _values[i] = env->GetStringUTFChars(str, nullptr);
    }

    fmi2SetStringTYPE* fmi2SetString = fmu->fmi2SetString_;
    fmi2Status status = (*fmi2SetString)((void*)c, __vr, size, _values.data());

    env->ReleaseLongArrayElements(vr, _vr, 0);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setBoolean(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jbooleanArray values)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);
    jlong* _vr = env->GetLongArrayElements(vr, nullptr);
    jboolean* _values = env->GetBooleanArrayElements(values, nullptr);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (auto i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    fmi2SetBooleanTYPE* fmi2SetBoolean = fmu->fmi2SetBoolean_;
    fmi2Status status = (*fmi2SetBoolean)((void*)c, __vr, size, (fmi2Boolean*)_values);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseBooleanArrayElements(values, _values, 0);

    free(__vr);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setAll(JNIEnv* env, jobject obj, jlong p, jlong c, jlongArray intVr, jintArray intValues, jlongArray realVr, jdoubleArray realValues, jlongArray strVr, jobjectArray strValues, jlongArray boolVr, jbooleanArray boolValues)
{
    jint intStatus = Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setInteger(env, obj, p, c, intVr, intValues);
    jint realStatus = Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setReal(env, obj, p, c, realVr, realValues);
    jint strStatus = Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setString(env, obj, p, c, strVr, strValues);
    jint boolStatus = Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setBoolean(env, obj, p, c, boolVr, boolValues);

    return (intStatus + realStatus + strStatus + boolStatus == 4) ? JNI_TRUE : JNI_FALSE;
}


JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getFMUstate(JNIEnv* env, jobject, jlong p, jlong c, jobject state)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    jclass cls = env->FindClass("no/ntnu/ihb/fmi4j/util/LongByReference");
    jfieldID id = env->GetFieldID(cls, "value", "J");

    fmi2FMUstate _state;
    fmi2GetFMUstateTYPE* fmi2GetFMUstate = fmu->fmi2GetFMUstate_;
    fmi2Status status = (*fmi2GetFMUstate)((void*)c, &_state);

    env->SetLongField(state, id, (jlong)_state);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_setFMUstate(JNIEnv*, jobject, jlong p, jlong c, jlong state)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2SetFMUstateTYPE* fmi2SetFMUstate = fmu->fmi2SetFMUstate_;
    return (*fmi2SetFMUstate)((void*)c, (fmi2FMUstate)state);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_freeFMUstate(JNIEnv*, jobject, jlong p, jlong c, jlong state)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2FreeFMUstateTYPE* fmi2FreeFMUstate = fmu->fmi2FreeFMUstate_;
    auto _state = (fmi2FMUstate)state;
    return (*fmi2FreeFMUstate)((void*)c, &_state);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_serializedFMUstateSize(JNIEnv* env, jobject, jlong p, jlong c, jlong state, jobject size)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    jclass size_cls = env->FindClass("no/ntnu/ihb/fmi4j/util/IntByReference");
    jfieldID size_id = env->GetFieldID(size_cls, "value", "I");

    size_t _size;
    fmi2SerializedFMUstateSizeTYPE* fmi2SerializedFMUstateSize = fmu->fmi2SerializedFMUstateSize_;
    fmi2Status status = (*fmi2SerializedFMUstateSize)((void*)c, (fmi2FMUstate)state, &_size);

    env->SetIntField(size, size_id, (jint)_size);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_serializeFMUstate(JNIEnv* env, jobject, jlong p, jlong c, jlong state, jbyteArray serializedState)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(serializedState);

    auto _serializedState = (fmi2Byte*)malloc(sizeof(fmi2Byte) * size);
    fmi2SerializeFMUstateTYPE* fmi2SerializeFMUstate = fmu->fmi2SerializeFMUstate_;
    fmi2Status status = (*fmi2SerializeFMUstate)((void*)c, (fmi2FMUstate)state, _serializedState, size);

    env->SetByteArrayRegion(serializedState, 0, size, (jbyte*)_serializedState);
    free(_serializedState);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_deSerializeFMUstate(JNIEnv* env, jobject, jlong p, jlong c, jobject state, jbyteArray serializedState)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    jclass cls = env->FindClass("no/ntnu/ihb/fmi4j/util/LongByReference");
    jfieldID id = env->GetFieldID(cls, "value", "J");

    const jsize size = env->GetArrayLength(serializedState);
    jbyte* _serializedState = env->GetByteArrayElements(serializedState, nullptr);

    fmi2DeSerializeFMUstateTYPE* fmi2DeSerializeFMUstate = fmu->fmi2DeSerializeFMUstate_;

    auto _state = (fmi2FMUstate)env->GetLongField(state, id);
    fmi2Status status = (*fmi2DeSerializeFMUstate)((void*)c, (fmi2Byte*)_serializedState, size, &_state);

    env->SetLongField(state, id, (jlong)_state);

    env->ReleaseByteArrayElements(serializedState, _serializedState, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_getDirectionalDerivative(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vUnknown_ref, jlongArray vKnown_ref, jdoubleArray dvKnown_ref, jdoubleArray dvUnknown_ref)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize nUnknown = env->GetArrayLength(vUnknown_ref);
    const jsize nKnown = env->GetArrayLength(vUnknown_ref);

    jlong* _vUnknown_ref = env->GetLongArrayElements(vUnknown_ref, nullptr);
    jlong* _vKnown_ref = env->GetLongArrayElements(vKnown_ref, nullptr);
    jdouble* _dvKnown_ref = env->GetDoubleArrayElements(dvKnown_ref, nullptr);
    jdouble* _dvUnknown_ref = env->GetDoubleArrayElements(dvUnknown_ref, nullptr);

    fmi2GetDirectionalDerivativeTYPE* fmi2GetDirectionalDerivative = fmu->fmi2GetDirectionalDerivative_;
    fmi2Status status = (*fmi2GetDirectionalDerivative)((void*)c, (fmi2ValueReference*)_vUnknown_ref, nUnknown, (fmi2ValueReference*)_vKnown_ref, nKnown, _dvKnown_ref, _dvUnknown_ref);

    env->ReleaseLongArrayElements(vUnknown_ref, _vUnknown_ref, 0);
    env->ReleaseLongArrayElements(vKnown_ref, _vKnown_ref, 0);

    env->ReleaseDoubleArrayElements(dvKnown_ref, _dvKnown_ref, 0);
    env->ReleaseDoubleArrayElements(dvUnknown_ref, _dvUnknown_ref, 0);

    return status;
}

JNIEXPORT jboolean JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2Library_free(JNIEnv*, jobject, jlong p)
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
Functions for FMI2 for Co-Simulation
****************************************************/
JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2CoSimulationLibrary_doStep(JNIEnv*, jobject, jlong p, jlong c, jdouble currentCommunicationPoint, jdouble communicationStepSize, jboolean noSetFMUStatePriorToCurrentPoint)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2DoStepTYPE* fmi2DoStep = fmu->fmi2DoStep_;
    return (*fmi2DoStep)((void*)c, currentCommunicationPoint, communicationStepSize, noSetFMUStatePriorToCurrentPoint);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2CoSimulationLibrary_setRealInputDerivatives(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jintArray order, jdoubleArray value)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);

    jlong* _vr = env->GetLongArrayElements(vr, 0);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    jint* _order = env->GetIntArrayElements(order, nullptr);
    jdouble* _value = env->GetDoubleArrayElements(value, nullptr);

    fmi2SetRealInputDerivativesTYPE* fmi2SetRealInputDerivatives = fmu->fmi2SetRealInputDerivatives_;
    fmi2Status status = (*fmi2SetRealInputDerivatives)((void*)c, __vr, size, (fmi2Integer*)_order, _value);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseIntArrayElements(order, _order, 0);

    env->ReleaseDoubleArrayElements(value, _value, 0);

    free(__vr);

    return status;
}


JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2CoSimulationLibrary_getRealOutputDerivatives(JNIEnv* env, jobject, jlong p, jlong c, jlongArray vr, jintArray order, jdoubleArray value)
{

    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(vr);

    jlong* _vr = env->GetLongArrayElements(vr, nullptr);

    auto __vr = (fmi2ValueReference*)malloc(sizeof(fmi2ValueReference) * size);
    for (unsigned int i = 0; i < size; ++i) {
        __vr[i] = (fmi2ValueReference)_vr[i];
    }

    jint* _order = env->GetIntArrayElements(order, nullptr);
    auto _value = (fmi2Real*)malloc(sizeof(fmi2Real) * size);

    fmi2GetRealOutputDerivativesTYPE* fmi2GetRealOutputDerivatives = fmu->fmi2GetRealOutputDerivatives_;
    fmi2Status status = (*fmi2GetRealOutputDerivatives)((void*)c, __vr, size, (fmi2Integer*)_order, _value);

    env->ReleaseLongArrayElements(vr, _vr, 0);
    env->ReleaseIntArrayElements(order, _order, 0);

    env->SetDoubleArrayRegion(value, 0, size, _value);
    free(_value);
    free(__vr);

    return status;
}

/***************************************************
Functions for FMI2 for Model Exchange
****************************************************/
JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_enterEventMode(JNIEnv*, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2EnterEventModeTYPE* fmi2EnterEventMode = fmu->fmi2EnterEventMode_;
    return (*fmi2EnterEventMode)((void*)c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_enterContinuousTimeMode(JNIEnv*, jobject, jlong p, jlong c)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2EnterContinuousTimeModeTYPE* fmi2EnterContinuousTimeMode = fmu->fmi2EnterContinuousTimeMode_;
    return (*fmi2EnterContinuousTimeMode)((void*)c);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_setTime(JNIEnv*, jobject, jlong p, jlong c, jdouble time)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);
    fmi2SetTimeTYPE* fmi2SetTime = fmu->fmi2SetTime_;
    return (*fmi2SetTime)((void*)c, time);
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_setContinuousStates(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray x)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(x);
    jdouble* _x = env->GetDoubleArrayElements(x, nullptr);

    fmi2SetContinuousStatesTYPE* fmi2SetContinuousStates = fmu->fmi2SetContinuousStates_;
    fmi2Status status = (*fmi2SetContinuousStates)((void*)c, _x, size);

    env->ReleaseDoubleArrayElements(x, _x, 0);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_getDerivatives(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray derivatives)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(derivatives);
    auto _derivatives = (fmi2Real*)malloc(sizeof(fmi2Real) * size);

    fmi2GetDerivativesTYPE* fmi2GetDerivatives = fmu->fmi2GetDerivatives_;
    fmi2Status status = (*fmi2GetDerivatives)((void*)c, _derivatives, size);

    env->SetDoubleArrayRegion(derivatives, 0, size, _derivatives);
    free(_derivatives);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_getEventIndicators(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray eventIndicators)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(eventIndicators);
    auto _eventIndicators = (fmi2Real*)malloc(sizeof(fmi2Real) * size);

    fmi2GetEventIndicatorsTYPE* fmi2GetEventIndicators = fmu->fmi2GetEventIndicators_;
    fmi2Status status = (*fmi2GetEventIndicators)((void*)c, _eventIndicators, size);

    env->SetDoubleArrayRegion(eventIndicators, 0, size, _eventIndicators);
    free(_eventIndicators);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_getContinuousStates(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray x)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(x);
    auto _x = (fmi2Real*)malloc(sizeof(fmi2Real) * size);

    fmi2GetContinuousStatesTYPE* fmi2GetContinuousStates = fmu->fmi2GetContinuousStates_;
    fmi2Status status = (*fmi2GetContinuousStates)((void*)c, _x, size);

    env->SetDoubleArrayRegion(x, 0, size, _x);
    free(_x);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_getNominalsOfContinuousStates(JNIEnv* env, jobject, jlong p, jlong c, jdoubleArray x_nominal)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    const jsize size = env->GetArrayLength(x_nominal);
    auto _x_nominal = (fmi2Real*)malloc(sizeof(fmi2Real) * size);

    fmi2GetNominalsOfContinuousStatesTYPE* fmi2GetNominalsOfContinuousStates = fmu->fmi2GetNominalsOfContinuousStates_;
    fmi2Status status = (*fmi2GetNominalsOfContinuousStates)((void*)c, _x_nominal, size);

    env->SetDoubleArrayRegion(x_nominal, 0, size, _x_nominal);
    free(_x_nominal);

    return status;
}

JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_completedIntegratorStep(JNIEnv* env, jobject, jlong p, jlong c, jboolean noSetFMUStatePriorToCurrentPoint, jobject enterEventMode, jobject terminateSimulation)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    fmi2Boolean _enterEventMode;
    fmi2Boolean _terminateSimulation;

    jclass cls = env->FindClass("no/ntnu/ihb/fmi4j/util/BooleanByReference");
    jfieldID id = env->GetFieldID(cls, "value", "Z");

    fmi2CompletedIntegratorStepTYPE* fmi2CompletedIntegratorStep = fmu->fmi2CompletedIntegratorStep_;
    fmi2Status status = (*fmi2CompletedIntegratorStep)((void*)c, noSetFMUStatePriorToCurrentPoint, &_enterEventMode, &_terminateSimulation);

    env->SetBooleanField(enterEventMode, id, _enterEventMode);
    env->SetBooleanField(terminateSimulation, id, _terminateSimulation);

    return status;
}


JNIEXPORT jint JNICALL Java_no_ntnu_ihb_fmi4j_importer_fmi2_jni_Fmi2ModelExchangeLibrary_newDiscreteStates(JNIEnv* env, jobject obj, jlong p, jlong c, jobject states)
{
    auto fmu = reinterpret_cast<FmuInstance*>(p);

    jclass cls = env->FindClass("no/ntnu/ihb/fmi4j/importer/fmi2/jni/EventInfo");

    jfieldID newDiscreteStatesNeeded_id = env->GetFieldID(cls, "newDiscreteStatesNeeded", "Z");
    jfieldID terminateSimulation_id = env->GetFieldID(cls, "terminateSimulation", "Z");
    jfieldID nominalsOfContinuousStatesChanged_id = env->GetFieldID(cls, "nominalsOfContinuousStatesChanged", "Z");
    jfieldID valuesOfContinuousStatesChanged_id = env->GetFieldID(cls, "valuesOfContinuousStatesChanged", "Z");
    jfieldID nextEventTimeDefined_id = env->GetFieldID(cls, "nextEventTimeDefined", "Z");
    jfieldID nextEventTime_id = env->GetFieldID(cls, "nextEventTime", "D");

    fmi2EventInfo _states = {
        env->GetBooleanField(states, newDiscreteStatesNeeded_id),
        env->GetBooleanField(states, terminateSimulation_id),
        env->GetBooleanField(states, nominalsOfContinuousStatesChanged_id),
        env->GetBooleanField(states, valuesOfContinuousStatesChanged_id),
        env->GetBooleanField(states, nextEventTimeDefined_id),
        env->GetDoubleField(states, nextEventTime_id),
    };

    fmi2NewDiscreteStatesTYPE* fmi2NewDiscreteStates = fmu->fmi2NewDiscreteStates_;
    fmi2Status status = (*fmi2NewDiscreteStates)((void*)c, &_states);

    env->SetBooleanField(states, newDiscreteStatesNeeded_id, _states.newDiscreteStatesNeeded);
    env->SetBooleanField(states, terminateSimulation_id, _states.terminateSimulation);
    env->SetBooleanField(states, nominalsOfContinuousStatesChanged_id, _states.nominalsOfContinuousStatesChanged);
    env->SetBooleanField(states, valuesOfContinuousStatesChanged_id, _states.valuesOfContinuousStatesChanged);
    env->SetBooleanField(states, nextEventTimeDefined_id, _states.nextEventTimeDefined);
    env->SetDoubleField(states, nextEventTime_id, _states.nextEventTime);

    return status;
}

#ifdef __cplusplus
}
#endif
