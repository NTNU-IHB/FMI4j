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

package no.ntnu.ihb.fmi4j.importer.misc

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Supplier

/**
 *
 * @author Lars Ivar Hatledal
 */
internal class FmiMethod private constructor(
        val name: String
) {

    var bit: Int = getAndIncrement()

    companion object {

        val fmi2GetTypesPlatform = FmiMethod("fmi2GetTypesPlatform")

        val fmi2GetVersion = FmiMethod("fmi2GetVersion")

        val fmi2SetDebugLogging = FmiMethod("fmi2SetDebugLogging")

        val fmi2Instantiate = FmiMethod("fmi2Instantiate")

        val fmi2FreeInstance = FmiMethod("fmi2FreeInstance")

        val fmi2SetupExperiment = FmiMethod("fmi2SetupExperiment")

        val fmi2EnterInitializationMode = FmiMethod("fmi2EnterInitializationMode")

        val fmi2ExitInitializationMode = FmiMethod("fmi2ExitInitializationMode")

        val fmi2Terminate = FmiMethod("fmi2Terminate")

        val fmi2Reset = FmiMethod("fmi2Reset")

        val fmi2GetReal = FmiMethod("fmi2GetReal")

        val fmi2GetInteger = FmiMethod("fmi2GetInteger")

        val fmi2GetBoolean = FmiMethod("fmi2GetBoolean")

        val fmi2GetString = FmiMethod("fmi2GetString")

        val fmi2SetReal = FmiMethod("fmi2SetReal")

        val fmi2SetInteger = FmiMethod("fmi2SetInteger")

        val fmi2SetBoolean = FmiMethod("fmi2SetBoolean")

        val fmi2SetString = FmiMethod("fmi2SetString")

        val fmi2GetFMUstate = FmiMethod("fmi2GetFMUstate")

        val fmi2SetFMUstate = FmiMethod("fmi2SetFMUstate")

        val fmi2FreeFMUstate = FmiMethod("fmi2FreeFMUstate")

        val fmi2SerializedFMUstateSize = FmiMethod("fmi2SerializedFMUstateSize")

        val fmi2SerializeFMUstate = FmiMethod("fmi2SerializeFMUstate")

        val fmi2DeSerializeFMUstate = FmiMethod("fmi2DeSerializeFMUstate")

        val fmi2GetDirectionalDerivative = FmiMethod("fmi2GetDirectionalDerivative")


        val fmi2SetRealInputDerivatives = FmiMethod("fmi2SetRealInputDerivatives")

        val fmi2GetRealOutputDerivatives = FmiMethod("fmi2GetRealOutputDerivatives")

        val fmi2DoStep = FmiMethod("fmi2DoStep")

        val fmi2CancelStep = FmiMethod("fmi2CancelStep")

        val fmi2GetStatus = FmiMethod("fmi2GetStatus")

        val fmi2GetRealStatus = FmiMethod("fmi2GetRealStatus")

        val fmi2GetIntegerStatus = FmiMethod("fmi2GetIntegerStatus")

        val fmi2GetBooleanStatus = FmiMethod("fmi2GetBooleanStatus")

        val fmi2GetStringStatus = FmiMethod("fmi2GetStringStatus")


        val fmi2EnterEventMode = FmiMethod("fmi2EnterEventMode")

        val fmi2NewDiscreteStates = FmiMethod("fmi2NewDiscreteStates")

        val fmi2EnterContinuousTimeMode = FmiMethod("fmi2EnterContinuousTimeMode")

        val fmi2CompletedIntegratorStep = FmiMethod("fmi2CompletedIntegratorStep")

        val fmi2SetTime = FmiMethod("fmi2SetTime")

        val fmi2SetContinuousStates = FmiMethod("fmi2SetContinuousStates")

        val fmi2GetEventIndicators = FmiMethod("fmi2GetEventIndicators")

        val fmi2GetContinuousStates = FmiMethod("fmi2GetContinuousStates")

        val fmi2GetDerivatives = FmiMethod("fmi2GetDerivatives")

        val fmi2GetNominalsOfContinuousStates = FmiMethod("fmi2GetNominalsOfContinuousStates")

        private var bitCount = 0

        private fun getAndIncrement(): Int {
            return 1 shl bitCount++
        }

    }

}

