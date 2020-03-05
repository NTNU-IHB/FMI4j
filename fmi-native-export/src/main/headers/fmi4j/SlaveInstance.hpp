
#ifndef FMI4J_SLAVEINSTANCE_HPP
#define FMI4J_SLAVEINSTANCE_HPP

#include <cppfmu/cppfmu_cs.hpp>

#include <jni.h>
#include <string>

namespace fmi4j
{

class SlaveInstance : public cppfmu::SlaveInstance
{

public:
    SlaveInstance(JNIEnv* env, const std::string& instanceName, const std::string& resources);

    void initialize();

    void SetupExperiment(cppfmu::FMIBoolean toleranceDefined, cppfmu::FMIReal tolerance, cppfmu::FMIReal tStart, cppfmu::FMIBoolean stopTimeDefined, cppfmu::FMIReal tStop) override;
    void EnterInitializationMode() override;
    void ExitInitializationMode() override;
    void Terminate() override;
    void Reset() override;
    bool DoStep(cppfmu::FMIReal currentCommunicationPoint, cppfmu::FMIReal communicationStepSize, cppfmu::FMIBoolean newStep, cppfmu::FMIReal& endOfStep) override;

    void GetReal(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIReal* value) const override;
    void SetReal(const cppfmu::FMIValueReference* vr, std::size_t nvr, const cppfmu::FMIReal* value) override;
    void SetInteger(const cppfmu::FMIValueReference* vr, std::size_t nvr, const cppfmu::FMIInteger* value) override;
    void SetBoolean(const cppfmu::FMIValueReference* vr, std::size_t nvr, const cppfmu::FMIBoolean* value) override;
    void SetString(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIString const* value) override;
    void GetInteger(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIInteger* value) const override;
    void GetBoolean(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIBoolean* value) const override;
    void GetString(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIString* value) const override;

    ~SlaveInstance() override;


private:
    JavaVM* jvm_;

    jobject classLoader_;
    jobject slaveInstance_;

    std::string slaveName_;
    const std::string resources_;
    const std::string instanceName_;

    jmethodID ctorId_;

    jmethodID setupExperimentId_;
    jmethodID enterInitialisationModeId_;
    jmethodID exitInitializationModeId_;

    jmethodID doStepId_;
    jmethodID terminateId_;

    jmethodID getRealId_;
    jmethodID setRealId_;

    jmethodID getIntegerId_;
    jmethodID setIntegerId_;

    jmethodID getBooleanId_;
    jmethodID setBooleanId_;

    jmethodID getStringId_;
    jmethodID setStringId_;
};

} // namespace fmi4j

#endif
