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

package no.ntnu.ihb.fmi4j.common


/**
 * @author Lars Ivar Hatledal
 */
interface FmuVariableReader {

    fun readInteger(vr: ValueReferences, ref: IntArray): FmiStatus
    fun readReal(vr: ValueReferences, ref: RealArray): FmiStatus
    fun readString(vr: ValueReferences, ref: StringArray): FmiStatus
    fun readBoolean(vr: ValueReferences, ref: BooleanArray): FmiStatus

}


/**
 * @author Lars Ivar Hatledal
 */
interface FmuVariableWriter {

    fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus
    fun writeReal(vr: ValueReferences, value: RealArray): FmiStatus
    fun writeString(vr: ValueReferences, value: StringArray): FmiStatus
    fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus

}


/**
 * @author Lars Ivar Hatledal
 */
interface FmuVariableAccessor: FmuVariableReader, FmuVariableWriter
