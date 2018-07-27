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

package no.mechatronics.sfi.fmi4j.importer.misc

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

    companion object {

        @JvmStatic
        val fmi2GetTypesPlatform = FmiMethod("fmi2GetTypesPlatform")
        @JvmStatic
        val fmi2GetVersion = FmiMethod("fmi2GetVersion")
        @JvmStatic
        val fmi2SetDebugLogging = FmiMethod("fmi2SetDebugLogging")
        @JvmStatic
        val fmi2Instantiate = FmiMethod("fmi2Instantiate")
        @JvmStatic
        val fmi2FreeInstance = FmiMethod("fmi2FreeInstance")
        @JvmStatic
        val fmi2SetupExperiment = FmiMethod("fmi2SetupExperiment")
        @JvmStatic
        val fmi2EnterInitializationMode = FmiMethod("fmi2EnterInitializationMode")
        @JvmStatic
        val fmi2ExitInitializationMode = FmiMethod("fmi2ExitInitializationMode")
        @JvmStatic
        val fmi2Terminate = FmiMethod("fmi2Terminate")
        @JvmStatic
        val fmi2Reset = FmiMethod("fmi2Reset")
        @JvmStatic
        val fmi2GetReal = FmiMethod("fmi2GetReal")
        @JvmStatic
        val fmi2GetInteger = FmiMethod("fmi2GetInteger")
        @JvmStatic
        val fmi2GetBoolean = FmiMethod("fmi2GetBoolean")
        @JvmStatic
        val fmi2GetString = FmiMethod("fmi2GetString")
        @JvmStatic
        val fmi2SetReal = FmiMethod("fmi2SetReal")
        @JvmStatic
        val fmi2SetInteger = FmiMethod("fmi2SetInteger")
        @JvmStatic
        val fmi2SetBoolean = FmiMethod("fmi2SetBoolean")
        @JvmStatic
        val fmi2SetString = FmiMethod("fmi2SetString")
        @JvmStatic
        val fmi2GetFMUstate = FmiMethod("fmi2GetFMUstate")
        @JvmStatic
        val fmi2SetFMUstate = FmiMethod("fmi2SetFMUstate")
        @JvmStatic
        val fmi2FreeFMUstate = FmiMethod("fmi2FreeFMUstate")
        @JvmStatic
        val fmi2SerializedFMUstateSize = FmiMethod("fmi2SerializedFMUstateSize")
        @JvmStatic
        val fmi2SerializeFMUstate = FmiMethod("fmi2SerializeFMUstate")
        @JvmStatic
        val fmi2DeSerializeFMUstate = FmiMethod("fmi2DeSerializeFMUstate")
        @JvmStatic
        val fmi2GetDirectionalDerivative = FmiMethod("fmi2GetDirectionalDerivative")

        @JvmStatic
        val fmi2SetRealInputDerivatives = FmiMethod("fmi2SetRealInputDerivatives")
        @JvmStatic
        val fmi2GetRealOutputDerivatives = FmiMethod("fmi2GetRealOutputDerivatives")
        @JvmStatic
        val fmi2DoStep = FmiMethod("fmi2DoStep")
        @JvmStatic
        val fmi2CancelStep = FmiMethod("fmi2CancelStep")
        @JvmStatic
        val fmi2GetStatus = FmiMethod("fmi2GetStatus")
        @JvmStatic
        val fmi2GetRealStatus = FmiMethod("fmi2GetRealStatus")
        @JvmStatic
        val fmi2GetIntegerStatus = FmiMethod("fmi2GetIntegerStatus")
        @JvmStatic
        val fmi2GetBooleanStatus = FmiMethod("fmi2GetBooleanStatus")
        @JvmStatic
        val fmi2GetStringStatus = FmiMethod("fmi2GetStringStatus")

        @JvmStatic
        val fmi2EnterEventMode = FmiMethod("fmi2EnterEventMode")
        @JvmStatic
        val fmi2NewDiscreteStates = FmiMethod("fmi2NewDiscreteStates")
        @JvmStatic
        val fmi2EnterContinuousTimeMode = FmiMethod("fmi2EnterContinuousTimeMode")
        @JvmStatic
        val fmi2CompletedIntegratorStep = FmiMethod("fmi2CompletedIntegratorStep")
        @JvmStatic
        val fmi2SetTime = FmiMethod("fmi2SetTime")
        @JvmStatic
        val fmi2SetContinuousStates = FmiMethod("fmi2SetContinuousStates")
        @JvmStatic
        val fmi2GetEventIndicators = FmiMethod("fmi2GetEventIndicators")
        @JvmStatic
        val fmi2GetContinuousStates = FmiMethod("fmi2GetContinuousStates")
        @JvmStatic
        val fmi2GetDerivatives = FmiMethod("fmi2GetDerivatives")
        @JvmStatic
        val fmi2GetNominalsOfContinuousStates = FmiMethod("fmi2GetNominalsOfContinuousStates")

        private var bit = 0

        private fun getAndIncrement(): Int {
            return 1 shl bit++
        }

    }

    var bit: Int = getAndIncrement()

}

