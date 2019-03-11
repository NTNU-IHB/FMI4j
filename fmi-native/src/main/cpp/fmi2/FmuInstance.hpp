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

#include <stdio.h>
#include <stdlib.h>

#if defined(_MSC_VER) || defined(WIN32) || defined(__MINGW32__)
#include <windows.h>
#define DLL_HANDLE HMODULE
#else
#define DLL_HANDLE void*
#include <dlfcn.h>
#endif

#include "fmi2FunctionTypes.h"

namespace {

    template<class T>
    T loadFunction(DLL_HANDLE handle, const char *function_name) {
    #ifdef WIN32
        return (T) GetProcAddress(handle, function_name);
    #else
        return (T) dlsym(handle, function_name);
    #endif
    }

    DLL_HANDLE loadLibrary(const char* libName) {
    #ifdef WIN32
        return LoadLibrary(libName);
    #else
        return dlopen(libName, RTLD_NOW | RTLD_LOCAL);
    #endif
    }

    const char* status_to_string(fmi2Status status) {
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

    void logger(void* fmi2ComponentEnvironment, fmi2String instance_name, fmi2Status status, fmi2String category, fmi2String message, ...) {
        printf("status = %s, instanceName = %s, category = %s: %s\n", status_to_string(status), instance_name, category, message);
    }

}

class FmuInstance {

    public:

        fmi2CallbackFunctions callback_ = {
            logger, calloc, free, NULL, NULL
        };

        DLL_HANDLE handle_;

        fmi2GetVersionTYPE *fmi2GetVersion_;
        fmi2GetTypesPlatformTYPE *fmi2GetTypesPlatform_;

        fmi2SetDebugLoggingTYPE* fmi2SetDebugLogging_;

        fmi2InstantiateTYPE *fmi2Instantiate_;
        fmi2SetupExperimentTYPE *fmi2SetupExperiment_;
        fmi2EnterInitializationModeTYPE *fmi2EnterInitializationMode_;
        fmi2ExitInitializationModeTYPE *fmi2ExitInitializationMode_;

        fmi2ResetTYPE *fmi2Reset_;
        fmi2TerminateTYPE *fmi2Terminate_;

        fmi2GetIntegerTYPE *fmi2GetInteger_;
        fmi2GetRealTYPE *fmi2GetReal_;
        fmi2GetStringTYPE *fmi2GetString_;
        fmi2GetBooleanTYPE *fmi2GetBoolean_;

        fmi2SetIntegerTYPE *fmi2SetInteger_;
        fmi2SetRealTYPE *fmi2SetReal_;
        fmi2SetStringTYPE *fmi2SetString_;
        fmi2SetBooleanTYPE *fmi2SetBoolean_;

        fmi2GetFMUstateTYPE *fmi2GetFMUstate_;
        fmi2SetFMUstateTYPE *fmi2SetFMUstate_;
        fmi2FreeFMUstateTYPE *fmi2FreeFMUstate_;

        fmi2SerializedFMUstateSizeTYPE *fmi2SerializedFMUstateSize_;
        fmi2SerializeFMUstateTYPE *fmi2SerializeFMUstate_;
        fmi2DeSerializeFMUstateTYPE *fmi2DeSerializeFMUstate_;

        fmi2GetDirectionalDerivativeTYPE *fmi2GetDirectionalDerivative_;

        fmi2FreeInstanceTYPE *fmi2FreeInstance_;

        fmi2SetRealInputDerivativesTYPE *fmi2SetRealInputDerivatives_;
        fmi2GetRealOutputDerivativesTYPE *fmi2GetRealOutputDerivatives_;

        fmi2DoStepTYPE *fmi2DoStep_;
        fmi2CancelStepTYPE *fmi2CancelStep_;

