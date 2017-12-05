/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
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

package no.mechatronics.sfi.fmi4j.wrapper

import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.Pointer
import no.mechatronics.sfi.fmi4j.jna.convert
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.jna.Fmi2Library
import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2CallbackFunctions
import no.mechatronics.sfi.fmi4j.misc.ArrayBuffers
import no.mechatronics.sfi.fmi4j.misc.FmuState
import org.slf4j.LoggerFactory


private const val LIBRARY_PATH = "jna.library.path"

data class LibraryPath<E>(
        val dir: String,
        val name: String,
        val type: Class<E>
) {

    var library: E? = null
    var isDisposed: Boolean = false
    private set

    private companion object {

        val LOG = LoggerFactory.getLogger(LibraryPath::class.java)

    }

    init {
        System.setProperty(LIBRARY_PATH,dir)
        library = Native.loadLibrary(name, type)
        LOG.debug("Loaded native library '{}'", name)
    }

    fun dispose() {
        if (!isDisposed) {
            library = null
            System.gc()
            isDisposed = true
        }
    }

}


abstract class Fmi2Wrapper<E: Fmi2Library>(
   private val libraryPath: LibraryPath<E>
) {

    private companion object {
         val LOG = LoggerFactory.getLogger(Fmi2Wrapper::class.java)
    }

    protected val library: E
        get() {
            return libraryPath.library!!
        }

    protected lateinit var c: Pointer

    private val functions: Fmi2CallbackFunctions
    private val buffers: ArrayBuffers by lazy { ArrayBuffers() }

    var lastStatus: Fmi2Status = Fmi2Status.NONE
    private set

    internal var state: FmiState = FmiState.START

    var isTerminated: Boolean = false
        private set

    init {
        functions = Fmi2CallbackFunctions.ByValue()
    }

    protected fun updateStatus(staus: Fmi2Status) : Fmi2Status {
        lastStatus = staus
        return staus
    }

    fun getStateString(): String {
        return state.name
    }

    /**
     * @see Fmi2library.fmi2GetTypesPlatform
     */
    val typesPlatform: String
        get() = library.fmi2GetTypesPlatform()

    /**
     * @see Fmi2library.fmi2GetVersion
     */
    val version: String
        get() = library.fmi2GetVersion()


    /**
     * @see Fmi2library.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, nCategories: Int, categories: Array<String>) : Fmi2Status {
         return updateStatus(Fmi2Status.valueOf(library.fmi2SetDebugLogging(c,
                 convert(loggingOn), nCategories, categories)))
    }


    @Throws(Exception::class)
    fun instantiate(instanceName: String, type: Fmi2Type, guid: String, resourceLocation: String, visible: Boolean, loggingOn: Boolean) {
        state.isCallLegalDuringState(FmiMethod.fmi2Instantiate)
        this.c = library.fmi2Instantiate(instanceName, type.code, guid,
                resourceLocation, functions,
                convert(visible), convert(loggingOn))
        state = FmiState.INSTANTIATED
    }

    /**
     * @see Fmi2library.fmi2SetupExperiment
     */
    fun setupExperiment(toleranceDefined: Boolean, tolerance: Double, startTime: Double, stopTimeDefined: Boolean, stopTime: Double) : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2SetupExperiment)
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetupExperiment(c,
                convert(toleranceDefined),
                tolerance, startTime, convert(stopTimeDefined), stopTime)))
    }

    internal fun <T> updateState(t: T, newState: FmiState) : T {
        state = newState
        return t
    }

    /**
     * @see Fmi2library.fmi2EnterInitializationMode
     */
    fun enterInitializationMode() : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2EnterInitializationMode)
        return updateState(updateStatus(Fmi2Status.valueOf(library.fmi2EnterInitializationMode(c))), FmiState.INITIALISATION_MODE)
    }

    /**
     * @see Fmi2library.fmi2ExitInitializationMode
     */
    fun exitInitializationMode() : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2ExitInitializationMode)
        return updateStatus(Fmi2Status.valueOf(library.fmi2ExitInitializationMode(c)))
    }

    /**
     * @see Fmi2library.fmi2Terminate
     */
    fun terminate(): Boolean {

        if (!isTerminated) {
            state.isCallLegalDuringState(FmiMethod.fmi2Terminate)
            val terminate = library.fmi2Terminate(c);
            updateState(updateStatus(Fmi2Status.valueOf(terminate)), FmiState.TERMINATED)
            isTerminated = true

           Runtime.getRuntime().addShutdownHook(Thread({
               freeInstance()
           }))

            return true
        }
        return false
    }

    /**
     * @see Fmi2library.fmi2Reset
     */
    fun reset() : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2Reset)
        return updateState(updateStatus(Fmi2Status.valueOf(library.fmi2Reset(c))), FmiState.INSTANTIATED)
    }

    /**
     * @see Fmi2library.fmi2FreeInstance
     */
    private fun freeInstance() : Boolean {

        if (!libraryPath.isDisposed) {

            state.isCallLegalDuringState(FmiMethod.fmi2FreeInstance)
            library.fmi2FreeInstance(c)
            libraryPath.dispose()

            return true

        }

        return false
    }

    /**
     * @see Fmi2library.fmi2GetInteger
     */
    fun getInteger(valueReference: Int) : Int {
        with(buffers) {
            vr[0] = valueReference
            getInteger(vr, iv)
            return iv[0]
        }
    }

    /**
     * @see Fmi2library.fmi2GetInteger
     */
    fun getInteger(vr: IntArray) : IntArray {
        val value = IntArray(vr.size)
        getInteger(vr, value)
        return value
    }

    /**
     * @see Fmi2library.fmi2GetInteger
     */
    fun getInteger(vr: IntArray, value: IntArray) : IntArray {
        updateStatus(Fmi2Status.valueOf(library.fmi2GetInteger(c, vr, vr.size, value)))
        return value
    }

    fun getReal(valueReference: Int) : Double {
        with(buffers) {
            vr[0] = valueReference
            getReal(vr, rv)
            return rv[0]
        }
    }

    fun getReal(vr: IntArray) : DoubleArray {
        val value = DoubleArray(vr.size)
        getReal(vr, value)
        return value
    }

    fun getReal(vr: IntArray, value: DoubleArray) : DoubleArray {
        updateStatus(Fmi2Status.valueOf(library.fmi2GetReal(c, vr, vr.size, value)))
        return value
    }

    fun getString(valueReference: Int) : String {
        with(buffers) {
            vr[0] = valueReference
            getString(vr, sv)
            return sv[0]
        }
    }

    fun getString(vr: IntArray) : Array<String> {
        val value = Array<String>(vr.size, {""})
        getString(vr, value)
        return value
    }

    fun getString(vr: IntArray, value: Array<String>) : Array<String> {
        updateStatus(Fmi2Status.valueOf(library.fmi2GetString(c, vr, vr.size, value)))
        return value
    }

    /**
     * @see Fmi2library.fmi2GetBoolean
     */
    fun getBoolean(valueReference: Int) : Boolean {
        with(buffers) {
            vr[0] = valueReference
            getBoolean(vr, bv)
            return convert(bv[0])
        }
    }

    /**
     * @see Fmi2library.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray) : BooleanArray {
        val value = BooleanArray(vr.size)
        getBoolean(vr, value)
        return value
    }

    /**
     * @see Fmi2library.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray, value: BooleanArray) : BooleanArray {
        val byteArray = value.map { convert(it) }.toByteArray()
        updateStatus(Fmi2Status.valueOf(
                library.fmi2GetBoolean(c, vr, vr.size, byteArray)))
        for ((i, byte) in byteArray.withIndex()) {
            value[i] = convert(byte)
        }
        return value
    }

    /**
     * @see Fmi2library.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray, value: ByteArray) : ByteArray {
        updateStatus(Fmi2Status.valueOf(
                library.fmi2GetBoolean(c, vr, vr.size, value)))
        return value
    }

    /**
     * @see Fmi2library.fmi2SetInteger
     */
    fun setInteger( valueReference: Int, value: Int) : Fmi2Status {
        with(buffers) {
            vr[0] = valueReference
            iv[0] = value
            return setInteger(vr, iv)
        }
    }

    /**
     * @see Fmi2library.fmi2SetInteger
     */
    fun setInteger(vr: IntArray, value: IntArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetInteger(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2library.fmi2SetReal
     */
    fun setReal( valueReference: Int, value: Double) : Fmi2Status {
        with(buffers) {
            vr[0] = valueReference
            rv[0] = value
            return setReal(vr, rv)
        }
    }

    /**
     * @see Fmi2library.fmi2SetReal
     */
    fun setReal(vr: IntArray, value: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetReal(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2library.fmi2SetString
     */
    fun setString( valueReference: Int, value: String) : Fmi2Status {
        with(buffers) {
            vr[0] = valueReference
            sv[0] = value
            return setString(vr, sv)
        }
    }

    /**
     * @see Fmi2library.fmi2SetString
     */
    fun setString(vr: IntArray, value: Array<out String>) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetString(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2library.fmi2SetBoolean
     */
    fun setBoolean( valueReference: Int, value: Boolean) : Fmi2Status {
        with(buffers) {
            vr[0] = valueReference
            bv[0] = convert(value)
            return setBoolean(vr, bv)
        }
    }

    /**
     * @see Fmi2library.fmi2SetBoolean
     */
    fun setBoolean(vr: IntArray, value: ByteArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetBoolean(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2library.fmi2SetBoolean
     */
    fun setBoolean(vr: IntArray, value: BooleanArray) : Fmi2Status {
        return setBoolean(vr, value.map { convert(it) }.toByteArray())
    }

    fun getDirectionalDerivative(vUnknown_ref: IntArray, vKnown_ref: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray) : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2GetDirectionalDerivative)
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetDirectionalDerivative(c,
                vUnknown_ref, vUnknown_ref.size, vKnown_ref, vKnown_ref.size, dvKnown, dvUnknown)))
    }


    @JvmOverloads
    fun getFMUState(fmuState: FmuState = FmuState()): FmuState {
        state.isCallLegalDuringState(FmiMethod.fmi2GetFMUstate)
        updateStatus(Fmi2Status.valueOf(library.fmi2GetFMUstate(c, fmuState.pointerByReference)))
        return fmuState
    }

    fun setFMUState(fmuState: FmuState) : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2SetFMUstate)
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetFMUstate(c, fmuState.pointer)))
    }

    fun freeFMUState(fmuState: FmuState) : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2FreeFMUstate)
        return updateStatus(Fmi2Status.valueOf(library.fmi2FreeFMUstate(c, fmuState.pointerByReference)))
    }

    fun serializedFMUStateSize(fmuState: FmuState): Int {
        state.isCallLegalDuringState(FmiMethod.fmi2SerializedFMUstateSize)
        val memory = Memory(Pointer.SIZE.toLong())
        updateStatus(Fmi2Status.valueOf(library.fmi2SerializedFMUstateSize(c, fmuState.pointer, memory)))
        return memory.getInt(0)
    }

    fun serializeFMUState(fmuState: FmuState): ByteArray {
        state.isCallLegalDuringState(FmiMethod.fmi2SerializeFMUstate)
        val size = serializedFMUStateSize(fmuState)
        val buffer = ByteArray(size)
        updateStatus(Fmi2Status.valueOf(library.fmi2SerializeFMUstate(c, fmuState.pointer, buffer, size)))
        return buffer
    }

    fun deSerializeFMUState(serializedState: ByteArray): FmuState {
        state.isCallLegalDuringState(FmiMethod.fmi2DeSerializeFMUstate)
        val state = FmuState()
        updateStatus(Fmi2Status.valueOf(library.fmi2DeSerializeFMUstate(c, serializedState, serializedState.size, state.pointerByReference)))
        return state
    }

}
