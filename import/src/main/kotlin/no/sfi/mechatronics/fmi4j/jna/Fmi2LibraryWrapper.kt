package no.sfi.mechatronics.fmi4j.jna

import com.sun.jna.Memory
import com.sun.jna.Pointer
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.DoubleByReference
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import org.slf4j.LoggerFactory



sealed class Fmi2LibraryWrapper<E : Fmi2Library>(protected var library: E?) {

    val LOG = LoggerFactory.getLogger(Fmi2LibraryWrapper::class.java)

    protected var c: Pointer? = null

    private val functions: Fmi2CallbackFunctions

    var isTerminated: Boolean = false
        private set
    var isInstanceFreed: Boolean = false
        private set

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
     * @see Fmi2Library.fmi2GetTypesPlatform
     */
    val typesPlatform: String
        get() = library!!.fmi2GetTypesPlatform()

    /**
     * @see Fmi2Library.fmi2GetVersion
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
     * @see Fmi2Library.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, nCategories: Int, categories: Array<String>) : Fmi2Status {
         return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetDebugLogging(c!!,
                 convert(loggingOn), nCategories, categories)))
    }

    @Throws(Exception::class)
    fun instantiate(instanceName: String, type: Fmi2Type, guid: String, resourceLocation: String, visible: Boolean, loggingOn: Boolean) {
        this.c = library!!.fmi2Instantiate(instanceName, type.id, guid,
                resourceLocation, functions,
                convert(visible), convert(loggingOn))

        if (this.c == null) {
            throw Exception("Unable to instantiate FMU.. Returned null pointer")
        }

    }

    /**
     * @see Fmi2Library.fmi2SetupExperiment
     */
    fun setupExperiment(toleranceDefined: Boolean, tolerance: Double, startTime: Double, stopTimeDefined: Boolean, stopTime: Double) : Fmi2Status {

        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetupExperiment(c!!,
                convert(toleranceDefined),
                tolerance, startTime, convert(stopTimeDefined), stopTime)))
    }

    /**
     * @see Fmi2Library.fmi2EnterInitializationMode
     */
    fun enterInitializationMode() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2EnterInitializationMode(c!!)))
    }

    /**
     * @see Fmi2Library.fmi2ExitInitializationMode
     */
    fun exitInitializationMode() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2ExitInitializationMode(c!!)))
    }

    /**
     * @see Fmi2Library.fmi2Terminate
     */
    fun terminate(): Fmi2Status {
        if (!isTerminated) {
            val status = updateStatus(Fmi2Status.valueOf(library!!.fmi2Terminate(c!!)))
            isTerminated = true
            return status
        }
        return Fmi2Status.Discard
    }

    /**
     * @see Fmi2Library.fmi2Reset
     */
    fun reset() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2Reset(c!!)))
    }

    /**
     * @see Fmi2Library.fmi2FreeInstance
     */
    fun freeInstance() {

        if (library != null) {
            library!!.fmi2FreeInstance(c!!)
            library = null
            Thread.sleep(100)
            System.gc()

            isInstanceFreed = true

        }

    }

    /**
     * @see Fmi2Library.fmi2GetInteger
     */
    fun getInteger(valueReference: Int) : Int {
        vr.set(0, valueReference)
        getInteger(vr, iv)
        return iv.get(0)
    }

    /**
     * @see Fmi2Library.fmi2GetInteger
     */
    fun getInteger(vr: IntArray) : IntArray {
        val value = IntArray(vr.size)
        getInteger(vr, value)
        return value
    }


    /**
     * @see Fmi2Library.fmi2GetInteger
     */
    fun getInteger(vr: IntArray, value: IntArray) : IntArray {
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetInteger(c!!, vr, vr.size, value)))
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
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetReal(c!!, vr, vr.size, value)))
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
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetString(c!!, vr, vr.size, value)))
        return value
    }

    /**
     * @see Fmi2Library.fmi2GetBoolean
     */
    fun getBoolean(valueReference: Int) : Boolean {
        vr.set(0, valueReference)
        getBoolean(vr, bv)
        return convert(bv.get(0))
    }

    /**
     * @see Fmi2Library.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray) : BooleanArray {
        val value = BooleanArray(vr.size)
        getBoolean(vr, value)
        return value
    }

    /**
     * @see Fmi2Library.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray, value: BooleanArray) : BooleanArray {
        val byteArray = value.map { convert(it) }.toByteArray()
        updateStatus(Fmi2Status.valueOf(
                library!!.fmi2GetBoolean(c!!, vr, vr.size, byteArray)))
        for ((i, byte) in byteArray.withIndex()) {
            value[i] = convert(byte)
        }
        return value
    }

    /**
     * @see Fmi2Library.fmi2GetBoolean
     */
    fun getBoolean(vr: IntArray, value: ByteArray) : ByteArray {
        updateStatus(Fmi2Status.valueOf(
                library!!.fmi2GetBoolean(c!!, vr, vr.size, value)))
        return value
    }

    /**
     * @see Fmi2Library.fmi2SetInteger
     */
    fun setInteger( valueReference: Int, value: Int) : Fmi2Status {
        vr.set(0, valueReference)
        iv.set(0, value)
        return setInteger(vr, iv)
    }

    /**
     * @see Fmi2Library.fmi2SetInteger
     */
    fun setInteger(vr: IntArray, value: IntArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetInteger(c!!, vr, vr.size, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetReal
     */
    fun setReal( valueReference: Int, value: Double) : Fmi2Status {
        vr.set(0, valueReference)
        rv.set(0, value)
        return setReal(vr, rv)
    }

    /**
     * @see Fmi2Library.fmi2SetReal
     */
    fun setReal(vr: IntArray, value: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetReal(c!!, vr, vr.size, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetString
     */
    fun setString( valueReference: Int, value: String) : Fmi2Status {
        vr.set(0, valueReference)
        sv.set(0, value)
        return setString(vr, sv)
    }

    /**
     * @see Fmi2Library.fmi2SetString
     */
    fun setString(vr: IntArray, value: Array<String>) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetString(c!!, vr, vr.size, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetBoolean
     */
    fun setBoolean( valueReference: Int, value: Boolean) : Fmi2Status {
        vr.set(0, valueReference)
        bv.set(0, convert(value))
        return setBoolean(vr, bv)
    }

    /**
     * @see Fmi2Library.fmi2SetBoolean
     */
    fun setBoolean(vr: IntArray, value: ByteArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetBoolean(c!!, vr, vr.size, value)))
    }

    /**
     * @see Fmi2Library.fmi2SetBoolean
     */
    fun setBoolean(vr: IntArray, value: BooleanArray) : Fmi2Status {
        return setBoolean(vr, value.map { convert(it) }.toByteArray())
    }

    fun getDirectionalDerivative(vUnknown_ref: IntArray, vKnown_ref: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetDirectionalDerivative(c!!,
                vUnknown_ref, vUnknown_ref.size, vKnown_ref, vKnown_ref.size, dvKnown, dvUnknown)))
    }

    fun getFMUState(fmuState: Pointer): Pointer {

        val pointerByReference = PointerByReference(fmuState)
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetFMUstate(c!!, pointerByReference)))
        return pointerByReference.value
    }

    fun setFMUState(fmuState: Pointer) : Fmi2Status {
        val pointerByReference = PointerByReference(fmuState)
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetFMUstate(c!!, pointerByReference)))
    }

    fun freeFMUState(fmuState: Pointer) : Fmi2Status {
        val pointerByReference = PointerByReference(fmuState)
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2FreeFMUstate(c!!, pointerByReference)))
    }

    fun serializedFMUStateSize(fmuState: Pointer?): Int {
        val memory = Memory(Pointer.SIZE.toLong())
        updateStatus(Fmi2Status.valueOf(library!!.fmi2SerializedFMUstateSize(c!!, fmuState!!, memory)))
        return memory.getInt(0)
    }

    fun serializeFMUState(fmuState: Pointer): ByteArray {
        val size = serializedFMUStateSize(c)
        val buffer = ByteArray(size)
        updateStatus(Fmi2Status.valueOf(library!!.fmi2SerializeFMUstate(c!!, fmuState, buffer, size)))
        return buffer
    }

    fun deSerializeFMUState(serializedState: ByteArray): Pointer {
        val pointerByReference = PointerByReference(Memory(Pointer.SIZE.toLong()))
        updateStatus(Fmi2Status.valueOf(library!!.fmi2DeSerializeFMUstate(c!!, serializedState, serializedState.size, pointerByReference)))
        return pointerByReference.value
    }

}

