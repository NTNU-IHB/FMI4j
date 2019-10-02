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

package no.ntnu.ihb.fmi4j.importer.fmi2.jni

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.util.BooleanByReference
import java.io.File

/**
 * @author Lars Ivar Hatledal
 */
class Fmi2ModelExchangeLibrary(
        lib: File
) : Fmi2Library(lib) {

    private external fun enterEventMode(p: Long, c: Fmi2Component): NativeStatus

    private external fun newDiscreteStates(p: Long, c: Fmi2Component, ev: EventInfo): NativeStatus

    private external fun enterContinuousTimeMode(p: Long, c: Fmi2Component): NativeStatus

    private external fun setContinuousStates(p: Long, c: Fmi2Component, x: DoubleArray): NativeStatus

    private external fun completedIntegratorStep(
            p: Long, c: Fmi2Component, noSetFMUStatePriorToCurrentPoint: Boolean,
            enterEventMode: BooleanByReference, terminateSimulation: BooleanByReference): NativeStatus

    private external fun setTime(p: Long, c: Fmi2Component, time: Double): NativeStatus

    private external fun getDerivatives(p: Long, c: Fmi2Component, derivatives: DoubleArray): NativeStatus

    private external fun getEventIndicators(p: Long, c: Fmi2Component, eventIndicators: DoubleArray): NativeStatus

    private external fun getContinuousStates(p: Long, c: Fmi2Component, x: DoubleArray): NativeStatus

    private external fun getNominalsOfContinuousStates(p: Long, c: Fmi2Component, xNominals: DoubleArray): NativeStatus

    fun enterEventMode(c: Fmi2Component): FmiStatus {
        return enterEventMode(p, c).transform()
    }

    fun newDiscreteStates(c: Fmi2Component, ev: EventInfo): FmiStatus {
        return newDiscreteStates(p, c, ev).transform()
    }

    fun enterContinuousTimeMode(c: Fmi2Component): FmiStatus {
        return enterContinuousTimeMode(p, c).transform()
    }

    fun setContinuousStates(c: Fmi2Component, x: DoubleArray): FmiStatus {
        return setContinuousStates(p, c, x).transform()
    }

    fun completedIntegratorStep(
            c: Fmi2Component, noSetFMUStatePriorToCurrentPoint: Boolean,
            enterEventMode: BooleanByReference, terminateSimulation: BooleanByReference): FmiStatus {
        return completedIntegratorStep(p, c, noSetFMUStatePriorToCurrentPoint, enterEventMode, terminateSimulation).transform()
    }

    fun setTime(c: Fmi2Component, time: Double): FmiStatus {
        return setTime(p, c, time).transform()
    }

    fun getDerivatives(c: Fmi2Component, derivatives: DoubleArray): FmiStatus {
        return getDerivatives(p, c, derivatives).transform()
    }

    fun getEventIndicators(c: Fmi2Component, eventIndicators: DoubleArray): FmiStatus {
        return getEventIndicators(p, c, eventIndicators).transform()
    }

    fun getContinuousStates(c: Fmi2Component, x: DoubleArray): FmiStatus {
        return getContinuousStates(p, c, x).transform()
    }

    fun getNominalsOfContinuousStates(c: Fmi2Component, x_nominals: DoubleArray): FmiStatus {
        return getNominalsOfContinuousStates(p, c, x_nominals).transform()
    }

}

/**
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeLibraryWrapper(
        c: Long,
        library: Fmi2ModelExchangeLibrary
) : Fmi2LibraryWrapper<Fmi2ModelExchangeLibrary>(c, library) {

    private val enterEventMode = BooleanByReference()
    private val terminateSimulation = BooleanByReference()

    fun setTime(time: Double): FmiStatus {
        return updateStatus(library.setTime(c, time))
    }

    fun setContinuousStates(x: DoubleArray): FmiStatus {
        return updateStatus(library.setContinuousStates(c, x))
    }

    fun enterEventMode(): FmiStatus {
        return updateStatus(library.enterEventMode(c))
    }

    fun enterContinuousTimeMode(): FmiStatus {
        return updateStatus(library.enterContinuousTimeMode(c))
    }

    fun newDiscreteStates(eventInfo: EventInfo): FmiStatus {
        return updateStatus(library.newDiscreteStates(c, eventInfo))
    }

    fun completedIntegratorStep(): CompletedIntegratorStepResult {
        updateStatus(library.completedIntegratorStep(c,
                true, enterEventMode, terminateSimulation))
        return CompletedIntegratorStepResult(
                enterEventMode.value,
                terminateSimulation.value)
    }

    fun getDerivatives(derivatives: DoubleArray): FmiStatus {
        return updateStatus(library.getDerivatives(c, derivatives))
    }

    fun getEventIndicators(eventIndicators: DoubleArray): FmiStatus {
        return updateStatus(library.getEventIndicators(c, eventIndicators))
    }

    fun getContinuousStates(x: DoubleArray): FmiStatus {
        return updateStatus(library.getContinuousStates(c, x))
    }

    fun getNominalsOfContinuousStates(x_nominal: DoubleArray): FmiStatus {
        return updateStatus(library.getNominalsOfContinuousStates(c, x_nominal))
    }

}

/**
 *
 * @author Lars Ivar Hatledal
 */
data class CompletedIntegratorStepResult(
        val enterEventMode: Boolean,
        val terminateSimulation: Boolean
)


class EventInfo {

    var newDiscreteStatesNeeded: Boolean = false
    var terminateSimulation: Boolean = false
    var nominalsOfContinuousStatesChanged: Boolean = false
    var valuesOfContinuousStatesChanged: Boolean = false
    var nextEventTimeDefined: Boolean = false
    var nextEventTime: Double = 0.0

    override fun toString(): String {
        return "EventInfo{" +
                "newDiscreteStatesNeeded=" + newDiscreteStatesNeeded +
                ", terminateSimulation=" + terminateSimulation +
                ", nominalsOfContinuousStatesChanged=" + nominalsOfContinuousStatesChanged +
                ", valuesOfContinuousStatesChanged=" + valuesOfContinuousStatesChanged +
                ", nextEventTimeDefined=" + nextEventTimeDefined +
                ", nextEventTime=" + nextEventTime +
                '}'.toString()
    }

}
