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

package no.ntnu.ihb.fmi.common


/***
 *
 * Represents the result of reading a variable from an FMU
 *
 * @author Lars Ivar Hatledal
 */
sealed class VariableRead<out E>(

        /**
         * The read value
         */
        val value: E,

        /**
         * The read status
         */
        val status: Status

) {

    override fun toString(): String {
        return "VariableRead(value=$value, status=$status)"
    }

}

/**
 * @author Lars Ivar Hatledal
 */
class IntegerRead(
        value: Int,
        status: Status
) : VariableRead<Int>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class IntegerArrayRead(
        value: IntArray,
        status: Status
) : VariableRead<IntArray>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class RealRead(
        value: Real,
        status: Status
) : VariableRead<Real>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class RealArrayRead(
        value: RealArray,
        status: Status
) : VariableRead<RealArray>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class StringRead(
        value: String,
        status: Status
) : VariableRead<String>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class StringArrayRead(
        value: StringArray,
        status: Status
) : VariableRead<StringArray>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class BooleanRead(
        value: Boolean,
        status: Status
) : VariableRead<Boolean>(value, status)

/**
 * @author Lars Ivar Hatledal
 */
class BooleanArrayRead(
        value: BooleanArray,
        status: Status
) : VariableRead<BooleanArray>(value, status)


typealias EnumerationRead = IntegerRead
typealias EnumerationArrayRead = IntegerArrayRead