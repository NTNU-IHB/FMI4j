package no.mechatronics.sfi.fmi4j.jna.lib.wrapper

import com.sun.jna.ptr.ByteByReference
import no.mechatronics.sfi.fmi4j.jna.convert
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.lib.Fmi2ModelExchangeLibrary
import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2EventInfo


/**
 *
 * @author laht
 */
class Fmi2ModelExchangeLibraryWrapper(
        libraryFolder: String,
        libraryName: String
) : Fmi2LibraryWrapper<Fmi2ModelExchangeLibrary>(libraryFolder, libraryName, Fmi2ModelExchangeLibrary::class.java) {


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
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetTime(c, time)))
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
    fun setContinousStates(x: DoubleArray) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2SetContinuousStates(c, x, x.size)))
    }

    /**
     * The model enters Event Mode from the Continuous-Time Mode and
     * discrete-time equations may become active (and relations are not
     * “frozen”).
     */
    fun enterEventMode() : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2EnterEventMode(c)))
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
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2EnterContinuousTimeMode(c)))
    }

    fun newDiscreteStates(eventInfo: Fmi2EventInfo) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2NewDiscreteStates(c, eventInfo)))
    }

    fun completedIntegratorStep(noSetFMUStatePriorToCurrentPoint: Boolean) : Pair<Boolean, Boolean> {
        updateStatus(Fmi2Status.valueOf(
                library!!.fmi2CompletedIntegratorStep(c, convert(noSetFMUStatePriorToCurrentPoint),
                        enterEventMode, terminateSimulation)))
        return Pair(convert(enterEventMode.value), convert(terminateSimulation.value))
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
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetDerivatives(c, derivatives, derivatives.size)))
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
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetEventIndicators(c, eventIndicators, eventIndicators.size)))
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
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetContinuousStates(c, x, x.size)))
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
        return updateStatus(Fmi2Status.valueOf(library!!.fmi2GetNominalsOfContinuousStates(c, x_nominal, x_nominal.size)))
    }


}

