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

package no.ntnu.ihb.fmi4j.modeldescription.fmi1

import no.ntnu.ihb.fmi4j.modeldescription.variables.*

class JaxbScalarVariable internal constructor(
        private val v: FmiScalarVariable
): ScalarVariable {

    override val name: String
        get() = v.name
    override val valueReference: Long
        get() = v.valueReference
    override val description: String?
        get() = v.description
    override val causality: Causality?
        get() = v.causality?.let { Causality.fmi1ValueOf(it, v.variability) }
    override val variability: Variability?
        get() = v.variability?.let { Variability.fmi1ValueOf(it) }
    override val initial: Initial?
        get() = null

    /**
     * Return a typed version of this variable.
     */
    fun toTyped(): TypedScalarVariable<*> {
        
        return when {
            v.integer != null -> IntegerVariable(this, JaxbIntegerAttribute(v.integer))
            v.real != null -> RealVariable(this, JaxbRealAttribute(v.real))
            v.string != null -> StringVariable(this, JaxbStringAttribute(v.string))
            v._boolean != null -> BooleanVariable(this, JaxbBooleanAttribute(v._boolean))
            v.enumeration != null -> EnumerationVariable(this, JaxbEnumerationAttribute(v.enumeration))
            else -> throw IllegalStateException()
        }

    }

}

class JaxbIntegerAttribute internal constructor(
        private val attribute: FmiScalarVariable.Integer
): IntegerAttribute {

    override val declaredType: String?
        get() = attribute.declaredType
    override val start: Int?
        get() = attribute.start
    override val min: Int?
        get() = attribute.min
    override val max: Int?
        get() = attribute.max
    override val quantity: String?
        get() = attribute.quantity

}

class JaxbRealAttribute internal constructor(
        private val attribute: FmiScalarVariable.Real
): RealAttribute {

    override val declaredType: String?
        get() = attribute.declaredType
    override val start: Double?
        get() = attribute.start
    override val min: Double?
        get() = attribute.min
    override val max: Double?
        get() = attribute.max
    override val quantity: String?
        get() = attribute.quantity

    override val nominal: Double?
        get() = attribute.nominal
    override val derivative: Int?
        get() = null
    override val unbounded: Boolean
        get() = false
    override val reinit: Boolean
        get() = false
    override val unit: String?
        get() = attribute.unit
    override val displayUnit: String?
        get() = attribute.displayUnit
    override val relativeQuantity: Boolean
        get() = attribute.isRelativeQuantity

}

class JaxbStringAttribute internal constructor(
        private val attribute: FmiScalarVariable.String
): StringAttribute {

    override val declaredType: String?
        get() = attribute.declaredType
    override val start: String?
        get() = attribute.start

}

class JaxbBooleanAttribute internal constructor(
        private val attribute: FmiScalarVariable.Boolean
): BooleanAttribute {

    override val declaredType: String?
        get() = attribute.declaredType
    override val start: Boolean?
        get() = attribute.start

}

class JaxbEnumerationAttribute internal constructor(
        private val attribute: FmiScalarVariable.Enumeration
): EnumerationAttribute {

    override val declaredType: String?
        get() = attribute.declaredType
    override val start: Int?
        get() = attribute.start
    override val min: Int?
        get() = attribute.min
    override val max: Int?
        get() = attribute.max
    override val quantity: String?
        get() = attribute.quantity

}
