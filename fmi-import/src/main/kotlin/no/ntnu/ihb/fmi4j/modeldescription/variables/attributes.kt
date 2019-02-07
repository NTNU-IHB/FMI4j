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

package no.ntnu.ihb.fmi4j.modeldescription.variables

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.io.Serializable

/**
 * @author Lars Ivar Hatledal
 */
interface TypedAttribute<out E> : Serializable {

    /**
     * If present, name of type defined with TypeDefinitions / SimpleType. The value
     * defined in the corresponding TypeDefinition (see section 2.2.3) is used as
     * default. [If, for example “min” is present both in Real (of TypeDefinition) and in
     * “Real” (of ScalarVariable), then the “min” of ScalarVariable is actually
     * used.] For Real, Integer, Boolean, String, this attribute is optional. For
     * Enumeration it is required, because the Enumeration items are defined in
     * TypeDefinitions / SimpleType.
     */
    val declaredType: String?

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
    val start: E?

}

/**
 * @author Lars Ivar Hatledal
 */
interface BoundedTypedAttribute<out E> : TypedAttribute<E> {

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

    /**
     * Physical quantity of the variable, for example “Angle”, or “Energy”.
     * The quantity names are not standardized.
     */
    val quantity: String?
}


interface IntegerAttribute: BoundedTypedAttribute<Int>

/**
 * @author Lars Ivar Hatledal
 */
data class IntegerAttributeImpl(

        @JacksonXmlProperty
        override val min: Int? = null,

        @JacksonXmlProperty
        override val max: Int? = null,

        @JacksonXmlProperty
        override val quantity: String? = null,

        @JacksonXmlProperty
        override val start: Int? = null,

        @JacksonXmlProperty
        override val declaredType: String? = null

) : IntegerAttribute {

    override fun toString(): String {
        return "IntegerAttribute(min=$min, max=$max, start=$start, declaredType=$declaredType)"
    }
}

interface RealAttribute: BoundedTypedAttribute<Double> {

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
     * If present, this variable is the derivative of variable with ScalarVariable index "derivative",
     */
    val derivative: Int?

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
     * Only for Model exchange
     *
     * If true, state can be reinitialized at an event by the FMU.
     * If false, state will never be reinitialized at an event by the FMU
     */
    val reinit: Boolean

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

}

/**
 * @author Lars Ivar Hatledal
 */
data class RealAttributeImpl(

        @JacksonXmlProperty
        override val min: Double? = null,

        @JacksonXmlProperty
        override val max: Double? = null,

        @JacksonXmlProperty
        override val start: Double? = null,

        @JacksonXmlProperty
        override val declaredType: String? = null,

        @JacksonXmlProperty
        override val nominal: Double? = null,

        @JacksonXmlProperty
        override val derivative: Int? = null,

        @JacksonXmlProperty
        override val unbounded: Boolean? = null,

        @JacksonXmlProperty
        override val reinit: Boolean = false,

        @JacksonXmlProperty
        override val quantity: String? = null,

        @JacksonXmlProperty
        override val unit: String? = null,

        @JacksonXmlProperty
        override val displayUnit: String? = null,

        @JacksonXmlProperty
        override val relativeQuantity: String? = null

) : RealAttribute {

    override fun toString(): String {
        return "RealAttribute(min=$min, max=$max, start=$start, declaredType=$declaredType, nominal=$nominal, derivative=$derivative, unbounded=$unbounded, reinit=$reinit, quantity=$quantity, unit=$unit, displayUnit=$displayUnit, relativeQuantity=$relativeQuantity)"
    }
}

interface StringAttribute: TypedAttribute<String>
/**
 * @author Lars Ivar Hatledal
 */
data class StringAttributeImpl(

        @JacksonXmlProperty
        override val start: String? = null,

        @JacksonXmlProperty
        override val declaredType: String? = null

) : StringAttribute {

    override fun toString(): String {
        return "StringAttribute(start=$start, declaredType=$declaredType)"
    }

}

interface BooleanAttribute: TypedAttribute<Boolean>

/**
 * @author Lars Ivar Hatledal
 */
data class BooleanAttributeImpl(

        @JacksonXmlProperty
        override val start: Boolean? = null,

        @JacksonXmlProperty
        override val declaredType: String? = null

) : BooleanAttribute {

    override fun toString(): String {
        return "BooleanAttribute(start=$start, declaredType=$declaredType)"
    }
}

interface EnumerationAttribute: BoundedTypedAttribute<Int>

/**
 * @author Lars Ivar Hatledal
 */
data class EnumerationAttributeImpl(

        @JacksonXmlProperty
        override val min: Int? = null,

        @JacksonXmlProperty
        override val max: Int? = null,

        @JacksonXmlProperty
        override val quantity: String? = null,

        @JacksonXmlProperty
        override val start: Int? = null,

        @JacksonXmlProperty
        override val declaredType: String? = null

) : EnumerationAttribute {

    override fun toString(): String {
        return "EnumerationAttribute(min=$min, max=$max, quantity=$quantity, start=$start, declaredType=$declaredType)"
    }

}
