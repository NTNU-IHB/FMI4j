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

package no.ntnu.ihb.fmi4j.importer.fmi1

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.CoSimulationLibraryWrapper
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Represent a FMI Co-simulation instance
 *
 * @author Lars Ivar Hatledal
 */
class CoSimulationSlave internal constructor(
        instanceName: String,
        wrapper: CoSimulationLibraryWrapper,
        modelDescription: CoSimulationModelDescription
) : SlaveInstance, AbstractModelInstance<CoSimulationModelDescription, CoSimulationLibraryWrapper>(instanceName, wrapper, modelDescription) {

    /**
     * Call init with provided start and stop
     * @param start the start time
     * @param stop the stop time
     *
     */
    override fun setupExperiment(start: Double, stop: Double, tolerance: Double): Boolean {

        LOG.trace("FMU '${modelDescription.modelName}' setup with start=$start, stop=$stop")

        if (start < 0) {
            LOG.error("Start must be a positive value, was $start!")
            return false
        }
        startTime = start
        if (stop > startTime) {
            stopTime = stop
        }

        simulationTime = start

        return true.also {
            wrapper.lastStatus = FmiStatus.OK
        }
    }

    override fun exitInitializationMode(): Boolean {
        return wrapper.initializeSlave(startTime, stopTime).isOK()
    }

    override fun doStep(stepSize: Double): Boolean {

        val tNext = (simulationTime + stepSize)

        if (stopDefined && tNext > stopTime) {
            LOG.warn("Cannot perform doStep! tNext=$tNext > stopTime=$stopTime")
            return false
        }

        return wrapper.doStep(simulationTime, stepSize, newStep = true).let { status ->
            (status == FmiStatus.OK).also { success ->
                if (success) {
                    simulationTime = tNext
                }
            }
        }

    }

    override fun reset(): Boolean {
        return wrapper.reset().isOK()
    }

    fun setRealInputDerivatives(vr: ValueReferences, order: IntArray, value: DoubleArray): FmiStatus {
        return wrapper.setRealInputDerivatives(vr, order, value)
    }

    fun getRealOutputDerivatives(vr: ValueReferences, order: IntArray, value: DoubleArray): FmiStatus {
        return wrapper.getRealOutputDerivatives(vr, order, value)
    }

    fun getStatus(s: FmiStatusKind): FmiStatus {
        return wrapper.getStatus(s)
    }

    fun getRealStatus(s: FmiStatusKind): Double {
        return wrapper.getRealStatus(s)
    }

    fun getIntegerStatus(s: FmiStatusKind): Int {
        return wrapper.getIntegerStatus(s)
    }

    fun getBooleanStatus(s: FmiStatusKind): Boolean {
        return wrapper.getBooleanStatus(s)
    }

    fun getStringStatus(s: FmiStatusKind): String {
        return wrapper.getStringStatus(s)
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(CoSimulationSlave::class.java)
    }

}
