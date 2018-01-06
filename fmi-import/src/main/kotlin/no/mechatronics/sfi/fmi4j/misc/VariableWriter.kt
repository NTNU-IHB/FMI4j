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

    fun with(data: Int) : Fmi2Status
    fun with(data: Double) : Fmi2Status
    fun with(data: String) : Fmi2Status
    fun with(data: Boolean) : Fmi2Status

}

class VariableWriter internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: Int
): IVariableWriter {

    override fun with(data: Int) = wrapper.setInteger(valueReference, data)
    override fun with(data: Double) = wrapper.setReal(valueReference, data)
    override fun with(data: String) = wrapper.setString(valueReference, data)
    override fun with(data: Boolean) = wrapper.setBoolean(valueReference, data)

}

interface IVariablesWriter {
    fun with(vararg  data: Int) : Fmi2Status
    fun with(vararg data: Double) : Fmi2Status
    fun with(vararg data: String) : Fmi2Status
    fun with(vararg data: Boolean) : Fmi2Status
}

class VariablesWriter internal constructor(
        private val wrapper: Fmi2LibraryWrapper<*>,
        private val valueReference: IntArray
) : IVariablesWriter {

    override fun with(vararg data: Int) =  wrapper.setInteger(valueReference, data)
    override fun with(vararg data: Double) = wrapper.setReal(valueReference, data)
    override fun with(vararg data: String) = wrapper.setString(valueReference, data)
    override fun with(vararg data: Boolean) = wrapper.setBoolean(valueReference, data)


}

