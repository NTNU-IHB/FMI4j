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
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import no.mechatronics.sfi.fmi4j.proxy.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractFmu<E: ModelDescription, T: Fmi2LibraryWrapper<*>> internal constructor(
        val fmuFile: FmuFile,
        val modelDescription: E,
        val wrapper: T
) : AutoCloseable {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractFmu::class.java)
    }

    private val map: MutableMap<String, IntArray> by lazy {
        HashMap<String, IntArray>()
    }

    val variableAccessor = FmuVariableAccessorImpl(wrapper)

    init {
        modelVariables.forEach{
            if (it is AbstractTypedScalarVariable) {
                it.accessor = variableAccessor
            }
        }
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
        modelVariables.forEach { variable ->
            when(variable) {
                is IntegerVariable -> variable.start?.also { variable.value = it }
                is RealVariable -> variable.start?.also {  variable.value = it }
                is StringVariable -> variable.start?.also {  variable.value = it }
                is BooleanVariable -> variable.start?.also {  variable.value = it }
            }
        }
    }

}

class FmuVariableAccessorImpl(
    private val wrapper: Fmi2LibraryWrapper<*>
): FmuVariableAccessor {

    override fun getBoolean(valueReference: Int) = wrapper.getBoolean(valueReference)
    override fun getBoolean(vr: IntArray) = wrapper.getBoolean(vr)
    override fun getBoolean(vr: IntArray, value: BooleanArray) = wrapper.getBoolean(vr, value)
    override fun getBoolean(vr: IntArray, value: IntArray) = wrapper.getBoolean(vr, value)

    override fun getInteger(valueReference: Int) = wrapper.getInteger(valueReference)
    override fun getInteger(vr: IntArray) = wrapper.getInteger(vr)
    override fun getInteger(vr: IntArray, value: IntArray) = wrapper.getInteger(vr, value)

    override fun getReal(valueReference: Int) = wrapper.getReal(valueReference)
    override fun getReal(vr: IntArray) = wrapper.getReal(vr)
    override fun getReal(vr: IntArray, value: RealArray) = wrapper.getReal(vr, value)

    override fun getString(valueReference: Int) = wrapper.getString(valueReference)
    override fun getString(vr: IntArray) = wrapper.getString(vr)
    override fun getString(vr: IntArray, value: StringArray) = wrapper.getString(vr, value)

    override fun setBoolean(valueReference: Int, value: Boolean) {
        wrapper.setBoolean(valueReference, value)
    }

    override fun setBoolean(vr: IntArray, value: BooleanArray) {
        wrapper.setBoolean(vr, value)
    }

    override fun setBoolean(vr: IntArray, value: IntArray) {
        wrapper.setBoolean(vr, value)
    }

    override fun setInteger(valueReference: Int, value: Int) {
        wrapper.setInteger(valueReference, value)
    }

    override fun setInteger(vr: IntArray, value: IntArray) {
        wrapper.setInteger(vr, value)
    }

    override fun setReal(valueReference: Int, value: Real) {
        wrapper.setReal(valueReference, value)
    }

    override fun setReal(vr: IntArray, value: DoubleArray) {
        wrapper.setReal(vr, value)
    }

    override fun setString(valueReference: Int, value: String) {
        wrapper.setString(valueReference, value)
    }

    override fun setString(vr: IntArray, value: StringArray) {
        wrapper.setString(vr, value)
    }
}