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

import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper

/**
 * @author Lars Ivar Hatledal
 */
class FmuVariableAccessorImpl(
        private val modelVariables: ModelVariables,
        private val wrapper: Fmi2LibraryWrapper<*>
): FmuVariableAccessor {

    private val map by lazy {
        mutableMapOf<String, Int>()
    }

    private fun process(name: String): Int {
        if (name !in map) {
            map[name]= modelVariables.getValueReference(name)
        }
        return map[name]!!
    }

    override fun getBoolean(name: String): Boolean  = getBoolean(process(name))
    override fun getBoolean(valueReference: Int) = wrapper.getBoolean(valueReference)
    override fun getBoolean(vr: IntArray) = wrapper.getBoolean(vr)
    override fun getBoolean(vr: IntArray, value: BooleanArray) = wrapper.getBoolean(vr, value)
    override fun getBoolean(vr: IntArray, value: IntArray) = wrapper.getBoolean(vr, value)

    override fun getInteger(name: String) = getInteger(process(name))
    override fun getInteger(valueReference: Int) = wrapper.getInteger(valueReference)
    override fun getInteger(vr: IntArray) = wrapper.getInteger(vr)
    override fun getInteger(vr: IntArray, value: IntArray) = wrapper.getInteger(vr, value)

    override fun getReal(name: String) = getReal(process(name))
    override fun getReal(valueReference: Int) = wrapper.getReal(valueReference)
    override fun getReal(vr: IntArray) = wrapper.getReal(vr)
    override fun getReal(vr: IntArray, value: RealArray) = wrapper.getReal(vr, value)

    override fun getString(name: String) = getString(process(name))
    override fun getString(valueReference: Int) = wrapper.getString(valueReference)
    override fun getString(vr: IntArray) = wrapper.getString(vr)
    override fun getString(vr: IntArray, value: StringArray) = wrapper.getString(vr, value)


    override fun setBoolean(name: String, value: Boolean) {
        wrapper.setBoolean(process(name), value)
    }

    override fun setBoolean(valueReference: Int, value: Boolean) {
        wrapper.setBoolean(valueReference, value)
    }

    override fun setBoolean(vr: IntArray, value: BooleanArray) {
        wrapper.setBoolean(vr, value)
    }

    override fun setBoolean(vr: IntArray, value: IntArray) {
        wrapper.setBoolean(vr, value)
    }

    override fun setInteger(name: String, value: Int) {
        wrapper.setInteger(process(name), value)
    }

    override fun setInteger(valueReference: Int, value: Int) {
        wrapper.setInteger(valueReference, value)
    }

    override fun setInteger(vr: IntArray, value: IntArray) {
        wrapper.setInteger(vr, value)
    }

    override fun setReal(valueReference: Int, value: Real) {
        wrapper.setReal(valueReference, value)
    }

    override fun setReal(name: String, value: Real) {
        wrapper.setReal(process(name), value)
    }

    override fun setReal(vr: IntArray, value: DoubleArray) {
        wrapper.setReal(vr, value)
    }

    override fun setString(name: String, value: String) {
        wrapper.setString(process(name), value)
    }

    override fun setString(valueReference: Int, value: String) {
        wrapper.setString(valueReference, value)
    }

    override fun setString(vr: IntArray, value: StringArray) {
        wrapper.setString(vr, value)
    }

}