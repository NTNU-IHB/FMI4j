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
import com.sun.jna.ptr.IntByReference
import no.mechatronics.sfi.fmi4j.common.Fmi2Status
import no.mechatronics.sfi.fmi4j.misc.Fmi2Boolean
import no.mechatronics.sfi.fmi4j.misc.Fmi2True
import no.mechatronics.sfi.fmi4j.proxy.structs.Fmi2EventInfo
import no.mechatronics.sfi.fmi4j.misc.LibraryProvider
import no.mechatronics.sfi.fmi4j.proxy.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper

/**
 *
 * @author Lars Ivar Hatledal
 */
interface Fmi2ModelExchangeLibrary : Fmi2Library {

    /**
     * Set a new time instant and re-initialize caching of variables that depend on time, provided the
     * newly provided time value is different to the previously set time value (variables that depend
     * solely on constants or parameters need not to be newly computed in the sequel, but the
     * previously computed values can be reused).
     */
    fun fmi2SetTime(c: Pointer, time: Double): Int

    /**
     * Set a new (continuous) state vector and re-initialize caching of variables that depend on the
     * states. Argument nx is the length of vector x and is provided for checking purposes (variables
     * that depend solely on constants, parameters, time, and inputs do not need to be newly computed
     * in the sequel, but the previously computed values can be reused). Note, the continuous states
     * might also be changed in Event Mode.
     * Note: fmi2Status = fmi2Discard is possible
     */
    fun fmi2SetContinuousStates(c: Pointer, x: DoubleArray, nx: Int): Int

    /**
     * The model enters Event Mode from the Continuous-Time Mode and discrete-time equations may
     * become active (and relations are not “frozen”).
     */
    fun fmi2EnterEventMode(c: Pointer): Int

    /**
     * The FMU is in Event Mode and the super dense time is incremented by this call.
     * If the super dense time before a call to fmi2NewDiscreteStates was (tR,tI) then the time
     * instant after the call is (tR,tI + 1).
     * If return argument fmi2eventInfo->newDiscreteStatesNeeded = fmi2True, the FMU
     * should stay in Event Mode and the FMU requires to set new inputs to the FMU (fmi2SetXXX on
     * inputs), to compute and get the outputs (fmi2GetXXX on outputs) and to call
     * fmi2NewDiscreteStates again. Depending on the connection with other FMUs, the
     * environment shall
     * • call fmi2Terminate, if terminateSimulation = fmi2True is returned by at least one
     * FMU,
     * • call fmi2EnterContinuousTimeMode if all FMUs return newDiscreteStatesNeeded =
     * fmi2False.
     * • stay in Event Mode otherwise.
     * When the FMU is terminated, it is assumed that an appropriate message is printed by the logger
     * function (see section 2.1.5) to explain the reason for the termination.
     * If nominalsOfContinuousStatesChanged = fmi2True then the nominal values of the
     * states have changed due to the function call and can be inquired with
     * fmi2GetNominalsOfContinuousStates.
     * If valuesOfContinuousStatesChanged = fmi2True then at least one element of the
     * continuous state vector has changed its value due to the function call. The new values of the
     * states can be inquired with fmi2GetContinuousStates. If no element of the continuous state
     * vector has changed its value, valuesOfContinuousStatesChanged must return fmi2False. [if
     * fmi2True would be returned in this case, an infinite event loop may occur.]
     * If nextEventTimeDefined = fmi2True, then the simulation shall integrate at most until
     * time = nextEventTime, and shall call fmi2EnterEventMode at this time instant. If integration is
     * stopped before nextEventTime, for example due to a state event, the definition of
     * nextEventTime becomes obsolete.
     */
    fun fmi2NewDiscreteStates(c: Pointer, eventInfo: Fmi2EventInfo): Int


    /**
     * The model enters Continuous-Time Mode and all discrete-time equations become inactive and all
     * relations are “frozen”.
     * This function has to be called when changing from Event Mode (after the global event iteration
     * in Event Mode over all involved FMUs and other models has converged) into Continuous-Time
     * Mode.
     * [This function might be used for the following purposes:
     * • If the FMU stores results internally on file, then the results after the initialization and/or the
     * event has been processed can be stored.
     * • If the FMU contains dynamically changing states, then a new state selection might be
     * performed with this function.]
     */
    fun fmi2EnterContinuousTimeMode(c: Pointer): Int


    /**
     * This function must be called by the environment after every completed step of the integrator
     * provided the capability flag completedIntegratorStepNotNeeded = false.
     * Argument noSetFMUStatePriorToCurrentPoint is fmi2True if fmi2SetFMUState will no
     * longer be called for time instants prior to current time in this simulation run [the FMU can use this
     * flag to flush a result buffer].
     * The function returns enterEventMode to signal to the environment if the FMU shall call
     * fmi2EnterEventMode, and it returns terminateSimulation to signal if the simulation shall be
     * terminated. If enterEventMode = fmi2False and terminateSimulation = fmi2False the
     * FMU stays in Continuous-Time Mode without calling fmi2EnterContinuousTimeMode again.
     * When the integrator step is completed and the states are modified by the integrator afterwards
     * (for example correction by a BDF method), then fmi2SetContinuousStates(..) has to be
     * called with the updated states before fmi2CompletedIntegratorStep(..) is called.
     * When the integrator step is completed and one or more event indicators change sign (with
     * respect to the previously completed integrator step), then the integrator or the environment has to
     * determine the time instant of the sign change that is closest to the previous completed step up to
     * a certain precision (usually a small multiple of the machine epsilon). This is usually performed by
     * an iteration where time is varied and state variables needed during the iteration are determined
     * by interpolation. Function fmi2CompletedIntegratorStep must be called after this state event
     * location procedure and not after the successful computation of the time step by the integration
     * algorithm. The intended purpose of the function call is to indicate to the FMU that at this stage all
     * inputs and state variables have valid (accepted) values.
     * After fmi2CompletedIntegratorStep is called, it is still allowed to go back in time (calling
     * fmi2SetTime) and inquire values of variables at previous time instants with fmi2GetXXX [for
     * example to determine values of non-state variables at output points]: However, it is not allowed to
     * go back in time over the previous completedIntegratorStep or the previous
     * fmi2EnterEventMode call.
     */
    fun fmi2CompletedIntegratorStep(c: Pointer,
                                    noSetFMUStatePriorToCurrentPoint: Int,
                                    enterEventMode: IntByReference,
                                    terminateSimulation: IntByReference): Int

