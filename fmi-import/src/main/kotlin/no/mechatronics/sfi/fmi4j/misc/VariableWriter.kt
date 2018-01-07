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

interface IVariableWriter {

    fun writeInt(data: Int) : Fmi2Status
    fun writeReal(data: Double) : Fmi2Status
    fun writeString(data: String) : Fmi2Status
    fun writeBoolean(data: Boolean) : Fmi2Status

}

class IntWriter(
        wrapper: Fmi2LibraryWrapper<*>,
        valueReference: Int
) : VariableAccessor(wrapper, valueReference) {

    fun write(value: Int) = wrapper.setInteger(valueReference, value)

}

class RealWriter(
        wrapper: Fmi2LibraryWrapper<*>,
        valueReference: Int
) : VariableAccessor(wrapper, valueReference) {

    fun write(value: Double) = wrapper.setReal(valueReference, value)

}

class StringWriter(
        wrapper: Fmi2LibraryWrapper<*>,
        valueReference: Int
) : VariableAccessor(wrapper, valueReference) {

    fun write(value: String) = wrapper.setString(valueReference, value)

}

class BooleanWriter(
        wrapper: Fmi2LibraryWrapper<*>,
        valueReference: Int
) : VariableAccessor(wrapper, valueReference) {

    fun write(value: Boolean) = wrapper.setBoolean(valueReference, value)

}

class VariableWriter internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
): IVariableWriter {

    override fun writeInt(data: Int) = wrapper.setInteger(valueReference, data)
    override fun writeReal(data: Double) = wrapper.setReal(valueReference, data)
    override fun writeString(data: String) = wrapper.setString(valueReference, data)
    override fun writeBoolean(data: Boolean) = wrapper.setBoolean(valueReference, data)

}

interface IVariablesWriter {
    fun writeInt(vararg  data: Int) : Fmi2Status
    fun writeReal(vararg data: Double) : Fmi2Status
    fun writeString(vararg data: String) : Fmi2Status
    fun writeBoolean(vararg data: Boolean) : Fmi2Status
}

class VariablesWriter internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: IntArray
) : IVariablesWriter {

    override fun writeInt(vararg data: Int) =  wrapper.setInteger(valueReference, data)
    override fun writeReal(vararg data: Double) = wrapper.setReal(valueReference, data)
    override fun writeString(vararg data: String) = wrapper.setString(valueReference, data)
    override fun writeBoolean(vararg data: Boolean) = wrapper.setBoolean(valueReference, data)


}

