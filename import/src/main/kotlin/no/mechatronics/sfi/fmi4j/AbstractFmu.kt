package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.misc.*
import no.mechatronics.sfi.fmi4j.modeldescription.*
import no.mechatronics.sfi.fmi4j.modeldescription.enums.Causality
import no.mechatronics.sfi.fmi4j.proxy.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractFmu<E: ModelDescription, T: Fmi2LibraryWrapper<*>>(
        val fmuFile: FmuFile,
        val modelDescription: E,
        val wrapper: T
) {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractFmu::class.java)
    }

    var isInitialized = false
        private set

    val modelVariables = modelDescription.modelVariables

    private val map: MutableMap<String, IntArray> = HashMap()

    /**
     * @see Fmi2Library.fmi2GetTypesPlatform
     */
    fun getTypesPlatform() = wrapper.typesPlatform

    /**
     * @see Fmi2Library.fmi2GetVersion
     */
    fun getVersion() = wrapper.version

    fun isTerminated() = wrapper.isTerminated

    /**
     * @see Fmi2Library.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, nCategories: Int, categories: Array<String>)
            =  wrapper.setDebugLogging(loggingOn, nCategories, categories)

    fun write(vr: Int) : VariableWriter {
        return VariableWriter(wrapper, vr)
    }

    fun read(vr: Int) : VariableReader {
        return VariableReader(wrapper, vr)
    }

    fun write(name: String) : VariableWriter {
        return write(modelVariables.get(name)!!.valueReference)
    }

    fun read(name: String) : VariableReader {
        return read(modelVariables.get(name)!!.valueReference)
    }

    fun init() = init(0.0)
    fun init(start :Double) = init(start, -1.0)
    open fun init(start: Double, stop: Double): Boolean {

        if (!isInitialized) {

            assignStartValues()

            val stopDefined = stop > start
            wrapper.setupExperiment(true, 1E-4, start, stopDefined, if (stopDefined) stop else Double.MAX_VALUE)

            wrapper.enterInitializationMode()
            if (getLastStatus() !== Fmi2Status.OK) {
                return false
            }
            wrapper.exitInitializationMode()

            isInitialized = true

            return getLastStatus() === Fmi2Status.OK

        }

        return false

    }

    fun getLastStatus(): Fmi2Status = wrapper.lastStatus

    /**
     * Terminates the FMU
     * @see Fmi2Library.fmi2Terminate
     */
    fun terminate() : Boolean {
        if (wrapper.terminate()) {
            LOG.debug("FMU {} terminated!", modelDescription.modelName)
            return true
        }
        return false
    }

    /**
     * @see Fmi2Library.fmi2Reset
     */
    fun reset() = reset(true)

    /**
     * @see Fmi2Library.fmi2Reset
     */
    fun reset(requireReinit: Boolean) : Boolean {
        if (wrapper.reset() == Fmi2Status.OK) {
            if (requireReinit) {
                isInitialized = false
            }
            return true
        }
        return false
    }

    fun checkGetScalar(vr: Int) : Boolean {

        val variable = modelVariables.getByValueReference(vr)
        if (variable == null) {
            return false
        } else if (variable.causality == Causality.OUTPUT) {
            return true
        } else {
            return variable is RealVariable && variable.derivative != null
        }
    }

    fun getInteger(vr: Int) : Int {
        return wrapper.getInteger(vr)
    }

    fun getInteger(vr: IntArray) = wrapper.getInteger(vr)

    fun getInteger(vr: IntArray, value: IntArray) = wrapper.getInteger(vr, value)

    fun getReal(vr: Int) : Double {
        return wrapper.getReal(vr)
    }

    fun getReal(vr: IntArray) = wrapper.getReal(vr)

    fun getReal(vr: IntArray, value: DoubleArray) = wrapper.getReal(vr, value)

    fun getString(vr: Int) = wrapper.getString(vr)

    fun getString(vr: IntArray)  = wrapper.getString(vr)

    fun getString(vr: IntArray, value: Array<String>) = wrapper.getString(vr, value)

    fun getBoolean(vr: Int) : Boolean = wrapper.getBoolean(vr)

    fun getBoolean(vr: IntArray) : BooleanArray  = wrapper.getBoolean(vr)

    fun getBoolean(vr: IntArray, value: BooleanArray) = wrapper.getBoolean(vr, value)

    fun setInteger(vr: Int, value: Int) = wrapper.setInteger(vr, value)

    fun setInteger(vr: IntArray, value: IntArray) = wrapper.setInteger(vr, value)

    fun setIntegerArray(name: String, values: IntArray) : Fmi2Status {

        if (name in map) {
            return setInteger(map[name]!!, values)
        } else {
            val names: List<String> = List(values.size, {i -> "$name[$i]"})
            val vr : IntArray = modelDescription.modelVariables.getValueReferences(names)!!
            map[name] = vr
            return setInteger(vr, values)
        }

    }


    fun setReal(vr: Int, value: Double) = wrapper.setReal(vr, value)

    fun setReal(vr: IntArray, value: DoubleArray) = wrapper.setReal(vr, value)

    fun setRealArray(name: String, values: DoubleArray) : Fmi2Status {

        if (name in map) {
            return setReal(map[name]!!, values)
        } else {
            val names: List<String> = List(values.size, {i -> "$name[$i]"})
            val vr : IntArray = modelDescription.modelVariables.getValueReferences(names)!!
            map[name] = vr
            return setReal(vr, values)
        }

    }


    fun setString( valueReference: Int, value: String) = wrapper.setString(valueReference, value)

    fun setString(vr: IntArray, value: Array<out String>) = wrapper.setString(vr, value)

    fun setStringArray(name: String, values: Array<String>) : Fmi2Status {

        if (name in map) {
            return setString(map[name]!!, values)
        } else {
            val names: List<String> = List(values.size, {i -> "$name[$i]"})
            val vr : IntArray = modelDescription.modelVariables.getValueReferences(names)!!
            map[name] = vr
            return setString(vr, values)
        }

    }


    fun setBoolean( valueReference: Int, value: Boolean) = wrapper.setBoolean(valueReference, value)

    fun setBoolean(vr: IntArray, value: BooleanArray) = wrapper.setBoolean(vr, value)

    fun setBooleanArray(name: String, values: BooleanArray) : Fmi2Status {

        if (name in map) {
            return setBoolean(map[name]!!, values)
        } else {
            val names: List<String> = List(values.size, {i -> "$name[$i]"})
            val vr : IntArray = modelDescription.modelVariables.getValueReferences(names)!!
            map[name] = vr
            return setBoolean(vr, values)
        }

    }

    fun getDirectionalDerivative(d: DirectionalDerivatives): Fmi2Status {
        if (!modelDescription.providesDirectionalDerivative) {
            LOG.warn("Method call not allowed, FMU does not provide directional derivatives!")
            return Fmi2Status.Discard
        } else {
            return wrapper.getDirectionalDerivative(d.vUnknown_ref, d.vKnown_ref, d.dvKnown, d.dvUnknown)
        }
    }


    fun getFMUState() : FmuState? {
        if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("Method call not allowed, FMU cannot get and set FMU state!")
            return null
        } else {
            return wrapper.getFMUState()
        }
    }

    fun setFMUState(fmuState: FmuState): Fmi2Status {
        if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("Method call not allowed, FMU cannot get and set FMU state!")
            return Fmi2Status.Discard
        } else {
            return wrapper.setFMUState(fmuState)
        }
    }

    fun freeFMUState(fmuState: FmuState) : Fmi2Status {
        if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("Method call not allowed, FMU cannot get and set FMU state!")
            return Fmi2Status.Discard
        } else {
            return wrapper.freeFMUState(fmuState)
        }
    }

    fun serializedFMUStateSize(fmuState: FmuState): Int = wrapper.serializedFMUStateSize(fmuState)

    fun serializeFMUState(fmuState: FmuState) = wrapper.serializeFMUState(fmuState)

    fun deSerializeFMUState(serializedState: ByteArray) = wrapper.deSerializeFMUState(serializedState)

    private fun assignStartValues() {

        modelVariables.variables.forEach {

            if (it.start != null) {
                when(it) {
                    is IntegerVariable -> it.value = it.start!!
                    is RealVariable -> it.value = it.start!!
                    is StringVariable -> it.value = it.start!!
                    is BooleanVariable -> it.value = it.start!!
                }
            }

        }
    }



}