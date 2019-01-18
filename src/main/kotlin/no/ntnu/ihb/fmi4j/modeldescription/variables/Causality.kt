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

package no.ntnu.ihb.fmi4j.modeldescription.variables


/**
 * Enumeration that defines the causality of the variable.
 *
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
enum class Causality {

    /**
     * Independent parameter (a data value that is constant during the
     * simulation and is provided by the environment and cannot be used in
     * connections). variability must be "fixed" or "tunable". initial must be
     * exact or not present (meaning exact).
     */
    PARAMETER,

    /**
     * A data value that is constant during the simulation and is computed
     * during initialization or when tunable parameters change. variability must
     * be "fixed" or "tunable". initial must be "approx", "calculated" or not
     * present (meaning calculated).
     */
    CALCULATED_PARAMETER,

    /**
     * The variable value can be provided from another model or slave. It is not
     * allowed to define initial.
     */
    INPUT,

    /**
     * The variable value can be used by another model or slave. The algebraic
     * relationship to the inputs is defined via the dependencies attribute of
     * <fmiModelDescription><ModelStructure><Outputs><Unknown>.
     */
    OUTPUT,

    /**
     * Local variable that is calculated from other categories or is a
     * continuoustime state (see section 2.2.8). It is not allowed to use the
     * variable value in another model or slave.
     */
    LOCAL,

    /**
     * The independent variable (usually “time”). All categories are a function
     * of this independent variable. variability must be "continuous". At most
     * one ScalarVariable of an FMU can be defined as "independent". If no
     * variable is defined as "independent", it is implicitely present with name
     * = "time" and unit = "s". If one variable is defined as "independent", it
     * must be defined as "Real" without a "start" attribute. It is not allowed
     * to call function fmi2SetReal on an "independent" variable. Instead, its
     * value is initialized with fmi2SetupExperiment and after initialization
     * set by fmi2SetTime for ModelExchange and by arguments
     * currentCommunicationPoint and communicationStepSize of fmi2DoStep for
     * CoSimulation. [The actual value can be inquired with fmi2GetReal.]
     */
    INDEPENDENT;

}

