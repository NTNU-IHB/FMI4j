package no.mechatronics.sfi.fmi4j.jna.lib.wrapper

import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference
import no.mechatronics.sfi.fmi4j.jna.convert
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.jna.lib.Fmi2Library
import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2CallbackFunctions
import org.slf4j.LoggerFactory


abstract class Fmi2LibraryWrapper<E: Fmi2Library>(
   libraryFolder: String,
   libraryName: String,
   type: Class<E>
) {

    companion object {
        internal val LOG = LoggerFactory.getLogger(Fmi2LibraryWrapper::class.java)
    }

    protected var library: E?

    protected lateinit var c: Pointer

    private val functions: Fmi2CallbackFunctions

    var isTerminated: Boolean = false
        private set
    var isInstanceFreed: Boolean = false
        private set
    
    init {
        System.setProperty("jna.library.path", libraryFolder)
        library = Native.loadLibrary(libraryName, type)!!
    }

    

    private val vr: IntArray by lazy {
        IntArray(1)
    }

    private val iv: IntArray by lazy {
        IntArray(1)
    }

    private val rv: DoubleArray by lazy {
        DoubleArray(1)
    }

    private val sv: Array<String> by lazy {
        Array<String>(1, {""})
    }

    private val bv: ByteArray by lazy {
        ByteArray(1)
    }

    var lastStatus: Fmi2Status = Fmi2Status.NONE
        private set


    /**
     * @see Fmi2library!!.fmi2GetTypesPlatform
     */
    val typesPlatform: String
        get() = library!!.fmi2GetTypesPlatform()

    /**
     * @see Fmi2library!!.fmi2GetVersion
     */
    val version: String
        get() = library!!.fmi2GetVersion()

    init {
        this.functions = Fmi2CallbackFunctions.ByValue()
    }

    protected fun updateStatus(status: Fmi2Status) : Fmi2Status {
        this.lastStatus = status
        return status
    }

    /**
     * @see Fmi2library!!.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, nCategories: Int, categories: Array<String>) : Fmi2Status {
         return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetDebugLogging(c,
                 convert(loggingOn), nCategories, categories)))
    }

    @Throws(Exception::class)
    fun instantiate(instanceName: String, type: Fmi2Type, guid: String, resourceLocation: String, visible: Boolean, loggingOn: Boolean) {
        this.c = library!!.fmi2Instantiate(instanceName, type.id, guid,
                resourceLocation, functions,
                convert(visible), convert(loggingOn))
    }

    /**
     * @see Fmi2library!!.fmi2SetupExperiment
     */
    fun setupExperiment(toleranceDefined: Boolean, tolerance: Double, startTime: Double, stopTimeDefined: Boolean, stopTime: Double) : Fmi2Status {

        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetupExperiment(c,
                convert(toleranceDefined),
                tolerance, startTime, convert(stopTimeDefined), stopTime)))
    }

    /**
     * @see Fmi2library!!.fmi2EnterInitializationMode
     */
    fun enterInitializationMode() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2EnterInitializationMode(c)))
    }

    /**
     * @see Fmi2library!!.fmi2ExitInitializationMode
     */
    fun exitInitializationMode() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2ExitInitializationMode(c)))
    }

    /**
     * @see Fmi2library!!.fmi2Terminate
     */
    fun terminate(): Boolean {
        if (!isTerminated) {
            val status = updateStatus(Fmi2Status.valueOf(library!!.fmi2Terminate(c)))
            isTerminated = true
            return true
        }
        return false
    }

    /**
     * @see Fmi2library!!.fmi2Reset
     */
    fun reset() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2Reset(c)))
    }

    /**
     * @see Fmi2library!!.fmi2FreeInstance
     */
    fun freeInstance() : Boolean {

        if (library != null) {
            library!!.fmi2FreeInstance(c)

            library = null
            System.gc()
            System.gc()

            isInstanceFreed = true
            return true

        }

        return false

    }

    /**
     * @see Fmi2library!!.fmi2GetInteger
     */
    fun getInteger(valueReference: Int) : Int {
        vr.set(0, valueReference)
        getInteger(vr, iv)
        return iv.get(0)
    }

    /**
     * @see Fmi2library!!.fmi2GetInteger
     */
    fun getInteger(vr: IntArray) : IntArray {
        val value = IntArray(vr.size)
        getInteger(vr, value)
        return value
    }


    /**
     * @see Fmi2library!!.fmi2GetInteger
     */
    fun getInteger(vr: IntArray, value: IntArray) : IntArray {
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetInteger(c, vr, vr.size, value)))
        return value
    }

    fun getReal(valueReference: Int) : Double {
        vr.set(0, valueReference)
        getReal(vr, rv)
        return rv.get(0)
    }

    fun getReal(vr: IntArray) : DoubleArray {
        val value = DoubleArray(vr.size)
        getReal(vr, value)
        return value
    }

    fun getReal(vr: IntArray, value: DoubleArray) : DoubleArray {
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetReal(c, vr, vr.size, value)))
        return value
    }

    fun getString(valueReference: Int) : String {
        vr.set(0, valueReference)
        getString(vr, sv)
        return sv.get(0)
    }

    fun getString(vr: IntArray) : Array<String> {
        val value = Array<String>(vr.size, {""})
        getString(vr, value)
        return value
    }

    fun getString(vr: IntArray, value: Array<String>) : Array<String> {
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetString(c, vr, vr.size, value)))
        return value
    }

    /**
     * @see Fmi2library!!.fmi2GetBoolean
     */
    fun getBoolean(valueReference: Int) : Boolean {
        vr.set(0, valueReference)
        getBoolean(vr, bv)
        return convert(bv.get(0))
    }

    /**
     * @see Fmi2library!!.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray) : BooleanArray {
        val value = BooleanArray(vr.size)
        getBoolean(vr, value)
        return value
    }

    /**
     * @see Fmi2library!!.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray, value: BooleanArray) : BooleanArray {
        val byteArray = value.map { convert(it) }.toByteArray()
        updateStatus(Fmi2Status.valueOf(
                library!!.fmi2GetBoolean(c, vr, vr.size, byteArray)))
        for ((i, byte) in byteArray.withIndex()) {
            value[i] = convert(byte)
        }
        return value
    }

    /**
     * @see Fmi2library!!.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray, value: ByteArray) : ByteArray {
        updateStatus(Fmi2Status.valueOf(
                library!!.fmi2GetBoolean(c, vr, vr.size, value)))
        return value
    }

    /**
     * @see Fmi2library!!.fmi2SetInteger
     */
    fun setInteger( valueReference: Int, value: Int) : Fmi2Status {
        vr.set(0, valueReference)
        iv.set(0, value)
        return setInteger(vr, iv)
    }

    /**
     * @see Fmi2library!!.fmi2SetInteger
     */
    fun setInteger(vr: IntArray, value: IntArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetInteger(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2library!!.fmi2SetReal
     */
    fun setReal( valueReference: Int, value: Double) : Fmi2Status {
        vr.set(0, valueReference)
        rv.set(0, value)
        return setReal(vr, rv)
    }

    /**
     * @see Fmi2library!!.fmi2SetReal
     */
    fun setReal(vr: IntArray, value: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetReal(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2library!!.fmi2SetString
     */
    fun setString( valueReference: Int, value: String) : Fmi2Status {
        vr.set(0, valueReference)
        sv.set(0, value)
        return setString(vr, sv)
    }

    /**
     * @see Fmi2library!!.fmi2SetString
     */
    fun setString(vr: IntArray, value: Array<String>) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetString(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2library!!.fmi2SetBoolean
     */
    fun setBoolean( valueReference: Int, value: Boolean) : Fmi2Status {
        vr.set(0, valueReference)
        bv.set(0, convert(value))
        return setBoolean(vr, bv)
    }

    /**
     * @see Fmi2library!!.fmi2SetBoolean
     */
    fun setBoolean(vr: IntArray, value: ByteArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetBoolean(c, vr, vr.size, value)))
    }

    /**
     * @see Fmi2library!!.fmi2SetBoolean
     */
    fun setBoolean(vr: IntArray, value: BooleanArray) : Fmi2Status {
        return setBoolean(vr, value.map { convert(it) }.toByteArray())
    }

    fun getDirectionalDerivative(vUnknown_ref: IntArray, vKnown_ref: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetDirectionalDerivative(c,
                vUnknown_ref, vUnknown_ref.size, vKnown_ref, vKnown_ref.size, dvKnown, dvUnknown)))
    }

    fun getFMUState(fmuState: Pointer): Pointer {

        val pointerByReference = PointerByReference(fmuState)
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetFMUstate(c, pointerByReference)))
        return pointerByReference.value
    }

    fun setFMUState(fmuState: Pointer) : Fmi2Status {
        val pointerByReference = PointerByReference(fmuState)
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetFMUstate(c, pointerByReference)))
    }

    fun freeFMUState(fmuState: Pointer) : Fmi2Status {
        val pointerByReference = PointerByReference(fmuState)
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2FreeFMUstate(c, pointerByReference)))
    }

    fun serializedFMUStateSize(fmuState: Pointer?): Int {
        val memory = Memory(Pointer.SIZE.toLong())
        updateStatus(Fmi2Status.valueOf(library!!.fmi2SerializedFMUstateSize(c, fmuState!!, memory)))
        return memory.getInt(0)
    }

    fun serializeFMUState(fmuState: Pointer): ByteArray {
        val size = serializedFMUStateSize(c)
        val buffer = ByteArray(size)
        updateStatus(Fmi2Status.valueOf(library!!.fmi2SerializeFMUstate(c, fmuState, buffer, size)))
        return buffer
    }

    fun deSerializeFMUState(serializedState: ByteArray): Pointer {
        val pointerByReference = PointerByReference(Memory(Pointer.SIZE.toLong()))
        updateStatus(Fmi2Status.valueOf(library!!.fmi2DeSerializeFMUstate(c, serializedState, serializedState.size, pointerByReference)))
        return pointerByReference.value
    }

}
