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

package no.mechatronics.sfi.fmi4j.importer

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.common.FmuInstance
import no.mechatronics.sfi.fmi4j.common.FmuVariableAccessor
import no.mechatronics.sfi.fmi4j.importer.misc.*
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.FmiLibrary
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.FmiLibraryWrapper
import no.mechatronics.sfi.fmi4j.modeldescription.SpecificModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.UnsupportedOperationException

abstract class AbstractFmuInstance<out E : SpecificModelDescription, out T : FmiLibraryWrapper<*>> internal constructor(
        val fmu: Fmu,
        val wrapper: T
): FmuInstance {

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(AbstractFmuInstance::class.java)
    }

    abstract override val modelDescription: E

    override val variableAccessor: FmuVariableAccessor
            = FmuVariableAccessorImpl(wrapper, modelVariables)

    init {
        modelVariables.forEach { variable ->
            if (variable is AbstractTypedScalarVariable) {
                variable::class.java.getField("accessor").also { field ->
                    field.set(variable, variableAccessor)
                }
            }
        }
    }

    /**
     * @see FmiLibrary.fmi2GetTypesPlatform
     */
    val typesPlatform
        get() = wrapper.typesPlatform

    /**
     * @see FmiLibrary.fmi2GetVersion
     */
    val version
        get() = wrapper.version

    /**
     * Has the FMU been initialized yet?
     * That is, has init() been called?
     */
    override var isInitialized = false
        protected set

    /**
     * @see FmiLibraryWrapper.isTerminated
     */
    override val isTerminated
        get() = wrapper.isTerminated


    protected var stopDefined = false
        private set

    protected var stopTime: Double = 0.0
        private set

    /**
     * @see FmiLibraryWrapper.lastStatus
     */
    override val lastStatus: FmiStatus
        get() =  wrapper.lastStatus

    /**
     * @see FmiLibrary.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, nCategories: Int, categories: Array<String>): FmiStatus
            =  wrapper.setDebugLogging(loggingOn, nCategories, categories)

    override fun init() {
        init(0.0)
    }

    /**
     * Call init with provided start
     *
     * @param start the start time
     */
    override fun init(start: Double) {
        init(start, 0.0)
    }

    /**
     * Call init with provided start and stop
     * @param start the start time
     * @param stop the stop time
     *
     * @throws IllegalArgumentException if start < 0
     * @throws IllegalStateException if a necessary FMU call does not return OK
     */
    override fun init(start: Double, stop: Double) {

        if (!isInitialized) {

            if (start < 0) {
                throw IllegalArgumentException("Start must be a positive value")
            }

            assignStartValues {
                it.variability != Variability.CONSTANT &&
                        (it.initial == Initial.EXACT || it.initial == Initial.APPROX)
            }.also {
                LOG.debug("Applied start values to $it variables with variability != CONSTANT and initial == EXACT or APPROX ")
            }

            stopDefined = (stop > start)
            if (stopDefined) stopTime = stop
            LOG.debug("setupExperiment params: start=$start, stopDefined=$stopDefined, stop=$stopTime")
            wrapper.setupExperiment(true, 1E-4, start, stopDefined, stopTime).also {
                if (it != FmiStatus.OK) {
                    throw IllegalStateException("setupExperiment returned status $it")
                }
            }

            wrapper.enterInitializationMode().also {
                if (it != FmiStatus.OK) {
                    throw IllegalStateException("enterInitializationMode returned status $it")
                }
            }

            assignStartValues {
                it.variability != Variability.CONSTANT &&
                        (it.initial == Initial.EXACT || it.causality == Causality.INPUT)
            }.also {
                LOG.debug("Applied start values to $it variables with variability != CONSTANT and initial == EXACT or causality == INPUT ")
            }

            wrapper.exitInitializationMode().also {
                if (it != FmiStatus.OK) {
                    throw IllegalArgumentException("exitInitializationMode returned status $it")
                }
            }

            isInitialized = true

        } else {
            LOG.warn("Trying to call init, but FMU has already been initialized, and has not been reset!")
        }

    }

    override fun terminate(): Boolean {
        return terminate(true)
    }

    /**
     * Terminates the FMU
     *
     * @param freeInstance true if you are completely finished with the fmuInstance
     *
     * @see FmiLibrary.fmi2Terminate
     * @see FmiLibrary.fmi2FreeInstance
     */
    fun terminate(freeInstance: Boolean): Boolean {
        return wrapper.terminate(freeInstance).let { status ->
            LOG.debug("FMU '${modelDescription.modelName}' terminated with status $status! #${hashCode()}")
            status == FmiStatus.OK
        }
    }

    /**
     * @see FmiLibrary.fmi2Reset
     */
    override fun reset(): Boolean {
        return wrapper.reset() == FmiStatus.OK
    }

    /**
     * Custom reset function for invoking reset on FMUs that dont comply with the standard (e.g. 20-sim).
     *
     * @param requireReinit According to the FMI spec, init() must be called after a call to reset().
     * Setting requireReinit to false allows you to ignore that.
     * Only use if the tool you are using does not implement the standard correctly.
     *
     * @see FmiLibrary.fmi2Reset
     */
    fun reset(requireReinit: Boolean): Boolean {
        return reset().also {
            if (requireReinit) {
                isInitialized = false
            }
        }
    }

    /**
     * @see FmiLibrary.fmi2GetDirectionalDerivative
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
     * @see FmiLibrary.fmi2GetFMUstate
     */
    @JvmOverloads
    fun getFMUState(state: FmuState = FmuState()): FmuState {
        if (!modelDescription.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.getFMUState(state)
    }

    /**
     * @see FmiLibrary.fmi2SetFMUstate
     */
    fun setFMUState(fmuState: FmuState): FmiStatus {
        if (!modelDescription.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.setFMUState(fmuState)
    }

    /**
     * @see FmiLibrary.fmi2FreeFMUstate
     */
    fun freeFMUState(fmuState: FmuState): FmiStatus {
        if (!modelDescription.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.freeFMUState(fmuState)
    }

    /**
     * @see FmiLibrary.fmi2SerializedFMUstateSize
     */
    fun serializedFMUStateSize(fmuState: FmuState): Int {
        if (!modelDescription.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot serialize FMU state!")
        }
        return wrapper.serializedFMUStateSize(fmuState)
    }

    /**
     * @see FmiLibrary.fmi2SerializeFMUstate
     */
    fun serializeFMUState(fmuState: FmuState): ByteArray {
        if (!modelDescription.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot serialize FMU state!")
        }
        return wrapper.serializeFMUState(fmuState)
    }

    /**
     * @see FmiLibrary.fmi2DeSerializeFMUstate
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

    private fun assignStartValues(predicate: (TypedScalarVariable<*>) -> Boolean): Int {
        val variables = modelVariables.filter {
            it.start != null && predicate.invoke(it)
        }

        variables.forEach { variable ->
            when (variable) {
                is IntegerVariable -> variable.write(variable.start!!)
                is RealVariable -> variable.write(variable.start!!)
                is StringVariable -> variable.write(variable.start!!)
                is BooleanVariable -> variable.write(variable.start!!)
                is EnumerationVariable -> variable.write(variable.start!!)
            }
        }
        return variables.size
    }

}





