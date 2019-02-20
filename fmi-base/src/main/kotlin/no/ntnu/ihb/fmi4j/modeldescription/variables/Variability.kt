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
 * Enumeration that defines the time dependency of the variable, in other words it defines
 * the time instants when a variable can change its value. [The purpose of this attribute is
 * to define when a result value needs to be inquired and to be stored. For example
 * discrete categories change their values only at event instants (ModelExchange) or at a
 * communication point (CoSimulation) and it is therefore only necessary to inquire them
 * with fmi2GetXXX and store them at event times]. Allowed values of this enumeration:
 * • "constant": The value of the variable never changes.
 * • "fixed": The value of the variable is fixed after initialization, in other words after
 * fmi2ExitInitializationMode was called the variable value does not change
 * anymore.
 * • "tunable": The value of the variable is constant between external events
 * (ModelExchange) and between Communication Points (CoSimulation) due to
 * changing categories with causality = "parameter" or "input" and
 * variability = "tunable". Whenever a parameter or input signal with
 * variability = "tunable" changes, then an event is triggered externally
 * (ModelExchange) or the change is performed at the next Communication Point
 * (CoSimulation) and the categories with variability = "tunable" and causality =
 * "calculatedParameter" or "output" must be newly computed.
 * • "discrete":
 * ModelExchange: The value of the variable is constant between external and internal
 * events (= time, state, step events defined implicitly in the FMU).
 * CoSimulation: By convention, the variable is from a “real” sampled data system and
 * its value is only changed at Communication Points (also inside the slave).
 * • "continuous": Only a variable of type = “Real” can be “continuous”.
 * ModelExchange: No restrictions on value changes.
 * CoSimulation: By convention, the variable is from a differential
 * The default is “continuous”.
 * [Note, the information about continuous states is defined with element
 * fmiModelDescription.ModelStructure.Derivatives]
 *
 * @author Lars Ivar Hatledal
 */
enum class Variability {

    /**
     * The value of the variable never changes.
     */
    CONSTANT,

    /**
     * The value of the variable is fixed after initialization, in other words
     * after fmi2ExitInitializationMode was called the variable value does not
     * change anymore.
     */
    FIXED,

    /**
     * The value of the variable is constant between external events
     * (ModelExchange) and between Communication Points (CoSimulation) due to
     * changing categories with causality = "parameter" or "input" and
     * variability = "tunable". Whenever a parameter or input signal with
     * variability = "tunable" changes, then an event is triggered externally
     * (ModelExchange) or the change is performed at the next Communication
     * Point (CoSimulation) and the categories with variability = "tunable" and
     * causality = "calculatedParameter" or "output" must be newly computed.
     */
    TUNABLE,

    /**
     * ModelExchange: The value of the variable is constant between external and
     * internal events (= time, state, step events defined implicitly in the
     * FMU). CoSimulation: By convention, the variable is from a “realAttribute” sampled
     * data system and its value is only changed at Communication Points (also
     * inside the slave).
     */
    DISCRETE,

    /**
     * Only a variable of type = “Real” can be “continuous”. ModelExchange: No
     * restrictions on value changes. CoSimulation: By convention, the variable
     * is from a differential
     */
    CONTINUOUS;

}
