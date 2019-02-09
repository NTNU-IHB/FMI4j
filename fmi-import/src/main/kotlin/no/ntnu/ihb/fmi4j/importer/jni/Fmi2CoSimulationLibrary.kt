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

package no.ntnu.ihb.fmi4j.importer.jni

import no.ntnu.ihb.fmi4j.common.FmiStatus
import no.ntnu.ihb.fmi4j.common.ValueReferences

/**
 * @author Lars Ivar Hatledal
 */
class Fmi2CoSimulationLibrary(
        libName: String
) : Fmi2Library(libName) {

    private external fun step(p: Long, c: Fmi2Component, currentCommunicationPoint: Double,
                              communicationStepSize: Double, noSetFMUStatePriorToCurrentPoint: Boolean): NativeStatus

    private external fun cancelStep(p: Long, c: Fmi2Component): NativeStatus

    private external fun setRealInputDerivatives(p: Long, c: Fmi2Component, vr: ValueReferences, order: IntArray,
                                                 value: DoubleArray): NativeStatus

    private external fun getRealOutputDerivatives(p: Long, c: Fmi2Component, vr: ValueReferences, order: IntArray,
                                                  value: DoubleArray): NativeStatus

    private external fun getStatus(p: Long, c: Fmi2Component, s: Int, value: IntByReference): NativeStatus

    private external fun getIntegerStatus(p: Long, c: Fmi2Component, s: Int, value: IntByReference): NativeStatus

    private external fun getRealStatus(p: Long, c: Fmi2Component, s: Int, value: DoubleByReference): NativeStatus

    private external fun getStringStatus(p: Long, c: Fmi2Component, s: Int, value: StringByReference): NativeStatus

    private external fun getBooleanStatus(p: Long, c: Fmi2Component, s: Int, value: BooleanByReference): NativeStatus

    private external fun getMaxStepSize(p: Long, c: Fmi2Component, stepSize: DoubleByReference): NativeStatus


    fun step(c: Fmi2Component,
             currentCommunicationPoint: Double,
             communicationStepSize: Double,
             noSetFMUStatePriorToCurrentPoint: Boolean): FmiStatus {
        return step(p, c, currentCommunicationPoint, communicationStepSize, noSetFMUStatePriorToCurrentPoint).transform()
    }

    fun cancelStep(c: Fmi2Component): FmiStatus {
        return cancelStep(p, c).transform()
    }

    fun setRealInputDerivatives(c: Fmi2Component,
                                vr: ValueReferences, order: IntArray,
                                value: DoubleArray): FmiStatus {
        return setRealInputDerivatives(p, c, vr, order, value).transform()
    }

    fun getRealOutputDerivatives(c: Fmi2Component,
                                 vr: ValueReferences, order: IntArray,
                                 value: DoubleArray): FmiStatus {
        return getRealOutputDerivatives(p, c, vr, order, value).transform()
    }

    fun getStatus(c: Fmi2Component, s: Int, value: IntByReference): FmiStatus {
        return getStatus(p, c, s, value).transform()
    }

    fun getIntegerStatus(c: Fmi2Component, s: Int, value: IntByReference): FmiStatus {
        return getIntegerStatus(p, c, s, value).transform()
    }

    fun getRealStatus(c: Fmi2Component, s: Int, value: DoubleByReference): FmiStatus {
        return getRealStatus(p, c, s, value).transform()
    }

    fun getStringStatus(c: Fmi2Component, s: Int, value: StringByReference): FmiStatus {
        return getStringStatus(p, c, s, value).transform()
    }

    fun getBooleanStatus(c: Fmi2Component, s: Int, value: BooleanByReference): FmiStatus {
        return getBooleanStatus(p, c, s, value).transform()
    }

    fun getMaxStepSize(c: Fmi2Component, stepSize: DoubleByReference): FmiStatus {
        return getMaxStepSize(p, c, stepSize).transform()
    }

}