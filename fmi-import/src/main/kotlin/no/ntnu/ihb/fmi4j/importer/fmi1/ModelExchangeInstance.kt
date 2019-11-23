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
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.EventInfo
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.ModelExchangeLibraryWrapper
import no.ntnu.ihb.fmi4j.modeldescription.ModelExchangeModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Represent a FMI Model Exchange instance
 *
 * @author Lars Ivar Hatledal
 */
open class ModelExchangeInstance internal constructor(
        instanceName: String,
        wrapper: ModelExchangeLibraryWrapper,
        modelDescription: ModelExchangeModelDescription
) : AbstractModelInstance<ModelExchangeModelDescription, ModelExchangeLibraryWrapper>(instanceName, wrapper, modelDescription) {


    internal val eventInfo = EventInfo()
    private var relativeTolerance: Double = 0.0

    override fun setup(start: Double, stop: Double, tolerance: Double): Boolean {

        LOG.debug("FMU '${modelDescription.modelName}' setup with start=$start, stop=$stop")

        if (start < 0) {
            LOG.error("Start must be a positive value, was $start!")
            return false
        }
        startTime = start
        if (stop > startTime) {
            stopTime = stop
        }

        setTime(start)

        relativeTolerance = tolerance

        return true.also {
            wrapper.lastStatus = FmiStatus.OK
        }

    }

    override fun exitInitializationMode(): Boolean {
        val toleranceControlled = relativeTolerance > 0
        return wrapper.initialize(toleranceControlled, relativeTolerance).isOK()
    }

    override fun reset(): Boolean {
        throw IllegalStateException("Reset not supported by FMI 1.0 for Model Exchange")
    }

    fun setTime(time: Double): FmiStatus {
        return wrapper.setTime(time).also {
            simulationTime = time
        }
    }

    fun setContinuousStates(x: DoubleArray) = wrapper.setContinuousStates(x)

    fun completedIntegratorStep() = wrapper.completedIntegratorStep()

    fun getDerivatives(derivatives: DoubleArray) = wrapper.getDerivatives(derivatives)

    fun getEventIndicators(eventIndicators: DoubleArray) = wrapper.getEventIndicators(eventIndicators)

    fun getContinuousStates(x: DoubleArray) = wrapper.getContinuousStates(x)

    fun getNominalsOfContinuousStates(xNominal: DoubleArray) = wrapper.getNominalsOfContinuousStates(xNominal)

    fun getStateValueReferences(vrx: ValueReferences) = wrapper.getStateValueReferences(vrx)

    private companion object {

        val LOG: Logger = LoggerFactory.getLogger(ModelExchangeInstance::class.java)

    }

}
