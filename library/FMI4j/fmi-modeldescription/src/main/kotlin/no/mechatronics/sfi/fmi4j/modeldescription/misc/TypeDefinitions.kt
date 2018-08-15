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

package no.mechatronics.sfi.fmi4j.modeldescription.misc

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import java.io.Serializable

typealias TypeDefinitions = List<SimpleType>

/**
 * @author Lars Ivar Hatledal
 */
data class SimpleType(

        /**
         * Name of SimpleType element.
         * "name" must be unique with respect to all other elements
         * of the TypeDefinitions list. Furthermore, "name" of a SimpleType
         * must bee different to all "name"s of ScalarVariable
         */
        @JacksonXmlProperty
        val name: String,

        /**
         * Description of the SimpleType
         */
        @JacksonXmlProperty
        val description: String

) : Serializable {

    @JacksonXmlProperty(localName = ScalarVariable.INTEGER_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var integerAttribute: IntegerAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.REAL_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var realAttribute: RealAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.STRING_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var stringAttribute: StringAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.BOOLEAN_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var booleanAttribute: BooleanAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.ENUMERATION_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var enumerationAttribute: EnumerationAttribute? = null

}