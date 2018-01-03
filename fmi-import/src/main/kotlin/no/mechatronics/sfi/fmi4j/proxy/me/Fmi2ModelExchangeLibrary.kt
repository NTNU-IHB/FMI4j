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

package no.mechatronics.sfi.fmi4j.proxy.me

import com.sun.jna.Pointer
import com.sun.jna.ptr.ByteByReference
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.proxy.structs.Fmi2EventInfo
import no.mechatronics.sfi.fmi4j.misc.LibraryProvider
import no.mechatronics.sfi.fmi4j.misc.convert
import no.mechatronics.sfi.fmi4j.proxy.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper


interface Fmi2ModelExchangeLibrary : Fmi2Library {

    fun fmi2SetTime(c: Pointer, time: Double): Int

    fun fmi2SetContinuousStates(c: Pointer, x: DoubleArray, nx: Int): Int

    fun fmi2EnterEventMode(c: Pointer): Int

    fun fmi2EnterContinuousTimeMode(c: Pointer): Int

    fun fmi2NewDiscreteStates(c: Pointer, eventInfo: Fmi2EventInfo): Int

    fun fmi2CompletedIntegratorStep(c: Pointer,
                                    noSetFMUStatePriorToCurrentPoint: Byte,
                                    enterEventMode: ByteByReference,
                                    terminateSimulation: ByteByReference): Int

    fun fmi2GetDerivatives(c: Pointer, derivatives: DoubleArray, nx: Int): Int

    fun fmi2GetEventIndicators(c: Pointer, eventIndicators: DoubleArray, ni: Int): Int

    fun fmi2GetContinuousStates(c: Pointer, x: DoubleArray, nx: Int): Int

    fun fmi2GetNominalsOfContinuousStates(c: Pointer, x_nominal: DoubleArray, nx: Int): Int

}


/**
 *
 * @author laht
 */
