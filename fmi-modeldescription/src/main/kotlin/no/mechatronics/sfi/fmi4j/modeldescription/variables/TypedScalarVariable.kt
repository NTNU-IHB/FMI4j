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

package no.mechatronics.sfi.fmi4j.modeldescription.variables

import no.mechatronics.sfi.fmi4j.common.*
import java.io.Serializable


/**
 * @author Lars Ivar Hatledal
 */
interface TypedScalarVariable<E> : ScalarVariable {

    /**
     * Initial or guess value of variable. This value is also stored in the C functions
     * [Therefore, calling fmi2SetXXX to set start values is only necessary, if a different
     * value as stored in the xml file is desired.] The interpretation of start is defined by
     * ScalarVariable / initial. A different start value can be provided with an
     * fmi2SetXXX function before fmi2ExitInitializationMode is called (but not
     * for categories with variability = ″constant″).
     * [The standard approach is to set the start value before
     * fmi2EnterInitializationMode. However, if the initialization shall be modified
     * in the calling environment (e.g. changing from initialization of states to steadystate
     * initialization), it is also possible to use the start value as iteration variable of
     * an algebraic loop: Via an additional condition in the environment, such as 𝑥̇ = 0,
     * the actual start value is determined.]
     */
    var start: E?

    /**
     * Accesses the FMU and returns the current value of the variable
     * represented by this valueReference, as well as the status
     */
    fun read(): FmuRead<E>

    /**
     * Accesses the FMU and writes the provided value to the FMU
     * variable represented by this valueReference
     *
     * @value value to set
     */
    fun write(value: E): FmiStatus

    fun asIntegerVariable(): IntegerVariable = if (this is IntegerVariable) this else throw IllegalAccessException("Variable is not an ${IntegerVariable::class.java.simpleName}, but an ${this::class.java.simpleName}")

    fun asRealVariable(): RealVariable = if (this is RealVariable) this else throw throw IllegalAccessException("Variable is not an ${RealVariable::class.java.simpleName}, but an ${this::class.java.simpleName}")

    fun asStringVariable(): StringVariable = if (this is StringVariable) this else throw IllegalAccessException("Variable is not an ${StringVariable::class.java.simpleName}, but an ${this::class.java.simpleName}")

    fun asBooleanVariable(): BooleanVariable = if (this is BooleanVariable) this else throw IllegalAccessException("Variable is not an ${BooleanVariable::class.java.simpleName}, but an ${this::class.java.simpleName}")

    fun asEnumerationVariable(): EnumerationVariable = if (this is EnumerationVariable) this else throw IllegalAccessException("Variable is not an ${EnumerationVariable::class.java.simpleName}, but an ${this::class.java.simpleName}")

}

/**
 * @author Lars Ivar Hatledal
 */
interface BoundedTypedScalarVariable<E> : TypedScalarVariable<E> {

    /**
     * Minimum value of variable (variable Value ≥ min). If not defined, the
     * minimum is the largest negative number that can be represented on the
     * machine. The min definition is an information from the FMU to the
     * environment defining the region in which the FMU is designed to operate, see
     * also comment after this table.
     */
    val min: E?

    /**
     * Maximum value of variable (variableValue ≤ max). If not defined, the
     * maximum is the largest positive number that can be represented on the
     * machine. The max definition is an information from the FMU to the
     * environment defining the region in which the FMU is designed to operate, see
     * also comment after this table.
     */
    val max: E?
}

/**
 * @author Lars Ivar Hatledal
 */
sealed class AbstractTypedScalarVariable<E>(
        v: ScalarVariable
) : ScalarVariable by v, TypedScalarVariable<E>, Serializable {

    @JvmField
    internal var accessor: FmuVariableAccessor? = null

}

/**
 * @author Lars Ivar Hatledal
 */
sealed class AbstractBoundedTypedScalarVariable<E>(
        v: ScalarVariable
) : BoundedTypedScalarVariable<E>, AbstractTypedScalarVariable<E>(v)

/**
 * @author Lars Ivar Hatledal
 */
