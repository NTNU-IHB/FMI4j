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

package no.mechatronics.sfi.fmi4j.importer.proxy.v2.me

import com.sun.jna.Pointer
import com.sun.jna.ptr.IntByReference
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.importer.misc.FmiBoolean
import no.mechatronics.sfi.fmi4j.importer.misc.FmiTrue
import no.mechatronics.sfi.fmi4j.importer.misc.FmiLibraryProvider
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.FmiLibraryWrapper
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.structs.FmiEventInfo

/**
 *
 * @author Lars Ivar Hatledal
 */
data class CompletedIntegratorStep(
        val enterEventMode: Boolean,
        val terminateSimulation: Boolean
)

/**
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeLibraryWrapper(
        c: Pointer,
        library: FmiLibraryProvider<FmiModelExchangeLibrary>
) : FmiLibraryWrapper<FmiModelExchangeLibrary>(c, library) {


    private val enterEventMode = IntByReference()
    private val terminateSimulation = IntByReference()


    /**
     * @see FmiModelExchangeLibrary.fmi2SetTime
     * @param time
     */
    fun setTime(time: Double): FmiStatus {
        return updateStatus(FmiStatus.valueOf(library.fmi2SetTime(c, time)))
    }

    /**
     * 
     * @see FmiModelExchangeLibrary.fmi2SetContinuousStates
     * @param x state
     */
    fun setContinuousStates(x: DoubleArray): FmiStatus {
        return updateStatus(FmiStatus.valueOf(library.fmi2SetContinuousStates(c, x, x.size)))
    }

    /**
     * @see FmiModelExchangeLibrary.fmi2EnterEventMode
     */
    fun enterEventMode(): FmiStatus {
        return updateStatus(FmiStatus.valueOf(library.fmi2EnterEventMode(c)))
    }

    /**
     * @see FmiModelExchangeLibrary.fmi2EnterContinuousTimeMode
     */
    fun enterContinuousTimeMode(): FmiStatus {
        return updateStatus(FmiStatus.valueOf(library.fmi2EnterContinuousTimeMode(c)))
    }

    fun newDiscreteStates(eventInfo: FmiEventInfo): FmiStatus {
        return updateStatus(FmiStatus.valueOf(library.fmi2NewDiscreteStates(c, eventInfo)))
    }

    fun completedIntegratorStep(): CompletedIntegratorStep {
        updateStatus(FmiStatus.valueOf(library.fmi2CompletedIntegratorStep(c,
                FmiTrue, enterEventMode, terminateSimulation)))
        return CompletedIntegratorStep(
                FmiBoolean.convert(enterEventMode.value),
                FmiBoolean.convert(terminateSimulation.value))
    }

    /**
     * @see FmiModelExchangeLibrary.fmi2GetDerivatives
     * @param derivatives
     */
    fun getDerivatives(derivatives: DoubleArray): FmiStatus {
        return updateStatus(FmiStatus.valueOf(library.fmi2GetDerivatives(c, derivatives, derivatives.size)))
    }

    /**
     *
     * @param eventIndicators
     */
    fun getEventIndicators(eventIndicators: DoubleArray): FmiStatus {
        return updateStatus(FmiStatus.valueOf(library.fmi2GetEventIndicators(c, eventIndicators, eventIndicators.size)))
    }

    /**
     *
     * @param x
     */
    fun getContinuousStates(x: DoubleArray): FmiStatus {
        return updateStatus(FmiStatus.valueOf(library.fmi2GetContinuousStates(c, x, x.size)))
    }

    /**
     *
     * @param x_nominal
     */
    fun getNominalsOfContinuousStates(x_nominal: DoubleArray): FmiStatus {
        return updateStatus(FmiStatus.valueOf(library.fmi2GetNominalsOfContinuousStates(c, x_nominal, x_nominal.size)))
    }

}

