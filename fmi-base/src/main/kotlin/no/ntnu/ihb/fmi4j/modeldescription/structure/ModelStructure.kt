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

package no.ntnu.ihb.fmi4j.modeldescription.structure

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.io.Serializable


/**
 * Defines the structure of the model. Especially, the ordered lists of
 * outputs, continuous-time states and initial unknowns (the unknowns
 * during Initialization Mode) are defined here. Furthermore, the
 * dependency of the unknowns from the knowns can be optionally
 * defined. [This information can be, for example used to compute
 * efficiently a sparse Jacobian for simulation or to utilize the
 * input/output dependency in order to detect that in some cases there
 * are actually no algebraic loops when connecting FMUs together].
 *
 * @author Lars Ivar Hatledal
 */
interface ModelStructure {

    /**
     * Ordered list of all outputs, in other words a list of ScalarVariable indices
     * where every corresponding ScalarVariable must have causality = "output"
     */
    val outputs: List<Unknown>

    /**
     * Ordered list of all state derivatives, in other words a list of ScalarVariable
     * indices where every corresponding ScalarVariable must be a state
     * derivative. [Note, only continuous Real variables are listed here. If a state or a
     * derivative of a state shall not be exposed from the FMU, or if states are not
     * statically associated with a variable (due to dynamic state selection), then dummy
     * ScalarVariables have to be introduced, for example x[4], or
     * xDynamicStateSet2[5]. The ordering of the variables in this list is defined by the
     * exporting tool. Usually, it is best to order according to the declaration order of the
     * states in the source model, since then the <Derivatives> list does not change, if
     * the declaration order of states in the source model is not changed. This is e.g.
     * important for linearization, in order that the interpretation of the state vector does
     * not change for a re-exported FMU.]. The corresponding continuous-time states
     * are defined by attribute derivative of the corresponding ScalarVariable state
     * derivative element. [Note, higher order derivatives must be mapped to first order
     * derivatives but the mapping definition can be preserved due to attribute
     * derivative.
     * For Co-Simulation, element “Derivatives” is ignored if capability flag
     * providesDirectionalDerivative has a value of false, in other words cannot
     * be computed [which is the default. If an FMU supports both ModelExchange and
     * CoSimulation, then the “Derivatives” element might be present, since it is needed
     * for ModelExchange. If the above flag is set to false for the CoSimulation case,
     * then the “Derivatives” element is ignored for CoSimulation. If “inline integration” is
     * used for a CoSimulation slave, then the model still has continuous-time states and
     * just a special solver is used.
     */
    val derivatives: List<Unknown>

    val initialUnknowns: List<Unknown>
}

/**
 *
 * @author Lars Ivar Hatledal
 */

class ModelStructureImpl: ModelStructure, Serializable {

    @JacksonXmlProperty(localName = "Outputs")
    private val _outputs: Outputs? = null

    @JacksonXmlProperty(localName = "Derivatives")
    private val _derivatives: Derivatives? = null

    @JacksonXmlProperty(localName = "InitialUnknowns")
    private val _initialUnknowns: InitialUnknowns? = null

    override val outputs: List<Unknown>
        get() = _outputs?.unknowns ?: emptyList()

    override val derivatives: List<Unknown>
        get() = _derivatives?.unknowns ?: emptyList()

    override val initialUnknowns: List<Unknown>
        get() = _initialUnknowns?.unknowns ?: emptyList()

    override fun toString(): String {
        return "ModelStructureImpl(outputs=$outputs, derivatives=$derivatives, initialUnknowns=$initialUnknowns)"
    }

}

/**
 *
 * @author Lars Ivar Hatledal
 */
class Outputs(

        @JacksonXmlProperty(localName = "Unknown")
        @JacksonXmlElementWrapper(useWrapping = false)
        val unknowns: List<UnknownImpl>? = null

) : Serializable

/**
 *
 * @author Lars Ivar Hatledal
 */
class Derivatives(

        @JsonProperty("Unknown")
        @JacksonXmlElementWrapper(useWrapping = false)
        val unknowns: List<UnknownImpl>? = null

) : Serializable

/**
 *
 * @author Lars Ivar Hatledal
 */
class InitialUnknowns(

        @JsonProperty("Unknown")
        @JacksonXmlElementWrapper(useWrapping = false)
        val unknowns: List<UnknownImpl>? = null

) : Serializable

