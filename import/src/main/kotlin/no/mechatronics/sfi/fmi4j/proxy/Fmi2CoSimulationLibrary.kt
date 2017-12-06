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

package no.mechatronics.sfi.fmi4j.proxy

import com.sun.jna.Pointer
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.DoubleByReference
import com.sun.jna.ptr.IntByReference
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2StatusKind
import no.mechatronics.sfi.fmi4j.misc.*


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


class Fmi2CoSimulationWrapper(
        libraryFolder: String,
        libraryName: String
) : Fmi2Wrapper<Fmi2CoSimulationLibrary>(libraryFolder, libraryName, Fmi2CoSimulationLibrary::class.java) {

    /**
     * @see Fmi2CoSimulationlibrary.fmi2SetRealInputDerivatives
     */
    fun setRealInputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray) : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2SetRealInputDerivatives)
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetRealInputDerivatives(c, vr, vr.size, order, value)))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetRealOutputDerivatives
     */
    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray) : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2GetRealOutputDerivatives)
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetRealOutputDerivatives(c, vr, vr.size, order, value)))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2DoStep
     */
    fun doStep(t: Double, dt: Double, noSetFMUStatePriorToCurrent: Boolean) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2DoStep(c, t, dt, convert(noSetFMUStatePriorToCurrent))))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2CancelStep
     */
    fun cancelStep() : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2CancelStep)
        return updateState(updateStatus(Fmi2Status.valueOf(library.fmi2CancelStep(c))), FmiState.STEP_CANCELED)
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetStatus
     */
    fun getStatus(s: Fmi2StatusKind): Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2GetStatus)
        val i = IntByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetIntegerStatus(c, s.code, i)))
        return Fmi2Status.valueOf(i.value)
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetRealStatus
     */
    fun getRealStatus(s: Fmi2StatusKind): Double {
        state.isCallLegalDuringState(FmiMethod.fmi2GetRealStatus)
        val d = DoubleByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetRealStatus(c, s.code, d)))
        return d.value
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetIntegerStatus
     */
    fun getIntegerStatus(s: Fmi2StatusKind): Int {
        state.isCallLegalDuringState(FmiMethod.fmi2GetIntegerStatus)
        val i = IntByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetIntegerStatus(c, s.code, i)))
        return i.value
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetBooleanStatus
     */
    fun getBooleanStatus(s: Fmi2StatusKind): Boolean {
        state.isCallLegalDuringState(FmiMethod.fmi2GetBooleanStatus)
        val b = ByteByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetBooleanStatus(c, s.code, b)))
        return convert(b.value)
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetStringStatus
     */
    fun getStringStatus(s: Fmi2StatusKind): String {
        state.isCallLegalDuringState(FmiMethod.fmi2GetStringStatus)
        val str = StringByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetStringStatus(c, s.code, str)))
        return str.value
    }

}
