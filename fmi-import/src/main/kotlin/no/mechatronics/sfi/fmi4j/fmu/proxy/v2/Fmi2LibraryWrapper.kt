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


package no.mechatronics.sfi.fmi4j.fmu.proxy.v2

import com.sun.jna.Memory
import com.sun.jna.Pointer
import no.mechatronics.sfi.fmi4j.common.*
import no.mechatronics.sfi.fmi4j.fmu.misc.ArrayBuffers
import no.mechatronics.sfi.fmi4j.fmu.misc.FmiBoolean
import no.mechatronics.sfi.fmi4j.fmu.misc.FmuState
import no.mechatronics.sfi.fmi4j.fmu.misc.LibraryProvider
import no.mechatronics.sfi.fmi4j.fmu.proxy.v2.structs.Fmi2CallbackFunctions
import no.mechatronics.sfi.fmi4j.modeldescription.variables.StringArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * @author Lars Ivar Hatledal
 */
abstract class Fmi2LibraryWrapper<out E : Fmi2Library>(
        protected var c: Pointer,
        private val libraryProvider: LibraryProvider<E>
) {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(Fmi2LibraryWrapper::class.java)
    }

    private val functions: Fmi2CallbackFunctions = Fmi2CallbackFunctions()
    private val buffers: ArrayBuffers = ArrayBuffers()

    var isInstanceFreed = false
        private set

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

    protected val library: E
        get() = libraryProvider.get()


    protected fun updateStatus(status: Int): FmiStatus {
        return updateStatus(FmiStatus.valueOf(status))
    }

    protected fun updateStatus(status: FmiStatus): FmiStatus {
        return status.also { lastStatus = it }
    }

    /**
     * @see Fmi2Library.fmi2GetTypesPlatform
     */
    val typesPlatform: String = library.fmi2GetTypesPlatform()

    /**
     *
     * @see Fmi2Library.fmi2GetVersion()
     */
    val version: String = library.fmi2GetVersion()


    /**
     * @see Fmi2Library.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, nCategories: Int, categories: StringArray): FmiStatus {
        return updateStatus(library.fmi2SetDebugLogging(c,
                FmiBoolean.convert(loggingOn), nCategories, categories))
    }

    /**
     * @see Fmi2Library.fmi2SetupExperiment
     */
    fun setupExperiment(toleranceDefined: Boolean, tolerance: Double, startTime: Double, stopTimeDefined: Boolean, stopTime: Double): FmiStatus {
        return updateStatus(library.fmi2SetupExperiment(c,
                FmiBoolean.convert(toleranceDefined), tolerance,
                startTime, FmiBoolean.convert(stopTimeDefined), stopTime))
    }

    /**
     * @see Fmi2Library.fmi2EnterInitializationMode
     */
    fun enterInitializationMode(): FmiStatus {
        return updateStatus(library.fmi2EnterInitializationMode(c))
    }

    /**
     * @see Fmi2Library.fmi2ExitInitializationMode
     */
    fun exitInitializationMode(): FmiStatus {
        return updateStatus(library.fmi2ExitInitializationMode(c))
    }

    /**
     * @see Fmi2Library.fmi2Terminate
     */
    @JvmOverloads
    fun terminate(freeInstance: Boolean = true): FmiStatus {

        if (!isTerminated) {

            return try {
                updateStatus(library.fmi2Terminate(c))
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
            return FmiStatus.Discard
        }
    }

    /**
     * @see Fmi2Library.fmi2FreeInstance
     */
    internal fun freeInstance() {
        if (!isInstanceFreed) {
            var success = false
            try {
                library.fmi2FreeInstance(c)
                success = true
            } catch (ex: Error) {
                LOG.error("Error caught on fmi2FreeInstance: ${ex.javaClass.simpleName}")
            } finally {
                val msg = if (success) "successfully" else "unsuccessfully"
                LOG.debug("Instance freed $msg")
                isInstanceFreed = true
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2Reset
     */
    fun reset(): FmiStatus {
        return updateStatus(library.fmi2Reset(c)).also { status ->
            if (status == FmiStatus.OK) {
                isTerminated = false
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetInteger
     */
    fun getInteger(valueReference: Int): FmuIntegerRead {
        return with(buffers) {
            vr[0] = valueReference
            library.fmi2GetInteger(c, vr, vr.size, iv).let {
                FmuIntegerRead(iv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetInteger
     */
    @JvmOverloads
    fun getInteger(vr: IntArray, value: IntArray = IntArray(vr.size)): FmuIntegerArrayRead {
        return library.fmi2GetInteger(c, vr, vr.size, value).let {
            FmuIntegerArrayRead(value, updateStatus(it))
        }
    }

    /**
     * @see Fmi2Library.fmi2GetReal
     */
    fun getReal(valueReference: Int): FmuRealRead {
        return with(buffers) {
            vr[0] = valueReference
            library.fmi2GetReal(c, vr, vr.size, rv).let {
                FmuRealRead(rv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetReal
     */
    @JvmOverloads
    fun getReal(vr: IntArray, value: DoubleArray = DoubleArray(vr.size)): FmuRealArrayRead {
        return library.fmi2GetReal(c, vr, vr.size, value).let {
            FmuRealArrayRead(value, updateStatus(it))
        }
    }

    /**
     * @see Fmi2Library.fmi2GetString
     */
    fun getString(valueReference: Int): FmuStringRead {
        return with(buffers) {
            vr[0] = valueReference
            library.fmi2GetString(c, vr, vr.size, sv).let {
                FmuStringRead(sv[0], updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetString
     */
    @JvmOverloads
    fun getString(vr: IntArray, value: StringArray = StringArray(vr.size, { "" })): FmuStringArrayRead {
        return (library.fmi2GetString(c, vr, vr.size, value)).let {
            FmuStringArrayRead(value, updateStatus(it))
        }
    }

    /**
     * @see Fmi2Library.fmi2GetBoolean
     */
    fun getBoolean(valueReference: Int): FmuBooleanRead {
        return with(buffers) {
            vr[0] = valueReference
            library.fmi2GetBoolean(c, vr, vr.size, iv).let {
                FmuBooleanRead(FmiBoolean.convert(iv[0]), updateStatus(it))
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetBoolean
     */
    @JvmOverloads
    fun getBoolean(vr: IntArray, value: BooleanArray = BooleanArray(vr.size)): FmuBooleanArrayRead {
        val intArray = value.map { FmiBoolean.convert(it) }.toIntArray()
        return library.fmi2GetBoolean(c, vr, vr.size, intArray).let {
            for ((i, v) in intArray.withIndex()) {
                value[i] = FmiBoolean.convert(v)
            }
            FmuBooleanArrayRead(value, updateStatus(it))
        }
    }

    /**
     * @see Fmi2Library.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray, value: IntArray): FmuIntegerArrayRead {
        return library.fmi2GetBoolean(c, vr, vr.size, value).let {
            FmuIntegerArrayRead(value, updateStatus(it))
        }
    }

    /**
     * @see Fmi2Library.fmi2SetInteger
     */
    fun setInteger(valueReference: Int, value: Int): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            iv[0] = value
            setInteger(vr, iv)
        }
    }

    /**
     * @see Fmi2Library.fmi2SetInteger
     */
    fun setInteger(vr: IntArray, value: IntArray): FmiStatus {
        return updateStatus((library.fmi2SetInteger(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetReal
     */
    fun setReal(valueReference: Int, value: Double): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            rv[0] = value
            setReal(vr, rv)
        }
    }

    /**
     * @see Fmi2Library.fmi2SetReal
     */
    fun setReal(vr: IntArray, value: DoubleArray): FmiStatus {
        return updateStatus((library.fmi2SetReal(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetString
     */
    fun setString(valueReference: Int, value: String): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            sv[0] = value
            setString(vr, sv)
        }
    }

    /**
     * @see Fmi2Library.fmi2SetString
     */
    fun setString(vr: IntArray, value: StringArray): FmiStatus {
        return updateStatus((library.fmi2SetString(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetBoolean
     */
    fun setBoolean(valueReference: Int, value: Boolean): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            iv[0] = FmiBoolean.convert(value)
            setBoolean(vr, iv)
        }
    }

    /**
     * @see Fmi2Library.fmi2SetBoolean
     */
    fun setBoolean(vr: IntArray, value: IntArray): FmiStatus {
        return updateStatus(library.fmi2SetBoolean(c, vr, vr.size, value))
    }

    /**
     * @see Fmi2Library.fmi2SetBoolean
     */
    fun setBoolean(vr: IntArray, value: BooleanArray): FmiStatus {
        return setBoolean(vr, value.map { if (it) 1 else 0 }.toIntArray())
    }

    /**
     * @see Fmi2Library.fmi2GetDirectionalDerivative
     */
    fun getDirectionalDerivative(vUnknown_ref: IntArray, vKnown_ref: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray): FmiStatus {
        return updateStatus(library.fmi2GetDirectionalDerivative(c,
                vUnknown_ref, vUnknown_ref.size, vKnown_ref, vKnown_ref.size, dvKnown, dvUnknown))
    }

    /**
     * @see Fmi2Library.fmi2GetFMUstate
     */
    fun getFMUState(fmuState: FmuState): FmuState {
        return fmuState.also {
            updateStatus(library.fmi2GetFMUstate(c, fmuState))
        }
    }

    /**
     * @see Fmi2Library.fmi2GetFMUstate
     */
    fun setFMUState(fmuState: FmuState): FmiStatus {
        return updateStatus(library.fmi2SetFMUstate(c, fmuState.pointer))
    }

    /**
     * @see Fmi2Library.fmi2FreeFMUstate
     */
    fun freeFMUState(fmuState: FmuState): FmiStatus {
        return updateStatus(library.fmi2FreeFMUstate(c, fmuState))
    }

    /**
     * @see Fmi2Library.fmi2SerializedFMUstateSize
     */
    fun serializedFMUStateSize(fmuState: FmuState): Int {
        val memory = Memory(Pointer.SIZE.toLong())
        updateStatus(library.fmi2SerializedFMUstateSize(c, fmuState.pointer, memory))
        return memory.getInt(0)
    }

    /**
     * @see Fmi2Library.fmi2SerializeFMUstate
     */
    fun serializeFMUState(fmuState: FmuState): ByteArray {
        val size = serializedFMUStateSize(fmuState)
        return ByteArray(size).also {
            updateStatus(library.fmi2SerializeFMUstate(c, fmuState.pointer, it, size))
        }
    }

    /**
     * @see Fmi2Library.fmi2DeSerializeFMUstate
     */
    fun deSerializeFMUState(serializedState: ByteArray): FmuState {
        return FmuState().also { state ->
            updateStatus(library.fmi2DeSerializeFMUstate(c, serializedState, serializedState.size, state))
        }
    }

}







