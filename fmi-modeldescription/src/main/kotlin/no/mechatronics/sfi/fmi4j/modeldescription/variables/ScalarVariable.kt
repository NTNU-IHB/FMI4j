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

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import no.mechatronics.sfi.fmi4j.common.*
import java.io.Serializable

const val INTEGER_TYPE = "Integer"
const val REAL_TYPE = "Real"
const val STRING_TYPE = "String"
const val BOOLEAN_TYPE = "Boolean"
const val ENUMERATION_TYPE = "Enumeration"

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
    val declaredType: String?

    /**
     * An optional description string describing the meaning of the variable
     */
    val description: String?

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
data class ScalarVariableImpl(

        @JacksonXmlProperty
        override val name: String,

        @JacksonXmlProperty
        override val valueReference: Int,

        @JacksonXmlProperty
        override val declaredType: String? = null,

        @JacksonXmlProperty
        override val description: String? = null,

        @JacksonXmlProperty
        override val causality: Causality? = null,

        @JacksonXmlProperty
        override val variability: Variability? = null,

        @JacksonXmlProperty
        override val initial: Initial? = null


) : ScalarVariable, Serializable {

    @JacksonXmlProperty(localName = INTEGER_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var integerAttribute: IntegerAttribute? = null

    @JacksonXmlProperty(localName = REAL_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var realAttribute: RealAttribute? = null

    @JacksonXmlProperty(localName = STRING_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var stringAttribute: StringAttribute? = null

    @JacksonXmlProperty(localName = BOOLEAN_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var booleanAttribute: BooleanAttribute? = null

    @JacksonXmlProperty(localName = ENUMERATION_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var enumerationAttribute: EnumerationAttribute? = null

    /**
     * Return a typed version of this variable.
     */
    fun toTyped(): TypedScalarVariable<*> {

        return when {
            integerAttribute != null -> IntegerVariableImpl(this)
            realAttribute != null -> RealVariableImpl(this)
            stringAttribute != null -> StringVariableImpl(this)
            booleanAttribute != null -> BooleanVariableImpl(this)
            enumerationAttribute != null -> EnumerationVariableImpl(this)
            else -> throw IllegalStateException("All attributes are null!")
        }

    }

    override fun toString(): String {

        val attribute = when {
            integerAttribute != null -> integerAttribute
            realAttribute != null -> realAttribute
            stringAttribute != null -> stringAttribute
            booleanAttribute != null -> booleanAttribute
            enumerationAttribute != null -> enumerationAttribute
            else -> throw IllegalStateException("All attributes are null!")
        }

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
            add("attribute=$attribute")
        }.joinToString (", ")

        return "ScalarVariableImpl($entries)"
    }

}

