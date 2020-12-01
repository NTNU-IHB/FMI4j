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

package no.ntnu.ihb.fmi4j.importer.fmi2

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.FmuState
import no.ntnu.ihb.fmi4j.ModelInstance
import no.ntnu.ihb.fmi4j.importer.DirectAccessor
import no.ntnu.ihb.fmi4j.importer.fmi2.jni.Fmi2LibraryWrapper
import no.ntnu.ihb.fmi4j.modeldescription.CommonModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

/**
 * Base class for FMU instances
 *
 * @author Lars Ivar Hatledal
 */
abstract class AbstractModelInstance<out E : CommonModelDescription, out T : Fmi2LibraryWrapper<*>> internal constructor(
        override val instanceName: String,
        val wrapper: T,
        override val modelDescription: E
) : ModelInstance<E>, DirectAccessor {

    val typesPlatform
        get() = wrapper.typesPlatform

    val version
        get() = wrapper.version

    /**
     * @see Fmi2LibraryWrapper.isTerminated
     */
    override val isTerminated
        get() = wrapper.isTerminated

    private var startTime: Double = 0.0
    protected var stopTime: Double = 0.0
        private set

    protected val stopDefined
        get() = stopTime > startTime

    /**
     * Current simulation time
     */
    override var simulationTime: Double = 0.0
        internal set

    override val lastStatus: FmiStatus
        get() = wrapper.lastStatus


    fun setDebugLogging(loggingOn: Boolean, categories: Array<String>): FmiStatus
            = wrapper.setDebugLogging(loggingOn, categories)

    /**
     * Call init with provided start and stop
     * @param start the start time
     * @param stop the stop time
     *
     * @throws IllegalArgumentException if start < 0
     */
    override fun setupExperiment(start: Double, stop: Double, tolerance: Double): Boolean {

        LOG.debug("FMU '${modelDescription.modelName}' setupExperiment with start=$start, stop=$stop, tolerance=$tolerance")

        if (start < 0) {
            LOG.error("Start must be a positive value, was $start!")
            return false
        }
        startTime = start
        if (stop > startTime) {
            stopTime = stop
        }

        return (wrapper.setupExperiment(tolerance, startTime, stopTime).isOK()).also {
            simulationTime = start
        }

    }

    override fun enterInitializationMode(): Boolean {
        LOG.trace("FMU '${modelDescription.modelName}' enterInitializationMode")
        return wrapper.enterInitializationMode().isOK()
    }

    override fun exitInitializationMode(): Boolean {
        LOG.trace("FMU '${modelDescription.modelName}' exitInitializationMode")
        return wrapper.exitInitializationMode().isOK()
    }

    /**
     * Terminates the FMU
     */
    override fun terminate(): Boolean {
        if (isTerminated) return false
        return wrapper.terminate().let { status ->
            LOG.debug("${modelDescription.modelName} instance '${instanceName}' terminated with status $status!}")
            status.isOK()
        }
    }

    override fun reset(): Boolean {
        return wrapper.reset().isOK()
    }

    override fun close() {
        terminate()
        wrapper.freeInstance()
    }

    override fun getDirectionalDerivative(vUnknownRef: ValueReferences, vKnownRef: ValueReferences, dvKnown: RealArray): RealArray {
        check(modelDescription.attributes.providesDirectionalDerivative) { "Illegal call. FMU does not provide directional derivatives!" }
        return RealArray(vUnknownRef.size).also { dvUnknown ->
            wrapper.getDirectionalDerivative(vUnknownRef, vKnownRef, dvKnown, dvUnknown)
        }
    }

    override fun getFMUstate(): FmuState {
        if (!modelDescription.attributes.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.getFMUState()
    }

    override fun setFMUstate(state: FmuState): Boolean {
        if (!modelDescription.attributes.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU '${modelDescription.modelName}' cannot get and set FMU state!")
        }
        return wrapper.setFMUState(state).isOK()
    }

    override fun freeFMUstate(state: FmuState): Boolean {
        if (!modelDescription.attributes.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU '${modelDescription.modelName}' cannot get and set FMU state!")
        }
        return wrapper.freeFMUState(state).isOK()
    }

    fun serializedFMUstateSize(fmuState: FmuState): Int {
        if (!modelDescription.attributes.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU '${modelDescription.modelName}' cannot serialize/deserialize FMU state!")
        }
        return wrapper.serializedFMUStateSize(fmuState)
    }

    override fun serializeFMUstate(state: FmuState): ByteArray {
        if (!modelDescription.attributes.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU '${modelDescription.modelName}' cannot serialize/deserialize FMU state!")
        }
        return wrapper.serializeFMUState(state)
    }

    override fun deSerializeFMUstate(state: ByteArray): FmuState {
        if (!modelDescription.attributes.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU '${modelDescription.modelName}' cannot serialize/deserialize FMU state!")
        }
        return wrapper.deSerializeFMUState(state)
    }

    override fun readInteger(vr: ValueReferences, ref: IntArray): FmiStatus {
        return wrapper.readInteger(vr, ref)
    }

    override fun readReal(vr: ValueReferences, ref: RealArray): FmiStatus {
        return wrapper.readReal(vr, ref)
    }

    override fun readRealDirect(vr: ByteBuffer, ref: ByteBuffer): FmiStatus {
        return wrapper.readRealDirect(vr, ref)
    }

    override fun readString(vr: ValueReferences, ref: StringArray): FmiStatus {
        return wrapper.readString(vr, ref)
    }

    override fun readBoolean(vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return wrapper.readBoolean(vr, ref)
    }

    override fun readALl(intVr: ValueReferences?, intRefs: IntArray?, realVr: ValueReferences?, realRefs: DoubleArray?, strVr: ValueReferences?, strRefs: StringArray?, boolVr: ValueReferences?, boolRefs: BooleanArray?): FmiStatus {
        return wrapper.writeAll(intVr, intRefs, realVr, realRefs, strVr, strRefs, boolVr, boolRefs)
    }

    override fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus {
        return wrapper.writeInteger(vr, value)
    }

    override fun writeReal(vr: ValueReferences, value: RealArray): FmiStatus {
        return wrapper.writeReal(vr, value)
    }

    override fun writeRealDirect(vr: ByteBuffer, value: ByteBuffer): FmiStatus {
        return wrapper.writeRealDirect(vr, value)
    }

    override fun writeString(vr: ValueReferences, value: StringArray): FmiStatus {
        return wrapper.writeString(vr, value)
    }

    override fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus {
        return wrapper.writeBoolean(vr, value)
    }

    override fun writeAll(intVr: ValueReferences?, intValues: IntArray?, realVr: ValueReferences?, realValues: DoubleArray?, strVr: ValueReferences?, strValues: StringArray?, boolVr: ValueReferences?, boolValues: BooleanArray?): FmiStatus {
        return wrapper.writeAll(intVr, intValues, realVr, realValues, strVr, strValues, boolVr, boolValues)
    }


    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(AbstractModelInstance::class.java)
    }

}