class CoSimulationLibraryWrapper(library: Fmi2CoSimulationLibrary) : Fmi2LibraryWrapper<Fmi2CoSimulationLibrary>(library) {

    /**
     * @see Fmi2CoSimulationLibrary.fmi2SetRealInputDerivatives
     */
    fun setRealInputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetRealInputDerivatives(c!!, vr, vr.size, order, value)))
    }

    /**
     * @see Fmi2CoSimulationLibrary.fmi2GetRealOutputDerivatives
     */
    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetRealOutputDerivatives(c!!, vr, vr.size, order, value)))
    }

    /**
     * @see Fmi2CoSimulationLibrary.fmi2DoStep
     */
    fun doStep(t: Double, dt: Double, noSetFMUStatePriorToCurrent: Boolean) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2DoStep(c!!, t, dt, convert(noSetFMUStatePriorToCurrent))))
    }

    /**
     * @see Fmi2CoSimulationLibrary.fmi2CancelStep
     */
    fun cancelStep() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2CancelStep(c!!)))
    }

    /**
     * @see Fmi2CoSimulationLibrary.fmi2GetStatus
     */
    fun getStatus(c: Pointer, s: Fmi2StatusKind): Fmi2Status {
        val i = IntByReference()
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetIntegerStatus(c, s, i)))
        return Fmi2Status.valueOf(i.value)
    }

    /**
     * @see Fmi2CoSimulationLibrary.fmi2GetRealStatus
     */
    fun getRealStatus(c: Pointer, s: Fmi2StatusKind): Double {
        val d = DoubleByReference()
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetRealStatus(c, s, d)))
        return d.value
    }

    /**
     * @see Fmi2CoSimulationLibrary.fmi2GetIntegerStatus
     */
    fun getIntegerStatus(c: Pointer, s: Fmi2StatusKind): Int {
        val i = IntByReference()
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetIntegerStatus(c, s, i)))
        return i.value
    }

    /**
     * @see Fmi2CoSimulationLibrary.fmi2GetBooleanStatus
     */
    fun getBooleanStatus(c: Pointer, s: Fmi2StatusKind): Boolean {
        val b = ByteByReference()
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetBooleanStatus(c, s, b)))
        return convert(b.value)
    }

    /**
     * @see Fmi2CoSimulationLibrary.fmi2GetStringStatus
     */
    fun getStringStatus(c: Pointer, s: Fmi2StatusKind): String {
        val str = StringByReference()
        updateStatus(Fmi2Status.valueOf(library!!.fmi2GetStringStatus(c, s, str)))
        return str.value
    }

}


