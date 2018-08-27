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

import no.mechatronics.sfi.fmi4j.common.*
import no.mechatronics.sfi.fmi4j.importer.misc.*
import no.mechatronics.sfi.fmi4j.modeldescription.SpecificModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for FMU instances
 *
 * @author Lars Ivar Hatledal
 */
abstract class AbstractFmuInstance<out E : SpecificModelDescription, out T : Fmi2LibraryWrapper<*>> internal constructor(
        val fmu: Fmu,
        val wrapper: T
) : FmuInstance {

    abstract override val modelDescription: E

    override val variableAccessor: FmuVariableAccessor = FmuVariableAccessorImpl(wrapper, modelVariables)

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

    override val canGetAndSetFMUstate: Boolean
        get() = modelDescription.canGetAndSetFMUstate

    override val canSerializeFMUstate: Boolean
        get() = modelDescription.canSerializeFMUstate

    override val providesDirectionalDerivative: Boolean
        get() = modelDescription.providesDirectionalDerivative

    /**
     * Has the FMU been initialized yet?
     * That is, has init() been called?
     */
    override var isInitialized = false
        protected set

    /**
     * @see Fmi2LibraryWrapper.isTerminated
     */
    override val isTerminated
        get() = wrapper.isTerminated


    protected var stopDefined = false
        private set

    protected var stopTime: Double = 0.0
        private set

    /**
     * Current simulation time
     */
    override var simulationTime: Double = 0.0
        internal set

    /**
     * @see Fmi2LibraryWrapper.lastStatus
     */
    override val lastStatus: FmiStatus
        get() = wrapper.lastStatus

    /**
     * @see FmiLibrary.fmi2SetDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, categories: Array<String>): FmiStatus
            = wrapper.setDebugLogging(loggingOn, categories)

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
            wrapper.setupExperiment(true, 1E-4, start, stopTime).also {
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

            simulationTime = start
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
        return (wrapper.reset() == FmiStatus.OK).also {
            isInitialized = false
        }
    }

    protected fun finalize() {
        if (!isTerminated) {
            LOG.warn("Instance ${modelDescription.modelName} was not terminated before garbage collection. Doing it for you..")
            close()
        }
    }

    override fun getDirectionalDerivative(vUnknownRef: IntArray, vKnownRef: IntArray, dvKnown: RealArray): RealArray {
        if (!providesDirectionalDerivative) {
            throw IllegalStateException("Illegal call. FMU does not provide directional derivatives!")
        }
        return RealArray(vUnknownRef.size).also {
            wrapper.getDirectionalDerivative(vUnknownRef, vKnownRef, dvKnown, it)
        }
    }

    /**
     * @see FmiLibrary.fmi2GetFMUstate
     */
    override fun getFMUstate(): FmuState {
        if (!modelDescription.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.getFMUState()
    }

    /**
     * @see FmiLibrary.fmi2SetFMUstate
     */
    override fun setFMUstate(state: FmuState): Boolean {
        if (!modelDescription.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.setFMUState(state) == FmiStatus.OK
    }

    /**
     * @see FmiLibrary.fmi2FreeFMUstate
     */
    override fun freeFMUstate(state: FmuState): Boolean {
        if (!modelDescription.canGetAndSetFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot get and set FMU state!")
        }
        return wrapper.freeFMUState(state) == FmiStatus.OK
    }

    /**
     * @see FmiLibrary.fmi2SerializedFMUstateSize
     */
    fun serializedFMUstateSize(fmuState: FmuState): Int {
        if (!modelDescription.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot serialize/deserialize FMU state!")
        }
        return wrapper.serializedFMUStateSize(fmuState)
    }

    /**
     * @see FmiLibrary.fmi2SerializeFMUstate
     */
    override fun serializeFMUstate(state: FmuState): ByteArray {
        if (!modelDescription.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot serialize/deserialize FMU state!")
        }
        return wrapper.serializeFMUState(state)
    }

    /**
     * @see FmiLibrary.fmi2DeSerializeFMUstate
     */
    override fun deSerializeFMUstate(state: ByteArray): FmuState {
        if (!modelDescription.canSerializeFMUstate) {
            throw UnsupportedOperationException("Method call not allowed, FMU cannot serialize/deserialize FMU state!")
        }
        return wrapper.deSerializeFMUState(state)
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

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(AbstractFmuInstance::class.java)
    }

}
