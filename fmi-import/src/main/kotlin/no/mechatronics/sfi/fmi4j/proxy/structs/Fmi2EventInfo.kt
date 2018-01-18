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

package no.mechatronics.sfi.fmi4j.proxy.structs


import com.sun.jna.Structure
import java.util.Arrays

/**
 *
 * @author Lars Ivar Hatledal
 */
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
