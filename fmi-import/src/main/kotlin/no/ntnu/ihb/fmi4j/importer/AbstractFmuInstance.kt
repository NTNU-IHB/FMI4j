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

package no.ntnu.ihb.fmi4j.importer

import no.ntnu.ihb.fmi4j.common.*
import no.ntnu.ihb.fmi4j.importer.jni.Fmi2LibraryWrapper
import no.ntnu.ihb.fmi4j.modeldescription.CommonModelDescription
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for FMU instances
 *
 * @author Lars Ivar Hatledal
 */
abstract class AbstractFmuInstance<out E : CommonModelDescription, out T : Fmi2LibraryWrapper<*>> internal constructor(
        val wrapper: T,
        override val modelDescription: E
) : FmuInstance<E> {

    /**
     * @see Fmi2Library.getTypesPlatform
     */
    val typesPlatform
        get() = wrapper.typesPlatform

    /**
     * @see Fmi2Library.getVersion
     */
    val version
        get() = wrapper.version

    /**
     * @see Fmi2LibraryWrapper.isTerminated
     */
    override val isTerminated
        get() = wrapper.isTerminated

    protected var startTime: Double = 0.0
        private set

    protected var stopTime: Double = 0.0
        private set

    protected val stopDefined
        get() = stopTime > startTime

    /**
     * Current simulation time
     */
    override var simulationTime: Double = 0.0
        internal set

    /**
     * @see Fmi2LibraryWrapper.lastStatus
     */
    override val lastStatus: FmiStatus
        get() = wrapper.lastStatus


    /**
     * @see Fmi2Library.setDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, categories: Array<String>): FmiStatus
            = wrapper.setDebugLogging(loggingOn, categories)

    /**
     * Call init with provided start and stop
     * @param start the start time
     * @param stop the stop time
     *
     * @throws IllegalArgumentException if start < 0
     */
    override fun setup(start: Double, stop: Double, tolerance: Double): Boolean {

        LOG.debug("FMU '${modelDescription.modelName}' setup with start=$start, stop=$stop, tolerance=$tolerance")

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

    override fun terminate(): Boolean {
        return terminate(true)
    }

    /**
     * Terminates the FMU
     *
     * @param freeInstance true if you are completely finished with the fmuInstance
     *
     * @see Fmi2Library.terminate
     * @see Fmi2Library.freeInstance
     */
    fun terminate(freeInstance: Boolean): Boolean {
        return wrapper.terminate(freeInstance).let { status ->
            LOG.debug("FMU '${modelDescription.modelName}' terminated with status $status! #${hashCode()}")
            status.isOK()
        }
    }

    /**
     * @see Fmi2Library.reset
     */
    override fun reset(): Boolean {
        return wrapper.reset().isOK()
    }

    protected fun finalize() {
        if (!isTerminated) {
            LOG.warn("Instance ${modelDescription.modelName} was not terminated before garbage collection. Doing it for you..")
            close()
        }
    }

    override fun getDirectionalDerivative(vUnknownRef: ValueReferences, vKnownRef: ValueReferences, dvKnown: RealArray): RealArray {
        if (!modelDescription.attributes.providesDirectionalDerivative) {
            throw IllegalStateException("Illegal call. FMU does not provide directional derivatives!")
        }
        return RealArray(vUnknownRef.size).also { dvUnknown ->
            wrapper.getDirectionalDerivative(vUnknownRef, vKnownRef, dvKnown, dvUnknown)
        }
    }

    /**
     * @see Fmi2Library.getFMUstate
     */
    override fun getFMUstate(): FmuState {
        if (!modelDescription.attributes.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.getFMUState()
    }

    /**
     * @see Fmi2Library.setFMUstate
     */
    override fun setFMUstate(state: FmuState): Boolean {
        if (!modelDescription.attributes.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU '${modelDescription.modelName}' cannot get and set FMU state!")
        }
        return wrapper.setFMUState(state).isOK()
    }

    /**
     * @see Fmi2Library.freeFMUstate
     */
    override fun freeFMUstate(state: FmuState): Boolean {
        if (!modelDescription.attributes.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU '${modelDescription.modelName}' cannot get and set FMU state!")
        }
        return wrapper.freeFMUState(state).isOK()
    }

    /**
     * @see Fmi2Library.serializedFMUstateSize
     */
    fun serializedFMUstateSize(fmuState: FmuState): Int {
        if (!modelDescription.attributes.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU '${modelDescription.modelName}' cannot serialize/deserialize FMU state!")
        }
        return wrapper.serializedFMUStateSize(fmuState)
    }

    /**
     * @see Fmi2Library.serializeFMUstate
     */
    override fun serializeFMUstate(state: FmuState): ByteArray {
        if (!modelDescription.attributes.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU '${modelDescription.modelName}' cannot serialize/deserialize FMU state!")
        }
        return wrapper.serializeFMUState(state)
    }

    /**
     * @see Fmi2Library.deSerializeFMUstate
     */
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

    override fun readString(vr: ValueReferences, ref: StringArray): FmiStatus {
        return wrapper.readString(vr, ref)
    }

    override fun readBoolean(vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return wrapper.readBoolean(vr, ref)
    }

    override fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus {
        return wrapper.writeInteger(vr, value)
    }

    override fun writeReal(vr: ValueReferences, value: RealArray): FmiStatus {
        return wrapper.writeReal(vr, value)
    }

    override fun writeString(vr: ValueReferences, value: StringArray): FmiStatus {
        return wrapper.writeString(vr, value)
    }

    override fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus {
        return wrapper.writeBoolean(vr, value)
    }

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(AbstractFmuInstance::class.java)
    }

}
