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

package no.ntnu.ihb.fmi4j.importer.jni

import no.ntnu.ihb.fmi4j.common.*
import no.ntnu.ihb.fmi4j.importer.misc.ArrayBuffers
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Lars Ivar Hatledal
 */
abstract class Fmi2LibraryWrapper<E : Fmi2Library>(
        protected var c: Long,
        library: E
) {

    private val buffers: ArrayBuffers by lazy {
        ArrayBuffers()
    }

    private var _library: E? = library

    protected val library: E
        get() = _library ?: throw IllegalAccessException("Library is no longer accessible!")

    val isInstanceFreed: Boolean
        get() = _library == null

    /**
     * The status returned from the last call to a FMU function
     */
    var lastStatus: Status = Status.NONE
        private set

    /**
     * Has terminate been called on the FMU?
     */
    var isTerminated: Boolean = false
        private set


    protected fun updateStatus(status: Status): Status {
        return status.also { lastStatus = it }
    }

    /**
     * @see Fmi2Library.getTypesPlatform
     */
    val typesPlatform: String
        get() = library.getTypesPlatform()

    /**
     *
     * @see Fmi2Library.getVersion()
     */
    val version: String
        get() = library.getVersion()


    /**
     * @see Fmi2Library.setDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, categories: StringArray): Status {
        return updateStatus(library.setDebugLogging(c, loggingOn, categories))
    }

    /**
     * @see Fmi2Library.setupExperiment
     */
    fun setupExperiment(tolerance: Double, startTime: Double, stopTime: Double): Status {
        return updateStatus(library.setupExperiment(c, tolerance, startTime, stopTime))
    }

    /**
     * @see Fmi2Library.enterInitializationMode
     */
    fun enterInitializationMode(): Status {
        return updateStatus(library.enterInitializationMode(c))
    }

    /**
     * @see Fmi2Library.exitInitializationMode
     */
    fun exitInitializationMode(): Status {
        return updateStatus(library.exitInitializationMode(c))
    }

    /**
     * @see Fmi2Library.terminate
     */
    @JvmOverloads
    fun terminate(freeInstance: Boolean = true): Status {

        if (!isTerminated) {

            return try {
                updateStatus(library.terminate(c))
            } catch (ex: Error) {
                LOG.error("Error caught on fmi2Terminate: ${ex.javaClass.simpleName}")
                updateStatus(Status.OK)
            } finally {
                isTerminated = true
                if (freeInstance) {
                    freeInstance()
                }
            }

        } else {
            LOG.warn("Terminated has already been called..")
            return Status.OK
        }
    }

    /**
     * @see Fmi2Library.freeInstance
     */
    internal fun freeInstance() {
        if (!isInstanceFreed) {
            var success = false
            try {
                library.freeInstance(c)
                success = true
            } catch (ex: Error) {
                LOG.error("Error caught on fmi2FreeInstance: ${ex.javaClass.simpleName}")
            } finally {
                val msg = if (success) "successfully" else "unsuccessfully"
                LOG.debug("Instance freed $msg")
                _library = null
                System.gc()
            }
        }
    }

    /**
     * @see Fmi2Library.reset
     */
    fun reset(): Status {
        return updateStatus(library.reset(c)).also { status ->
            if (status == Status.OK) {
                isTerminated = false
            }
        }
    }

    /**
     * @see Fmi2Library.getInteger
     */
    @Synchronized
    fun readInteger(valueReference: ValueReference): IntegerRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getInteger(c, vr, iv).let {
                IntegerRead(iv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.getInteger
     */
    fun readInteger(vr: ValueReferences, value: IntArray): Status {
        return library.getInteger(c, vr, value).let { updateStatus(it) }
    }

    /**
     * @see Fmi2Library.getReal
     */
    @Synchronized
    fun readReal(valueReference: ValueReference): RealRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getReal(c, vr, rv).let {
                RealRead(rv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.getReal
     */
    fun readReal(vr: ValueReferences, value: DoubleArray): Status {
        return library.getReal(c, vr, value).let { updateStatus(it) }
    }

    /**
     * @see Fmi2Library.getString
     */
    @Synchronized
    fun readString(valueReference: ValueReference): StringRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getString(c, vr, sv).let {
                StringRead(sv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.getString
     */
    fun readString(vr: ValueReferences, value: StringArray): Status {
        return library.getString(c, vr, value).let { updateStatus(it) }
    }

    /**
     * @see Fmi2Library.getBoolean
     */
    @Synchronized
    fun readBoolean(valueReference: ValueReference): BooleanRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getBoolean(c, vr, bv).let {
                BooleanRead(bv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.getBoolean
     */
    fun readBoolean(vr: ValueReferences, value: BooleanArray): Status {
        return library.getBoolean(c, vr, value).let { updateStatus(it) }
    }

    /**
     * @see Fmi2Library.setInteger
     */
    @Synchronized
    fun writeInteger(valueReference: ValueReference, value: Int): Status {
        return with(buffers) {
            vr[0] = valueReference
            iv[0] = value
            writeInteger(vr, iv)
        }
    }

    /**
     * @see Fmi2Library.setInteger
     */
    fun writeInteger(vr: ValueReferences, value: IntArray): Status {
        return updateStatus((library.setInteger(c, vr, value)))
    }

    /**
     * @see Fmi2Library.setReal
     */
    @Synchronized
    fun writeReal(valueReference: ValueReference, value: Double): Status {
        return with(buffers) {
            vr[0] = valueReference
            rv[0] = value
            writeReal(vr, rv)
        }
    }

    /**
     * @see Fmi2Library.setReal
     */
    fun writeReal(vr: ValueReferences, value: DoubleArray): Status {
        return updateStatus((library.setReal(c, vr, value)))
    }

    /**
     * @see Fmi2Library.setString
     */
    @Synchronized
    fun writeString(valueReference: ValueReference, value: String): Status {
        return with(buffers) {
            vr[0] = valueReference
            sv[0] = value
            writeString(vr, sv)
        }
    }

    /**
     * @see Fmi2Library.setString
     */
    fun writeString(vr: ValueReferences, value: StringArray): Status {
        return updateStatus((library.setString(c, vr, value)))
    }

    /**
     * @see Fmi2Library.setBoolean
     */
    @Synchronized
    fun writeBoolean(valueReference: ValueReference, value: Boolean): Status {
        return with(buffers) {
            vr[0] = valueReference
            bv[0] = value
            writeBoolean(vr, bv)
        }
    }

    /**
     * @see Fmi2Library.setBoolean
     */
    fun writeBoolean(vr: ValueReferences, value: BooleanArray): Status {
        return updateStatus(library.setBoolean(c, vr, value))
    }

    /**
     * @see Fmi2Library.getDirectionalDerivative
     */
    fun getDirectionalDerivative(vUnknown_ref: ValueReferences, vKnown_ref: ValueReferences, dvKnown: DoubleArray, dvUnknown: DoubleArray): Status {
        return updateStatus(library.getDirectionalDerivative(c,
                vUnknown_ref, vKnown_ref, dvKnown, dvUnknown))
    }

    /**
     * @see Fmi2Library.getFMUstate
     */
    fun getFMUState(): FmuState {
        return LongByReference().also {
            updateStatus(library.getFMUstate(c, it))
        }.value
    }

    /**
     * @see Fmi2Library.getFMUstate
     */
    fun setFMUState(fmuState: FmuState): Status {
        return updateStatus(library.setFMUstate(c, fmuState))
    }

    /**
     * @see Fmi2Library.freeFMUstate
     */
    fun freeFMUState(fmuState: FmuState): Status {
        return updateStatus(library.freeFMUstate(c, fmuState))
    }

    /**
     * @see Fmi2Library.serializedFMUstateSize
     */
    fun serializedFMUStateSize(fmuState: FmuState): Int {
        return IntByReference().let {
            updateStatus(library.serializedFMUstateSize(c, fmuState, it))
            it.value
        }
    }

    /**
     * @see Fmi2Library.serializeFMUstate
     */
    fun serializeFMUState(fmuState: FmuState): ByteArray {
        val size = serializedFMUStateSize(fmuState)
        return ByteArray(size).also {
            updateStatus(library.serializeFMUstate(c, fmuState, it))
        }
    }

    /**
     * @see Fmi2Library.deSerializeFMUstate
     */
    fun deSerializeFMUState(serializedState: ByteArray): FmuState {
        return LongByReference().also { state ->
            updateStatus(library.deSerializeFMUstate(c, state, serializedState))
        }.value
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(Fmi2LibraryWrapper::class.java)
    }

}
