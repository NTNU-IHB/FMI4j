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

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.common.FmuRead
import no.mechatronics.sfi.fmi4j.modeldescription.enums.*
import no.mechatronics.sfi.fmi4j.modeldescription.variables.attributes.*
import java.io.Serializable
import javax.xml.bind.annotation.*

typealias Real = Double
typealias RealArray = DoubleArray
typealias StringArray = Array<String>

/**
 * @author Lars Ivar Hatledal
 */
interface ScalarVariable {

    /**
     * The full, unique name of the variable. Every variable is uniquely identified within an FMU
     * instance by this name or by its ScalarVariable index (the element position in the
     * ModelVariables list; the first list element has index=1).
     */
    val name: String

    /**
     * A handle of the variable to efficiently identify the variable value in the model interface.
     * This handle is a secret of the tool that generated the C functions. It is not required to be
     * unique. The only guarantee is that valueReference is sufficient to identify the respective variable value in the call of the C functions. This implies that it is unique for a
     * particular base data type (Real, Integer/Enumeration, Boolean, String) with
     * exception of categories that have identical values (such categories are also called “alias”
     * categories). This attribute is “required”.
     */
    val valueReference: Int

    /**
     * If present, name of type defined with TypeDefinitions / SimpleType. The value
     * defined in the corresponding TypeDefinition (see section 2.2.3) is used as
     * default. [If, for example “min” is present both in Real (of TypeDefinition) and in
     * “Real” (of ScalarVariable), then the “min” of ScalarVariable is actually
     * used.] For Real, Integer, Boolean, String, this attribute is optional. For
     * Enumeration it is required, because the Enumeration items are defined in
     * TypeDefinitions / SimpleType.
     */
    val declaredType: String

    /**
     * An optional description string describing the meaning of the variable
     */
    val description: String

    /**
     * @see Causality
     */
    val causality: Causality?

    /**
     * @see Variability
     */
    val variability: Variability?

    /**
     * @see Initial
     */
    val initial: Initial?


}

/**
 * @author Lars Ivar Hatledal
 */
@XmlRootElement(name="ScalarVariable")
@XmlAccessorType(XmlAccessType.FIELD)
class ScalarVariableImpl internal constructor() : ScalarVariable {

    @XmlAttribute
    override val name: String = ""

    @XmlAttribute
    override val declaredType: String = ""

    @XmlAttribute
    override val description: String = ""

    @XmlAttribute
    override val causality: Causality? = null

    @XmlAttribute
    override val variability: Variability? = null

    @XmlAttribute
    override var initial: Initial? = null

    @XmlAttribute(name="valueReference")
    private val _valueReference: Int? = null

    override val valueReference: Int
        get(){
            return _valueReference ?: throw IllegalStateException("ValueReference was null!")
        }

    @XmlElement(name="Integer")
    internal val integerAttribute: IntegerAttribute? = null

    @XmlElement(name="Real")
    internal val realAttribute: RealAttribute? = null

    @XmlElement(name="String")
    internal val stringAttribute: StringAttribute? = null

    @XmlElement(name="Boolean")
    internal val booleanAttribute: BooleanAttribute? = null

    override fun toString(): String {
        return "ScalarVariableImpl(name='$name', declaredType='$declaredType', description='$description', causality=$causality, variability=$variability, initial=$initial)"
    }

}

interface TypedScalarVariable<E>: ScalarVariable {
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

    /**
     * Integer, Real, String or Boolean
     */
    val typeName: String

    fun asIntegerVariable(): IntegerVariable
            = if (this is IntegerVariable) this else throw IllegalAccessException("Variable is not an ${IntegerVariable::class.java.simpleName}, but an ${this::class.java.simpleName}")

    fun asRealVariable(): RealVariable
            = if (this is RealVariable) this else throw throw IllegalAccessException("Variable is not an ${RealVariable::class.java.simpleName}, but an ${this::class.java.simpleName}")

    fun asStringVariable(): StringVariable
            = if (this is StringVariable) this else throw IllegalAccessException("Variable is not an ${StringVariable::class.java.simpleName}, but an ${this::class.java.simpleName}")

    fun asBooleanVariable(): BooleanVariable
            = if (this is BooleanVariable) this else throw IllegalAccessException("Variable is not an ${BooleanVariable::class.java.simpleName}, but an ${this::class.java.simpleName}")


}

interface BoundedScalarVariable<E>: TypedScalarVariable<E> {

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
sealed class AbstractTypedScalarVariable<E>: TypedScalarVariable<E>, Serializable {

