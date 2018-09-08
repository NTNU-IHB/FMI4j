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

package no.mechatronics.sfi.fmi4j.importer.jni

import no.mechatronics.sfi.fmi4j.common.*
import no.mechatronics.sfi.fmi4j.importer.misc.ArrayBuffers
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
    var lastStatus: FmiStatus = FmiStatus.NONE
        private set

    /**
     * Has terminate been called on the FMU?
     */
    var isTerminated: Boolean = false
        private set


    protected fun updateStatus(status: Int): FmiStatus {
        return updateStatus(FmiStatus.valueOf(status))
    }

    protected fun updateStatus(status: FmiStatus): FmiStatus {
        return status.also { lastStatus = it }
    }

    /**
     * @see Fmi2Library.fmi2GetTypesPlatform
     */
    val typesPlatform: String = library.getTypesPlatform()

    /**
     *
     * @see Fmi2Library.fmi2GetVersion()
     */
    val version: String = library.getVersion()


    /**
     * @see Fmi2Library.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, categories: StringArray): FmiStatus {
        return updateStatus(library.setDebugLogging(c, loggingOn, categories))
    }

    /**
     * @see Fmi2Library.fmi2SetupExperiment
     */
    fun setupExperiment(toleranceDefined: Boolean, tolerance: Double, startTime: Double, stopTime: Double): FmiStatus {
        return updateStatus(library.setupExperiment(c, toleranceDefined, tolerance, startTime, stopTime))
    }

    /**
     * @see Fmi2Library.fmi2EnterInitializationMode
     */
    fun enterInitializationMode(): FmiStatus {
        return updateStatus(library.enterInitializationMode(c))
    }

    /**
     * @see Fmi2Library.fmi2ExitInitializationMode
     */
    fun exitInitializationMode(): FmiStatus {
        return updateStatus(library.exitInitializationMode(c))
    }

    /**
     * @see Fmi2Library.fmi2Terminate
     */
    @JvmOverloads
    fun terminate(freeInstance: Boolean = true): FmiStatus {

        if (!isTerminated) {

            return try {
                updateStatus(library.terminate(c))
            } catch (ex: Error) {
                LOG.error("Error caught on fmi2Terminate: ${ex.javaClass.simpleName}")
                updateStatus(FmiStatus.OK)
            } finally {
                isTerminated = true
                if (freeInstance) {
                    freeInstance()
                }
            }

        } else {
            LOG.warn("Terminated has already been called..")
            return FmiStatus.OK
        }
    }

    /**
     * @see Fmi2Library.fmi2FreeInstance
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
     * @see Fmi2Library.fmi2Reset
     */
    fun reset(): FmiStatus {
        return updateStatus(library.reset(c)).also { status ->
            if (status == FmiStatus.OK) {
                isTerminated = false
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetInteger
     */
    @Synchronized
    fun readInteger(valueReference: Int): FmuIntegerRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getInteger(c, vr, iv).let {
                FmuIntegerRead(iv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetInteger
     */
    @JvmOverloads
    fun readInteger(vr: IntArray, value: IntArray = IntArray(vr.size)): FmiStatus {
        return library.getInteger(c, vr, value).let { updateStatus(it) }
    }

    /**
     * @see Fmi2Library.fmi2GetReal
     */
    @Synchronized
    fun readReal(valueReference: Int): FmuRealRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getReal(c, vr, rv).let {
                FmuRealRead(rv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetReal
     */
    @JvmOverloads
    fun readReal(vr: IntArray, value: DoubleArray = DoubleArray(vr.size)): FmiStatus {
        return library.getReal(c, vr, value).let { updateStatus(it) }
    }

    /**
     * @see Fmi2Library.fmi2GetString
     */
    @Synchronized
    fun readString(valueReference: Int): FmuStringRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getString(c, vr, sv).let {
                FmuStringRead(sv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetString
     */
    @JvmOverloads
    fun readString(vr: IntArray, value: StringArray = StringArray(vr.size) { "" }): FmiStatus {
        return library.getString(c, vr, value).let { updateStatus(it) }
    }

    /**
     * @see Fmi2Library.fmi2GetBoolean
     */
    @Synchronized
    fun readBoolean(valueReference: Int): FmuBooleanRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getBoolean(c, vr, bv).let {
                FmuBooleanRead(bv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetBoolean
     */
    @JvmOverloads
    fun readBoolean(vr: IntArray, value: BooleanArray = BooleanArray(vr.size)): FmiStatus {
        return library.getBoolean(c, vr, value).let { updateStatus(it) }
    }

    /**
     * @see Fmi2Library.fmi2SetInteger
     */
    @Synchronized
    fun writeInteger(valueReference: Int, value: Int): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            iv[0] = value
            writeInteger(vr, iv)
        }
    }

    /**
     * @see Fmi2Library.fmi2SetInteger
     */
    fun writeInteger(vr: IntArray, value: IntArray): FmiStatus {
        return updateStatus((library.setInteger(c, vr, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetReal
     */
    @Synchronized
    fun writeReal(valueReference: Int, value: Double): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            rv[0] = value
            writeReal(vr, rv)
        }
    }

    /**
     * @see Fmi2Library.fmi2SetReal
     */
    fun writeReal(vr: IntArray, value: DoubleArray): FmiStatus {
        return updateStatus((library.setReal(c, vr, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetString
     */
    @Synchronized
    fun writeString(valueReference: Int, value: String): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            sv[0] = value
            writeString(vr, sv)
        }
    }

    /**
     * @see Fmi2Library.fmi2SetString
     */
    fun writeString(vr: IntArray, value: StringArray): FmiStatus {
        return updateStatus((library.setString(c, vr, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetBoolean
     */
    @Synchronized
    fun writeBoolean(valueReference: Int, value: Boolean): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            bv[0] = value
            writeBoolean(vr, bv)
        }
    }

    /**
     * @see Fmi2Library.fmi2SetBoolean
     */
    fun writeBoolean(vr: IntArray, value: BooleanArray): FmiStatus {
        return updateStatus(library.setBoolean(c, vr, value))
    }

    /**
     * @see Fmi2Library.fmi2GetDirectionalDerivative
     */
    fun getDirectionalDerivative(vUnknown_ref: IntArray, vKnown_ref: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray): FmiStatus {
        return updateStatus(library.getDirectionalDerivative(c,
                vUnknown_ref, vKnown_ref, dvKnown, dvUnknown))
    }

    /**
     * @see Fmi2Library.fmi2GetFMUstate
     */
    fun getFMUState(): FmuState {
        return LongByReference().also {
            updateStatus(library.getFMUstate(c, it))
        }.value
    }

    /**
     * @see Fmi2Library.fmi2GetFMUstate
     */
    fun setFMUState(fmuState: FmuState): FmiStatus {
        return updateStatus(library.setFMUstate(c, fmuState))
    }

    /**
     * @see Fmi2Library.fmi2FreeFMUstate
     */
    fun freeFMUState(fmuState: FmuState): FmiStatus {
        return updateStatus(library.freeFMUstate(c, fmuState))
    }

    /**
     * @see Fmi2Library.fmi2SerializedFMUstateSize
     */
    fun serializedFMUStateSize(fmuState: FmuState): Int {
        return IntByReference().let {
            updateStatus(library.serializedFMUstateSize(c, fmuState, it))
            it.value
        }
    }

    /**
     * @see Fmi2Library.fmi2SerializeFMUstate
     */
    fun serializeFMUState(fmuState: FmuState): ByteArray {
        val size = serializedFMUStateSize(fmuState)
        return ByteArray(size).also {
            updateStatus(library.serializeFMUstate(c, fmuState, it))
        }
    }

    /**
     * @see Fmi2Library.fmi2DeSerializeFMUstate
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
