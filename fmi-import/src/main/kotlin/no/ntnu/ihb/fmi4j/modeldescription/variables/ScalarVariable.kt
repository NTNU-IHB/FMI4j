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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.io.Serializable


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
    val valueReference: Long

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

    companion object {
        const val INTEGER_TYPE = "Integer"
        const val REAL_TYPE = "Real"
        const val STRING_TYPE = "String"
        const val BOOLEAN_TYPE = "Boolean"
        const val ENUMERATION_TYPE = "Enumeration"
    }

}

/**
 * @author Lars Ivar Hatledal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class ScalarVariableImpl(

        @JacksonXmlProperty
        override val name: String,

        @JacksonXmlProperty
        override val valueReference: Long,

        @JacksonXmlProperty
        override val description: String? = null,

        @JacksonXmlProperty
        override val causality: Causality? = null,

        @JacksonXmlProperty
        override val variability: Variability? = null,

        @JacksonXmlProperty
        override val initial: Initial? = null


) : ScalarVariable, Serializable {

    @JacksonXmlProperty(localName = ScalarVariable.INTEGER_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var integerAttribute: IntegerAttributeImpl? = null

    @JacksonXmlProperty(localName = ScalarVariable.REAL_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var realAttribute: RealAttributeImpl? = null

    @JacksonXmlProperty(localName = ScalarVariable.STRING_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var stringAttribute: StringAttributeImpl? = null

    @JacksonXmlProperty(localName = ScalarVariable.BOOLEAN_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var booleanAttribute: BooleanAttributeImpl? = null

    @JacksonXmlProperty(localName = ScalarVariable.ENUMERATION_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var enumerationAttribute: EnumerationAttributeImpl? = null

    /**
     * Return a typed version of this variable.
     */
    fun toTyped(): TypedScalarVariable<*> {

        return when {
            integerAttribute != null -> IntegerVariable(this, integerAttribute!!)
            realAttribute != null -> RealVariable(this, realAttribute!!)
            stringAttribute != null -> StringVariable(this, stringAttribute!!)
            booleanAttribute != null -> BooleanVariable(this, booleanAttribute!!)
            enumerationAttribute != null -> EnumerationVariable(this, enumerationAttribute!!)
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
            add("attribute=$attribute")
        }.joinToString(", ")

        return "ScalarVariableImpl($entries)"
    }

}

