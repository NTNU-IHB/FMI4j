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

package no.mechatronics.sfi.fmi4j.importer.jni

class Fmi2CoSimulationLibrary(
        libName: String
) : Fmi2Library(libName) {

    external fun step(c: Fmi2Component, currentCommunicationPoint: Double,
                      communicationStepSize: Double, noSetFMUStatePriorToCurrentPoint: Boolean): NativeStatus

    external fun cancelStep(c: Fmi2Component): NativeStatus

    external fun setRealInputDerivatives(c: Fmi2Component, vr: IntArray, order: IntArray,
                                         value: DoubleArray): NativeStatus

    external fun getRealOutputDerivatives(c: Fmi2Component, vr: IntArray, order: IntArray,
                                          value: DoubleArray): NativeStatus

    external fun getStatus(c: Fmi2Component, s: Int, value: IntByReference): NativeStatus

    external fun getIntegerStatus(c: Fmi2Component, s: Int, value: IntByReference): NativeStatus

    external fun getRealStatus(c: Fmi2Component, s: Int, value: DoubleByReference): NativeStatus

    external fun getStringStatus(c: Fmi2Component, s: Int, value: StringByReference): NativeStatus

    external fun getBooleanStatus(c: Fmi2Component, s: Int, value: BooleanByReference): NativeStatus

    external fun getMaxStepSize(c: Fmi2Component, stepSize: DoubleByReference): NativeStatus
}