/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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

package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.variables.*

/**
 * @author Lars Ivar Hatledal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class JacksonScalarVariable(

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


) : ScalarVariable {

    @JacksonXmlProperty(localName = ScalarVariable.INTEGER_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var integerAttribute: JacksonIntegerAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.REAL_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var realAttribute: JacksonRealAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.STRING_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var stringAttribute: JacksonStringAttributeImpl? = null

    @JacksonXmlProperty(localName = ScalarVariable.BOOLEAN_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var booleanAttribute: JacksonBooleanAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.ENUMERATION_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var enumerationAttribute: JacksonEnumerationAttribute? = null

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

        return "JacksonScalarVariable($entries)"
    }

}

