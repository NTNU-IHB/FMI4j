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

package no.ntnu.ihb.fmi4j.common

/**
 *
 *  Indicates the success of a function call
 *
 * @author Lars Ivar Hatledal
 */
enum class FmiStatus(
        val code: Int
) {

    NONE(-1),

    /**
     * All well
     */
    OK(0),

    /**
     * Things are not quite right, but the computation can continue. Function “logger” was
     * called in the model (see below) and it is expected that this function has shown the prepared
     * information message to the user.
     */
    Warning(1),

    /**
     * This return status is only possible, if explicitly defined for the corresponding function
     * (ModelExchange: fmi2SetReal, fmi2SetInteger, fmi2SetBoolean, fmi2SetString,
     * fmi2SetContinuousStates, fmi2GetReal, fmi2GetDerivatives,
     * fmi2GetContinuousStates, fmi2GetEventIndicators;
     * CoSimulation: fmi2SetReal, fmi2SetInteger, fmi2SetBoolean, fmi2SetString, fmi2DoStep,
     * fmiGetXXXStatus):
     * For “model exchange”: It is recommended to perform a smaller step size and evaluate the model
     * equations again, for example because an iterative solver in the model did not converge or because a
     * function is outside of its domain (for example sqrt(<negative number>)). If this is not possible, the
     * simulation has to be terminated.
     * For “co-simulation”: fmi2Discard is returned also if the slave is not able to return the required
     * status information. The master has to decide if the simulation run can be continued.
     * In both cases, function “logger” was called in the FMU (see below) and it is expected that this
     * function has shown the prepared information message to the user if the FMU was called in debug
     * mode (loggingOn = fmi2True). Otherwise, “logger” should not show a message.
     */
    Discard(2),

    /**
     * The FMU encountered an error. The simulation cannot be continued with this FMU
     * instance. If one of the functions returns fmi2Error, it can be tried to restart the simulation from a
     * formerly stored FMU state by calling fmi2SetFMUstate. This can be done if the capability flag
     * canGetAndSetFMUstate is true and fmu2GetFMUstate was called before in non-erroneous state. If
     * not, the simulation cannot be continued and fmi2FreeInstance or fmi2Reset must be called
     * afterwards.
     * Further processing is possible after this call; especially other FMU instances are not affected.
     * Function “logger” was called in the FMU (see below) and it is expected that this function has shown
     * the prepared information message to the user
     */
    Error(3),

    /**
     * The model computations are irreparably corrupted for all FMU instances. [For example,
     * due to a run-time exception such as access violation or integer division by zero during the execution
     * of an fmi function]. Function “logger” was called in the FMU (see below) and it is expected that this
     * function has shown the prepared information message to the user. It is not possible to call any other
     * function for any of the FMU instances.
     */
    Fatal(4),

    /**
     * Is returned only from the co-simulation interface, if the slave executes the function in an
     * asynchronous way. That means the slave starts to compute but returns immediately. The master has
     * to call fmi2GetStatus(..., fmi2DoStepStatus) to determine, if the slave has finished the
     * computation. Can be returned only by fmi2DoStep and by fmi2GetStatus (see section 4.2.3)
     */
    Pending(5);

    fun isOK(): Boolean {
        return this == OK;
    }

    companion object {

        @JvmStatic
        fun valueOf(i: Int): FmiStatus {
            for (status in values()) {
                if (i == status.code) {
                    return status
                }
            }
            throw IllegalArgumentException("$i not in range of ${values().map { it.code }}")
        }

    }

}