interface IntegerVariable : BoundedTypedScalarVariable<Int>

/**
 * @author Lars Ivar Hatledal
 */
class IntegerVariableImpl internal constructor(
        v: ScalarVariableImpl
) : IntegerVariable, AbstractBoundedTypedScalarVariable<Int>(v) {

    private val attribute = v.integerAttribute
            ?: throw IllegalStateException("Variable is not an Integer!")

    override val min: Int? = attribute.min
    override val max: Int? = attribute.max
    override var start = attribute.start

    override fun read(): FmuIntegerRead {
        return accessor?.readInteger(valueReference)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun write(value: Int): FmiStatus {
        return accessor?.writeInteger(valueReference, value)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun toString(): String {
        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            accessor?.also { add("value=${read().value}") }
            min?.also { add("min=$min") }
            max?.also { add("min=$min") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
        }.joinToString(", ")

        return "${IntegerVariableImpl::class.java.simpleName}($entries)"
    }

}

/**
 * @author Lars Ivar Hatledal
 */
interface RealVariable : BoundedTypedScalarVariable<Real> {

    /**
     * Nominal value of variable. If not defined and no other information about the
     * nominal value is available, then nominal = 1 is assumed.
     * [The nominal value of a variable can be, for example used to determine the
     * absolute tolerance for this variable as needed by numerical algorithms:
     * absoluteTolerance = nominal*tolerance*0.01
     * where tolerance is, e.g., the relative tolerance defined in
     * <DefaultExperiment>, see section 2.2.5.]
     */
    val nominal: Double?

    /**
     * If true, indicates that the variable gets during time integration much larger
     * than its nominal value nominal. [Typical examples are the monotonically
     * increasing rotation angles of crank shafts and the longitudinal position of a
     * vehicle along the track in long distance simulations. This information can, for
     * example, be used to increase numerical stability and accuracy by setting the
     * corresponding bound for the relative error to zero (relative tolerance = 0.0), if
     * the corresponding variable or an alias of it is a continuous state variable.]
     */
    val unbounded: Boolean?

    /**
     * Physical quantity of the variable, for example “Angle”, or “Energy”. The
     * quantity names are not standardized.
     */
    val quantity: String?

    /**
     * Unit of the variable defined with UnitDefinitions.Unit.name that is used
     * for the model equations [, for example “N.m”: in this case a Unit.name =
     * "N.m" must be present under UnitDefinitions].
     */
    val unit: String?

    /**
     * Default display unit. The conversion to the “unit” is defined with the element
     * “<fmiModelDescription><UnitDefinitions>”. If the corresponding
     * “displayUnit” is not defined under <UnitDefinitions> <Unit>
     * <DisplayUnit>, then displayUnit is ignored. It is an error if
     * displayUnit is defined in element Real, but unit is not, or unit is not
     * defined under <UnitDefinitions><Unit>.
     */
    val displayUnit: String?

    /**
     * If this attribute is true, then the “offset” of “displayUnit” must be ignored
     * (for example 10 degree Celsius = 10 Kelvin if “relativeQuantity = true”
     * and not 283,15 Kelvin).
     */
    val relativeQuantity: String?

    /**
     * Only for Model exchange
     * <br>
     * If true, state can be reinitialized at an event by the FMU. If false, state will never be reinitialized at an event by the FMU
     */
    val reinit: Boolean?

    /**
     * If present, this variable is the derivative of variable with ScalarVariable index "derivative",
     */
    val derivative: Int?
}

/**
 * @author Lars Ivar Hatledal
 */
class RealVariableImpl internal constructor(
        v: ScalarVariableImpl
) : RealVariable, AbstractBoundedTypedScalarVariable<Real>(v) {

    private val attribute = v.realAttribute
            ?: throw IllegalStateException("Variable is not an Real!")

    override val min: Double? = attribute.min
    override val max: Double? = attribute.max
    override var start: Double? = attribute.start
    override val nominal: Double? = attribute.nominal
    override val unbounded: Boolean? = attribute.unbounded
    override val quantity: String? = attribute.quantity
    override val unit: String? = attribute.unit
    override val displayUnit: String? = attribute.displayUnit
    override val relativeQuantity: String? = attribute.relativeQuantity
    override val reinit: Boolean? = attribute.reinit
    override val derivative: Int? = attribute.derivative

    override fun read(): FmuRealRead {
        return accessor?.readReal(valueReference)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun write(value: Real): FmiStatus {
        return accessor?.writeReal(valueReference, value)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            accessor?.also { add("value=${read().value}") }
            min?.also { add("min=$min") }
            max?.also { add("min=$min") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            nominal?.also { add("nominal=$nominal") }
            unbounded?.also { add("unbounded=$unbounded") }
            quantity?.also { add("quantity=$quantity") }
            unit?.also { add("unit=$unit") }
            displayUnit?.also { add("displayUnit=$displayUnit") }
            relativeQuantity?.also { add("relativeQuantity=$relativeQuantity") }
            derivative?.also { add("derivative=$derivative") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
        }.joinToString(", ")

        return "${RealVariableImpl::class.java.simpleName}($entries)"

    }

}

/**
 * @author Lars Ivar Hatledal
 */
interface StringVariable : TypedScalarVariable<String>

/**
 * @author Lars Ivar Hatledal
 */
class StringVariableImpl internal constructor(
        v: ScalarVariableImpl
) : StringVariable, AbstractTypedScalarVariable<String>(v) {

    private val attribute = v.stringAttribute
            ?: throw IllegalStateException("Variable is not an String!")

    override var start = attribute.start

    override fun read(): FmuStringRead {
        return accessor?.readString(valueReference)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun write(value: String): FmiStatus {
        return accessor?.writeString(valueReference, value)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            accessor?.also { add("value=${read().value}") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
        }.joinToString(", ")

        return "${StringVariableImpl::class.java.simpleName}($entries)"

    }

}

/**
 * @author Lars Ivar Hatledal
 */
interface BooleanVariable : TypedScalarVariable<Boolean>

/**
 * @author Lars Ivar Hatledal
 */
class BooleanVariableImpl internal constructor(
        v: ScalarVariableImpl
) : BooleanVariable, AbstractTypedScalarVariable<Boolean>(v) {

    private val attribute = v.booleanAttribute
            ?: throw IllegalStateException("Variable is not an Boolean!")

    override var start = attribute.start

    override fun read(): FmuBooleanRead {
        return accessor?.readBoolean(valueReference)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun write(value: Boolean): FmiStatus {
        return accessor?.writeBoolean(valueReference, value)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            accessor?.also { add("value=${read().value}") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
        }.joinToString(", ")

        return "${BooleanVariableImpl::class.java.simpleName}($entries)"

    }

}

/**
 * @author Lars Ivar Hatledal
 */
interface EnumerationVariable : BoundedTypedScalarVariable<Int> {

    /**
     * Physical quantity of the variable, for example “Angle”, or “Energy”. The
     * quantity names are not standardized.
     */
    val quantity: String?

}

/**
 * @author Lars Ivar Hatledal
 */
class EnumerationVariableImpl internal constructor(
        v: ScalarVariableImpl
) : EnumerationVariable, AbstractBoundedTypedScalarVariable<Int>(v) {

    private val attribute = v.enumerationAttribute
            ?: throw IllegalStateException("Variable is not an Enumeration!")

    override val min: Int? = attribute.min
    override val max: Int? = attribute.max
    override var start: Int? = attribute.start

    override val quantity: String? = attribute.quantity

    override fun read(): FmuRead<Int> {
        return accessor?.readInteger(valueReference)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun write(value: Int): FmiStatus {
        return accessor?.writeInteger(valueReference, value)
                ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            accessor?.also { add("value=${read().value}") }
            min?.also { add("min=$min") }
            max?.also { add("min=$min") }
            quantity?.also { add("quantity=$quantity") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
        }.joinToString(", ")

        return "${EnumerationVariableImpl::class.java.simpleName}($entries)"

    }

}

