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

package no.mechatronics.sfi.fmi4j.modeldescription.variables

import no.mechatronics.sfi.fmi4j.common.Fmi2Status
import no.mechatronics.sfi.fmi4j.common.FmuRead


/**
 *
 * @author Lars Ivar Hatledal
 */
interface VariableAccessor {

    fun getInteger(name: String): FmuRead<Int>
    fun getInteger(valueReference: Int) : FmuRead<Int>
    fun getInteger(vr: IntArray) : FmuRead<IntArray>
    fun getInteger(vr: IntArray, value: IntArray) : FmuRead<IntArray>

    fun getReal(name: String): FmuRead<Real>
    fun getReal(valueReference: Int) : FmuRead<Real>
    fun getReal(vr: IntArray) : FmuRead<RealArray>
    fun getReal(vr: IntArray, value: RealArray) : FmuRead<RealArray>

    fun getString(name: String): FmuRead<String>
    fun getString(valueReference: Int) : FmuRead<String>
    fun getString(vr: IntArray) : FmuRead<StringArray>
    fun getString(vr: IntArray, value: StringArray) : FmuRead<StringArray>

    fun getBoolean(name: String): FmuRead<Boolean>
    fun getBoolean(valueReference: Int): FmuRead<Boolean>
    fun getBoolean(vr: IntArray): FmuRead<BooleanArray>
    fun getBoolean(vr: IntArray, value: BooleanArray): FmuRead<BooleanArray>
    fun getBoolean(vr: IntArray, value: IntArray): FmuRead<IntArray>

    fun setInteger(name: String, value: Int): Fmi2Status
    fun setInteger(valueReference: Int, value: Int): Fmi2Status
    fun setInteger(vr: IntArray, value: IntArray): Fmi2Status

    fun setReal(name: String, value: Real): Fmi2Status
    fun setReal(valueReference: Int, value: Real): Fmi2Status
    fun setReal(vr: IntArray, value: RealArray): Fmi2Status

    fun setString(name: String, value: String): Fmi2Status
    fun setString(valueReference: Int, value: String): Fmi2Status
    fun setString(vr: IntArray, value: StringArray): Fmi2Status

    fun setBoolean(name: String, value: Boolean): Fmi2Status
    fun setBoolean(valueReference: Int, value: Boolean): Fmi2Status
    fun setBoolean(vr: IntArray, value: IntArray): Fmi2Status
    fun setBoolean(vr: IntArray, value: BooleanArray): Fmi2Status

}
