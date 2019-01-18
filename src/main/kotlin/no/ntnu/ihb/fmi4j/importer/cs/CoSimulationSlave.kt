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

package no.ntnu.ihb.fmi4j.importer.cs

import no.ntnu.ihb.fmi4j.common.FmiStatus
import no.ntnu.ihb.fmi4j.common.FmuSlave
import no.ntnu.ihb.fmi4j.common.ValueReferences
import no.ntnu.ihb.fmi4j.importer.AbstractFmuInstance
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Represent a FMI Co-simulation instance
 *
 * @author Lars Ivar Hatledal
 */
class CoSimulationSlave internal constructor(
        wrapper: CoSimulationLibraryWrapper,
        modelDescription: CoSimulationModelDescription
) : FmuSlave, AbstractFmuInstance<CoSimulationModelDescription, CoSimulationLibraryWrapper>(wrapper, modelDescription) {

    /**
     * @see CoSimulationLibraryWrapper.doStep
     */
    override fun doStep(stepSize: Double): Boolean {

        val tNext = (simulationTime + stepSize)

        if (stopDefined && tNext > stopTime) {
            LOG.warn("Cannot perform step! tNext=$tNext > stopTime=$stopTime")
            return false
        }

        return wrapper.doStep(simulationTime, stepSize, noSetFMUStatePriorToCurrent = true).let { status ->
            (status == FmiStatus.OK).also {success ->
                if (success) {
                    simulationTime = tNext
                }
            }
        }

    }

    /**
     * @see CoSimulationLibraryWrapper.cancelStep
     */
    override fun cancelStep(): Boolean {
        return (wrapper.cancelStep() == FmiStatus.OK)
    }

    /**
     * Terminates and frees the FMU instance
     *
     * @see no.ntnu.ihb.fmi4j.importer.proxy.v2.FmiLibrary.fmi2Terminate
     * @see no.ntnu.ihb.fmi4j.importer.proxy.v2.FmiLibrary.fmi2FreeInstance
     */
    override fun terminate(): Boolean {
        return super.terminate(freeInstance = true)
    }

    /**
     * @see CoSimulationLibraryWrapper.setRealInputDerivatives
     */
    fun setRealInputDerivatives(vr: ValueReferences, order: IntArray, value: DoubleArray): FmiStatus {
        return wrapper.setRealInputDerivatives(vr, order, value)
    }

    /**
     * @see CoSimulationLibraryWrapper.getRealOutputDerivatives
     */
    fun getRealOutputDerivatives(vr: ValueReferences, order: IntArray, value: DoubleArray): FmiStatus {
        return wrapper.getRealOutputDerivatives(vr, order, value)
    }

    /**
     * @see CoSimulationLibraryWrapper.getStatus
     */
    fun getStatus(s: FmiStatusKind): FmiStatus {
        return wrapper.getStatus(s)
    }

    /**
     * @see CoSimulationLibraryWrapper.getRealStatus
     */
    fun getRealStatus(s: FmiStatusKind): Double {
        return wrapper.getRealStatus(s)
    }

    /**
     * @see CoSimulationLibraryWrapper.getIntegerStatus
     */
    fun getIntegerStatus(s: FmiStatusKind): Int {
        return wrapper.getIntegerStatus(s)
    }

    /**
     * @see CoSimulationLibraryWrapper.getBooleanStatus
     */
    fun getBooleanStatus(s: FmiStatusKind): Boolean {
        return wrapper.getBooleanStatus(s)
    }

    /**
     * @see CoSimulationLibraryWrapper.getStringStatus
     */
    fun getStringStatus(s: FmiStatusKind): String {
        return wrapper.getStringStatus(s)
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(CoSimulationSlave::class.java)
    }

}