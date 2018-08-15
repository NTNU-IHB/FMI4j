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

package no.mechatronics.sfi.fmi4j.modeldescription

import no.mechatronics.sfi.fmi4j.modeldescription.misc.CoSimulationData
import java.io.Serializable


/**
 * @author Lars Ivar Hatledal
 */
interface CoSimulationModelDescription : SpecificModelDescription {

    /**
     * The slave is able to provide derivatives of outputs with maximum order.
     * Calling of mi2GetRealOutputDerivatives(...) is allowed up to the order defined by
     * maxOutputDerivativeOrder.
     */
    val maxOutputDerivativeOrder: Int


    /**
     * The slave can handle variable communication step size.
     * The communication step size (parameter communicationStepSize of fmi2DoStep(...) )
     * has not to be constant for each call.
     */
    val canHandleVariableCommunicationStepSize: Boolean

    /**
     * The slave is able to interpolate continuous inputs.
     * Calling of fmi2SetRealInputDerivatives(...) has an effect for the slave.
     */
    val canInterpolateInputs: Boolean

    /**
     * This flag describes the ability to carry out the
     * fmi2DoStep(...) call asynchronously.
     */
    val canRunAsynchronuously: Boolean

}

/**
 * @author Lars Ivar Hatledal
 */
class CoSimulationModelDescriptionImpl internal constructor(
        private val modelDescription: ModelDescriptionImpl,
        cs: CoSimulationData
) : CommonModelDescription by modelDescription, CoSimulationModelDescription, CoSimulationData by cs, Serializable {

    override fun toString(): String {
        return "CoSimulationModelDescriptionImpl(\n${modelDescription.stringContent}\n)"
    }

}
