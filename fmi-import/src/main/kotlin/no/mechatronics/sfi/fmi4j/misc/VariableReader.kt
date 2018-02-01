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

package no.mechatronics.sfi.fmi4j.misc

import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status

/**
 *
 * @author Lars Ivar Hatledal
 */
interface VariableReader {
    fun asIntReader(): IntReader
    fun asRealReader(): RealReader
    fun asStringReader(): StringReader
    fun asBooleanReader(): BooleanReader
}

/**
 *
 * @author Lars Ivar Hatledal
 */
interface Reader<E> {
    fun read() : E
}

interface IntReader: Reader<Int>
interface RealReader: Reader<Double>
interface StringReader: Reader<String>
interface BooleanReader: Reader<Boolean>

/**
 *
 * @author Lars Ivar Hatledal
 */
class IntReaderImpl internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
) : IntReader {

    override fun read() = wrapper.getInteger(valueReference)

}

/**
 *
 * @author Lars Ivar Hatledal
 */
class RealReaderImpl internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
) : RealReader {

    override fun read() = wrapper.getReal(valueReference)

}

/**
 *
 * @author Lars Ivar Hatledal
 */
class StringReaderImpl internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
) : StringReader {

    override fun read() = wrapper.getString(valueReference)

}

/**
 *
 * @author Lars Ivar Hatledal
 */
class BooleanReaderImpl internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
) : BooleanReader {

    override fun read() = wrapper.getBoolean(valueReference)

}

/**
 *
 * @author Lars Ivar Hatledal
 */
interface VariablesReader {
    fun readInteger() : IntArray
    fun readReal() : DoubleArray
    fun readString() : Array<String>
    fun readBoolean() : BooleanArray
}

/**
 *
 * @author Lars Ivar Hatledal
 */
class VariablesReaderImpl internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: IntArray
): VariablesReader {

    override fun readInteger() : IntArray = wrapper.getInteger(valueReference)
    override fun readReal() : DoubleArray = wrapper.getReal(valueReference)
    override fun readString() : Array<String> = wrapper.getString(valueReference)
    override fun readBoolean() : BooleanArray = wrapper.getBoolean(valueReference)

}