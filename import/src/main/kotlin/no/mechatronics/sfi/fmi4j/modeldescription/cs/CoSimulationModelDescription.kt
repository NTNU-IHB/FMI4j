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

package no.mechatronics.sfi.fmi4j.modeldescription.cs

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import java.io.File
import java.io.FileInputStream
import java.net.URL
import javax.xml.bind.annotation.XmlRootElement


@XmlRootElement(name = "fmiModelDescription")
class CoSimulationModelDescription : ModelDescription() {

    companion object {
        @JvmStatic
        fun parseModelDescription(xml: String) : CoSimulationModelDescription = ModelDescription.parseModelDescription(xml, CoSimulationModelDescription::class.java)
        @JvmStatic
        fun parseModelDescription(url: URL): CoSimulationModelDescription = ModelDescription.parseModelDescription(url.openStream(), CoSimulationModelDescription::class.java)
        @JvmStatic
        fun parseModelDescription(file: File): CoSimulationModelDescription = ModelDescription.parseModelDescription(FileInputStream(file), CoSimulationModelDescription::class.java)
    }

    /**
     * The slave is able to provide derivatives of
     * outputs with maximum order. Calling of
     * fmi2GetRealOutputDerivatives(...) is
     * allowed up to the order defined by
     * maxOutputDerivativeOrder.
     *
     * @return
     */
    val maxOutputDerivativeOrder: Int
        get() = cs!!.maxOutputDerivativeOrder

    /**
     * If true, a tool is needed to execute the
     * model. The FMU just contains the
     * communication to this tool (see Figure 8).
     * [Typically, this information is only utilized for
     * information purposes. For example a
     * co-simulation master can inform the user
     * that a tool has to be available on the
     * computer where the slave is instantiated.
     * The name of the tool can be taken from
     * attribute generationTool of
     * fmiModelDescription. ]
     *
     * @return
     */
    fun needsExecutionTool(): Boolean {
        return cs!!.needsExecutionTool
    }

    /**
     * The slave can handle variable
     * communication step size. The
     * communication step size (parameter
     * communicationStepSize of
     * fmi2DoStep(...) ) has not to be constant
     * for each call.
     *
     * @return
     */
    fun canHandleVariableCommunicationStepSize(): Boolean {
        return cs!!.canHandleVariableCommunicationStepSize
    }

    /**
     * The slave is able to interpolate continuous
     * inputs. Calling of
     * fmi2SetRealInputDerivatives(...) has
     * an effect for the slave.
     *
     * @return
     */
    fun canInterpolateInputs(): Boolean {
        return cs!!.canInterpolateInputs
    }

    /**
     * This flag describes the ability to carry out the
     * fmi2DoStep(...) call asynchronously.
     *
     * @return
     */
    fun canRunAsynchronuosly(): Boolean {
        return cs!!.canRunAsynchronuosly
    }

    /**
     * This flag indicates cases (especially for
     * embedded code), where only one instance
     * per FMU is possible
     * (multiple instantiation is default = false; if
     * multiple instances are needed, the FMUs
     * must be instantiated in different processes).
     *
     * @return
     */
    fun canBeInstantiatedOnlyOncePerProcess(): Boolean {
        return cs!!.canBeInstantiatedOnlyOncePerProcess
    }

    /**
     * If true, the slave uses its own functions for
     * memory allocation and freeing only. The
     * callback functions allocateMemory and
     * freeMemory given in fmi2Instantiate are
     * ignored.
     *
     * @return
     */
    fun canNotUseMemoryManagementFunctions(): Boolean {
        return cs!!.canNotUseMemoryManagementFunctions
    }

    /**
     * If true, the environment can inquire the
     * internal FMU state and can restore it. That
     * is, fmi2GetFMUstate, fmi2SetFMUstate,
     * and fmi2FreeFMUstate are supported by
     * the FMU.
     *
     * @return
     */
    fun canGetAndSetFMUstate(): Boolean {
        return cs!!.canGetAndSetFMUstate
    }

    /**
     * If true, the environment can serialize the
     * internal FMU state, in other words
     * fmi2SerializedFMUstateSize,
     * fmi2SerializeFMUstate,
     * fmi2DeSerializeFMUstate are supported
     * by the FMU. If this is the case, then flag
     * canGetAndSetFMUstate must be true as
     * well.
     *
     * @return
     */
    fun canSerializeFMUstate(): Boolean {
        return cs!!.canSerializeFMUstate
    }

    /**
     * If true, the directional derivative of the
     * equations at communication points can be
     * computed with
     * fmi2GetDirectionalDerivative(..)
     *
     * @return
     */
    fun providesDirectionalDerivative(): Boolean {
        return cs!!.providesDirectionalDerivative
    }

    /**
     * FMI extension
     */
    fun canGetAndSetFMUState(): Boolean {
        return cs!!.canGetAndSetFMUState
    }

}
