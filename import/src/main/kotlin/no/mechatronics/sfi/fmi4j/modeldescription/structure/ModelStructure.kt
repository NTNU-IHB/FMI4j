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

package no.mechatronics.sfi.fmi4j.modeldescription.structure

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper


/**
 * Defines the structure of the model. Especially, the ordered lists of
outputs, continuous-time states and initial unknowns (the unknowns
during Initialization Mode) are defined here. Furthermore, the
dependency of the unkowns from the knowns can be optionally
defined. [This information can be, for example used to compute
efficiently a sparse Jacobian for simulation or to utilize the
input/output dependency in order to detect that in some cases there
are actually no algebraic loops when connecting FMUs together].
 */
@XmlAccessorType(XmlAccessType.FIELD)
class ModelStructure {

    @XmlElementWrapper(name = "Outputs")
    @XmlElement(name = "Unknown")
    private val _outputs: List<Int>? = null

    val outputs: List<Int>
        get() {
            if (_outputs == null) {
                return emptyList()
            } else {
                return _outputs
            }
        }

    @XmlElementWrapper(name = "Derivatives")
    @XmlElement(name = "Unknown")
    private val _derivatives: List<Unknown>? = null

    val derivatives: List<Unknown>
        get() {
            if (_derivatives == null) {
                return emptyList()
            } else {
                return _derivatives
            }
        }

    @XmlElementWrapper(name = "InitialUnknowns")
    @XmlElement(name = "Unknown")
    private val _initialUnknowns: List<Unknown>? = null

    val initialUnknowns: List<Unknown>
        get() {
            if (_initialUnknowns == null) {
                return emptyList()
            } else {
                return _initialUnknowns
            }
        }

    override fun toString(): String {
        return "ModelStructure(outputs=$outputs, derivatives=$derivatives, initialUnknowns=$initialUnknowns)"
    }


}

