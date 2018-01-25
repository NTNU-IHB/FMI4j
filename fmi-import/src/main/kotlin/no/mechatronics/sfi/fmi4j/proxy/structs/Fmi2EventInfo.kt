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
import no.mechatronics.sfi.fmi4j.misc.Fmi2False
import no.mechatronics.sfi.fmi4j.misc.Fmi2True
import java.util.Arrays

/**
 *
 * @author Lars Ivar Hatledal
 */
open class Fmi2EventInfo : Structure() {

    @JvmField
    internal var newDiscreteStatesNeeded: Int = Fmi2False
    @JvmField
    internal var terminateSimulation: Int = Fmi2False
    @JvmField
    internal var nominalsOfContinuousStatesChanged: Int = Fmi2False
    @JvmField
    internal var valuesOfContinuousStatesChanged: Int = Fmi2False
    @JvmField
    internal var nextEventTimeDefined: Int = Fmi2False
    @JvmField
    internal var nextEventTime: Double = 0.0

    fun getNextEventTime(): Double = nextEventTime

    fun getNewDiscreteStatesNeeded(): Boolean {
        return newDiscreteStatesNeeded != Fmi2False
    }

    fun getTerminateSimulation(): Boolean {
        return terminateSimulation != Fmi2False
    }

    fun getNominalsOfContinuousStatesChanged(): Boolean {
        return nominalsOfContinuousStatesChanged != Fmi2False
    }

    fun getValuesOfContinuousStatesChanged(): Boolean {
        return valuesOfContinuousStatesChanged != Fmi2False
    }

    fun getNextEventTimeDefined(): Boolean {
        return nextEventTimeDefined != Fmi2False
    }

    fun setNewDiscreteStatesNeededTrue() {
        this.newDiscreteStatesNeeded = Fmi2True
    }

    fun setTerminateSimulationFalse() {
        this.terminateSimulation = Fmi2False
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







}
