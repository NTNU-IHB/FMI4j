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
import no.ntnu.ihb.fmi4j.importer.AbstractFmuInstance
import no.ntnu.ihb.fmi4j.importer.jni.EventInfo
import no.ntnu.ihb.fmi4j.modeldescription.ModelExchangeModelDescription

/**
 * Represent a FMI Model Exchange instance
 *
 * @author Lars Ivar Hatledal
 */
open class ModelExchangeInstance internal constructor(
        wrapper: ModelExchangeLibraryWrapper,
        modelDescription: ModelExchangeModelDescription
) : AbstractFmuInstance<ModelExchangeModelDescription, ModelExchangeLibraryWrapper>(wrapper, modelDescription) {

    val eventInfo = EventInfo()

    /**
     * @see ModelExchangeLibraryWrapper.setTime
     *
     * @param time
     */
    fun setTime(time: Double): FmiStatus {
        return wrapper.setTime(time).also {
            simulationTime = time
        }
    }

    /**
     * @see ModelExchangeLibraryWrapper.setContinuousStates
     *
     * @param x states
     */
    fun setContinuousStates(x: DoubleArray) = wrapper.setContinuousStates(x)

    /**
     * @see ModelExchangeLibraryWrapper.enterEventMode
     */
    fun enterEventMode() = wrapper.enterEventMode()

    /**
     * @see ModelExchangeLibraryWrapper.enterContinuousTimeMode
     */
    fun enterContinuousTimeMode() = wrapper.enterContinuousTimeMode()

    /**
     * @see ModelExchangeLibraryWrapper.newDiscreteStates
     *
     */
    fun newDiscreteStates() = wrapper.newDiscreteStates(eventInfo)

    /**
     * @see ModelExchangeLibraryWrapper.completedIntegratorStep
     */
    fun completedIntegratorStep() = wrapper.completedIntegratorStep()

    /**
     * @see ModelExchangeLibraryWrapper.getDerivatives
     *
     * @param derivatives
     */
    fun getDerivatives(derivatives: DoubleArray) = wrapper.getDerivatives(derivatives)

    /**
     * @see ModelExchangeLibraryWrapper.getEventIndicators
     *
     * @param eventIndicators
     */
    fun getEventIndicators(eventIndicators: DoubleArray) = wrapper.getEventIndicators(eventIndicators)

    /**
     * @see ModelExchangeLibraryWrapper.getContinuousStates
     *
     * @param x
     */
    fun getContinuousStates(x: DoubleArray) = wrapper.getContinuousStates(x)

    /**
     * @see ModelExchangeLibraryWrapper.getNominalsOfContinuousStates
     *
     * @param x_nominal
     */
    fun getNominalsOfContinuousStates(x_nominal: DoubleArray) = wrapper.getNominalsOfContinuousStates(x_nominal)

}
