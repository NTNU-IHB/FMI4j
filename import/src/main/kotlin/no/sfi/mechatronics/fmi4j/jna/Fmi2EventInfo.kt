package no.sfi.mechatronics.fmi4j.jna


import com.sun.jna.Structure
import java.util.Arrays

open class Fmi2EventInfo : Structure() {

    @JvmField
    var newDiscreteStatesNeeded: Byte = 0
    @JvmField
    var terminateSimulation: Byte = 0
    @JvmField
    var nominalsOfContinuousStatesChanged: Byte = 0
    @JvmField
    var valuesOfContinuousStatesChanged: Byte = 0
    @JvmField
    var nextEventTimeDefined: Byte = 0
    @JvmField
    var nextEventTime: Double = 0.0

    fun getNewDiscreteStatesNeeded(): Boolean {
        return newDiscreteStatesNeeded.toInt() != 0
    }

    fun getTerminateSimulation(): Boolean {
        return terminateSimulation.toInt() != 0
    }

    fun getNominalsOfContinuousStatesChanged(): Boolean {
        return nominalsOfContinuousStatesChanged.toInt() != 0
    }

    fun getValuesOfContinuousStatesChanged(): Boolean {
        return valuesOfContinuousStatesChanged.toInt() != 0
    }

    fun getNextEventTimeDefined(): Boolean {
        return nextEventTimeDefined.toInt() != 0
    }

    fun setNewDiscreteStatesNeededTrue() {
        this.newDiscreteStatesNeeded = 1
    }

    fun setTerminateSimulationFalse() {
        this.terminateSimulation = 0
    }

    override fun getFieldOrder(): List<String> {
        return Arrays.asList(
                "newDiscreteStatesNeeded",
                "terminateSimulation",
                "nominalsOfContinuousStatesChanged",
                "valuesOfContinuousStatesChanged",
                "nextEventTimeDefined",
                "nextEventTime")
    }

    override fun toString(): String {
        return "EventInfo{newDiscreteStatesNeeded=$newDiscreteStatesNeeded, terminateSimulation=$terminateSimulation, nominalsOfContinuousStatesChanged=$nominalsOfContinuousStatesChanged, valuesOfContinuousStatesChanged=$valuesOfContinuousStatesChanged, nextEventTimeDefined=$nextEventTimeDefined, nextEventTime=$nextEventTime}"
    }

    class ByReference : Fmi2EventInfo(), Structure.ByReference

}
