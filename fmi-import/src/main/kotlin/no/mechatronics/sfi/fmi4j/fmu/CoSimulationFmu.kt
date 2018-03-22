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

package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.FmiSimulation
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import no.mechatronics.sfi.fmi4j.proxy.v2.cs.CoSimulationLibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.v2.cs.Fmi2StatusKind
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CoSimulationFmu internal constructor(
        fmuFile: FmuFile,
        wrapper: CoSimulationLibraryWrapper
) : AbstractFmu<CoSimulationModelDescription, CoSimulationLibraryWrapper>(fmuFile, wrapper), FmiSimulation {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(CoSimulationFmu::class.java)
    }

    /**
     * Current simulation time
     */
    override var currentTime: Double = 0.0
        private set


    override val modelDescription: CoSimulationModelDescription
        get() = fmuFile.modelDescription.asCoSimulationModelDescription()

    override fun init(start: Double, stop: Double): FmiStatus {
        return super.init(start, stop).also {
            currentTime = start
        }
    }

    /**
     * @see CoSimulationLibraryWrapper.doStep
     */
    override fun doStep(stepSize: Double): FmiStatus {

        if (!isInitialized) {
            LOG.warn("Calling doStep without having called init(), " +
                    "remember that you have to call init() again after a call to reset()!")
            return FmiStatus.Discard
        }

        return wrapper.doStep(currentTime, stepSize, true).also {
            currentTime += stepSize
        }

    }

    override fun terminate(): FmiStatus {
        return super.terminate(true)
    }

    /**
     * @see CoSimulationLibraryWrapper.cancelStep
     */
    fun cancelStep() = wrapper.cancelStep()

    /**
     * @see CoSimulationLibraryWrapper.setRealInputDerivatives
     */
    fun setRealInputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.setRealInputDerivatives(vr, order, value)

    /**
     * @see CoSimulationLibraryWrapper.getRealOutputDerivatives
     */
    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.getRealOutputDerivatives(vr, order, value)

    /**
     * @see CoSimulationLibraryWrapper.getStatus
     */
    fun getStatus(s: Fmi2StatusKind) = wrapper.getStatus(s)

    /**
     * @see CoSimulationLibraryWrapper.getRealStatus
     */
    fun getRealStatus(s: Fmi2StatusKind) = wrapper.getRealStatus(s)

    /**
     * @see CoSimulationLibraryWrapper.getIntegerStatus
     */
    fun getIntegerStatus(s: Fmi2StatusKind) = wrapper.getIntegerStatus(s)

    /**
     * @see CoSimulationLibraryWrapper.getBooleanStatus
     */
    fun getBooleanStatus(s: Fmi2StatusKind) = wrapper.getBooleanStatus(s)

    /**
     * @see CoSimulationLibraryWrapper.getStringStatus
     */
    fun getStringStatus(s: Fmi2StatusKind) = wrapper.getStringStatus(s)

}