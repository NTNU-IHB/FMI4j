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
import no.mechatronics.sfi.fmi4j.fmu.misc.DirectionalDerivatives
import no.mechatronics.sfi.fmi4j.fmu.misc.FmuState
import no.mechatronics.sfi.fmi4j.fmu.proxy.v2.Fmi2Library
import no.mechatronics.sfi.fmi4j.fmu.proxy.v2.Fmi2LibraryWrapper
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.lang.UnsupportedOperationException

abstract class AbstractFmu<out E: ModelDescription, out T: Fmi2LibraryWrapper<*>> internal constructor(
        val fmuFile: FmuFile,
        val wrapper: T
) : Closeable {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractFmu::class.java)
    }

    val variableAccessor: VariableAccessor
            = FmuVariableAccessor(modelVariables, wrapper)

    init {
        modelVariables.forEach{
            if (it is AbstractTypedScalarVariable) {
                it.accessor = variableAccessor
            }
        }
    }

    abstract val modelDescription: E

    /**
     * @see ModelDescription.modelName
     */
    val modelName: String
        get() = modelDescription.modelName

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
    fun setDebugLogging(loggingOn: Boolean, nCategories: Int, categories: Array<String>): FmiStatus
            =  wrapper.setDebugLogging(loggingOn, nCategories, categories)


    /**
     * Call init with 0.0 as start.
     */
    fun init() = init(0.0)

    /**
     * Call init with provided start
     * @param start the start time
     */
    fun init(start :Double) = init(start, -1.0)

    /**
     * Call init with provided start and stop
     * @param start the start time
     * @param stop the stop time
     */
    open fun init(start: Double, stop: Double): FmiStatus {

        if (!isInitialized) {

            if (start < 0) {
                throw IllegalArgumentException("Start must be a positive value")
            }

            assignStartValues {
                it.variability != Variability.CONSTANT &&
                        it.initial == Initial.EXACT || it.initial == Initial.APPROX
            }

            val stopDefined = stop > start
            @Suppress("NAME_SHADOWING")
            val stop = if (stopDefined) stop else Double.MAX_VALUE
            var status = wrapper.setupExperiment(false, 1E-4,
                    start, stopDefined, stop)
            if (status != FmiStatus.OK) {
                return lastStatus
            }
            status = wrapper.enterInitializationMode()
            LOG.trace("Called enterInitializationMode with status $status")
            if (status != FmiStatus.OK) {
                return lastStatus
            }

            assignStartValues {
                it.variability != Variability.CONSTANT &&
                        (it.initial != Initial.EXACT || it.causality == Causality.INPUT)
            }

            status = wrapper.exitInitializationMode()
            LOG.trace("Called exitInitializationMode with status $status")
            if (status != FmiStatus.OK) {
                return lastStatus
            }

            isInitialized = true

            return FmiStatus.OK

        } else {
            LOG.warn("Trying to call init, but FMU has already been initialized, and has not been reset!")
            return FmiStatus.Discard
        }

    }

    /**
     * Terminates the FMU
     *
     * @param freeInstance true if you are completely finished with the fmu
     *
     * @see Fmi2Library.fmi2Terminate
     * @see Fmi2Library.fmi2FreeInstance
     */
    @JvmOverloads
    open fun terminate(freeInstance: Boolean = true): FmiStatus {
        return wrapper.terminate(freeInstance).also { status ->
            LOG.debug("FMU '${modelDescription.modelName}' terminated with status $status! #${hashCode()}")
        }
    }

    /**
     * Same as calling terminate(true), needed in order to implement Closable
     * @see Closeable
     */
    override fun close() {
        terminate(true)
    }

    /**
     * @see Fmi2Library.fmi2Reset
     */
    fun reset(): FmiStatus {
        return reset(true)
    }

    /**
     *
     * @param requireReinit According to the FMI spec, init() must be called after a call to reset().
     * Setting requireReinit to false allows you to ignore that.
     * Only use if the tools you are using does not implement the standard correctly.
     *
     * @see Fmi2Library.fmi2Reset
     */
    fun reset(requireReinit: Boolean): FmiStatus {
        return wrapper.reset().also {
            if (requireReinit) {
                isInitialized = false
            }
        }
    }

    /**
     * @see Fmi2Library.fmi2GetDirectionalDerivative
     */
    fun getDirectionalDerivative(d: DirectionalDerivatives): FmiStatus {
        return if (!modelDescription.providesDirectionalDerivative) {
            LOG.warn("Method call not allowed, FMU does not provide directional derivatives!")
            FmiStatus.Discard
        } else {
            wrapper.getDirectionalDerivative(d.vUnknownRef, d.vKnownRef, d.dvKnown, d.dvUnknown)
        }
    }

    /**
     * @see Fmi2Library.fmi2GetFMUstate
     */
    @JvmOverloads
    fun getFMUState(state: FmuState = FmuState()): FmuState {
        if (!modelDescription.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.getFMUState(state)
    }

    /**
     * @see Fmi2Library.fmi2SetFMUstate
     */
    fun setFMUState(fmuState: FmuState): FmiStatus {
        if (!modelDescription.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.setFMUState(fmuState)
    }

    /**
     * @see Fmi2Library.fmi2FreeFMUstate
     */
    fun freeFMUState(fmuState: FmuState): FmiStatus {
        if (!modelDescription.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.freeFMUState(fmuState)
    }

    /**
     * @see Fmi2Library.fmi2SerializedFMUstateSize
     */
    fun serializedFMUStateSize(fmuState: FmuState): Int {
        if (!modelDescription.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot serialize FMU state!")
        }
        return wrapper.serializedFMUStateSize(fmuState)
    }

    /**
     * @see Fmi2Library.fmi2SerializeFMUstate
     */
    fun serializeFMUState(fmuState: FmuState):ByteArray {
        if (!modelDescription.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot serialize FMU state!")
        }
        return wrapper.serializeFMUState(fmuState)
    }

    /**
     * @see Fmi2Library.fmi2DeSerializeFMUstate
     */
    fun deSerializeFMUState(serializedState: ByteArray): FmuState {
        if (!modelDescription.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot serialize FMU state!")
        }
        return wrapper.deSerializeFMUState(serializedState)
    }

    fun getIntVector(name: String): IntegerVariableVector {
        val variables = modelVariables.filter {
            it is IntegerVariable && it.name.startsWith(name)
                    && it.name.contains("[") && it.name.contains("]")
        }.map { it.asIntegerVariable() }
        if (variables.isEmpty()) {
            throw IllegalArgumentException("$name does not match a vector")
        }
        return IntegerVariableVector(variableAccessor, variables)
    }

    fun getRealVector(name: String): RealVariableVector {
        val variables = modelVariables.filter {
            (it is RealVariable) && it.name.startsWith(name) && it.name.contains("[") && it.name.contains("]")
        }.map { it.asRealVariable() }
        if (variables.isEmpty()) {
            throw IllegalArgumentException("$name does not match a vector")
        }
        return RealVariableVector(variableAccessor, variables)
    }

    fun getStringVector(name: String): StringVariableVector {
        val variables = modelVariables.filter {
            (it is StringVariable) && it.name.startsWith(name) && it.name.contains("[") && it.name.contains("]")
        }.map { it.asStringVariable() }
        if (variables.isEmpty()) {
            throw IllegalArgumentException("$name does not match a vector")
        }
        return StringVariableVector(variableAccessor, variables)
    }

    fun getBooleanVector(name: String): BooleanVariableVector {
        val variables = modelVariables.filter {
            (it is BooleanVariable) && it.name.startsWith(name) && it.name.contains("[") && it.name.contains("]")
        }.map { it.asBooleanVariable() }
        if (variables.isEmpty()) {
            throw IllegalArgumentException("$name does not match a vector")
        }
        return BooleanVariableVector(variableAccessor, variables)
    }

    private fun assignStartValues(predicate: (TypedScalarVariable<*>) -> Boolean) {
        val variables = modelVariables.filter {
            it.start != null && predicate.invoke(it)
        }
        LOG.debug("Setting start values for ${variables.size} variables")
        variables.forEach { variable ->

            when (variable) {
                is IntegerVariable -> variable.write(variable.start!!)
                is RealVariable -> variable.write(variable.start!!)
                is StringVariable -> variable.write(variable.start!!)
                is BooleanVariable -> variable.write(variable.start!!)
                is EnumerationVariable -> variable.write(variable.start!!)
            }

        }
    }

}





