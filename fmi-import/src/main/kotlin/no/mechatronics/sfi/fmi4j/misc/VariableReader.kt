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


interface IVariableReader {
    fun asInteger() : Int
    fun asReal() : Double
    fun asString() : String
    fun asBoolean() : Boolean
}

class IntReader internal constructor(
        wrapper: Fmi2LibraryWrapper<*>,
        valueReference: Int
) : VariableAccessor(wrapper, valueReference) {

    fun read() = wrapper.getInteger(valueReference)

}

class RealReader internal constructor(
        wrapper: Fmi2LibraryWrapper<*>,
        valueReference: Int
) : VariableAccessor(wrapper, valueReference) {

    fun read() = wrapper.getReal(valueReference)

}

class StringReader internal constructor(
        wrapper: Fmi2LibraryWrapper<*>,
        valueReference: Int
) : VariableAccessor(wrapper, valueReference) {

    fun read() = wrapper.getString(valueReference)

}

class BooleanReader internal constructor(
        wrapper: Fmi2LibraryWrapper<*>,
        valueReference: Int
) : VariableAccessor(wrapper, valueReference) {

    fun read() = wrapper.getBoolean(valueReference)

}

class VariableReader internal constructor(
        wrapper: Fmi2LibraryWrapper<*>,
        valueReference: Int
) : VariableAccessor(wrapper, valueReference), IVariableReader {

    override fun asInteger() : Int = wrapper.getInteger(valueReference)
    override fun asReal() : Double = wrapper.getReal(valueReference)
    override fun asString() : String = wrapper.getString(valueReference)
    override fun asBoolean() : Boolean = wrapper.getBoolean(valueReference)

}

interface IVariablesReader {
    fun asInteger() : IntArray
    fun asReal() : DoubleArray
    fun asString() : Array<String>
    fun asBoolean() : BooleanArray
}

class VariablesReader internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: IntArray
): IVariablesReader {

    override fun asInteger() : IntArray = wrapper.getInteger(valueReference)
    override fun asReal() : DoubleArray = wrapper.getReal(valueReference)
    override fun asString() : Array<String> = wrapper.getString(valueReference)
    override fun asBoolean() : BooleanArray = wrapper.getBoolean(valueReference)

}