    @JvmField
    @Transient
    internal var accessor: VariableAccessor? = null

}

sealed class AbstractBoundedScalarVariable<E>: BoundedScalarVariable<E>, AbstractTypedScalarVariable<E>()

/**
 * @author Lars Ivar HatLedal
 */
class IntegerVariable internal constructor(private val v : ScalarVariableImpl) : ScalarVariable by v, AbstractBoundedScalarVariable<Int>() {

    private val attribute = v.integerAttribute ?: throw AssertionError()

    override val typeName = "Integer"
    override val min: Int? = attribute.min
    override val max: Int? = attribute.max
    override var start = attribute.start

    override fun read(): FmuRead<Int> {
        return accessor?.readInteger(valueReference) ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun write(value: Int): FmiStatus {
        return accessor?.writeInteger(valueReference, value) ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun toString(): String {
        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            causality?.also { add("causality=$causality") }
            start?.also { add("start=$start") }
            accessor?.also { add("value=${read()}") }
            min?.also { add("min=$min") }
            max?.also { add("min=$min") }
        }.joinToString { ", " }

        return "IntegerVariable($entries)"
    }

}

/**
 * @author Lars Ivar Hatledal
 */
class RealVariable internal constructor(private val v : ScalarVariableImpl) : ScalarVariable by v, AbstractBoundedScalarVariable<Real>() {

    private val attribute: RealAttribute = v.realAttribute ?: throw AssertionError()

    override val typeName = "Real"
    override val min = attribute.min
    override val max = attribute.max

    /**
     * @see RealAttribute.nominal
     */
    val nominal = attribute.nominal

    /**
     * @see RealAttribute.unbounded
     */
    val unbounded = attribute.unbounded

    /**
     * @see RealAttribute.quantity
     */
    val quantity = attribute.quantity

    /**
     * @see RealAttribute.unit
     */
    val unit = attribute.unit

    /**
     * @see RealAttribute.displayUnit
     */
    val displayUnit = attribute.displayUnit

    /**
     * @see RealAttribute.relativeQuantity
     */
    val relativeQuantity = attribute.relativeQuantity

    /**
     * @see RealAttribute.derivative
     */
    val derivative = attribute.derivative

    override var start = attribute.start

    override fun read(): FmuRead<Real> {
        return accessor?.readReal(valueReference) ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun write(value: Real): FmiStatus {
        return accessor?.writeReal(valueReference, value) ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            causality?.also { add("causality=$causality") }
            start?.also { add("start=$start") }
            accessor?.also { add("value=${read()}") }
            min?.also { add("min=$min") }
            max?.also { add("min=$min") }
            nominal?.also { add("nominal=$nominal") }
            unbounded?.also { add("unbounded=$unbounded") }
            quantity?.also { add("quantity=$quantity") }
            unit?.also { add("unit=$unit") }
            displayUnit?.also { add("displayUnit=$displayUnit") }
            relativeQuantity?.also { add("relativeQuantity=$relativeQuantity") }
            derivative?.also { add("derivative=$derivative") }
        }.joinToString (", ")

        return "RealVariable($entries)"

    }


}

/**
 * @author Lars Ivar Hatledal
 */
class StringVariable internal constructor(private val v : ScalarVariableImpl) : ScalarVariable by v, AbstractTypedScalarVariable<String>() {

    private val attribute = v.stringAttribute ?: throw AssertionError()

    override val typeName = "String"
    override var start = attribute.start

    override fun read(): FmuRead<String> {
        return accessor?.readString(valueReference) ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun write(value: String): FmiStatus {
        return accessor?.writeString(valueReference, value) ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            causality?.also { add("causality=$causality") }
            start?.also { add("start=$start") }
            accessor?.also { add("value=${read()}") }
        }.joinToString (", ")

        return "StringVariable($entries)"

    }

}

/**
 * @author Lars Ivar Hatledal
 */
class BooleanVariable internal constructor(private val v : ScalarVariableImpl) : ScalarVariable by v, AbstractTypedScalarVariable<Boolean>() {

    private val attribute = v.booleanAttribute ?: throw AssertionError()

    override val typeName = "Boolean"
    override var start = attribute.start

    override fun read(): FmuRead<Boolean> {
        return accessor?.readBoolean(valueReference) ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun write(value: Boolean): FmiStatus {
        return accessor?.writeBoolean(valueReference, value) ?: throw IllegalStateException("No accessor assigned!")
    }

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            causality?.also { add("causality=$causality") }
            start?.also { add("start=$start") }
            accessor?.also { add("value=${read()}") }
        }.joinToString (", ")

        return "BooleanVariable($entries)"

    }

}

