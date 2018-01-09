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

package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.misc.*
import no.mechatronics.sfi.fmi4j.modeldescription.*
import no.mechatronics.sfi.fmi4j.modeldescription.enums.Causality
import no.mechatronics.sfi.fmi4j.proxy.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractFmu<E: IModelDescription, T: Fmi2LibraryWrapper<*>> internal constructor(
        val fmuFile: FmuFile,
        val modelDescription: E,
        val wrapper: T
) : IAccessorProvider, AutoCloseable {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractFmu::class.java)
    }

    var isInitialized = false
        private set

    val modelVariables: IModelVariables = modelDescription.modelVariables

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

    override fun getWriter(vr: Int) = VariableWriter(wrapper, vr)
    override fun getWriter(name: String) = getWriter(modelVariables.getByName(name)!!.valueReference)
    override fun getWriter(variable: IntegerVariable) = IntWriter(wrapper, variable.valueReference)
    override fun getWriter(variable: RealVariable) = RealWriter(wrapper, variable.valueReference)
    override fun getWriter(variable: StringVariable) = StringWriter(wrapper, variable.valueReference)
    override fun getWriter(variable: BooleanVariable) = BooleanWriter(wrapper, variable.valueReference)

    override fun getReader(vr: Int) = VariableReader(wrapper, vr)
    override fun getReader(name: String) = getReader(modelVariables.getByName(name)!!.valueReference)
    override fun getReader(variable: IntegerVariable) = IntReader(wrapper, variable.valueReference)
    override fun getReader(variable: RealVariable) = RealReader(wrapper, variable.valueReference)
    override fun getReader(variable: StringVariable) = StringReader(wrapper, variable.valueReference)
    override fun getReader(variable: BooleanVariable) = BooleanReader(wrapper, variable.valueReference)

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

    override fun close() {
        terminate()
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
            val vr : IntArray = modelDescription.modelVariables.getValueReferences(names)
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
            val vr : IntArray = modelDescription.modelVariables.getValueReferences(names)
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
            val vr : IntArray = modelDescription.modelVariables.getValueReferences(names)
            map[name] = vr
            return setString(vr, values)
        }

    }


    fun setBoolean(valueReference: Int, value: Boolean) = wrapper.setBoolean(valueReference, value)

    fun setBoolean(vr: IntArray, value: BooleanArray) = wrapper.setBoolean(vr, value)

    fun setBooleanArray(name: String, values: BooleanArray) : Fmi2Status {

        if (name in map) {
            return setBoolean(map[name]!!, values)
        } else {
            val names: List<String> = List(values.size, {i -> "$name[$i]"})
            val vr : IntArray = modelDescription.modelVariables.getValueReferences(names)
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
                    is IntegerVariable -> setInteger(it.valueReference, it.start!!)
                    is RealVariable -> setReal(it.valueReference, it.start!!)
                    is StringVariable -> setString(it.valueReference, it.start!!)
                    is BooleanVariable -> setBoolean(it.valueReference, it.start!!)
                }
            }

        }
    }



}