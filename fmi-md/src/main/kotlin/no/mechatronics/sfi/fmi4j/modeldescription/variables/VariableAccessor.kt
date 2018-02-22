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

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.common.FmuRead


/**
 *
 * @author Lars Ivar Hatledal
 */
interface VariableAccessor {

    fun readInteger(name: String): FmuRead<Int>
    fun readInteger(valueReference: Int) : FmuRead<Int>
    fun readInteger(vr: IntArray) : FmuRead<IntArray>
    fun readInteger(vr: IntArray, value: IntArray) : FmuRead<IntArray>

    fun readReal(name: String): FmuRead<Real>
    fun readReal(valueReference: Int) : FmuRead<Real>
    fun readReal(vr: IntArray) : FmuRead<RealArray>
    fun readReal(vr: IntArray, value: RealArray) : FmuRead<RealArray>

    fun readString(name: String): FmuRead<String>
    fun readString(valueReference: Int) : FmuRead<String>
    fun readString(vr: IntArray) : FmuRead<StringArray>
    fun readString(vr: IntArray, value: StringArray) : FmuRead<StringArray>

    fun readBoolean(name: String): FmuRead<Boolean>
    fun readBoolean(valueReference: Int): FmuRead<Boolean>
    fun readBoolean(vr: IntArray): FmuRead<BooleanArray>
    fun readBoolean(vr: IntArray, value: BooleanArray): FmuRead<BooleanArray>
    fun readBoolean(vr: IntArray, value: IntArray): FmuRead<IntArray>

    fun writeInteger(name: String, value: Int): FmiStatus
    fun writeInteger(valueReference: Int, value: Int): FmiStatus
    fun writeInteger(vr: IntArray, value: IntArray): FmiStatus

    fun writeReal(name: String, value: Real): FmiStatus
    fun writeReal(valueReference: Int, value: Real): FmiStatus
    fun writeReal(vr: IntArray, value: RealArray): FmiStatus

    fun writeString(name: String, value: String): FmiStatus
    fun writeString(valueReference: Int, value: String): FmiStatus
    fun writeString(vr: IntArray, value: StringArray): FmiStatus

    fun writeBoolean(name: String, value: Boolean): FmiStatus
    fun writeBoolean(valueReference: Int, value: Boolean): FmiStatus
    fun writeBoolean(vr: IntArray, value: IntArray): FmiStatus
    fun writeBoolean(vr: IntArray, value: BooleanArray): FmiStatus

}
