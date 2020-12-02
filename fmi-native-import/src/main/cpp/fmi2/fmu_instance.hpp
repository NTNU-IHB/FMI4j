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

#include "../util.hpp"
#include "fmi2FunctionTypes.h"

#include <cstdarg>

namespace
{

const char* status_to_string(fmi2Status status)
{
    switch (status) {
        case 0: return "OK";
        case 1: return "Warning";
        case 2: return "Discard";
        case 3: return "Error";
        case 4: return "Fatal";
        case 5: return "Pending";
        default: return "Unknown";
    }
}

void logger(void*, fmi2String instance_name, fmi2Status status, fmi2String category, fmi2String message, ...)
{
    char msg[1000];
    va_list argp;

    va_start(argp, message);
    vsprintf(msg, message, argp);
    va_end(argp);

    if (!instance_name) instance_name = "?";
    if (!category) category = "?";

    printf("[FMI native bridge] status = %s, instanceName = %s, category = %s: %s\n", status_to_string(status), instance_name, category, msg);
}

} // namespace

class FmuInstance
{

public:
    fmi2CallbackFunctions callback_ = {
        logger, calloc, free, nullptr, nullptr};

    DLL_HANDLE handle_;

    fmi2GetVersionTYPE* fmi2GetVersion_;
    fmi2GetTypesPlatformTYPE* fmi2GetTypesPlatform_;

    fmi2SetDebugLoggingTYPE* fmi2SetDebugLogging_;

    fmi2InstantiateTYPE* fmi2Instantiate_;
    fmi2SetupExperimentTYPE* fmi2SetupExperiment_;
    fmi2EnterInitializationModeTYPE* fmi2EnterInitializationMode_;
    fmi2ExitInitializationModeTYPE* fmi2ExitInitializationMode_;

    fmi2ResetTYPE* fmi2Reset_;
    fmi2TerminateTYPE* fmi2Terminate_;

    fmi2GetIntegerTYPE* fmi2GetInteger_;
    fmi2GetRealTYPE* fmi2GetReal_;
    fmi2GetBooleanTYPE* fmi2GetBoolean_;
    fmi2GetStringTYPE* fmi2GetString_;
    fmi2GetAllTYPE* fmi2GetAllType_;

    fmi2SetIntegerTYPE* fmi2SetInteger_;
    fmi2SetRealTYPE* fmi2SetReal_;
    fmi2SetBooleanTYPE* fmi2SetBoolean_;
    fmi2SetStringTYPE* fmi2SetString_;
    fmi2SetAllTYPE* fmi2SetAllType_;

    fmi2GetFMUstateTYPE* fmi2GetFMUstate_;
    fmi2SetFMUstateTYPE* fmi2SetFMUstate_;
    fmi2FreeFMUstateTYPE* fmi2FreeFMUstate_;

    fmi2SerializedFMUstateSizeTYPE* fmi2SerializedFMUstateSize_;
    fmi2SerializeFMUstateTYPE* fmi2SerializeFMUstate_;
    fmi2DeSerializeFMUstateTYPE* fmi2DeSerializeFMUstate_;

    fmi2GetDirectionalDerivativeTYPE* fmi2GetDirectionalDerivative_;

    fmi2FreeInstanceTYPE* fmi2FreeInstance_;

    fmi2SetRealInputDerivativesTYPE* fmi2SetRealInputDerivatives_;
    fmi2GetRealOutputDerivativesTYPE* fmi2GetRealOutputDerivatives_;

    fmi2DoStepTYPE* fmi2DoStep_;
    fmi2CancelStepTYPE* fmi2CancelStep_;

    fmi2GetStatusTYPE* fmi2GetStatus_;
    fmi2GetRealStatusTYPE* fmi2GetRealStatus_;
    fmi2GetIntegerStatusTYPE* fmi2GetIntegerStatus_;
    fmi2GetBooleanStatusTYPE* fmi2GetBooleanStatus_;
    fmi2GetStringStatusTYPE* fmi2GetStringStatus_;

    fmi2EnterEventModeTYPE* fmi2EnterEventMode_;
    fmi2EnterContinuousTimeModeTYPE* fmi2EnterContinuousTimeMode_;
    fmi2SetTimeTYPE* fmi2SetTime_;
    fmi2SetContinuousStatesTYPE* fmi2SetContinuousStates_;
    fmi2GetDerivativesTYPE* fmi2GetDerivatives_;
    fmi2GetEventIndicatorsTYPE* fmi2GetEventIndicators_;
    fmi2GetContinuousStatesTYPE* fmi2GetContinuousStates_;
    fmi2GetNominalsOfContinuousStatesTYPE* fmi2GetNominalsOfContinuousStates_;
    fmi2CompletedIntegratorStepTYPE* fmi2CompletedIntegratorStep_;
    fmi2NewDiscreteStatesTYPE* fmi2NewDiscreteStates_;

