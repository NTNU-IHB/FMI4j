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

import no.ntnu.ihb.fmi4j.modeldescription.Real

/**
 * @author Lars Ivar Hatledal
 */
interface TypedScalarVariable<E> : ScalarVariable, TypedAttribute<E> {

    fun asIntegerVariable(): IntegerVariable = when {
        this is IntegerVariable -> this
        else -> throw IllegalAccessException(
                "Variable is not an ${VariableType.INTEGER.typeName}, but an ${type.typeName}!")
    }

    fun asRealVariable(): RealVariable = when {
        this is RealVariable -> this
        else -> throw throw IllegalAccessException(
                "Variable is not an ${VariableType.REAL.typeName}, but an ${type.typeName}!")
    }

    fun asBooleanVariable(): BooleanVariable = when {
        this is BooleanVariable -> this
        else -> throw IllegalAccessException(
                "Variable is not an ${VariableType.BOOLEAN.typeName}, but an ${type.typeName}!")
    }

    fun asStringVariable(): StringVariable = when {
        this is StringVariable -> this
        else -> throw IllegalAccessException(
                "Variable is not an ${VariableType.STRING.typeName}, but an ${type.typeName}!")
    }

    fun asEnumerationVariable(): EnumerationVariable = when {
        this is EnumerationVariable -> this
        else -> throw IllegalAccessException(
                "Variable is not an ${VariableType.ENUMERATION.typeName}, but an ${type.typeName}!")
    }

}

interface BoundedScalarVariable<E>: TypedScalarVariable<E>, BoundedTypedAttribute<E>

/**
 * @author Lars Ivar Hatledal
 */
class IntegerVariable (
        v: ScalarVariable,
        a: IntegerAttribute
) : BoundedScalarVariable<Int>, ScalarVariable by v, IntegerAttribute by a {

    override val type: VariableType
        get() = VariableType.INTEGER

    override fun toString(): String {
        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            min?.also { add("min=$min") }
            max?.also { add("min=$min") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
        }.joinToString(", ")

        return "${IntegerVariable::class.java.simpleName}($entries)"
    }

}

/**
 * @author Lars Ivar Hatledal
 */
class RealVariable (
        v: ScalarVariable,
        a: RealAttribute
) : BoundedScalarVariable<Real>, ScalarVariable by v, RealAttribute by a {

    override val type: VariableType
        get() = VariableType.REAL

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            min?.also { add("min=$min") }
            max?.also { add("min=$min") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            nominal?.also { add("nominal=$nominal") }
            unbounded.also { add("unbounded=$unbounded") }
            quantity?.also { add("quantity=$quantity") }
            unit?.also { add("unit=$unit") }
            displayUnit?.also { add("displayUnit=$displayUnit") }
            relativeQuantity.also { add("relativeQuantity=$relativeQuantity") }
            derivative?.also { add("derivative=$derivative") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
            reinit.also { add("reinit=$reinit") }
        }.joinToString(", ")

        return "${RealVariable::class.java.simpleName}($entries)"

    }

}

/**
 * @author Lars Ivar Hatledal
 */
class BooleanVariable (
        v: ScalarVariable,
        a: BooleanAttribute
) : TypedScalarVariable<Boolean>, ScalarVariable by v, BooleanAttribute by a {

    override val type: VariableType
        get() = VariableType.BOOLEAN

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
        }.joinToString(", ")

        return "${BooleanVariable::class.java.simpleName}($entries)"

    }

}

/**
 * @author Lars Ivar Hatledal
 */
class StringVariable (
        v: ScalarVariable,
        a: StringAttribute
) : TypedScalarVariable<String>, ScalarVariable by v, StringAttribute by a {

    override val type: VariableType
        get() = VariableType.STRING

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
        }.joinToString(", ")

        return "${StringVariable::class.java.simpleName}($entries)"

    }

}

/**
 * @author Lars Ivar Hatledal
 */
class EnumerationVariable (
        v: ScalarVariable,
        a: EnumerationAttribute
) : BoundedScalarVariable<Int>, ScalarVariable by v, EnumerationAttribute by a {

    override val type: VariableType
        get() = VariableType.ENUMERATION

    override fun toString(): String {

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            start?.also { add("start=$start") }
            min?.also { add("min=$min") }
            max?.also { add("min=$min") }
            quantity?.also { add("quantity=$quantity") }
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            declaredType?.also { add("declaredType=$declaredType") }
        }.joinToString(", ")

        return "${EnumerationVariable::class.java.simpleName}($entries)"

    }

}
