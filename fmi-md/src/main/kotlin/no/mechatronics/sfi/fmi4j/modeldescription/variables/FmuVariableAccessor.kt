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


/**
 *
 * @author Lars Ivar Hatledal
 */
interface FmuVariableAccessor {

    fun getInteger(name: String): Int
    fun getInteger(valueReference: Int) : Int
    fun getInteger(vr: IntArray) : IntArray
    fun getInteger(vr: IntArray, value: IntArray) : IntArray

    fun getReal(name: String): Real
    fun getReal(valueReference: Int) : Real
    fun getReal(vr: IntArray) : RealArray
    fun getReal(vr: IntArray, value: RealArray) : RealArray

    fun getString(name: String): String
    fun getString(valueReference: Int) : String
    fun getString(vr: IntArray) : StringArray
    fun getString(vr: IntArray, value: StringArray) : StringArray

    fun getBoolean(name: String): Boolean
    fun getBoolean(valueReference: Int): Boolean
    fun getBoolean(vr: IntArray): BooleanArray
    fun getBoolean(vr: IntArray, value: BooleanArray): BooleanArray
    fun getBoolean(vr: IntArray, value: IntArray): IntArray

    fun setInteger(name: String, value: Int)
    fun setInteger(valueReference: Int, value: Int)
    fun setInteger(vr: IntArray, value: IntArray)

    fun setReal(name: String, value: Real)
    fun setReal(valueReference: Int, value: Real)
    fun setReal(vr: IntArray, value: RealArray)

    fun setString(name: String, value: String)
    fun setString(valueReference: Int, value: String)
    fun setString(vr: IntArray, value: StringArray)

    fun setBoolean(name: String, value: Boolean)
    fun setBoolean(valueReference: Int, value: Boolean)
    fun setBoolean(vr: IntArray, value: IntArray)
    fun setBoolean(vr: IntArray, value: BooleanArray)

}
