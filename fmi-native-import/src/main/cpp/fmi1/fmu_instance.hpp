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
#include "fmiFunctionTypes.h"

#include <cstdarg>
#include <iostream>
#include <string>
#include <utility>

namespace
{

const char* status_to_string(fmiStatus status)
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

void logger(fmiComponent, fmiString instance_name, fmiStatus status, fmiString category, fmiString message, ...)
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

std::string full_function_name(const std::string& modelIdentifier, const std::string& function_name)
{
    return modelIdentifier + "_" + function_name;
}

} // namespace

class FmuInstance
{

public:
    fmiMeCallbackFunctions meCallback_ = {
        logger, calloc, free};

    fmiCsCallbackFunctions csCallback_ = {
        logger, calloc, free, nullptr};

    DLL_HANDLE handle_;

    /*common functions*/
    fmiGetVersionTYPE* fmiGetVersion_;
    fmiGetTypesPlatformTYPE* fmiGetTypesPlatform_;

    fmiSetDebugLoggingTYPE* fmiSetDebugLogging_;

    fmiGetIntegerTYPE* fmiGetInteger_;
    fmiGetRealTYPE* fmiGetReal_;
    fmiGetStringTYPE* fmiGetString_;
    fmiGetBooleanTYPE* fmiGetBoolean_;

    fmiSetIntegerTYPE* fmiSetInteger_;
    fmiSetRealTYPE* fmiSetReal_;
    fmiSetStringTYPE* fmiSetString_;
    fmiSetBooleanTYPE* fmiSetBoolean_;

    /*Co-simulation*/
    fmiInstantiateSlaveTYPE* fmiInstantiateSlave_;
    fmiInitializeSlaveTYPE* fmiInitializeSlave_;

    fmiSetRealInputDerivativesTYPE* fmiSetRealInputDerivatives_;
    fmiGetRealOutputDerivativesTYPE* fmiGetRealOutputDerivatives_;

    fmiDoStepTYPE* fmiDoStep_;
    fmiResetSlaveTYPE* fmiResetSlave_;

    fmiTerminateSlaveTYPE* fmiTerminateSlave_;
    fmiFreeSlaveInstanceTYPE* fmiFreeSlaveInstance_;

    /*Model Exchange*/
    fmiInstantiateModelTYPE* fmiInstantiateModel_;
    fmiInitializeTYPE* fmiInitialize_;

    fmiGetDerivativesTYPE* fmiGetDerivatives_;
    fmiGetEventIndicatorsTYPE* fmiGetEventIndicators_;

    fmiSetTimeTYPE* fmiSetTime_;
    fmiSetContinuousStatesTYPE* fmiSetContinuousStates_;
    fmiCompletedIntegratorStepTYPE* fmiCompletedIntegratorStep_;

    fmiEventUpdateTYPE* fmiEventUpdate_;
    fmiGetContinuousStatesTYPE* fmiGetContinuousStates_;
    fmiGetNominalContinuousStatesTYPE* fmiGetNominalContinuousStates_;
    fmiGetStateValueReferencesTYPE* fmiGetStateValueReferences_;

    fmiTerminateTYPE* fmiTerminate_;
    fmiFreeModelInstanceTYPE* fmiFreeModelInstance_;

