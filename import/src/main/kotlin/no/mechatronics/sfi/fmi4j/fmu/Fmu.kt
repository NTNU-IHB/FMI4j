package no.mechatronics.sfi.fmi4j.fmu

import com.sun.jna.Pointer
import no.mechatronics.sfi.fmi4j.jna.lib.wrapper.Fmi2LibraryWrapper
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelVariables
import no.mechatronics.sfi.fmi4j.modeldescription.types.*
import org.slf4j.LoggerFactory
import java.util.logging.Level
import java.util.logging.Logger

abstract class FmuHelper<E : Fmi2LibraryWrapper<*>, T : ModelDescription>(
        val fmuFile: FmuFile,
        val fmi2Type: Fmi2Type,
        val visible: Boolean,
        val loggingOn: Boolean
) {

    abstract val wrapper: E
    abstract val modelDescription: T

}


abstract class Fmu<E : Fmi2LibraryWrapper<*>, T : ModelDescription> {

    companion object {
        private val LOG = LoggerFactory.getLogger(Fmu::class.java)
    }

    val wrapper: E
    val fmuFile: FmuFile
    val modelDescription: T

    val modelName: String
        get() =  modelDescription.modelName

    val modelVariables: ModelVariables
        get() = modelDescription.modelVariables


    val isTerminated: Boolean
        get() = wrapper.isTerminated

    val isInstanceFreed: Boolean
        get() = wrapper.isInstanceFreed

    var currentTime: Double = 0.0
        protected set


    private val map: MutableMap<String, IntArray> = HashMap()


    constructor(helper: FmuHelper<E, T>) {
        this.fmuFile = helper.fmuFile
        this.wrapper = helper.wrapper
        this.modelDescription = helper.modelDescription

        this.wrapper.instantiate(modelDescription.modelIdentifier, helper.fmi2Type, modelDescription.guid, fmuFile.getResourcesPath(), helper.visible, helper.loggingOn)
        injectWrapperInVariables()
    }

    /**
     * @see Fmi2Library.fmi2GetTypesPlatform
     */
    fun getTypesPlatform(): String =  wrapper.typesPlatform

    /**
     * @see Fmi2Library.fmi2GetVersion
     */
    fun getVersion(): String = wrapper.version

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

    @JvmOverloads
    open fun init(setup: ExperimentSetup = ExperimentSetup()): Boolean {

        assignStartValues()

        if (setup.useDefaultExperiment && modelDescription.defaultExperiment != null) {
            val de = modelDescription.defaultExperiment
            currentTime = de.startTime
            wrapper.setupExperiment(true, de.tolerance, currentTime, true, de.stopTime)
        } else {
            currentTime = setup.startTime
            wrapper.setupExperiment(setup.toleranceDefined, setup.tolerance, currentTime, setup.stopDefined, setup.stopTime)
        }

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
     * @param freeInstance should be free the FMU instance as well?
     * @see Fmi2Library.fmi2Terminate
     * @see Fmi2Library.fmi2FreeInstance
     */
    @JvmOverloads
    fun terminate(freeInstance: Boolean = true) {
        if (wrapper.terminate()) {
            LOG.info("FMU {} terminated!", modelDescription.modelName)
            if (freeInstance) {
                freeInstance()
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2Reset
     */
    fun reset() : Fmi2Status {
        LOG.info("Resetting FMU..")
        return wrapper.reset()
    }

    /**
     * @see Fmi2Library.fmi2FreeInstance
     */
    fun freeInstance() {
       if ( wrapper.freeInstance()) {
           LOG.info("FMU '{}' instance freed!", modelDescription.modelName)
           fmuFile.dispose()
       }
    }

    fun getInteger(vr: Int) = wrapper.getInteger(vr)

    fun getInteger(vr: IntArray) = wrapper.getInteger(vr)

    fun getInteger(vr: IntArray, value: IntArray) = wrapper.getInteger(vr, value)

    fun getReal(vr: Int) : Double = wrapper.getReal(vr)

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

    fun getFMUState(fmuState: Pointer) = wrapper.getFMUState(fmuState)

    fun setFMUState(fmuState: Pointer) = wrapper.setFMUState(fmuState)

    fun freeFMUState(fmuState: Pointer) = wrapper.freeFMUState(fmuState)

    fun serializedFMUStateSize(fmuState: Pointer): Int = wrapper.serializedFMUStateSize(fmuState)

    fun serializeFMUState(fmuState: Pointer) = wrapper.serializeFMUState(fmuState)

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
