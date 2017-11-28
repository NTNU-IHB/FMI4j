package no.mechatronics.sfi.fmi4j.fmu


data class Experiment(

        val startTime: Double = 0.0,
        val stopTime: Double = 0.0,
        val tolerance: Double = 1E-4,

        val stopDefined: Boolean = false,
        val toleranceDefined: Boolean = false,
        val useDefaultExperiment: Boolean = false
)