internal class FmiState private constructor(
        val name: String,
        vararg methods: FmiMethod
) {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(FmiState::class.java)


        val START = FmiState("START",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2Instantiate)


        val END = FmiState("END",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2Instantiate)


        val INSTANTIATED = FmiState("INSTANTIATED",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging,
                FmiMethod.fmi2FreeInstance, FmiMethod.fmi2SetupExperiment, FmiMethod.fmi2EnterInitializationMode, FmiMethod.fmi2Reset,
                FmiMethod.fmi2SetReal, FmiMethod.fmi2SetInteger, FmiMethod.fmi2SetBoolean, FmiMethod.fmi2SetString,
                FmiMethod.fmi2GetFMUstate, FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2SerializedFMUstateSize,
                FmiMethod.fmi2SerializeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2SetRealInputDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)


        val INITIALISATION_MODE = FmiState("INITIALISATION_MODE",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2ExitInitializationMode,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString,
                FmiMethod.fmi2GetFMUstate, FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate,
                FmiMethod.fmi2SetReal, FmiMethod.fmi2SetInteger, FmiMethod.fmi2SetBoolean, FmiMethod.fmi2SetString,
                FmiMethod.fmi2GetDirectionalDerivative, FmiMethod.fmi2SetRealInputDerivatives, FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates, FmiMethod.fmi2GetDerivatives)

        ////////////////////////////////////////////////////////////////////////CO-SIMULATION///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        val STEP_COMPLETE = FmiState("STEP_COMPLETE",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative, FmiMethod.fmi2SetRealInputDerivatives,
                FmiMethod.fmi2GetStatus, FmiMethod.fmi2GetRealStatus, FmiMethod.fmi2GetIntegerStatus, FmiMethod.fmi2GetBooleanStatus, FmiMethod.fmi2GetStringStatus)


        val STEP_IN_PROGRESS = FmiState("STEP_IN_PROGRESS",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetStatus, FmiMethod.fmi2GetRealStatus, FmiMethod.fmi2GetIntegerStatus, FmiMethod.fmi2GetBooleanStatus, FmiMethod.fmi2GetStringStatus)


        val STEP_FAILED = FmiState("STEP_FAILED",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetRealOutputDerivatives)


        val STEP_CANCELED = FmiState("STEP_CANCELED",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetRealOutputDerivatives)
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////MODEL EXCHANGE/////////////////////////////////////////////////////////////////////////////////////////////////////////////

        val EVENT_MODE = FmiState("EVENT_MODE",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString,
                FmiMethod.fmi2SetReal, FmiMethod.fmi2SetInteger, FmiMethod.fmi2SetBoolean, FmiMethod.fmi2SetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative, FmiMethod.fmi2EnterEventMode,
                FmiMethod.fmi2NewDiscreteStates, FmiMethod.fmi2EnterContinuousTimeMode, FmiMethod.fmi2SetTime, FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates,
                FmiMethod.fmi2GetDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)


        val CONTINUOUS_TIME_MODE = FmiState("CONTINUOUS_TIME_MODE",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString,
                FmiMethod.fmi2SetReal, FmiMethod.fmi2GetFMUstate, FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate,
                FmiMethod.fmi2GetDirectionalDerivative, FmiMethod.fmi2EnterEventMode, FmiMethod.fmi2CompletedIntegratorStep,
                FmiMethod.fmi2SetTime, FmiMethod.fmi2SetContinuousStates, FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates,
                FmiMethod.fmi2GetDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        val TERMINATED = FmiState("TERMINATED",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetRealOutputDerivatives, FmiMethod.fmi2GetStatus, FmiMethod.fmi2GetRealStatus, FmiMethod.fmi2GetIntegerStatus, FmiMethod.fmi2GetBooleanStatus, FmiMethod.fmi2GetStringStatus,
                FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates, FmiMethod.fmi2GetDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)


        val ERROR = FmiState("ERROR",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates, FmiMethod.fmi2GetDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)


        val FATAL = FmiState("FATAL")
    }

    private var allowedStates: Int

    init {
        allowedStates = 0
        for (state in methods) {
            allowedStates = allowedStates or state.bit
        }
    }

    internal fun isCallLegalDuringState(fmiMethod: FmiMethod, additionalRestrictions: Supplier<Boolean>? = null, log: Boolean = true): Boolean {

        fun logMsg(): String {
            return "FMI method ${fmiMethod.name} cannot be called during $name state!"
        }

        return if (fmiMethod.bit and allowedStates == 0) {
            false.also { if (log) LOG.warn(logMsg()) }
        } else {
            if (additionalRestrictions != null) {
                if (additionalRestrictions.get()) true else false.also { if (log) LOG.warn(logMsg()) }
            } else {
                true
            }

        }

    }

}