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

class Fmi2ModelExchangeLibrary(
        libName: String
) : Fmi2Library(libName) {

    private external fun enterEventMode(p: Long, c: Fmi2Component): NativeStatus

    private external fun newDiscreteStates(p: Long, c: Fmi2Component, ev: EventInfo): NativeStatus

    private external fun enterContinuousTimeMode(p: Long, c: Fmi2Component): NativeStatus

    private external fun setContinuousStates(p: Long, c: Fmi2Component, x: DoubleArray): NativeStatus

    private external fun completedIntegratorStep(
            p: Long, c: Fmi2Component, noSetFMUStatePriorToCurrentPoint: Boolean,
            enterEventMode: BooleanByReference, terminateSimulation: BooleanByReference): NativeStatus

    private external fun setTime(p: Long, c: Fmi2Component, time: Double): NativeStatus

    private external fun getDerivatives(p: Long, c: Fmi2Component, derivatives: DoubleArray): NativeStatus

    private external fun getEventIndicators(p: Long, c: Fmi2Component, eventIndicators: DoubleArray): NativeStatus

    private external fun getContinuousStates(p: Long, c: Fmi2Component, x: DoubleArray): NativeStatus

    private external fun getNominalsOfContinuousStates(p: Long, c: Fmi2Component, x_nominals: DoubleArray): NativeStatus

    fun enterEventMode(c: Fmi2Component) = enterEventMode(p, c)

    fun newDiscreteStates(c: Fmi2Component, ev: EventInfo) = newDiscreteStates(p, c, ev)

    fun enterContinuousTimeMode(c: Fmi2Component) = enterContinuousTimeMode(p, c)

    fun setContinuousStates(c: Fmi2Component, x: DoubleArray) = setContinuousStates(p, c, x)

    fun completedIntegratorStep(
            c: Fmi2Component, noSetFMUStatePriorToCurrentPoint: Boolean,
            enterEventMode: BooleanByReference, terminateSimulation: BooleanByReference) = completedIntegratorStep(p, c, noSetFMUStatePriorToCurrentPoint, enterEventMode, terminateSimulation)

    fun setTime(c: Fmi2Component, time: Double) = setTime(p, c, time)

    fun getDerivatives(c: Fmi2Component, derivatives: DoubleArray) = getDerivatives(p, c, derivatives)

    fun getEventIndicators(c: Fmi2Component, eventIndicators: DoubleArray) = getEventIndicators(p, c, eventIndicators)

    fun getContinuousStates(c: Fmi2Component, x: DoubleArray) = getContinuousStates(p, c, x)

    fun getNominalsOfContinuousStates(c: Fmi2Component, x_nominals: DoubleArray) = getNominalsOfContinuousStates(p, c, x_nominals)

}