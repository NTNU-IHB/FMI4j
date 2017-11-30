package no.mechatronics.sfi.fmi4j.wrapper

import org.slf4j.LoggerFactory


internal class FmiMethod private constructor(
        val name: String
) {

    companion object {

        @JvmStatic val fmi2GetTypesPlatform = FmiMethod("fmi2GetTypesPlatform")
        @JvmStatic val fmi2GetVersion = FmiMethod("fmi2GetVersion")
        @JvmStatic val fmi2SetDebugLogging = FmiMethod("fmi2SetDebugLogging")
        @JvmStatic val fmi2Instantiate = FmiMethod("fmi2Instantiate")
        @JvmStatic val fmi2FreeInstance = FmiMethod("fmi2FreeInstance")
        @JvmStatic val fmi2SetupExperiment = FmiMethod("fmi2SetupExperiment")
        @JvmStatic val fmi2EnterInitializationMode = FmiMethod("fmi2EnterInitializationMode")
        @JvmStatic val fmi2ExitInitializationMode = FmiMethod("fmi2ExitInitializationMode")
        @JvmStatic val fmi2Terminate = FmiMethod("fmi2Terminate")
        @JvmStatic val fmi2Reset = FmiMethod("fmi2Reset")
        @JvmStatic val fmi2GetReal = FmiMethod("fmi2GetReal")
        @JvmStatic val fmi2GetInteger = FmiMethod("fmi2GetInteger")
        @JvmStatic val fmi2GetBoolean = FmiMethod("fmi2GetBoolean")
        @JvmStatic val fmi2GetString = FmiMethod("fmi2GetString")
        @JvmStatic val fmi2SetReal = FmiMethod("fmi2SetReal")
        @JvmStatic val fmi2SetInteger = FmiMethod("fmi2SetInteger")
        @JvmStatic val fmi2SetBoolean = FmiMethod("fmi2SetBoolean")
        @JvmStatic val fmi2SetString = FmiMethod("fmi2SetString")
        @JvmStatic val fmi2GetFMUstate = FmiMethod("fmi2GetFMUstate")
        @JvmStatic val fmi2SetFMUstate = FmiMethod("fmi2SetFMUstate")
        @JvmStatic val fmi2FreeFMUstate = FmiMethod("fmi2FreeFMUstate")
        @JvmStatic val fmi2SerializedFMUstateSize = FmiMethod("fmi2SerializedFMUstateSize")
        @JvmStatic val fmi2SerializeFMUstate = FmiMethod("fmi2SerializeFMUstate")
        @JvmStatic val fmi2DeSerializeFMUstate = FmiMethod("fmi2DeSerializeFMUstate")
        @JvmStatic val fmi2GetDirectionalDerivative = FmiMethod("fmi2GetDirectionalDerivative")

        @JvmStatic val fmi2SetRealInputDerivatives = FmiMethod("fmi2SetRealInputDerivatives")
        @JvmStatic val fmi2GetRealOutputDerivatives = FmiMethod("fmi2GetRealOutputDerivatives")
        @JvmStatic val fmi2DoStep = FmiMethod("fmi2DoStep")
        @JvmStatic val fmi2CancelStep = FmiMethod("fmi2CancelStep")
        @JvmStatic val fmi2GetStatus = FmiMethod("fmi2GetStatus")
        @JvmStatic val fmi2GetRealStatus = FmiMethod("fmi2GetRealStatus")
        @JvmStatic val fmi2GetIntegerStatus = FmiMethod("fmi2GetIntegerStatus")
        @JvmStatic val fmi2GetBooleanStatus = FmiMethod("fmi2GetBooleanStatus")
        @JvmStatic val fmi2GetStringStatus = FmiMethod("fmi2GetStringStatus")

        private var bit = 0

        private fun getAndIncrement() : Int {
            return 1 shl  bit++
        }

    }

    var bit: Int = getAndIncrement()

}

internal class FmiState private constructor(
        val name: String,
        vararg methods: FmiMethod
) {

    companion object {

        private val LOG = LoggerFactory.getLogger(FmiState::class.java)

        @JvmStatic val START = FmiState("START",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2Instantiate)
        @JvmStatic val END = FmiState("END",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2Instantiate)
        @JvmStatic val INSTANTIATED = FmiState("INSTANTIATED",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging,
                FmiMethod.fmi2FreeInstance, FmiMethod.fmi2SetupExperiment, FmiMethod.fmi2EnterInitializationMode, FmiMethod.fmi2Reset,
                FmiMethod.fmi2GetFMUstate, FmiMethod.fmi2SetFMUstate, FmiMethod.fmi2FreeFMUstate, FmiMethod.fmi2SerializedFMUstateSize,
                FmiMethod.fmi2SerializeFMUstate, FmiMethod.fmi2DeSerializeFMUstate, FmiMethod.fmi2SetRealInputDerivatives)
        @JvmStatic val INITIALISATION_MODE = FmiState("INITIALISATION_MODE",
                FmiMethod.fmi2GetTypesPlatform, FmiMethod.fmi2GetVersion, FmiMethod.fmi2SetDebugLogging)
    }

    var allowedStates: Int

    init {
        allowedStates = 0
        for (state in methods) {
            allowedStates = allowedStates or state.bit
        }
    }

    fun isCallLegalDuringState(fmiMethod: FmiMethod) : Boolean {

        if (fmiMethod.bit and allowedStates == 0) {
            LOG.warn("FMI method {} cannot be called during {} state", fmiMethod.name, name)
            return false
        }
        return true

    }

}