class ModelExchangeLibraryWrapper(
        c: Pointer,
        library: LibraryProvider<Fmi2ModelExchangeLibrary>
) : Fmi2LibraryWrapper<Fmi2ModelExchangeLibrary>(c, library) {


    private val enterEventMode: ByteByReference = ByteByReference()
    private val terminateSimulation: ByteByReference = ByteByReference()


    /**
     * Set a new time instant and re-initialize caching of variables that depend
     * on time, provided the newly provided time value is different to the
     * previously set time value (variables that depend solely on constants or
     * parameters need not to be newly computed in the sequel, but the
     * previously computed values can be reused).
     *
     * @param time
     */
    fun setTime(time: Double) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetTime(c, time)))
    }

    /**
     * Set a new (continuous) state vector and re-initialize caching of
     * variables that depend on the states. Argument nx is the length of vector
     * x and is provided for checking purposes (variables that depend solely on
     * constants, parameters, time, and inputs do not need to be newly computed
     * in the sequel, but the previously computed values can be reused). Note,
     * the continuous states might also be changed in Event Mode. Note:
     * fmi2Status = fmi2Discard is possible.
     *
     * @param x
     */
    fun setContinuousStates(x: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetContinuousStates(c, x, x.size)))
    }

    /**
     * The model enters Event Mode from the Continuous-Time Mode and
     * discrete-time equations may become active (and relations are not
     * “frozen”).
     */
    fun enterEventMode() : Fmi2Status {
        return (updateStatus(Fmi2Status.valueOf(library.fmi2EnterEventMode(c))))
    }

    /**
     * The model enters Continuous-Time Mode and all discrete-time equations
     * become inactive and all relations are “frozen”. This function has to be
     * called when changing from Event Mode (after the global event iteration in
     * Event Mode over all involved FMUs and other models has converged) into
     * Continuous-Time Mode. [This function might be used for the following
     * purposes: • If the FMU stores results internally on file, then the
     * results after the initialization and/or the event has been processed can
     * be stored. • If the FMU contains dynamically changing states, then a new
     * state selection might be performed with this function. ]
     */
    fun enterContinuousTimeMode() : Fmi2Status {
        return (updateStatus(Fmi2Status.valueOf(library.fmi2EnterContinuousTimeMode(c))))
    }

    fun newDiscreteStates(eventInfo: Fmi2EventInfo) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2NewDiscreteStates(c, eventInfo)))
    }

    fun completedIntegratorStep() : CompletedIntegratorStep {
        updateStatus(Fmi2Status.valueOf(
                library.fmi2CompletedIntegratorStep(c, convert(true),
                        enterEventMode, terminateSimulation)))
        return CompletedIntegratorStep(convert(enterEventMode.value), convert(terminateSimulation.value))
    }

    /**
     * Compute state derivatives and event indicators at the current time
     * instant and for the current states. The derivatives are returned as a
     * vector with “nx” elements. A state event is triggered when the domain of
     * an event indicator changes from zj > 0 to zj ≤ 0 or vice versa. The FMU
     * must guarantee that at an event restart zj ≠ 0, for example by shifting
     * zj with a small value. Furthermore, zj should be scaled in the FMU with
     * its nominal value (so all elements of the returned vector
     * “eventIndicators” should be in the order of “one”). The event indicators
     * are returned as a vector with “ni” elements. The ordering of the elements
     * of the derivatives vector is identical to the ordering of the state
     * vector (for example derivatives[2] is the derivative of x[2]). Event
     * indicators are not necessarily related to variables on the Model
     * Description File. Note: fmi2Status = fmi2Discard is possible for both
     * functions.
     *
     * @param derivatives
     */
    fun getDerivatives(derivatives: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetDerivatives(c, derivatives, derivatives.size)))
    }

    /**
     * Compute state derivatives and event indicators at the current time
     * instant and for the current states. The derivatives are returned as a
     * vector with “nx” elements. A state event is triggered when the domain of
     * an event indicator changes from zj > 0 to zj ≤ 0 or vice versa. The FMU
     * must guarantee that at an event restart zj ≠ 0, for example by shifting
     * zj with a small value. Furthermore, zj should be scaled in the FMU with
     * its nominal value (so all elements of the returned vector
     * “eventIndicators” should be in the order of “one”). The event indicators
     * are returned as a vector with “ni” elements. The ordering of the elements
     * of the derivatives vector is identical to the ordering of the state
     * vector (for example derivatives[2] is the derivative of x[2]). Event
     * indicators are not necessarily related to variables on the Model
     * Description File. Note: fmi2Status = fmi2Discard is possible for both
     * functions.
     *
     * @param eventIndicators
     */
    fun getEventIndicators(eventIndicators: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetEventIndicators(c, eventIndicators, eventIndicators.size)))
    }

    /**
     * Return the new (continuous) state vector x. This function has to be
     * called directly after calling function fmi2EnterContinuousTimeMode if it
     * returns with eventInfo- >valuesOfContinuousStatesChanged = fmi2True
     * (indicating that the (continuous-time) state vector has changed).
     *
     * @param x
     */
    fun getContinuousStates(x: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetContinuousStates(c, x, x.size)))
    }

    /**
     * Return the nominal values of the continuous states. This function should
     * always be called after calling function fmi2NewDiscreteStates if it
     * returns with eventInfo-> nominalsOfContinuousStatesChanged = fmi2True
     * since then the nominal values of the continuous states have changed [e.g.
     * because the association of the continuous states to variables has changed
     * due to internal dynamic state selection]. If the FMU does not have
     * information about the nominal value of a continuous state i, a nominal
     * value x_nominal[i] = 1.0 should be returned. Note, it is required that
     * x_nominal[i] > 0.0 [Typically, the nominal values of the continuous
     * states are used to compute the absolute tolerance required by the
     * integrator. Example: absoluteTolerance[i] = 0.01*tolerance*x_nominal[i];]
     *
     * @param x_nominal
     */
    fun getNominalsOfContinuousStates(x_nominal: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetNominalsOfContinuousStates(c, x_nominal, x_nominal.size)))
    }



}

