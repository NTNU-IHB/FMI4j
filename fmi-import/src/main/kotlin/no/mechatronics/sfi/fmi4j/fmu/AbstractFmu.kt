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

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.misc.*
import no.mechatronics.sfi.fmi4j.modeldescription.*
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import no.mechatronics.sfi.fmi4j.proxy.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable

abstract class AbstractFmu<out E: ModelDescription, out T: Fmi2LibraryWrapper<*>> internal constructor(
        val fmuFile: FmuFile,
        val wrapper: T
) : Closeable {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractFmu::class.java)
    }

    val variableAccessor: VariableAccessor
            = VariableAccessorImpl(modelVariables, wrapper)

    init {
        modelVariables.forEach{
            if (it is AbstractTypedScalarVariable) {
                AbstractTypedScalarVariable::class.java.getField("accessor").apply {
                    isAccessible = true
                    set(it, variableAccessor)
                }
            }
        }
    }

    abstract val modelDescription: E

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
    val lastStatus: FmiStatus
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
            if (lastStatus != FmiStatus.OK) {
                return false
            }
            wrapper.exitInitializationMode()

            isInitialized = true

            return lastStatus == FmiStatus.OK

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
     * Same as calling terminate(), needed in order to implement Closable
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
        if (wrapper.reset() == FmiStatus.OK) {
            if (requireReinit) {
                isInitialized = false
            }
            return true
        }
        return false
    }

    fun getDirectionalDerivative(d: DirectionalDerivatives): FmiStatus {
        if (!modelDescription.providesDirectionalDerivative) {
            LOG.warn("Method call not allowed, FMU does not provide directional derivatives!")
            return FmiStatus.Discard
        } else {
            return wrapper.getDirectionalDerivative(d.vUnknownRef, d.vKnownRef, d.dvKnown, d.dvUnknown)
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

    fun setFMUState(fmuState: FmuState): FmiStatus {
        return if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("Method call not allowed, FMU cannot get and set FMU state!")
            FmiStatus.Discard
        } else {
            wrapper.setFMUState(fmuState)
        }
    }

    fun freeFMUState(fmuState: FmuState): FmiStatus {
        return if (!modelDescription.canGetAndSetFMUstate) {
            LOG.warn("Method call not allowed, FMU cannot get and set FMU state!")
            FmiStatus.Discard
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
                is IntegerVariable -> variable.start?.also { variable.write(it) }
                is RealVariable -> variable.start?.also {  variable.write(it) }
                is StringVariable -> variable.start?.also {  variable.write(it) }
                is BooleanVariable -> variable.start?.also {  variable.write(it) }
            }
        }
    }

}
