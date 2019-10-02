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

package no.ntnu.ihb.fmi4j.modeldescription

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
class ModelStructure (

        /**
     * Ordered list of all outputs, in other words a list of ScalarVariable indices
     * where every corresponding ScalarVariable must have causality = "output"
     */
        val outputs: List<Unknown> = emptyList(),

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
        val derivatives: List<Unknown> = emptyList(),

        val initialUnknowns: List<Unknown> = emptyList()
)


/**
 *
 * Dependency of scalar Unknown from Knowns in continuous-time and event mode (Model Exchange),
 * and at communications points (Co-simulation)
 *
 * @author Lars Ivar Hatledal
 */
data class Unknown (

    /**
     * ScalarVariable index of Unknown
     */
    val index: Int,

    /**
     * Defines the dependency of the Unknown (directly or indirectly via auxiliary variables)
     * on the Knowns in Continuous-Time and Event Mode (ModelExchange) and at Communication Points (CoSimulation)
     */
    val dependencies: List<Int>,

    /**
     * If present, it must be assumed that the Unknown depends on the Knowns
     * without a particular structure.
     */
    val dependenciesKind: List<String>

)