/**
 *
 * @author laht
 */
public class ModelExchangeLibraryWrapper(library: Fmi2ModelExchangeLibrary) : Fmi2LibraryWrapper<Fmi2ModelExchangeLibrary>(library) {


    private val enterEventMode: ByteByReference = ByteByReference()
    private val terminateSimulation: ByteByReference = ByteByReference()


    /**
     * Set a new time instant and re-initialize caching of variables that depend
     * on time, provided the newly provided time value is different to the
     * previously set time value (variables that depend solely on constants or
     * parameters need not to be newly computed in the sequel, but the
     * previously computed values can be reused).
     *
     * @param time
     */
    fun setTime(time: Double) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetTime(c!!, time)))
    }

    /**
     * Set a new (continuous) state vector and re-initialize caching of
     * variables that depend on the states. Argument nx is the length of vector
     * x and is provided for checking purposes (variables that depend solely on
     * constants, parameters, time, and inputs do not need to be newly computed
     * in the sequel, but the previously computed values can be reused). Note,
     * the continuous states might also be changed in Event Mode. Note:
     * fmi2Status = fmi2Discard is possible.
     *
     * @param x
     */
    fun setContinousStates(x: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetContinuousStates(c!!, x, x.size)))
    }

    /**
     * The model enters Event Mode from the Continuous-Time Mode and
     * discrete-time equations may become active (and relations are not
     * “frozen”).
     */
    fun enterEventMode() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2EnterEventMode(c!!)))
    }

    /**
     * The model enters Continuous-Time Mode and all discrete-time equations
     * become inactive and all relations are “frozen”. This function has to be
     * called when changing from Event Mode (after the global event iteration in
     * Event Mode over all involved FMUs and other models has converged) into
     * Continuous-Time Mode. [This function might be used for the following
     * purposes: • If the FMU stores results internally on file, then the
     * results after the initialization and/or the event has been processed can
     * be stored. • If the FMU contains dynamically changing states, then a new
     * state selection might be performed with this function. ]
     */
    fun enterContinuousTimeMode() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2EnterContinuousTimeMode(c!!)))
    }

    fun newDiscreteStates(eventInfo: Fmi2EventInfo) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2NewDiscreteStates(c!!, eventInfo)))
    }

    fun completedIntegratorStep(noSetFMUStatePriorToCurrentPoint: Boolean) : Pair<Boolean, Boolean> {
        updateStatus(Fmi2Status.valueOf(
                library!!.fmi2CompletedIntegratorStep(c!!, convert(noSetFMUStatePriorToCurrentPoint),
                        enterEventMode, terminateSimulation)))
        return Pair(convert(enterEventMode.value), convert(terminateSimulation.value))
    }

    /**
     * Compute state derivatives and event indicators at the current time
     * instant and for the current states. The derivatives are returned as a
     * vector with “nx” elements. A state event is triggered when the domain of
     * an event indicator changes from zj > 0 to zj ≤ 0 or vice versa. The FMU
     * must guarantee that at an event restart zj ≠ 0, for example by shifting
     * zj with a small value. Furthermore, zj should be scaled in the FMU with
     * its nominal value (so all elements of the returned vector
     * “eventIndicators” should be in the order of “one”). The event indicators
     * are returned as a vector with “ni” elements. The ordering of the elements
     * of the derivatives vector is identical to the ordering of the state
     * vector (for example derivatives[2] is the derivative of x[2]). Event
     * indicators are not necessarily related to variables on the Model
     * Description File. Note: fmi2Status = fmi2Discard is possible for both
     * functions.
     *
     * @param derivatives
     */
    fun getDerivatives(derivatives: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetDerivatives(c!!, derivatives, derivatives.size)))
    }

    /**
     * Compute state derivatives and event indicators at the current time
     * instant and for the current states. The derivatives are returned as a
     * vector with “nx” elements. A state event is triggered when the domain of
     * an event indicator changes from zj > 0 to zj ≤ 0 or vice versa. The FMU
     * must guarantee that at an event restart zj ≠ 0, for example by shifting
     * zj with a small value. Furthermore, zj should be scaled in the FMU with
     * its nominal value (so all elements of the returned vector
     * “eventIndicators” should be in the order of “one”). The event indicators
     * are returned as a vector with “ni” elements. The ordering of the elements
     * of the derivatives vector is identical to the ordering of the state
     * vector (for example derivatives[2] is the derivative of x[2]). Event
     * indicators are not necessarily related to variables on the Model
     * Description File. Note: fmi2Status = fmi2Discard is possible for both
     * functions.
     *
     * @param eventIndicators
     */
    fun getEventIndicators(eventIndicators: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetEventIndicators(c!!, eventIndicators, eventIndicators.size)))
    }

    /**
     * Return the new (continuous) state vector x. This function has to be
     * called directly after calling function fmi2EnterContinuousTimeMode if it
     * returns with eventInfo- >valuesOfContinuousStatesChanged = fmi2True
     * (indicating that the (continuous-time) state vector has changed).
     *
     * @param x
     */
    fun getContinuousStates(x: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetContinuousStates(c!!, x, x.size)))
    }

    /**
     * Return the nominal values of the continuous states. This function should
     * always be called after calling function fmi2NewDiscreteStates if it
     * returns with eventInfo-> nominalsOfContinuousStatesChanged = fmi2True
     * since then the nominal values of the continuous states have changed [e.g.
     * because the association of the continuous states to variables has changed
     * due to internal dynamic state selection]. If the FMU does not have
     * information about the nominal value of a continuous state i, a nominal
     * value x_nominal[i] = 1.0 should be returned. Note, it is required that
     * x_nominal[i] > 0.0 [Typically, the nominal values of the continuous
     * states are used to compute the absolute tolerance required by the
     * integrator. Example: absoluteTolerance[i] = 0.01*tolerance*x_nominal[i];]
     *
     * @param x_nominal
     */
    fun getNominalsOfContinuousStates(x_nominal: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetNominalsOfContinuousStates(c!!, x_nominal, x_nominal.size)))
    }


}