    /**
     * Compute state derivatives and event indicators at the current time instant and for the current
     * states. The derivatives are returned as a vector with “nx” elements. A state event is triggered
     * when the domain of an event indicator changes from zj > 0 to zj ≤ 0 or vice versa. The FMU must
     * guarantee that at an event restart zj ≠ 0, for example by shifting zj with a small value.
     * Furthermore, zj should be scaled in the FMU with its nominal value (so all elements of the
     * returned vector “eventIndicators” should be in the order of “one”). The event indicators are
     * returned as a vector with “ni” elements.
     * The ordering of the elements of the derivatives vector is identical to the ordering of the state
     * vector (for example derivatives[2] is the derivative of x[2]). Event indicators are not
     * necessarily related to variables on the Model Description File.
     *
     * Note: fmi2Status = fmi2Discard is possible.
     */
    fun fmi2GetDerivatives(c: Pointer, derivatives: DoubleArray, nx: Int): Int

    /**
     * Compute state derivatives and event indicators at the current time instant and for the current
     * states. The derivatives are returned as a vector with “nx” elements. A state event is triggered
     * when the domain of an event indicator changes from zj > 0 to zj ≤ 0 or vice versa. The FMU must
     * guarantee that at an event restart zj ≠ 0, for example by shifting zj with a small value.
     * Furthermore, zj should be scaled in the FMU with its nominal value (so all elements of the
     * returned vector “eventIndicators” should be in the order of “one”). The event indicators are
     * returned as a vector with “ni” elements.
     * The ordering of the elements of the derivatives vector is identical to the ordering of the state
     * vector (for example derivatives[2] is the derivative of x[2]). Event indicators are not
     * necessarily related to variables on the Model Description File.
     *
     * Note: fmi2Status = fmi2Discard is possible.
     */
    fun fmi2GetEventIndicators(c: Pointer, eventIndicators: DoubleArray, ni: Int): Int

    /**
     * Return the new (continuous) state vector x. This function has to be called directly after calling
     * function fmi2EnterContinuousTimeMode if it returns with eventInfo>valuesOfContinuousStatesChanged
     * = fmi2True (indicating that the (continuous-time)
     * state vector has changed).
     */
    fun fmi2GetContinuousStates(c: Pointer, x: DoubleArray, nx: Int): Int

    /**
     * Return the nominal values of the continuous states. This function should always be called after
     * calling function fmi2NewDiscreteStates if it returns with eventInfo->
     * nominalsOfContinuousStatesChanged = fmi2True since then the nominal values of the
     * continuous states have changed [e.g. because the association of the continuous states to
     * variables has changed due to internal dynamic state selection]. If the FMU does not have
     * information about the nominal value of a continuous state i, a nominal value x_nominal[i] =
     * 1.0 should be returned. Note, it is required that x_nominal[i] > 0.0 [Typically, the nominal
     * values of the continuous states are used to compute the absolute tolerance required by the
     * integrator
     */
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


    private val enterEventMode = IntByReference()
    private val terminateSimulation = IntByReference()


    /**
     * @inheritDoc
     *
     * @param time
     */
    fun setTime(time: Double) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetTime(c, time)))
    }

    /**
     * @inheritDoc
     *
     * @param x state
     */
    fun setContinuousStates(x: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetContinuousStates(c, x, x.size)))
    }

    /**
     * @inheritDoc
     */
    fun enterEventMode() : Fmi2Status {
        return (updateStatus(Fmi2Status.valueOf(library.fmi2EnterEventMode(c))))
    }

    /**
     * @inheritDoc
     */
    fun enterContinuousTimeMode() : Fmi2Status {
        return (updateStatus(Fmi2Status.valueOf(library.fmi2EnterContinuousTimeMode(c))))
    }

    fun newDiscreteStates(eventInfo: Fmi2EventInfo) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2NewDiscreteStates(c, eventInfo)))
    }

    fun completedIntegratorStep() : CompletedIntegratorStep {
        updateStatus(Fmi2Status.valueOf(library.fmi2CompletedIntegratorStep(c,
                Fmi2True, enterEventMode, terminateSimulation)))
        return CompletedIntegratorStep(
                Fmi2Boolean.convert(enterEventMode.value),
                Fmi2Boolean.convert(terminateSimulation.value))
    }

    /**
     * @inheritDoc
     *
     * @param derivatives
     */
    fun getDerivatives(derivatives: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetDerivatives(c, derivatives, derivatives.size)))
    }

    /**
     * @inheritDoc
     *
     * @param eventIndicators
     */
    fun getEventIndicators(eventIndicators: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetEventIndicators(c, eventIndicators, eventIndicators.size)))
    }

    /**
     * @inheritDoc
     *
     * @param x
     */
    fun getContinuousStates(x: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetContinuousStates(c, x, x.size)))
    }

    /**
     * @inheritDoc
     *
     * @param x_nominal
     */
    fun getNominalsOfContinuousStates(x_nominal: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetNominalsOfContinuousStates(c, x_nominal, x_nominal.size)))
    }

}

