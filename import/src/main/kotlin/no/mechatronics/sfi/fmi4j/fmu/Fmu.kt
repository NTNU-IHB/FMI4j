package no.mechatronics.sfi.fmi4j.fmu

import com.sun.org.apache.xpath.internal.operations.Bool
import no.mechatronics.sfi.fmi4j.wrapper.Fmi2Wrapper
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelVariables
import no.mechatronics.sfi.fmi4j.modeldescription.types.*
import no.mechatronics.sfi.fmi4j.wrapper.FmiMethod
import no.mechatronics.sfi.fmi4j.wrapper.FmiState
import no.mechatronics.sfi.fmi4j.wrapper.FmuState
import org.slf4j.LoggerFactory
import java.util.function.Supplier
import java.util.logging.Level
import java.util.logging.Logger

abstract class FmuHelper<E : Fmi2Wrapper<*>, T : ModelDescription>(
        val fmuFile: FmuFile,
        val fmi2Type: Fmi2Type,
        val visible: Boolean,
        val loggingOn: Boolean
) {

    abstract val wrapper: E
    abstract val modelDescription: T

}


abstract class Fmu<E : Fmi2Wrapper<*>, T : ModelDescription>(
        helper: FmuHelper<E, T>
) {

    private companion object {
        val LOG = LoggerFactory.getLogger(Fmu::class.java)
    }

    val wrapper: E
    val fmuFile: FmuFile
    val modelDescription: T
    val modelVariables: ModelVariables
    get() {
       return modelDescription.modelVariables
    }

     var currentTime: Double = 0.0

    private val map: MutableMap<String, IntArray> = HashMap()



    init {

        this.fmuFile = helper.fmuFile
        this.wrapper = helper.wrapper
        this.modelDescription = helper.modelDescription

        this.wrapper.instantiate(modelDescription.modelIdentifier, helper.fmi2Type,
                modelDescription.guid, fmuFile.getResourcesPath(), helper.visible, helper.loggingOn)
        injectWrapperInVariables()
    }

//    protected fun updateStatus(status: Fmi2Status) : Fmi2Status {
//        this.lastStatus = status
//
//        when (status) {
//            Fmi2Status.Error -> state = FmiState.ERROR
//            Fmi2Status.Fatal -> state = FmiState.FATAL
//        }
//
//        return status
//    }

    /**
     * @see Fmi2Library.fmi2GetTypesPlatform
     */
    fun getTypesPlatform() = wrapper.typesPlatform

    /**
     * @see Fmi2Library.fmi2GetVersion
     */
    fun getVersion() = wrapper.version

    /**
     * @see Fmi2Library.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, nCategories: Int, categories: Array<String>)
            =  wrapper.setDebugLogging(loggingOn, nCategories, categories)


    fun write(name: String) : VariableWriter {
        val valueReference = modelVariables.get(name)!!.valueReference
        return VariableWriter(this, valueReference)
    }

    fun read(name: String) : VariableReader {
        val valueReference = modelVariables.get(name)!!.valueReference
        return VariableReader(this, valueReference)
    }

    fun init() = init(0.0)
    fun init(start :Double) = init(start, -1.0)
    open fun init(start: Double, stop: Double): Boolean {

        assignStartValues()

        val stopDefined = stop > start
        currentTime = start
        wrapper.setupExperiment(true, 1E-4, currentTime, stopDefined, if (stopDefined) stop else Double.MAX_VALUE)


        wrapper.enterInitializationMode()
        if (getLastStatus() !== Fmi2Status.OK) {
            return false
        }
        wrapper.exitInitializationMode()
        return getLastStatus() === Fmi2Status.OK

    }

    fun getLastStatus(): Fmi2Status = wrapper.lastStatus

    /**
     * Terminates the FMU
     * @see Fmi2Library.fmi2Terminate
     */
    fun terminate() : Boolean {
        if (wrapper.terminate()) {
            LOG.info("FMU {} terminated!", modelDescription.modelName)
            return true
        }
        return false
    }

    /**
     * @see Fmi2Library.fmi2Reset
     */
    fun reset() : Boolean {
        return wrapper.reset() == Fmi2Status.OK
    }

    fun checkGetScalar(vr: Int) : Boolean {

        val variable = modelVariables.getByValueReference(vr)
        if (variable == null) {
            return false
        } else if (variable.causality == Causality.output) {
            return true
        } else {
            return variable is RealVariable && variable.derivative != null
        }
    }

    fun getInteger(vr: Int) : Int {
        wrapper.state.isCallLegalDuringState(FmiMethod.fmi2GetInteger, Supplier { checkGetScalar(vr) }, "During this state, such a call is only valid for a variable with causality = \"output\" or\n" +
                " continuous-time states or state derivatives (if element <Derivatives> is present)")
        return wrapper.getInteger(vr)
    }

    fun getInteger(vr: IntArray) = wrapper.getInteger(vr)

    fun getInteger(vr: IntArray, value: IntArray) = wrapper.getInteger(vr, value)

    fun getReal(vr: Int) : Double {
        wrapper.state.isCallLegalDuringState(FmiMethod.fmi2GetReal, Supplier { checkGetScalar(vr) }, "During this state, such a call is only valid for a variable with causality = \"output\" or\n" +
                " continuous-time states or state derivatives (if element <Derivatives> is present)")
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

    fun setString(vr: IntArray, value: Array<String>) = wrapper.setString(vr, value)

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

    fun getDirectionalDerivative(vUnknown_ref: IntArray, vKnown_ref: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray)
            = wrapper.getDirectionalDerivative(vUnknown_ref, vKnown_ref, dvKnown, dvUnknown)

    fun getFMUState() = wrapper.getFMUState()

    fun setFMUState(fmuState: FmuState) = wrapper.setFMUState(fmuState)

    fun freeFMUState(fmuState: FmuState) = wrapper.freeFMUState(fmuState)

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

    private fun injectWrapperInVariables() {
        try {
            val f = VariableBase::class.java.getDeclaredField("wrapper")
            f.isAccessible = true
            modelDescription.modelVariables.forEach{

                try {
                    f.set(it, wrapper)
                } catch (ex: IllegalArgumentException) {
                    Logger.getLogger(Fmu::class.java.name).log(Level.SEVERE, null, ex)
                } catch (ex: IllegalAccessException) {
                    Logger.getLogger(Fmu::class.java.name).log(Level.SEVERE, null, ex)
                }


            }

        } catch (ex: NoSuchFieldException) {
            Logger.getLogger(Fmu::class.java.name).log(Level.SEVERE, null, ex)
        } catch (ex: SecurityException) {
            Logger.getLogger(Fmu::class.java.name).log(Level.SEVERE, null, ex)
        }

    }


}
