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

package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.common.*
import no.ntnu.ihb.fmi4j.modeldescription.variables.*

/**
 * @author Lars Ivar Hatledal
 */
sealed class ScalarVariableVector<out E : TypedScalarVariable<*>> constructor(
        protected val accessor: FmuVariableAccessor,
        private val variables: List<E>
) {

    val size: Int = variables.size

    operator fun get(index: Int) = variables[index]

    protected val vr: ValueReferences = variables.map {
        it.valueReference
    }.toLongArray()

}

/**
 * @author Lars Ivar Hatledal
 */
class IntegerVariableVector internal constructor(
        accessor: FmuVariableAccessor,
        variables: List<IntegerVariable>
) : ScalarVariableVector<IntegerVariable>(accessor, variables) {

    private val values = IntArray(variables.size)

    fun write(value: IntArray): FmiStatus {
        if (value.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeInteger(vr, value)
    }

    fun read(): FmuIntegerArrayRead = accessor.readInteger(vr, values).let {
        FmuIntegerArrayRead(values, it)
    }

}

/**
 * @author Lars Ivar Hatledal
 */
class RealVariableVector internal constructor(
        accessor: FmuVariableAccessor,
        variables: List<RealVariable>
) : ScalarVariableVector<RealVariable>(accessor, variables) {

    private val values = RealArray(variables.size)

    fun write(values: RealArray): FmiStatus {
        if (values.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeReal(vr, values)
    }

    fun read(): FmuRealArrayRead = accessor.readReal(vr, values).let {
        FmuRealArrayRead(values, it)
    }

}

/**
 * @author Lars Ivar Hatledal
 */
class StringVariableVector internal constructor(
        accessor: FmuVariableAccessor,
        variables: List<StringVariable>
) : ScalarVariableVector<StringVariable>(accessor, variables) {

    private val values = StringArray(variables.size) { "" }

    fun write(values: StringArray): FmiStatus {
        if (values.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeString(vr, values)
    }

    fun read(): FmuStringArrayRead = accessor.readString(vr, values).let {
        FmuStringArrayRead(values, it)
    }

}

/**
 * @author Lars Ivar Hatledal
 */
class BooleanVariableVector internal constructor(
        accessor: FmuVariableAccessor,
        variables: List<BooleanVariable>
) : ScalarVariableVector<BooleanVariable>(accessor, variables) {

    private val values = BooleanArray(variables.size)

    fun write(values: BooleanArray): FmiStatus {
        if (values.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeBoolean(vr, values)
    }

    fun read(): FmuBooleanArrayRead = accessor.readBoolean(vr, values).let {
        FmuBooleanArrayRead(values, it)
    }

}