        fmi2GetStatusTYPE *fmi2GetStatus_;
        fmi2GetRealStatusTYPE *fmi2GetRealStatus_;
        fmi2GetIntegerStatusTYPE *fmi2GetIntegerStatus_;
        fmi2GetBooleanStatusTYPE *fmi2GetBooleanStatus_;
        fmi2GetStringStatusTYPE *fmi2GetStringStatus_;

        fmi2EnterEventModeTYPE *fmi2EnterEventMode_;
        fmi2EnterContinuousTimeModeTYPE *fmi2EnterContinuousTimeMode_;
        fmi2SetTimeTYPE *fmi2SetTime_;
        fmi2SetContinuousStatesTYPE *fmi2SetContinuousStates_;
        fmi2GetDerivativesTYPE *fmi2GetDerivatives_;
        fmi2GetEventIndicatorsTYPE *fmi2GetEventIndicators_;
        fmi2GetContinuousStatesTYPE *fmi2GetContinuousStates_;
        fmi2GetNominalsOfContinuousStatesTYPE *fmi2GetNominalsOfContinuousStates_;
        fmi2CompletedIntegratorStepTYPE *fmi2CompletedIntegratorStep_;
        fmi2NewDiscreteStatesTYPE *fmi2NewDiscreteStates_;

