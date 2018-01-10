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

interface VariableWriter {

    fun asIntWriter(): IntWriter
    fun asRealWriter(): RealWriter
    fun asStringWriter(): StringWriter
    fun asBooleanWriter(): BooleanWriter

}

interface IntWriter {
    fun write(value: Int)
}

interface RealWriter {
    fun write(value: Double)
}

interface StringWriter {
    fun write(value: String)
}

interface BooleanWriter {
    fun write(value: Boolean)
}

class IntWriterImpl(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
) : IntWriter {

    override fun write(value: Int) {
        wrapper.setInteger(valueReference, value)
    }

}

class RealWriterImpl(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
) : RealWriter {

    override fun write(value: Double) {
        wrapper.setReal(valueReference, value)
    }

}

class StringWriterImpl(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
) : StringWriter {

    override fun write(value: String) {
        wrapper.setString(valueReference, value)
    }

}

class BooleanWriterImpl(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
) : BooleanWriter {

    override fun write(value: Boolean) {
        wrapper.setBoolean(valueReference, value)
    }

}

//class VariableWriter internal constructor(
//        private val wrapper: Fmi2LibraryWrapper<*>,
//        private val valueReference: Int
//): IVariableWriter {
//
//    fun asIntWriter(): IntWriter
//
////    override fun write(data: Int) = wrapper.setInteger(valueReference, data)
////    override fun write(data: Double) = wrapper.setReal(valueReference, data)
////    override fun write(data: String) = wrapper.setString(valueReference, data)
////    override fun write(data: Boolean) = wrapper.setBoolean(valueReference, data)
//
//}

interface IVariablesWriter {
    fun write(vararg  data: Int) : Fmi2Status
    fun write(vararg data: Double) : Fmi2Status
    fun write(vararg data: String) : Fmi2Status
    fun write(vararg data: Boolean) : Fmi2Status
}

class VariablesWriter internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: IntArray
) : IVariablesWriter {

    override fun write(vararg data: Int) =  wrapper.setInteger(valueReference, data)
    override fun write(vararg data: Double) = wrapper.setReal(valueReference, data)
    override fun write(vararg data: String) = wrapper.setString(valueReference, data)
    override fun write(vararg data: Boolean) = wrapper.setBoolean(valueReference, data)


}

