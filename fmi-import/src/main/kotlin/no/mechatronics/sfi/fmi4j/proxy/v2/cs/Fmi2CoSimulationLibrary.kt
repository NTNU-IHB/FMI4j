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
import no.mechatronics.sfi.fmi4j.misc.*
import no.mechatronics.sfi.fmi4j.proxy.v2.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.v2.Fmi2LibraryWrapper

/**
 *
 * @author Lars Ivar Hatledal
 */
interface Fmi2CoSimulationLibrary : Fmi2Library {

    /**
     * Sets the n-th time derivative of real input variables. Argument “vr” is a vector of value
     * references that define the variables whose derivatives shall be set. The array “order”
     * contains the orders of the respective derivative (1 means the first derivative, 0 is not
     * allowed). Argument “value” is a vector with the values of the derivatives. “nvr” is the
     * dimension of the vectors.
     * Restrictions on using the function are the same as for the fmi2SetReal function.
     */
    fun fmi2SetRealInputDerivatives(c: Pointer, vr: IntArray, nvr: Int, order: IntArray, value: DoubleArray): Int

    /**
     * Retrieves the n-th derivative of output values. Argument “vr” is a vector of “nvr” value
     * references that define the variables whose derivatives shall be retrieved. The array “order”
     * contains the order of the respective derivative (1 means the first derivative, 0 is not allowed).
     * Argument “value” is a vector with the actual values of the derivatives.
     * Restrictions on using the function are the same as for the fmi2GetReal function.
     */
    fun fmi2GetRealOutputDerivatives(c: Pointer, vr: IntArray, nvr: Int, order: IntArray, value: DoubleArray): Int

    fun fmi2DoStep(c: Pointer, currentCommunicationPoint: Double, communicationStepSize: Double, noSetFMUStatePriorToCurrent: Int): Int

    /**
     * Can be called if fmi2DoStep returned fmi2Pending in order to stop the current
     * asynchronous execution. The master calls this function if for example the co-simulation run is
     * stopped by the user or one of the slaves. Afterwards it is only allowed to call fmi2Reset or
     * fmi2FreeInstance.
     */
    fun fmi2CancelStep(c: Pointer): Int

    /**
     * Informs the master about the actual status of the simulation run. Which status information is
     * to be returned is specified by the argument fmi2StatusKind
     */
    fun fmi2GetStatus(c: Pointer, s: Int, value: IntByReference): Int

    /**
     * Informs the master about the actual status of the simulation run. Which status information is
     * to be returned is specified by the argument fmi2StatusKind
     */
    fun fmi2GetRealStatus(c: Pointer, s: Int, value: DoubleByReference): Int

    /**
     * Informs the master about the actual status of the simulation run. Which status information is
     * to be returned is specified by the argument fmi2StatusKind
     */
    fun fmi2GetIntegerStatus(c: Pointer, s: Int, value: IntByReference): Int

    /**
     * Informs the master about the actual status of the simulation run. Which status information is
     * to be returned is specified by the argument fmi2StatusKind
     */
    fun fmi2GetBooleanStatus(c: Pointer, s: Int, value: IntByReference): Int

    /**
     * Informs the master about the actual status of the simulation run. Which status information is
     * to be returned is specified by the argument fmi2StatusKind
     */
    fun fmi2GetStringStatus(c: Pointer, s: Int, value: StringByReference): Int

    /**
     * Extension method
     */
    fun fmi2GetMaxStepsize(c: Pointer, value: DoubleByReference) : Int

}


/**
 * @author Lars Ivar Hatledal
 */
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
        return updateStatus(library.fmi2SetRealInputDerivatives(c, vr, vr.size, order, value))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetRealOutputDerivatives
     */
    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray) : FmiStatus {
        return updateStatus(library.fmi2GetRealOutputDerivatives(c, vr, vr.size, order, value))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2DoStep
     */
    fun doStep(t: Double, dt: Double, noSetFMUStatePriorToCurrent: Boolean) : FmiStatus {
        return updateStatus(library.fmi2DoStep(c, t, dt, FmiBoolean.convert(noSetFMUStatePriorToCurrent)))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2CancelStep
     */
    fun cancelStep() : FmiStatus {
        return (updateStatus(library.fmi2CancelStep(c)))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetStatus
     */
    fun getStatus(s: Fmi2StatusKind): FmiStatus {
        return intByReference.let {
            updateStatus(library.fmi2GetIntegerStatus(c, s.code, it))
            FmiStatus.valueOf(it.value)
        }
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetRealStatus
     */
    fun getRealStatus(s: Fmi2StatusKind): Double {
        return realByReference.let {
            updateStatus(library.fmi2GetRealStatus(c, s.code, it))
            it.value
        }

    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetIntegerStatus
     */
    fun getIntegerStatus(s: Fmi2StatusKind): Int {
       return intByReference.let {
           updateStatus(library.fmi2GetIntegerStatus(c, s.code, it))
           it.value
       }

    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetBooleanStatus
     */
    fun getBooleanStatus(s: Fmi2StatusKind): Boolean {
        return intByReference.let {
            updateStatus(library.fmi2GetBooleanStatus(c, s.code, it))
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
