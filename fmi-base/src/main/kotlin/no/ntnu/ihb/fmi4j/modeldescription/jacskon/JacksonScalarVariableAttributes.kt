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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.variables.*

/**
 * @author Lars Ivar Hatledal
 */
data class JacksonIntegerAttribute(

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
        return "JacksonIntegerAttribute(min=$min, max=$max, start=$start, declaredType=$declaredType)"
    }
}

/**
 * @author Lars Ivar Hatledal
 */
data class JacksonRealAttribute(

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
        override val relativeQuantity: Boolean = false

) : RealAttribute {

    override fun toString(): String {
        return "JacksonRealAttribute(min=$min, max=$max, start=$start, declaredType=$declaredType, nominal=$nominal, derivative=$derivative, unbounded=$unbounded, reinit=$reinit, quantity=$quantity, unit=$unit, displayUnit=$displayUnit, relativeQuantity=$relativeQuantity)"
    }
}


/**
 * @author Lars Ivar Hatledal
 */
data class JacksonStringAttributeImpl(

        @JacksonXmlProperty
        override val start: String? = null,

        @JacksonXmlProperty
        override val declaredType: String? = null

) : StringAttribute {

    override fun toString(): String {
        return "JacksonStringAttributeImpl(start=$start, declaredType=$declaredType)"
    }

}



/**
 * @author Lars Ivar Hatledal
 */
data class JacksonBooleanAttribute(

        @JacksonXmlProperty
        override val start: Boolean? = null,

        @JacksonXmlProperty
        override val declaredType: String? = null

) : BooleanAttribute {

    override fun toString(): String {
        return "JacksonBooleanAttribute(start=$start, declaredType=$declaredType)"
    }
}


/**
 * @author Lars Ivar Hatledal
 */
data class JacksonEnumerationAttribute(

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
        return "JacksonEnumerationAttribute(min=$min, max=$max, quantity=$quantity, start=$start, declaredType=$declaredType)"
    }

}
