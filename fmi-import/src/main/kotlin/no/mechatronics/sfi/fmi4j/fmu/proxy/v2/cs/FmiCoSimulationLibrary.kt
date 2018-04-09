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

package no.mechatronics.sfi.fmi4j.fmu.proxy.v2.cs

import com.sun.jna.Pointer
import com.sun.jna.ptr.DoubleByReference
import com.sun.jna.ptr.IntByReference
import no.mechatronics.sfi.fmi4j.fmu.misc.StringByReference
import no.mechatronics.sfi.fmi4j.fmu.proxy.v2.FmiLibrary

/**
 *
 * @author Lars Ivar Hatledal
 */
interface FmiCoSimulationLibrary : FmiLibrary {

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

    /**
     * The computation of a time step is started.
     * Argument currentCommunicationPoint is the current communication point of the master
     * (𝑡𝑐𝑖) and argument communicationStepSize is the communication step size (ℎ𝑐𝑖). The
     * latter must be > 0.0. The slave must integrate until time instant 𝑡𝑐𝑖+1 = 𝑡𝑐𝑖 + ℎ𝑐𝑖. [The calling
     * environment defines the communication points and fmi2DoStep must synchronize to these
     * points by always integrating exactly to 𝑡𝑐𝑖 + ℎ𝑐𝑖. It is up to fmi2DoStep how to achieve this.]
     * At the first call to fmiDoStep after fmi2ExitInitializationMode was called
     * currentCommunicationPoint must be equal to startTime as set with
     * fmi2SetupExperiment. [Formally argument currentCommunicationPoint is not needed.
     * It is present in order to handle a mismatch between the master and the FMU state of the
     * slave: The currentCommunicationPoint and the FMU state of the slaves defined by
     * former fmi2DoStep or fmi2SetFMUState calls have to be consistent with respect to each
     * other. For example, if the slave does not use the update formula for the independent variable
     * as required above, 𝑡𝑐𝑖+1 = 𝑡𝑐𝑖 + ℎ𝑐𝑖 (using argument 𝑡𝑐𝑖 = currentCommunicationPoint of
     * fmi2DoStep) but uses internally an own update formula, such as 𝑡𝑐𝑠,𝑖+1 = 𝑡𝑐𝑠,𝑖 + ℎ𝑐𝑠,𝑖 then
     * the slave could use as time increment ℎ𝑐𝑠,𝑖: = (𝑡𝑐𝑖 − 𝑡𝑐𝑠,𝑖) + ℎ𝑐𝑖 (instead of ℎ𝑐𝑠,𝑖: = ℎ𝑐𝑖,) to
     * avoid a mismatch between the master time 𝑡𝑐𝑖+1 and the slave internal time 𝑡𝑐𝑠,𝑖+1 for large i.]
     * Argument noSetFMUStatePriorToCurrentPoint is fmi2True if fmi2SetFMUState will
     * no longer be called for time instants prior to currentCommunicationPoint in this
     * simulation run [the slave can use this flag to flush a result buffer].
     * The function returns:
     * fmi2OK - if the communication step was computed successfully until its end.
     * fmi2Discard – if the slave computed successfully only a subinterval of the communication
     * step. The master can call the appropriate fmi2GetXXXStatus functions to get further
     * information. If possible, the master should retry the simulation with a shorter communication
     * step size. [Redoing a step is only possible if the FMU state has been recorded at the
     * beginning of the current (failed) step with fmi2GetFMUState. Redoing a step is performed
     * by calling fmi2SetFMUState and afterwards calling fmi2DoStep with the new
     * communicationStepSize. Note, it is not possible to change
     * currentCommunicationPoint in such a call.]
     * fmi2Error – the communication step could not be carried out at all. The master can try to
     * repeat the step with other input values and/or a different communication step size in the
     * same way as described in the fmi2Discard case above.
     * fmi2Fatal – if an error occurred which corrupted the FMU irreparably. [The master should
     * stop the simulation run immediatlely.] See section 2.1.3 for details.
     * fmi2Pending – is returned if the slave executes the function asynchronously. That means
     * the slave starts the computation but returns immediately. The master has to call
     * fmi2GetStatus(...,fmi2DoStep,...) to find out, if the slave is done. An alternative is to
     * wait until the callback function fmi2StepFinished is called by the slave. fmi2CancelStep
     * can be called to cancel the current computation. It is not allowed to call any other function
     * during a pending fmi2DoStep.
     */
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
    fun fmi2GetMaxStepsize(c: Pointer, value: DoubleByReference): Int

}

