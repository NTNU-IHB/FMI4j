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
import no.mechatronics.sfi.fmi4j.proxy.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractFmu<E: ModelDescription, T: Fmi2LibraryWrapper<*>> internal constructor(
        val fmuFile: FmuFile,
        val modelDescription: E,
        val wrapper: T
) : VariableAccessProvider, AutoCloseable {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractFmu::class.java)
    }

    private val map: MutableMap<String, IntArray> by lazy {
        HashMap<String, IntArray>()
    }


    /**
     * @see ModelDescription.modelVariables
     */
    val modelVariables: ModelVariables
        get() = modelDescription.modelVariables

    /**
     * @see Fmi2Library.fmi2GetTypesPlatform
     */
    val typesPlatform
        get() = wrapper.typesPlatform

    /**
     * @see Fmi2Library.fmi2GetVersion
     */
    val version
        get() = wrapper.version


    /**
     * Has the FMU been initialized yet?
     * That is, has init() been called?
     */
    var isInitialized = false
        private set

    /**
     * @see Fmi2LibraryWrapper.isTerminated
     */
    val isTerminated
        get() = wrapper.isTerminated


    /**
     * @see Fmi2LibraryWrapper.lastStatus
     */
    val lastStatus: Fmi2Status
        get() =  wrapper.lastStatus

    /**
     * @see Fmi2Library.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, nCategories: Int, categories: Array<String>)
            =  wrapper.setDebugLogging(loggingOn, nCategories, categories)

    override fun getWriter(vr: Int) = VariableAccessor(modelDescription, wrapper, vr)
    override fun getWriter(name: String) = VariableAccessor(modelDescription, wrapper, modelVariables.getByName(name).valueReference)
    override fun getWriter(variable: IntegerVariable) = IntWriterImpl(wrapper, variable.valueReference)
    override fun getWriter(variable: RealVariable) = RealWriterImpl(wrapper, variable.valueReference)
    override fun getWriter(variable: StringVariable) = StringWriterImpl(wrapper, variable.valueReference)
    override fun getWriter(variable: BooleanVariable) = BooleanWriterImpl(wrapper, variable.valueReference)

    override fun getReader(vr: Int) = VariableAccessor(modelDescription, wrapper, vr)
    override fun getReader(name: String) = VariableAccessor(modelDescription, wrapper, modelVariables.getByName(name).valueReference)
    override fun getReader(variable: IntegerVariable) = IntReaderImpl(wrapper, variable.valueReference)
    override fun getReader(variable: RealVariable) = RealReaderImpl(wrapper, variable.valueReference)
    override fun getReader(variable: StringVariable) = StringReaderImpl(wrapper, variable.valueReference)
    override fun getReader(variable: BooleanVariable) = BooleanReaderImpl(wrapper, variable.valueReference)

    fun init() = init(0.0)
    fun init(start :Double) = init(start, -1.0)
    open fun init(start: Double, stop: Double): Boolean {

        if (!isInitialized) {

            assignStartValues()

            val stopDefined = stop > start
            wrapper.setupExperiment(false, 1E-4, start, stopDefined, if (stopDefined) stop else Double.MAX_VALUE)

            wrapper.enterInitializationMode()
            if (lastStatus != Fmi2Status.OK) {
                return false
            }
            wrapper.exitInitializationMode()

            isInitialized = true

            return lastStatus == Fmi2Status.OK

        } else {
            LOG.warn("Trying to call init, but FMU has already been initialized, and has not been reset!")
            return false
        }

    }

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
     * Allows try with resources to be used.
     * Simply callas terminate()
     * @see terminate
     */
    override fun close() {
        terminate()
    }

    /**
     * @see Fmi2Library.fmi2Reset
     */
    fun reset() = reset(true)

    /**
     *
     * @param requireReinit According to the FMI spec, init() must be called after a call to reset().
     * Setting requireReinit to false allows you to ignore that.
     * Only use if the tools you are using does not implement the standard correctly.
     *
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

    fun readInteger(vr: Int): Int = wrapper.getInteger(vr)
    fun readInteger(vr: IntArray): IntArray = wrapper.getInteger(vr)
    fun readInteger(vr: IntArray, value: IntArray): IntArray = wrapper.getInteger(vr, value)

    fun readReal(vr: Int): Double = wrapper.getReal(vr)
    fun readReal(vr: IntArray): DoubleArray = wrapper.getReal(vr)
    fun readReal(vr: IntArray, value: DoubleArray): DoubleArray = wrapper.getReal(vr, value)

    fun readString(vr: Int): String = wrapper.getString(vr)
    fun readString(vr: IntArray): Array<String> = wrapper.getString(vr)
    fun readString(vr: IntArray, value: Array<String>): Array<String> = wrapper.getString(vr, value)

    fun readBoolean(vr: Int): Boolean = wrapper.getBoolean(vr)
    fun readBoolean(vr: IntArray): BooleanArray  = wrapper.getBoolean(vr)
    fun readBoolean(vr: IntArray, value: BooleanArray): BooleanArray = wrapper.getBoolean(vr, value)

    fun writeInteger(vr: Int, value: Int): Fmi2Status = wrapper.setInteger(vr, value)
    fun writeInteger(vr: IntArray, value: IntArray): Fmi2Status = wrapper.setInteger(vr, value)
    fun writeInteger(name: String, values: IntArray) {
        if (name !in map) {
            val names = List(values.size, { i -> "$name[$i]" })
            modelDescription.modelVariables.getValueReferences(names).also {
                map[name] = it
            }
        }
        writeInteger(map[name]!!, values)
    }

    fun writeReal(vr: Int, value: Double): Fmi2Status = wrapper.setReal(vr, value)
    fun writeReal(vr: IntArray, value: DoubleArray): Fmi2Status = wrapper.setReal(vr, value)
    fun writeReal(name: String, values: DoubleArray) {
        if (name !in map) {
            val names = List(values.size, { i -> "$name[$i]" })
            modelDescription.modelVariables.getValueReferences(names).also {
                map[name] = it
            }
        }
        writeReal(map[name]!!, values)
    }

    fun writeString( valueReference: Int, value: String): Fmi2Status = wrapper.setString(valueReference, value)
    fun writeString(vr: IntArray, value: Array<out String>): Fmi2Status = wrapper.setString(vr, value)
    fun writeString(name: String, values: Array<String>) {
        if (name !in map) {
            val names = List(values.size, { i -> "$name[$i]" })
            modelDescription.modelVariables.getValueReferences(names).also {
                map[name] = it
            }
        }
        writeString(map[name]!!, values)
    }

    fun writeBoolean(valueReference: Int, value: Boolean): Fmi2Status = wrapper.setBoolean(valueReference, value)
    fun writeBoolean(vr: IntArray, value: BooleanArray): Fmi2Status = wrapper.setBoolean(vr, value)
    fun writeBoolean(name: String, values: BooleanArray){
        if (name !in map) {
            val names = List(values.size, { i -> "$name[$i]" })
            modelDescription.modelVariables.getValueReferences(names).also {
                map[name] = it
            }
        }
        writeBoolean(map[name]!!, values)
    }

    fun getDirectionalDerivative(d: DirectionalDerivatives): Fmi2Status {
        if (!modelDescription.providesDirectionalDerivative) {
            LOG.warn("Method call not allowed, FMU does not provide directional derivatives!")
            return Fmi2Status.Discard
        } else {
            return wrapper.getDirectionalDerivative(d.vUnknown_ref, d.vKnown_ref, d.dvKnown, d.dvUnknown)
        }
    }


    fun getFMUState(): FmuState? {
        if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("Method call not allowed, FMU cannot get and set FMU state!")
            return null
        } else {
            return wrapper.getFMUState()
        }
    }

    fun setFMUState(fmuState: FmuState): Fmi2Status {
        return if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("Method call not allowed, FMU cannot get and set FMU state!")
            Fmi2Status.Discard
        } else {
            wrapper.setFMUState(fmuState)
        }
    }

    fun freeFMUState(fmuState: FmuState): Fmi2Status {
        return if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("Method call not allowed, FMU cannot get and set FMU state!")
            Fmi2Status.Discard
        } else {
            wrapper.freeFMUState(fmuState)
        }
    }

    fun serializedFMUStateSize(fmuState: FmuState): Int = wrapper.serializedFMUStateSize(fmuState)

    fun serializeFMUState(fmuState: FmuState):ByteArray = wrapper.serializeFMUState(fmuState)

    fun deSerializeFMUState(serializedState: ByteArray):FmuState = wrapper.deSerializeFMUState(serializedState)

    private fun assignStartValues() {
        modelVariables.variables.forEach { variable ->
            variable.start?.apply {
                when(variable) {
                    is IntegerVariable -> writeInteger(variable.valueReference, variable.start!!)
                    is RealVariable -> writeReal(variable.valueReference, variable.start!!)
                    is StringVariable -> writeString(variable.valueReference, variable.start!!)
                    is BooleanVariable -> writeBoolean(variable.valueReference, variable.start!!)
                }
            }
        }
    }

}