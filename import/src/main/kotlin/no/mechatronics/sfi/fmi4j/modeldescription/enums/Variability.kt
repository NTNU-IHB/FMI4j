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

package no.mechatronics.sfi.fmi4j.modeldescription.enums

import javax.xml.bind.annotation.adapters.XmlAdapter

enum class Variability {

    /**
     * The value of the variable never changes.
     */
    constant,
    /**
     * The value of the variable is fixed after initialization, in other words
     * after fmi2ExitInitializationMode was called the variable value does not
     * change anymore.
     */
    fixed,
    /**
     * The value of the variable is constant between external events
     * (ModelExchange) and between Communication Points (CoSimulation) due to
     * changing variables with causality = "parameter" or "input" and
     * variability = "tunable". Whenever a parameter or input signal with
     * variability = "tunable" changes, then an event is triggered externally
     * (ModelExchange) or the change is performed at the next Communication
     * Point (CoSimulation) and the variables with variability = "tunable" and
     * causality = "calculatedParameter" or "output" must be newly computed.
     */
    tunable,
    /**
     * ModelExchange: The value of the variable is constant between external and
     * internal events (= time, state, step events defined implicitly in the
     * FMU). CoSimulation: By convention, the variable is from a “realAttribute” sampled
     * data system and its value is only changed at Communication Points (also
     * inside the slave).
     */
    discrete,
    /**
     * Only a variable of type = “Real” can be “continuous”. ModelExchange: No
     * restrictions on value changes. CoSimulation: By convention, the variable
     * is from a differential
     */
    continuous;

}

class VariabilityAdapter : XmlAdapter<String, Variability>() {

    @Override
    override fun unmarshal(v: String) : Variability {
        return Variability.valueOf(v)
    }

    @Override
    override fun marshal(v: Variability) : String {
        TODO("not implemented")
    }

}