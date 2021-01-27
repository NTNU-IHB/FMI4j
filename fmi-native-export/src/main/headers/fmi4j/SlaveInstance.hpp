
#ifndef FMI4J_SLAVEINSTANCE_HPP
#define FMI4J_SLAVEINSTANCE_HPP

#include <cppfmu/cppfmu_cs.hpp>
#include <jni.h>
#include <string>

namespace fmi4j
{

struct jstring_ref
{
    const char* c_str;
    jstring j_str;
};

class SlaveInstance : public cppfmu::SlaveInstance
{

public:
    SlaveInstance(JNIEnv* env, std::string instanceName, std::string resources);

    void SetupExperiment(cppfmu::FMIBoolean toleranceDefined, cppfmu::FMIReal tolerance, cppfmu::FMIReal tStart, cppfmu::FMIBoolean stopTimeDefined, cppfmu::FMIReal tStop) override;
    void EnterInitializationMode() override;
    void ExitInitializationMode() override;

    bool DoStep(cppfmu::FMIReal currentCommunicationPoint, cppfmu::FMIReal communicationStepSize, cppfmu::FMIBoolean newStep, cppfmu::FMIReal& endOfStep) override;
    void Reset() override;
    void Terminate() override;

    void GetReal(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIReal* value) const override;
    void SetReal(const cppfmu::FMIValueReference* vr, std::size_t nvr, const cppfmu::FMIReal* value) override;
    void SetInteger(const cppfmu::FMIValueReference* vr, std::size_t nvr, const cppfmu::FMIInteger* value) override;
    void SetBoolean(const cppfmu::FMIValueReference* vr, std::size_t nvr, const cppfmu::FMIBoolean* value) override;
    void SetString(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIString const* value) override;
    void SetAll(
        const cppfmu::FMIValueReference* intVr, std::size_t nIntvr, const cppfmu::FMIInteger* intValue,
        const cppfmu::FMIValueReference* realVr, std::size_t nRealvr, const cppfmu::FMIReal* realValue,
        const cppfmu::FMIValueReference* boolVr, std::size_t nBoolvr, const cppfmu::FMIBoolean* boolValue,
        const cppfmu::FMIValueReference* strVr, std::size_t nStrvr, const cppfmu::FMIString* strValue) override;

    void GetInteger(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIInteger* value) const override;
    void GetBoolean(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIBoolean* value) const override;
    void GetString(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIString* value) const override;
    void GetAll(
        const cppfmu::FMIValueReference* intVr, std::size_t nIntvr, cppfmu::FMIInteger* intValue,
        const cppfmu::FMIValueReference* realVr, std::size_t nRealvr, cppfmu::FMIReal* realValue,
        const cppfmu::FMIValueReference* boolVr, std::size_t nBoolvr, cppfmu::FMIBoolean* boolValue,
        const cppfmu::FMIValueReference* strVr, std::size_t nStrvr, cppfmu::FMIString* strValue) const override;

    ~SlaveInstance() override;


private:
    JavaVM* jvm_{};

    jobject classLoader_;
    jobject slaveInstance_{};

    std::string slaveName_;
    const std::string resources_;
    const std::string instanceName_;

    jmethodID ctorId_;

    jmethodID setupExperimentId_;
    jmethodID enterInitialisationModeId_;
    jmethodID exitInitializationModeId_;

    jmethodID doStepId_;
    jmethodID terminateId_;
    jmethodID closeId_;

    jmethodID getRealId_;
    jmethodID setRealId_;

    jmethodID getIntegerId_;
    jmethodID setIntegerId_;

    jmethodID getBooleanId_;
    jmethodID setBooleanId_;

    jmethodID getStringId_;
    jmethodID setStringId_;

    jmethodID getAllId_;
    jmethodID setAllId_;

    jmethodID bulkIntValuesId_;
    jmethodID bulkRealValuesId_;
    jmethodID bulkBoolValuesId_;
    jmethodID bulkStrValuesId_;

    bool canGetSetAll_ = false;

    void initialize();
    void onClose();

    mutable std::vector<jstring_ref> strBuffer;

    inline void clearStrBuffer(JNIEnv* env) const
    {
        if (!strBuffer.empty()) {
            for (auto obj : strBuffer) {
                env->ReleaseStringUTFChars(obj.j_str, obj.c_str);
            }
            strBuffer.clear();
        }
    }
};

} // namespace fmi4j

#endif
