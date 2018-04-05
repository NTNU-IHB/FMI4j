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

package no.mechatronics.sfi.fmi4j.fmu.misc

import no.mechatronics.sfi.fmi4j.common.*
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*

/**
 * @author Lars Ivar Hatledal
 */
sealed class ScalarVariableVector<out E: TypedScalarVariable<*>> constructor(
        protected val accessor: VariableAccessor,
        protected val variables: List<E>
): Iterable<E> {

    val size: Int = variables.size

    operator fun get(index: Int) = variables[index]

    protected val vr: IntArray by lazy {
        variables.map { it.valueReference }.toIntArray()
    }

    override fun iterator(): Iterator<E>
            = variables.iterator()

}

/**
 * @author Lars Ivar Hatledal
 */
class IntegerVariableVector internal constructor(
        accessor: VariableAccessor,
        variables: List<IntegerVariable>
): ScalarVariableVector<IntegerVariable>(accessor, variables) {

    fun write(value: IntArray): FmiStatus {
        if (value.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeInteger(vr, value)
    }

    fun read(): FmuIntegerArrayRead
            = accessor.readInteger(vr)

}

/**
 * @author Lars Ivar Hatledal
 */
class RealVariableVector internal constructor(
        accessor: VariableAccessor,
        variables: List<RealVariable>
): ScalarVariableVector<RealVariable>(accessor, variables) {

    fun write(value: RealArray): FmiStatus {
        if (value.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeReal(vr, value)
    }

    fun read(): FmuRealArrayRead
            = accessor.readReal(vr)

}

/**
 * @author Lars Ivar Hatledal
 */
class StringVariableVector internal constructor(
        accessor: VariableAccessor,
        variables: List<StringVariable>
): ScalarVariableVector<StringVariable>(accessor, variables) {

    fun write(value: StringArray): FmiStatus {
        if (value.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeString(vr, value)
    }

    fun read(): FmuStringArrayRead
            = accessor.readString(vr)

}

/**
 * @author Lars Ivar Hatledal
 */
class BooleanVariableVector internal constructor(
        accessor: VariableAccessor,
        variables: List<BooleanVariable>
): ScalarVariableVector<BooleanVariable>(accessor, variables) {

    fun write(value: BooleanArray): FmiStatus {
        if (value.size != vr.size) {
            throw IllegalArgumentException("value.size != vector.size")
        }
        return accessor.writeBoolean(vr, value)
    }

    fun read(): FmuBooleanArrayRead
            = accessor.readBoolean(vr)

}