internal class FmiState private constructor(
        val name: String,
        vararg methods: FmiMethod
) {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(FmiState::class.java)

        @JvmStatic
        val START = FmiState("START",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2Instantiate)

        @JvmStatic
        val END = FmiState("END",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2Instantiate)

        @JvmStatic
        val INSTANTIATED = FmiState("INSTANTIATED",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging,
                FmiMethod.fmi2FreeInstance, FmiMethod.fmi2SetupExperiment, FmiMethod.fmi2EnterInitializationMode, FmiMethod.fmi2Reset,
                FmiMethod.fmi2SetReal, FmiMethod.fmi2SetInteger, FmiMethod.fmi2SetBoolean, FmiMethod.fmi2SetString,
                FmiMethod.fmi2GetFMUstate, FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2SerializedFMUstateSize,
                FmiMethod.fmi2SerializeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2SetRealInputDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)

        @JvmStatic
        val INITIALISATION_MODE = FmiState("INITIALISATION_MODE",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2ExitInitializationMode,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString,
                FmiMethod.fmi2GetFMUstate, FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate,
                FmiMethod.fmi2SetReal, FmiMethod.fmi2SetInteger, FmiMethod.fmi2SetBoolean, FmiMethod.fmi2SetString,
                FmiMethod.fmi2GetDirectionalDerivative, FmiMethod.fmi2SetRealInputDerivatives, FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates, FmiMethod.fmi2GetDerivatives)

        ////////////////////////////////////////////////////////////////////////CO-SIMULATION///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @JvmStatic
        val STEP_COMPLETE = FmiState("STEP_COMPLETE",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative, FmiMethod.fmi2SetRealInputDerivatives,
                FmiMethod.fmi2GetStatus, FmiMethod.fmi2GetRealStatus, FmiMethod.fmi2GetIntegerStatus, FmiMethod.fmi2GetBooleanStatus, FmiMethod.fmi2GetStringStatus)

        @JvmStatic
        val STEP_IN_PROGRESS = FmiState("STEP_IN_PROGRESS",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetStatus, FmiMethod.fmi2GetRealStatus, FmiMethod.fmi2GetIntegerStatus, FmiMethod.fmi2GetBooleanStatus, FmiMethod.fmi2GetStringStatus)

        @JvmStatic
        val STEP_FAILED = FmiState("STEP_FAILED",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetRealOutputDerivatives)

        @JvmStatic
        val STEP_CANCELED = FmiState("STEP_CANCELED",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetRealOutputDerivatives)
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////MODEL EXCHANGE/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @JvmStatic
        val EVENT_MODE = FmiState("EVENT_MODE",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString,
                FmiMethod.fmi2SetReal, FmiMethod.fmi2SetInteger, FmiMethod.fmi2SetBoolean, FmiMethod.fmi2SetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative, FmiMethod.fmi2EnterEventMode,
                FmiMethod.fmi2NewDiscreteStates, FmiMethod.fmi2EnterContinuousTimeMode, FmiMethod.fmi2SetTime, FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates,
                FmiMethod.fmi2GetDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)

        @JvmStatic
        val CONTINUOUS_TIME_MODE = FmiState("CONTINUOUS_TIME_MODE",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance, FmiMethod.fmi2Terminate,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString,
                FmiMethod.fmi2SetReal, FmiMethod.fmi2GetFMUstate, FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate,
                FmiMethod.fmi2GetDirectionalDerivative, FmiMethod.fmi2EnterEventMode, FmiMethod.fmi2CompletedIntegratorStep,
                FmiMethod.fmi2SetTime, FmiMethod.fmi2SetContinuousStates, FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates,
                FmiMethod.fmi2GetDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @JvmStatic
        val TERMINATED = FmiState("TERMINATED",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetRealOutputDerivatives, FmiMethod.fmi2GetStatus, FmiMethod.fmi2GetRealStatus, FmiMethod.fmi2GetIntegerStatus, FmiMethod.fmi2GetBooleanStatus, FmiMethod.fmi2GetStringStatus,
                FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates, FmiMethod.fmi2GetDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)

        @JvmStatic
        val ERROR = FmiState("ERROR",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging, FmiMethod.fmi2FreeInstance,
                FmiMethod.fmi2Reset, FmiMethod.fmi2GetReal, FmiMethod.fmi2GetInteger, FmiMethod.fmi2GetBoolean, FmiMethod.fmi2GetString, FmiMethod.fmi2GetFMUstate,
                FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2GetDirectionalDerivative,
                FmiMethod.fmi2GetEventIndicators, FmiMethod.fmi2GetContinuousStates, FmiMethod.fmi2GetDerivatives, FmiMethod.fmi2GetNominalsOfContinuousStates)

        @JvmStatic
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