    FmuInstance(const char* dir, const char* libName, const std::string& modelIdentifier)
    {
        handle_ = load_library(dir, libName);

        fmiGetVersion_ = load_function<fmiGetVersionTYPE*>(handle_, full_function_name(modelIdentifier, "_fmiGetVersion").c_str());
        fmiGetTypesPlatform_ = load_function<fmiGetTypesPlatformTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetTypesPlatform").c_str());

        fmiSetDebugLogging_ = load_function<fmiSetDebugLoggingTYPE*>(handle_, full_function_name(modelIdentifier, "fmiSetDebugLogging").c_str());

        fmiGetInteger_ = load_function<fmiGetIntegerTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetInteger").c_str());
        fmiGetReal_ = load_function<fmiGetRealTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetReal").c_str());
        fmiGetString_ = load_function<fmiGetStringTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetString").c_str());
        fmiGetBoolean_ = load_function<fmiGetBooleanTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetBoolean").c_str());

        fmiSetInteger_ = load_function<fmiSetIntegerTYPE*>(handle_, full_function_name(modelIdentifier, "fmiSetInteger").c_str());
        fmiSetReal_ = load_function<fmiSetRealTYPE*>(handle_, full_function_name(modelIdentifier, "fmiSetReal").c_str());
        fmiSetString_ = load_function<fmiSetStringTYPE*>(handle_, full_function_name(modelIdentifier, "fmiSetString").c_str());
        fmiSetBoolean_ = load_function<fmiSetBooleanTYPE*>(handle_, full_function_name(modelIdentifier, "fmiSetBoolean").c_str());

        /*Co simulation*/
        fmiInstantiateSlave_ = load_function<fmiInstantiateSlaveTYPE*>(handle_, full_function_name(modelIdentifier, "fmiInstantiateSlave").c_str());
        fmiInitializeSlave_ = load_function<fmiInitializeSlaveTYPE*>(handle_, full_function_name(modelIdentifier, "fmiInitializeSlave").c_str());

        fmiSetRealInputDerivatives_ = load_function<fmiSetRealInputDerivativesTYPE*>(handle_, full_function_name(modelIdentifier, "fmiSetRealInputDerivatives").c_str());
        fmiGetRealOutputDerivatives_ = load_function<fmiGetRealOutputDerivativesTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetRealOutputDerivatives").c_str());

        fmiDoStep_ = load_function<fmiDoStepTYPE*>(handle_, full_function_name(modelIdentifier, "fmiDoStep").c_str());
        fmiResetSlave_ = load_function<fmiResetSlaveTYPE*>(handle_, full_function_name(modelIdentifier, "fmiResetSlave").c_str());

        fmiTerminateSlave_ = load_function<fmiTerminateSlaveTYPE*>(handle_, full_function_name(modelIdentifier, "fmiTerminateSlave").c_str());
        fmiFreeSlaveInstance_ = load_function<fmiFreeSlaveInstanceTYPE*>(handle_, full_function_name(modelIdentifier, "fmiFreeSlaveInstance").c_str());

        /*Model Exchange*/
        fmiInstantiateModel_ = load_function<fmiInstantiateModelTYPE*>(handle_, full_function_name(modelIdentifier, "fmiInstantiateModel").c_str());
        fmiInitialize_ = load_function<fmiInitializeTYPE*>(handle_, full_function_name(modelIdentifier, "fmiInitialize").c_str());

        fmiGetDerivatives_ = load_function<fmiGetDerivativesTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetDerivatives").c_str());
        fmiGetEventIndicators_ = load_function<fmiGetEventIndicatorsTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetEventIndicators").c_str());

        fmiSetTime_ = load_function<fmiSetTimeTYPE*>(handle_, full_function_name(modelIdentifier, "fmiSetTime").c_str());
        fmiSetContinuousStates_ = load_function<fmiSetContinuousStatesTYPE*>(handle_, full_function_name(modelIdentifier, "fmiSetContinuousStates").c_str());

        fmiGetContinuousStates_ = load_function<fmiGetContinuousStatesTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetContinuousStates").c_str());
        fmiGetNominalContinuousStates_ = load_function<fmiGetNominalContinuousStatesTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetNominalContinuousStates").c_str());
        fmiCompletedIntegratorStep_ = load_function<fmiCompletedIntegratorStepTYPE*>(handle_, full_function_name(modelIdentifier, "fmiCompletedIntegratorStep").c_str());
        fmiGetStateValueReferences_ = load_function<fmiGetStateValueReferencesTYPE*>(handle_, full_function_name(modelIdentifier, "fmiGetStateValueReferences").c_str());

        fmiTerminate_ = load_function<fmiTerminateTYPE*>(handle_, full_function_name(modelIdentifier, "fmiTerminate").c_str());
        fmiFreeModelInstance_ = load_function<fmiFreeModelInstanceTYPE*>(handle_, full_function_name(modelIdentifier, "fmiFreeModelInstance").c_str());
    };
};
