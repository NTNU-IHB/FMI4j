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

package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.common.FmuIntegerArrayRead
import no.mechatronics.sfi.fmi4j.common.FmuIntegerRead
import no.mechatronics.sfi.fmi4j.common.FmuRead
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*

sealed class ScalarVariableVector<out E: TypedScalarVariable<*>> constructor(
        protected val accessor: VariableAccessor,
        protected val variables: List<E>
): Iterable<E> {

    val size: Int = variables.size

    operator fun get(index: Int) = variables[index]

    protected val vr: IntArray by lazy {
        variables.map { it.valueReference }.toIntArray()
    }

    override fun iterator() = variables.iterator()


}

class IntegerVariableVector internal constructor(
        accessor: VariableAccessor,
        variables: List<IntegerVariable>
): ScalarVariableVector<IntegerVariable>(accessor, variables) {

    constructor(accessor: VariableAccessor, vararg variables: IntegerVariable) : this(accessor, variables.asList())

    fun write(value: IntArray): FmiStatus {
        if (value.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeInteger(vr, value)
    }

    fun read() = accessor.readInteger(vr)

}

class RealVariableVector internal constructor(
        accessor: VariableAccessor,
        variables: List<RealVariable>
): ScalarVariableVector<RealVariable>(accessor, variables) {

    constructor(accessor: VariableAccessor, vararg variables: RealVariable) : this(accessor, variables.asList())

    fun write(value: RealArray): FmiStatus {
        if (value.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeReal(vr, value)
    }

    fun read() = accessor.readReal(vr)

}

class StringVariableVector internal constructor(
        accessor: VariableAccessor,
        variables: List<StringVariable>
): ScalarVariableVector<StringVariable>(accessor, variables) {

    constructor(accessor: VariableAccessor, vararg variables: StringVariable) : this(accessor, variables.asList())

    fun write(value: StringArray): FmiStatus {
        if (value.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeString(vr, value)
    }

    fun read() = accessor.readString(vr)

}

class BooleanVariableVector internal constructor(
        accessor: VariableAccessor,
        variables: List<BooleanVariable>
): ScalarVariableVector<BooleanVariable>(accessor, variables) {

    constructor(accessor: VariableAccessor, vararg variables: BooleanVariable) : this(accessor, variables.asList())

    fun write(value: BooleanArray): FmiStatus {
        if (value.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeBoolean(vr, value)
    }

    fun read() = accessor.readBoolean(vr)

}
