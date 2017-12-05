/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
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

package no.mechatronics.sfi.fmi4j.jna

import com.sun.jna.Pointer
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.DoubleByReference
import com.sun.jna.ptr.IntByReference
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2StatusKind


interface Fmi2CoSimulationLibrary : Fmi2Library {

    fun fmi2SetRealInputDerivatives(c: Pointer, vr: IntArray, nvr: Int, order: IntArray, value: DoubleArray): Int

    fun fmi2GetRealOutputDerivatives(c: Pointer, vr: IntArray, nvr: Int, order: IntArray, value: DoubleArray): Int

    fun fmi2DoStep(c: Pointer, currentCommunicationPoint: Double, communicationStepSize: Double, noSetFMUStatePriorToCurrent: Byte): Int

    fun fmi2CancelStep(c: Pointer): Int

    fun fmi2GetStatus(c: Pointer, s: Int, value: IntByReference): Int

    fun fmi2GetRealStatus(c: Pointer, s: Int, value: DoubleByReference): Int

    fun fmi2GetIntegerStatus(c: Pointer, s: Int, value: IntByReference): Int

    fun fmi2GetBooleanStatus(c: Pointer, s: Int, value: ByteByReference): Int

    fun fmi2GetStringStatus(c: Pointer, s: Int, value: StringByReference): Int

}