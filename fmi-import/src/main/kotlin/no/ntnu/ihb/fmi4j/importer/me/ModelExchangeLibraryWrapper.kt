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

package no.ntnu.ihb.fmi4j.importer.me

import no.ntnu.ihb.fmi4j.common.FmiStatus
import no.ntnu.ihb.fmi4j.importer.jni.BooleanByReference
import no.ntnu.ihb.fmi4j.importer.jni.EventInfo
import no.ntnu.ihb.fmi4j.importer.jni.Fmi2LibraryWrapper
import no.ntnu.ihb.fmi4j.importer.jni.Fmi2ModelExchangeLibrary

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

    /**
     * @see Fmi2ModelExchangeLibrary.fmi2SetTime
     * @param time
     */
    fun setTime(time: Double): FmiStatus {
        return updateStatus((library.setTime(c, time)))
    }

    /**
     *
     * @see Fmi2ModelExchangeLibrary.fmi2SetContinuousStates
     * @param x state
     */
    fun setContinuousStates(x: DoubleArray): FmiStatus {
        return updateStatus((library.setContinuousStates(c, x)))
    }

    /**
     * @see Fmi2ModelExchangeLibrary.fmi2EnterEventMode
     */
    fun enterEventMode(): FmiStatus {
        return updateStatus((library.enterEventMode(c)))
    }

    /**
     * @see Fmi2ModelExchangeLibrary.fmi2EnterContinuousTimeMode
     */
    fun enterContinuousTimeMode(): FmiStatus {
        return updateStatus((library.enterContinuousTimeMode(c)))
    }

    fun newDiscreteStates(eventInfo: EventInfo): FmiStatus {
        return updateStatus((library.newDiscreteStates(c, eventInfo)))
    }

    fun completedIntegratorStep(): CompletedIntegratorStepResult {
        updateStatus((library.completedIntegratorStep(c,
                true, enterEventMode, terminateSimulation)))
        return CompletedIntegratorStepResult(
                enterEventMode.value,
                terminateSimulation.value)
    }

    /**
     * @see Fmi2ModelExchangeLibrary.fmi2GetDerivatives
     * @param derivatives
     */
    fun getDerivatives(derivatives: DoubleArray): FmiStatus {
        return updateStatus((library.getDerivatives(c, derivatives)))
    }

    /**
     *
     * @param eventIndicators
     */
    fun getEventIndicators(eventIndicators: DoubleArray): FmiStatus {
        return updateStatus((library.getEventIndicators(c, eventIndicators)))
    }

    /**
     *
     * @param x
     */
    fun getContinuousStates(x: DoubleArray): FmiStatus {
        return updateStatus((library.getContinuousStates(c, x)))
    }

    /**
     *
     * @param x_nominal
     */
    fun getNominalsOfContinuousStates(x_nominal: DoubleArray): FmiStatus {
        return updateStatus((library.getNominalsOfContinuousStates(c, x_nominal)))
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