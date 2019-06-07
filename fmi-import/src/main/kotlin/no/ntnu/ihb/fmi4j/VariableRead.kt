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

package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.modeldescription.Real
import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray


/***
 *
 * Represents the result of reading a variable from an FMU
 *
 * @author Lars Ivar Hatledal
 */
sealed class VariableRead<out E>(

        /**
         * The value returned by the FMU during the call to getXXX
         */
        val value: E,

        /**
         * The status returned by the FMU during the call to getXXX
         */
        val status: FmiStatus

) {

    override fun toString(): String {
        return "VariableRead(value=$value, status=$status)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VariableRead<*>

        if (value != other.value) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + status.hashCode()
        return result
    }

}

/**
 * @author Lars Ivar Hatledal
 */
class IntegerRead(
        value: Int,
        status: FmiStatus
) : VariableRead<Int>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class IntegerArrayRead(
        value: IntArray,
        status: FmiStatus
) : VariableRead<IntArray>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class RealRead(
        value: Real,
        status: FmiStatus
) : VariableRead<Real>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class RealArrayRead(
        value: RealArray,
        status: FmiStatus
) : VariableRead<RealArray>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class StringRead(
        value: String,
        status: FmiStatus
) : VariableRead<String>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class FmuStringArrayRead(
        value: StringArray,
        status: FmiStatus
) : VariableRead<StringArray>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class BooleanRead(
        value: Boolean,
        status: FmiStatus
) : VariableRead<Boolean>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class BooleanArrayRead(
        value: BooleanArray,
        status: FmiStatus
) : VariableRead<BooleanArray>(value, status)


typealias EnumerationRead = IntegerRead
typealias EnumerationArrayRead = IntegerArrayRead
