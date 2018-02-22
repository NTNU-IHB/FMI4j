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

package no.mechatronics.sfi.fmi4j.proxy.v2.cs

import com.sun.jna.Pointer
import com.sun.jna.ptr.DoubleByReference
import com.sun.jna.ptr.IntByReference
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.proxy.v2.enums.Fmi2StatusKind
import no.mechatronics.sfi.fmi4j.misc.*
import no.mechatronics.sfi.fmi4j.proxy.v2.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.v2.Fmi2LibraryWrapper

/**
 *
 * @author Lars Ivar Hatledal
 */
interface Fmi2CoSimulationLibrary : Fmi2Library {

    fun fmi2SetRealInputDerivatives(c: Pointer, vr: IntArray, nvr: Int, order: IntArray, value: DoubleArray): Int

    fun fmi2GetRealOutputDerivatives(c: Pointer, vr: IntArray, nvr: Int, order: IntArray, value: DoubleArray): Int

    fun fmi2DoStep(c: Pointer, currentCommunicationPoint: Double, communicationStepSize: Double, noSetFMUStatePriorToCurrent: Int): Int

    fun fmi2CancelStep(c: Pointer): Int

    fun fmi2GetStatus(c: Pointer, s: Int, value: IntByReference): Int

    fun fmi2GetRealStatus(c: Pointer, s: Int, value: DoubleByReference): Int

    fun fmi2GetIntegerStatus(c: Pointer, s: Int, value: IntByReference): Int

    fun fmi2GetBooleanStatus(c: Pointer, s: Int, value: IntByReference): Int

    fun fmi2GetStringStatus(c: Pointer, s: Int, value: StringByReference): Int

    /**
     * Extension method
     */
    fun fmi2GetMaxStepsize(c: Pointer, value: DoubleByReference) : Int

}



class CoSimulationLibraryWrapper(
        c: Pointer,
        library: LibraryProvider<Fmi2CoSimulationLibrary>
) : Fmi2LibraryWrapper<Fmi2CoSimulationLibrary>(c, library) {

    private val intByReference: IntByReference by lazy { 
        IntByReference()
    }

    private val realByReference: DoubleByReference by lazy {
        DoubleByReference()
    }

    private val stringByReference: StringByReference by lazy {
        StringByReference()
    }

    fun getMaxStepsize() : Double {
        return realByReference.let {
            updateStatus(library.fmi2GetMaxStepsize(c, it))
            it.value
        }
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2SetRealInputDerivatives
     */
    fun setRealInputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray) : FmiStatus {
        return updateStatus((library.fmi2SetRealInputDerivatives(c, vr, vr.size, order, value)))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetRealOutputDerivatives
     */
    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray) : FmiStatus {
        return updateStatus((library.fmi2GetRealOutputDerivatives(c, vr, vr.size, order, value)))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2DoStep
     */
    fun doStep(t: Double, dt: Double, noSetFMUStatePriorToCurrent: Boolean) : FmiStatus {
        return updateStatus((library.fmi2DoStep(c, t, dt, FmiBoolean.convert(noSetFMUStatePriorToCurrent))))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2CancelStep
     */
    fun cancelStep() : FmiStatus {
        return (updateStatus((library.fmi2CancelStep(c))))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetStatus
     */
    fun getStatus(s: Fmi2StatusKind): FmiStatus {
        return intByReference.let {
            updateStatus((library.fmi2GetIntegerStatus(c, s.code, it)))
            FmiStatus.valueOf(it.value)
        }
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetRealStatus
     */
    fun getRealStatus(s: Fmi2StatusKind): Double {
        return realByReference.let {
            updateStatus((library.fmi2GetRealStatus(c, s.code, it)))
            it.value
        }

    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetIntegerStatus
     */
    fun getIntegerStatus(s: Fmi2StatusKind): Int {
       return intByReference.let {
           updateStatus((library.fmi2GetIntegerStatus(c, s.code, it)))
           it.value
       }

    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetBooleanStatus
     */
    fun getBooleanStatus(s: Fmi2StatusKind): Boolean {
        return intByReference.let {
            updateStatus((library.fmi2GetBooleanStatus(c, s.code, it)))
            FmiBoolean.convert(it.value)
        }
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetStringStatus
     */
    fun getStringStatus(s: Fmi2StatusKind): String {
        return stringByReference.let {
            updateStatus((library.fmi2GetStringStatus(c, s.code, it)))
            it.value
        }


    }

}
