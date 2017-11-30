package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.fmu.*
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelVariables

interface Fmi2Simulation {

    val fmuFile: FmuFile
    val modelDescription: ModelDescription
    val modelVariables: ModelVariables
    val currentTime: Double

    fun write(name: String) : VariableWriter
    fun read(name: String) : VariableReader

    fun init() : Boolean
    fun init(start: Double) : Boolean
    fun init(start: Double, stop: Double): Boolean
    fun doStep(dt: Double) : Boolean
    fun reset() : Boolean
    fun terminate() : Boolean

    fun getLastStatus() : Fmi2Status

}