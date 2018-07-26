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

package no.mechatronics.sfi.fmi4j.common


/**
 *
 * @author Lars Ivar Hatledal
 */
interface FmuVariableAccessor {

    fun readInteger(name: String): FmuIntegerRead
    fun readInteger(vr: ValueReference): FmuIntegerRead
    fun readInteger(vr: ValueReferences): FmuIntegerArrayRead
    fun readInteger(vr: ValueReferences, value: IntArray): FmuIntegerArrayRead

    fun readReal(name: String): FmuRealRead
    fun readReal(vr: ValueReference): FmuRealRead
    fun readReal(vr: ValueReferences): FmuRealArrayRead
    fun readReal(vr: ValueReferences, value: RealArray): FmuRealArrayRead

    fun readString(name: String): FmuStringRead
    fun readString(vr: ValueReference): FmuStringRead
    fun readString(vr: ValueReferences): FmuStringArrayRead
    fun readString(vr: ValueReferences, value: StringArray): FmuStringArrayRead

    fun readBoolean(name: String): FmuBooleanRead
    fun readBoolean(vr: ValueReference): FmuBooleanRead
    fun readBoolean(vr: ValueReferences): FmuBooleanArrayRead
    fun readBoolean(vr: ValueReferences, value: BooleanArray): FmuBooleanArrayRead
    fun readBoolean(vr: ValueReferences, value: IntArray): FmuIntegerArrayRead

    fun writeInteger(name: String, value: Int): FmiStatus
    fun writeInteger(vr: ValueReference, value: Int): FmiStatus
    fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus

    fun writeReal(name: String, value: Real): FmiStatus
    fun writeReal(vr: ValueReference, value: Real): FmiStatus
    fun writeReal(vr: ValueReferences, value: RealArray): FmiStatus

    fun writeString(name: String, value: String): FmiStatus
    fun writeString(vr: ValueReference, value: String): FmiStatus
    fun writeString(vr: ValueReferences, value: StringArray): FmiStatus

    fun writeBoolean(name: String, value: Boolean): FmiStatus
    fun writeBoolean(vr: ValueReference, value: Boolean): FmiStatus
    fun writeBoolean(vr: ValueReferences, value: IntArray): FmiStatus
    fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus

}
