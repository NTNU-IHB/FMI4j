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

package no.mechatronics.sfi.fmi4j.importer

import no.mechatronics.sfi.fmi4j.common.*
import no.mechatronics.sfi.fmi4j.common.FmuState
import no.mechatronics.sfi.fmi4j.importer.jni.FmiLibrary
import no.mechatronics.sfi.fmi4j.importer.jni.IntByReference
import no.mechatronics.sfi.fmi4j.importer.jni.LongByReference
import no.mechatronics.sfi.fmi4j.importer.misc.ArrayBuffers
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Lars Ivar Hatledal
 */
abstract class FmiLibraryWrapper<E : FmiLibrary>(
        protected var c: Long,
        library: E
) {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(FmiLibraryWrapper::class.java)
    }

    private val buffers: ArrayBuffers = ArrayBuffers()

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
     * @see FmiLibrary.fmi2GetTypesPlatform
     */
    val typesPlatform: String = library.getTypesPlatform()

    /**
     *
     * @see FmiLibrary.fmi2GetVersion()
     */
    val version: String = library.getVersion()


    /**
     * @see FmiLibrary.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, categories: StringArray): FmiStatus {
        return updateStatus(library.setDebugLogging(c, loggingOn, categories))
    }

    /**
     * @see FmiLibrary.fmi2SetupExperiment
     */
    fun setupExperiment(toleranceDefined: Boolean, tolerance: Double, startTime: Double, stopTime: Double): FmiStatus {
        return updateStatus(library.setupExperiment(c, toleranceDefined, tolerance, startTime, stopTime))
    }

    /**
     * @see FmiLibrary.fmi2EnterInitializationMode
     */
    fun enterInitializationMode(): FmiStatus {
        return updateStatus(library.enterInitializationMode(c))
    }

    /**
     * @see FmiLibrary.fmi2ExitInitializationMode
     */
    fun exitInitializationMode(): FmiStatus {
        return updateStatus(library.exitInitializationMode(c))
    }

    /**
     * @see FmiLibrary.fmi2Terminate
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
     * @see FmiLibrary.fmi2FreeInstance
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
     * @see FmiLibrary.fmi2Reset
     */
    fun reset(): FmiStatus {
        return updateStatus(library.reset(c)).also { status ->
            if (status == FmiStatus.OK) {
                isTerminated = false
            }
        }
    }

    /**
     * @see FmiLibrary.fmi2GetInteger
     */
    @Synchronized
    fun getInteger(valueReference: Int): FmuIntegerRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getInteger(c, vr, iv).let {
                FmuIntegerRead(iv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see FmiLibrary.fmi2GetInteger
     */
    @JvmOverloads
    fun getInteger(vr: IntArray, value: IntArray = IntArray(vr.size)): FmuIntegerArrayRead {
        return library.getInteger(c, vr, value).let {
            FmuIntegerArrayRead(value, updateStatus(it))
        }
    }

    /**
     * @see FmiLibrary.fmi2GetReal
     */
    @Synchronized
    fun getReal(valueReference: Int): FmuRealRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getReal(c, vr, rv).let {
                FmuRealRead(rv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see FmiLibrary.fmi2GetReal
     */
    @JvmOverloads
    fun getReal(vr: IntArray, value: DoubleArray = DoubleArray(vr.size)): FmuRealArrayRead {
        return library.getReal(c, vr, value).let {
            FmuRealArrayRead(value, updateStatus(it))
        }
    }

    /**
     * @see FmiLibrary.fmi2GetString
     */
    @Synchronized
    fun getString(valueReference: Int): FmuStringRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getString(c, vr, sv).let {
                FmuStringRead(sv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see FmiLibrary.fmi2GetString
     */
    @JvmOverloads
    fun getString(vr: IntArray, value: StringArray = StringArray(vr.size, { "" })): FmuStringArrayRead {
        return library.getString(c, vr, value).let {
            FmuStringArrayRead(value, updateStatus(it))
        }
    }

    /**
     * @see FmiLibrary.fmi2GetBoolean
     */
    @Synchronized
    fun getBoolean(valueReference: Int): FmuBooleanRead {
        return with(buffers) {
            vr[0] = valueReference
            library.getBoolean(c, vr, bv).let {
                FmuBooleanRead(bv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see FmiLibrary.fmi2GetBoolean
     */
    @JvmOverloads
    fun getBoolean(vr: IntArray, value: BooleanArray = BooleanArray(vr.size)): FmuBooleanArrayRead {
        return library.getBoolean(c, vr, value).let {
            FmuBooleanArrayRead(value, updateStatus(it))
        }
    }

    /**
     * @see FmiLibrary.fmi2SetInteger
     */
    @Synchronized
    fun setInteger(valueReference: Int, value: Int): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            iv[0] = value
            setInteger(vr, iv)
        }
    }

    /**
     * @see FmiLibrary.fmi2SetInteger
     */
    fun setInteger(vr: IntArray, value: IntArray): FmiStatus {
        return updateStatus((library.setInteger(c, vr, value)))
    }

    /**
     * @see FmiLibrary.fmi2SetReal
     */
    @Synchronized
    fun setReal(valueReference: Int, value: Double): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            rv[0] = value
            setReal(vr, rv)
        }
    }

    /**
     * @see FmiLibrary.fmi2SetReal
     */
    fun setReal(vr: IntArray, value: DoubleArray): FmiStatus {
        return updateStatus((library.setReal(c, vr, value)))
    }

    /**
     * @see FmiLibrary.fmi2SetString
     */
    @Synchronized
    fun setString(valueReference: Int, value: String): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            sv[0] = value
            setString(vr, sv)
        }
    }

    /**
     * @see FmiLibrary.fmi2SetString
     */
    fun setString(vr: IntArray, value: StringArray): FmiStatus {
        return updateStatus((library.setString(c, vr, value)))
    }

    /**
     * @see FmiLibrary.fmi2SetBoolean
     */
    @Synchronized
    fun setBoolean(valueReference: Int, value: Boolean): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            bv[0] = value
            setBoolean(vr, bv)
        }
    }

    /**
     * @see FmiLibrary.fmi2SetBoolean
     */
    fun setBoolean(vr: IntArray, value: BooleanArray): FmiStatus {
        return updateStatus(library.setBoolean(c, vr, value))
    }


    /**
     * @see FmiLibrary.fmi2GetDirectionalDerivative
     */
    fun getDirectionalDerivative(vUnknown_ref: IntArray, vKnown_ref: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray): FmiStatus {
        return updateStatus(library.getDirectionalDerivative(c,
                vUnknown_ref, vKnown_ref, dvKnown, dvUnknown))
    }

    /**
     * @see FmiLibrary.fmi2GetFMUstate
     */
    fun getFMUState(): FmuState {
        return LongByReference().also {
            updateStatus(library.getFMUstate(c, it))
        }.value
    }

    /**
     * @see FmiLibrary.fmi2GetFMUstate
     */
    fun setFMUState(fmuState: FmuState): FmiStatus {
        return updateStatus(library.setFMUstate(c, fmuState))
    }

    /**
     * @see FmiLibrary.fmi2FreeFMUstate
     */
    fun freeFMUState(fmuState: FmuState): FmiStatus {
        return updateStatus(library.freeFMUstate(c, fmuState))
    }

    /**
     * @see FmiLibrary.fmi2SerializedFMUstateSize
     */
    fun serializedFMUStateSize(fmuState: FmuState): Int {
        return IntByReference().let {
            updateStatus(library.serializedFMUstateSize(c, fmuState, it))
            it.value
        }

    }

    /**
     * @see FmiLibrary.fmi2SerializeFMUstate
     */
    fun serializeFMUState(fmuState: FmuState): ByteArray {
        val size = serializedFMUStateSize(fmuState)
        return ByteArray(size).also {
            updateStatus(library.serializeFMUstate(c, fmuState, it))
        }
    }

    /**
     * @see FmiLibrary.fmi2DeSerializeFMUstate
     */
    fun deSerializeFMUState(serializedState: ByteArray): FmuState {
        return LongByReference().also { state ->
            updateStatus(library.deSerializeFMUstate(c, state, serializedState))
        }.value
    }

}
