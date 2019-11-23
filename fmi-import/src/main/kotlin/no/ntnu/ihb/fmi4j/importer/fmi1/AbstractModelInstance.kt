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
import no.ntnu.ihb.fmi4j.FmuState
import no.ntnu.ihb.fmi4j.ModelInstance
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.Fmi1LibraryWrapper
import no.ntnu.ihb.fmi4j.modeldescription.CommonModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for FMU instances
 *
 * @author Lars Ivar Hatledal
 */
abstract class AbstractModelInstance<out E : CommonModelDescription, out T : Fmi1LibraryWrapper<*>> internal constructor(
        override val instanceName: String,
        val wrapper: T,
        override val modelDescription: E
) : ModelInstance<E> {

    val typesPlatform
        get() = wrapper.typesPlatform

    val version
        get() = wrapper.version

    override val isTerminated
        get() = wrapper.isTerminated

    protected var startTime: Double = 0.0
    protected var stopTime: Double = 0.0

    protected val stopDefined
        get() = stopTime > startTime

    /**
     * Current simulation time
     */
    override var simulationTime: Double = 0.0
        internal set

    override val lastStatus: FmiStatus
        get() = wrapper.lastStatus


    override fun enterInitializationMode(): Boolean {
        return true.also {
            wrapper.lastStatus = FmiStatus.OK
        }
    }

    override fun terminate(): Boolean {
        return terminate(true)
    }

    fun terminate(freeInstance: Boolean): Boolean {
        return wrapper.terminate(freeInstance).let { status ->
            LOG.debug("FMU '${modelDescription.modelName}' terminated with status $status! #${hashCode()}")
            status.isOK()
        }
    }

    override fun close() {
        terminate(true)
    }

    protected fun finalize() {
        if (!isTerminated) {
            LOG.warn("Instance ${modelDescription.modelName} was not terminated before garbage collection. Doing it for you..")
            close()
        }
    }

    override fun read(vr: ValueReferences, ref: IntArray): FmiStatus {
        return wrapper.read(vr, ref)
    }

    override fun read(vr: ValueReferences, ref: RealArray): FmiStatus {
        return wrapper.read(vr, ref)
    }

    override fun read(vr: ValueReferences, ref: StringArray): FmiStatus {
        return wrapper.read(vr, ref)
    }

    override fun read(vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return wrapper.read(vr, ref)
    }

    override fun write(vr: ValueReferences, value: IntArray): FmiStatus {
        return wrapper.write(vr, value)
    }

    override fun write(vr: ValueReferences, value: RealArray): FmiStatus {
        return wrapper.write(vr, value)
    }

    override fun write(vr: ValueReferences, value: StringArray): FmiStatus {
        return wrapper.write(vr, value)
    }

    override fun write(vr: ValueReferences, value: BooleanArray): FmiStatus {
        return wrapper.write(vr, value)
    }

    override fun getFMUstate(): FmuState {
        throw IllegalStateException("Feature not available for FMI 1.0")
    }

    override fun setFMUstate(state: FmuState): Boolean {
        throw IllegalStateException("Feature not available for FMI 1.0")
    }

    override fun freeFMUstate(state: FmuState): Boolean {
        throw IllegalStateException("Feature not available for FMI 1.0")
    }

    override fun serializeFMUstate(state: FmuState): ByteArray {
        throw IllegalStateException("Feature not available for FMI 1.0")
    }

    override fun deSerializeFMUstate(state: ByteArray): FmuState {
        throw IllegalStateException("Feature not available for FMI 1.0")
    }

    override fun getDirectionalDerivative(vUnknownRef: ValueReferences, vKnownRef: ValueReferences, dvKnown: RealArray): RealArray {
        throw IllegalStateException("Feature not available for FMI 1.0")
    }

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(AbstractModelInstance::class.java)
    }

}