    explicit FmuInstance(const char* dir, const char* libName)
    {
        handle_ = load_library(dir, libName);

        fmi2GetVersion_ = load_function<fmi2GetVersionTYPE*>(handle_, "fmi2GetVersion");
        fmi2GetTypesPlatform_ = load_function<fmi2GetTypesPlatformTYPE*>(handle_, "fmi2GetTypesPlatform");

        fmi2SetDebugLogging_ = load_function<fmi2SetDebugLoggingTYPE*>(handle_, "fmi2SetDebugLogging");

        fmi2Instantiate_ = load_function<fmi2InstantiateTYPE*>(handle_, "fmi2Instantiate");
        fmi2SetupExperiment_ = load_function<fmi2SetupExperimentTYPE*>(handle_, "fmi2SetupExperiment");
        fmi2EnterInitializationMode_ = load_function<fmi2EnterInitializationModeTYPE*>(handle_, "fmi2EnterInitializationMode");
        fmi2ExitInitializationMode_ = load_function<fmi2ExitInitializationModeTYPE*>(handle_, "fmi2ExitInitializationMode");

        fmi2Reset_ = load_function<fmi2ResetTYPE*>(handle_, "fmi2Reset");
        fmi2Terminate_ = load_function<fmi2TerminateTYPE*>(handle_, "fmi2Terminate");

        fmi2GetInteger_ = load_function<fmi2GetIntegerTYPE*>(handle_, "fmi2GetInteger");
        fmi2GetReal_ = load_function<fmi2GetRealTYPE*>(handle_, "fmi2GetReal");
        fmi2GetBoolean_ = load_function<fmi2GetBooleanTYPE*>(handle_, "fmi2GetBoolean");
        fmi2GetString_ = load_function<fmi2GetStringTYPE*>(handle_, "fmi2GetString");
        fmi2GetAllType_ = load_function<fmi2GetAllTYPE*>(handle_, "fmi2SetAll");

        fmi2SetInteger_ = load_function<fmi2SetIntegerTYPE*>(handle_, "fmi2SetInteger");
        fmi2SetReal_ = load_function<fmi2SetRealTYPE*>(handle_, "fmi2SetReal");
        fmi2SetBoolean_ = load_function<fmi2SetBooleanTYPE*>(handle_, "fmi2SetBoolean");
        fmi2SetString_ = load_function<fmi2SetStringTYPE*>(handle_, "fmi2SetString");
        fmi2SetAllType_ = load_function<fmi2SetAllTYPE*>(handle_, "fmi2SetAll");

        fmi2GetFMUstate_ = load_function<fmi2GetFMUstateTYPE*>(handle_, "fmi2GetFMUstate");
        fmi2SetFMUstate_ = load_function<fmi2SetFMUstateTYPE*>(handle_, "fmi2SetFMUstate");
        fmi2FreeFMUstate_ = load_function<fmi2FreeFMUstateTYPE*>(handle_, "fmi2FreeFMUstate");

        fmi2SerializedFMUstateSize_ = load_function<fmi2SerializedFMUstateSizeTYPE*>(handle_, "fmi2SerializedFMUstateSize");
        fmi2SerializeFMUstate_ = load_function<fmi2SerializeFMUstateTYPE*>(handle_, "fmi2SerializeFMUstate");
        fmi2DeSerializeFMUstate_ = load_function<fmi2DeSerializeFMUstateTYPE*>(handle_, "fmi2DeSerializeFMUstate");

        fmi2GetDirectionalDerivative_ = load_function<fmi2GetDirectionalDerivativeTYPE*>(handle_, "fmi2GetDirectionalDerivative");

        fmi2FreeInstance_ = load_function<fmi2FreeInstanceTYPE*>(handle_, "fmi2FreeInstance");


        fmi2SetRealInputDerivatives_ = load_function<fmi2SetRealInputDerivativesTYPE*>(handle_, "fmi2SetRealInputDerivatives");
        fmi2GetRealOutputDerivatives_ = load_function<fmi2GetRealOutputDerivativesTYPE*>(handle_, "fmi2GetRealOutputDerivatives");

        fmi2DoStep_ = load_function<fmi2DoStepTYPE*>(handle_, "fmi2DoStep");
        fmi2CancelStep_ = load_function<fmi2CancelStepTYPE*>(handle_, "fmi2CancelStep");

        fmi2GetStatus_ = load_function<fmi2GetStatusTYPE*>(handle_, "fmi2GetStatusTYPE");
        fmi2GetRealStatus_ = load_function<fmi2GetRealStatusTYPE*>(handle_, "fmi2GetRealStatusTYPE");
        fmi2GetIntegerStatus_ = load_function<fmi2GetIntegerStatusTYPE*>(handle_, "fmi2GetIntegerStatusTYPE");
        fmi2GetBooleanStatus_ = load_function<fmi2GetBooleanStatusTYPE*>(handle_, "fmi2GetBooleanStatusTYPE");
        fmi2GetStringStatus_ = load_function<fmi2GetStringStatusTYPE*>(handle_, "fmi2GetStringStatusTYPE");


        fmi2EnterEventMode_ = load_function<fmi2EnterEventModeTYPE*>(handle_, "fmi2EnterEventMode");
        fmi2EnterContinuousTimeMode_ = load_function<fmi2EnterContinuousTimeModeTYPE*>(handle_, "fmi2EnterContinuousTimeMode");
        fmi2SetTime_ = load_function<fmi2SetTimeTYPE*>(handle_, "fmi2SetTime");
        fmi2SetContinuousStates_ = load_function<fmi2SetContinuousStatesTYPE*>(handle_, "fmi2SetContinuousStates");
        fmi2GetDerivatives_ = load_function<fmi2GetDerivativesTYPE*>(handle_, "fmi2GetDerivatives");
        fmi2GetEventIndicators_ = load_function<fmi2GetEventIndicatorsTYPE*>(handle_, "fmi2GetEventIndicators");
        fmi2GetContinuousStates_ = load_function<fmi2GetContinuousStatesTYPE*>(handle_, "fmi2GetContinuousStates");
        fmi2GetNominalsOfContinuousStates_ = load_function<fmi2GetNominalsOfContinuousStatesTYPE*>(handle_, "fmi2GetNominalsOfContinuousStates");
        fmi2CompletedIntegratorStep_ = load_function<fmi2CompletedIntegratorStepTYPE*>(handle_, "fmi2CompletedIntegratorStep");
        fmi2NewDiscreteStates_ = load_function<fmi2NewDiscreteStatesTYPE*>(handle_, "fmi2NewDiscreteStates");
    };
};
