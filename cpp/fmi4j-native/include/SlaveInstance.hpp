
#ifndef FMI4J_SLAVEINSTANCE_HPP
#define FMI4J_SLAVEINSTANCE_HPP

#include <jni.h>
#include <cppfmu_cs.hpp>

namespace fmi4j {

class SlaveInstance: public cppfmu::SlaveInstance {

public:

    SlaveInstance(const cppfmu::Memory& memory, JNIEnv* env, const char* slaveClass);

    void SetupExperiment(cppfmu::FMIBoolean toleranceDefined, cppfmu::FMIReal tolerance, cppfmu::FMIReal tStart, cppfmu::FMIBoolean stopTimeDefined, cppfmu::FMIReal tStop) override;
    void EnterInitializationMode() override;
    void ExitInitializationMode() override;
    void Terminate() override;
    void Reset() override;
    bool DoStep(cppfmu::FMIReal currentCommunicationPoint, cppfmu::FMIReal communicationStepSize, cppfmu::FMIBoolean newStep, cppfmu::FMIReal& endOfStep) override;

    ~SlaveInstance() override;
    void GetReal(const cppfmu::FMIValueReference* vr, std::size_t nvr, cppfmu::FMIReal* value) const override;

private:
    JavaVM* jvm_;
    jobject slave_;

    jmethodID setupExperimentId_;
    jmethodID enterInitialisationModeId_;
    jmethodID exitInitializationModeId_;

    jmethodID doStepId_;
    jmethodID resetId_;
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

}

#endif
