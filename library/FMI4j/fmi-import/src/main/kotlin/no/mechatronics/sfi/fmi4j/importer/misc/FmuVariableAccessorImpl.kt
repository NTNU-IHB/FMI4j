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

package no.mechatronics.sfi.fmi4j.importer.misc

import no.mechatronics.sfi.fmi4j.common.*
import no.mechatronics.sfi.fmi4j.importer.FmiLibraryWrapper
import no.mechatronics.sfi.fmi4j.modeldescription.variables.ModelVariables

/**
 * @author Lars Ivar Hatledal
 */
class FmuVariableAccessorImpl(
        private val wrapper: FmiLibraryWrapper<*>,
        private val modelVariables: ModelVariables
) : FmuVariableAccessor {

    //read
    override fun readInteger(name: String) = readInteger(process(name))

    override fun readInteger(vr: ValueReference) = wrapper.getInteger(vr)
    override fun readInteger(vr: ValueReferences) = wrapper.getInteger(vr)
    override fun readInteger(vr: ValueReferences, value: IntArray) = wrapper.getInteger(vr, value)

    override fun readReal(name: String) = readReal(process(name))
    override fun readReal(vr: ValueReference) = wrapper.getReal(vr)
    override fun readReal(vr: ValueReferences) = wrapper.getReal(vr)
    override fun readReal(vr: ValueReferences, value: RealArray) = wrapper.getReal(vr, value)

    override fun readString(name: String) = readString(process(name))
    override fun readString(vr: ValueReference) = wrapper.getString(vr)
    override fun readString(vr: ValueReferences) = wrapper.getString(vr)
    override fun readString(vr: ValueReferences, value: StringArray) = wrapper.getString(vr, value)

    override fun readBoolean(name: String) = readBoolean(process(name))
    override fun readBoolean(vr: ValueReference) = wrapper.getBoolean(vr)
    override fun readBoolean(vr: ValueReferences) = wrapper.getBoolean(vr)
    override fun readBoolean(vr: ValueReferences, value: BooleanArray) = wrapper.getBoolean(vr, value)

    //write
    override fun writeInteger(name: String, value: Int) = wrapper.setInteger(process(name), value)
    override fun writeInteger(vr: ValueReference, value: Int) = wrapper.setInteger(vr, value)
    override fun writeInteger(vr: ValueReferences, value: IntArray) = wrapper.setInteger(vr, value)

    override fun writeReal(vr: Int, value: Real) = wrapper.setReal(vr, value)
    override fun writeReal(name: String, value: Real) = wrapper.setReal(process(name), value)
    override fun writeReal(vr: ValueReferences, value: DoubleArray) = wrapper.setReal(vr, value)

    override fun writeString(name: String, value: String) = wrapper.setString(process(name), value)
    override fun writeString(vr: ValueReference, value: String) = wrapper.setString(vr, value)
    override fun writeString(vr: ValueReferences, value: StringArray) = wrapper.setString(vr, value)

    override fun writeBoolean(name: String, value: Boolean) = wrapper.setBoolean(process(name), value)
    override fun writeBoolean(vr: ValueReference, value: Boolean) = wrapper.setBoolean(vr, value)
    override fun writeBoolean(vr: ValueReferences, value: BooleanArray) = wrapper.setBoolean(vr, value)

    private fun process(name: String): Int {
        return map.getOrPut(name) {
            modelVariables.getValueReference(name)
        }
    }

    private companion object {
        private val map = mutableMapOf<String, Int>()
    }

}
