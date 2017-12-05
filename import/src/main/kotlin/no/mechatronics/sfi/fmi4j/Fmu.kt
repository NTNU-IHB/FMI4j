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

package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.misc.DirectionalDerivatives
import no.mechatronics.sfi.fmi4j.misc.FmuFile
import no.mechatronics.sfi.fmi4j.misc.VariableReader
import no.mechatronics.sfi.fmi4j.misc.VariableWriter
import no.mechatronics.sfi.fmi4j.wrapper.Fmi2Wrapper
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.modeldescription.*
import no.mechatronics.sfi.fmi4j.modeldescription.enums.*
import no.mechatronics.sfi.fmi4j.wrapper.FmiMethod
import no.mechatronics.sfi.fmi4j.wrapper.FmuState
import org.slf4j.LoggerFactory
import java.util.function.Supplier
import java.util.logging.Level
import java.util.logging.Logger


abstract class Fmu<E : Fmi2Wrapper<*>, T : ModelDescription> (
        val fmuFile: FmuFile
) {

    private companion object {
        val LOG : org.slf4j.Logger = LoggerFactory.getLogger(Fmu::class.java)
    }

    abstract val wrapper: E
    abstract val fmi2Type: Fmi2Type
    abstract val modelDescription: T
    val modelVariables: ModelVariables
    get() {
       return modelDescription.modelVariables
    }

    var currentTime: Double = 0.0
    protected set

    var isInstantiated = false
    private set
    var isInitialized = false
    private set
    private val map: MutableMap<String, IntArray> = HashMap()

    @JvmOverloads
    protected fun instantiate(visible: Boolean = false, loggingOn: Boolean = false) {
        if (!isInstantiated) {
            this.wrapper.instantiate(modelDescription.modelIdentifier, fmi2Type,
                    modelDescription.guid, fmuFile.getResourcesPath(), visible, loggingOn)
            injectWrapperInVariables()
            isInstantiated = true
        }
    }

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
        return VariableWriter(this, vr)
    }

    fun read(vr: Int) : VariableReader {
        return VariableReader(this, vr)
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

            if (!isInstantiated) {
                instantiate()
            }

            assignStartValues()

            val stopDefined = stop > start
            currentTime = start
            wrapper.setupExperiment(true, 1E-4, currentTime, stopDefined, if (stopDefined) stop else Double.MAX_VALUE)


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
            LOG.warn("FMU does not provide directional derivatives")
            return Fmi2Status.Discard
        } else {
            return wrapper.getDirectionalDerivative(d.vUnknown_ref, d.vKnown_ref, d.dvKnown, d.dvUnknown)
        }
    }


    fun getFMUState() : FmuState? {
        if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("FMU cannot get and set FMU state")
            return null
        } else {
            return wrapper.getFMUState()
        }
    }

    fun setFMUState(fmuState: FmuState): Fmi2Status {
        if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("FMU cannot get and set FMU state")
            return Fmi2Status.Discard
        } else {
            return wrapper.setFMUState(fmuState)
        }
    }

    fun freeFMUState(fmuState: FmuState) : Fmi2Status {
        if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("FMU cannot get and set FMU state")
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
