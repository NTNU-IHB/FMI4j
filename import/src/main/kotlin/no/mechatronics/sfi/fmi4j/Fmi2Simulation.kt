package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.fmu.FmuFile
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelVariables

interface Fmi2Simulation {

    val fmuFile: FmuFile
    val modelDescription: ModelDescription
    val modelVariables: ModelVariables
    val currentTime: Double

    fun init() : Boolean
    fun init(start: Double) : Boolean
    fun init(start: Double, stop: Double): Boolean
    fun doStep(dt: Double) : Boolean
    fun terminate() : Boolean

}