        explicit FmuInstance(const char* libName) {
            handle_ = loadLibrary(libName);

            fmi2GetVersion_ = loadFunction<fmi2GetVersionTYPE *>(handle_, "fmi2GetVersion");
            fmi2GetTypesPlatform_ = loadFunction<fmi2GetTypesPlatformTYPE *>(handle_, "fmi2GetTypesPlatform");

            fmi2SetDebugLogging_ = loadFunction<fmi2SetDebugLoggingTYPE *>(handle_, "fmi2SetDebugLogging");

            fmi2Instantiate_ = loadFunction<fmi2InstantiateTYPE *>(handle_, "fmi2Instantiate");
            fmi2SetupExperiment_ = loadFunction<fmi2SetupExperimentTYPE *>(handle_, "fmi2SetupExperiment");
            fmi2EnterInitializationMode_ = loadFunction<fmi2EnterInitializationModeTYPE *>(handle_, "fmi2EnterInitializationMode");
            fmi2ExitInitializationMode_ = loadFunction<fmi2ExitInitializationModeTYPE *>(handle_, "fmi2ExitInitializationMode");

            fmi2Reset_ = loadFunction<fmi2ResetTYPE *>(handle_, "fmi2Reset");
            fmi2Terminate_ = loadFunction<fmi2TerminateTYPE *>(handle_, "fmi2Terminate");

            fmi2GetInteger_ = loadFunction<fmi2GetIntegerTYPE *>(handle_, "fmi2GetInteger");
            fmi2GetReal_ = loadFunction<fmi2GetRealTYPE *>(handle_, "fmi2GetReal");
            fmi2GetString_ = loadFunction<fmi2GetStringTYPE *>(handle_, "fmi2GetString");
            fmi2GetBoolean_ = loadFunction<fmi2GetBooleanTYPE *>(handle_, "fmi2GetBoolean");

            fmi2SetInteger_ = loadFunction<fmi2SetIntegerTYPE *>(handle_, "fmi2SetInteger");
            fmi2SetReal_ = loadFunction<fmi2SetRealTYPE *>(handle_, "fmi2SetReal");
            fmi2SetString_ = loadFunction<fmi2SetStringTYPE *>(handle_, "fmi2SetString");
            fmi2SetBoolean_ = loadFunction<fmi2SetBooleanTYPE *>(handle_, "fmi2SetBoolean");

            fmi2GetFMUstate_ = loadFunction<fmi2GetFMUstateTYPE *>(handle_, "fmi2GetFMUstate");
            fmi2SetFMUstate_ = loadFunction<fmi2SetFMUstateTYPE *>(handle_, "fmi2SetFMUstate");
            fmi2FreeFMUstate_ = loadFunction<fmi2FreeFMUstateTYPE *>(handle_, "fmi2FreeFMUstate");

            fmi2SerializedFMUstateSize_ = loadFunction<fmi2SerializedFMUstateSizeTYPE *>(handle_, "fmi2SerializedFMUstateSize");
            fmi2SerializeFMUstate_ = loadFunction<fmi2SerializeFMUstateTYPE *>(handle_, "fmi2SerializeFMUstate");
            fmi2DeSerializeFMUstate_ = loadFunction<fmi2DeSerializeFMUstateTYPE *>(handle_, "fmi2DeSerializeFMUstate");

            fmi2GetDirectionalDerivative_ = loadFunction<fmi2GetDirectionalDerivativeTYPE *>(handle_, "fmi2GetDirectionalDerivative");

            fmi2FreeInstance_ = loadFunction<fmi2FreeInstanceTYPE *>(handle_, "fmi2FreeInstance");


            fmi2SetRealInputDerivatives_ = loadFunction<fmi2SetRealInputDerivativesTYPE *>(handle_, "fmi2SetRealInputDerivatives");
            fmi2GetRealOutputDerivatives_ = loadFunction<fmi2GetRealOutputDerivativesTYPE *>(handle_, "fmi2GetRealOutputDerivatives");

            fmi2DoStep_ = loadFunction<fmi2DoStepTYPE *>(handle_, "fmi2DoStep");
            fmi2CancelStep_ = loadFunction<fmi2CancelStepTYPE *>(handle_, "fmi2CancelStep");

            fmi2GetStatus_ = loadFunction<fmi2GetStatusTYPE *>(handle_, "fmi2GetStatusTYPE");
            fmi2GetRealStatus_ = loadFunction<fmi2GetRealStatusTYPE *>(handle_, "fmi2GetRealStatusTYPE");
            fmi2GetIntegerStatus_ = loadFunction<fmi2GetIntegerStatusTYPE *>(handle_, "fmi2GetIntegerStatusTYPE");
            fmi2GetBooleanStatus_ = loadFunction<fmi2GetBooleanStatusTYPE *>(handle_, "fmi2GetBooleanStatusTYPE");
            fmi2GetStringStatus_ = loadFunction<fmi2GetStringStatusTYPE *>(handle_, "fmi2GetStringStatusTYPE");


            fmi2EnterEventMode_ = loadFunction<fmi2EnterEventModeTYPE *>(handle_, "fmi2EnterEventMode");
            fmi2EnterContinuousTimeMode_ = loadFunction<fmi2EnterContinuousTimeModeTYPE *>(
                    handle_, "fmi2EnterContinuousTimeMode");
            fmi2SetTime_ = loadFunction<fmi2SetTimeTYPE *>(handle_, "fmi2SetTime");
            fmi2SetContinuousStates_ = loadFunction<fmi2SetContinuousStatesTYPE *>(handle_, "fmi2SetContinuousStates");
            fmi2GetDerivatives_ = loadFunction<fmi2GetDerivativesTYPE *>(handle_, "fmi2GetDerivatives");
            fmi2GetEventIndicators_ = loadFunction<fmi2GetEventIndicatorsTYPE *>(handle_, "fmi2GetEventIndicators");
            fmi2GetContinuousStates_ = loadFunction<fmi2GetContinuousStatesTYPE *>(handle_, "fmi2GetContinuousStates");
            fmi2GetNominalsOfContinuousStates_ = loadFunction<fmi2GetNominalsOfContinuousStatesTYPE *>(
                    handle_, "fmi2GetNominalsOfContinuousStates");
            fmi2CompletedIntegratorStep_ = loadFunction<fmi2CompletedIntegratorStepTYPE *>(handle_, "fmi2CompletedIntegratorStep");
            fmi2NewDiscreteStates_ = loadFunction<fmi2NewDiscreteStatesTYPE *>(handle_, "fmi2NewDiscreteStates");

        };

};