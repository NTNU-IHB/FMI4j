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

package no.mechatronics.sfi.fmi4j.modeldescription.me

import no.mechatronics.sfi.fmi4j.misc.SourceFile
import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
internal class ModelExchangeInfo {

    @XmlAttribute
    val modelIdentifier: String? = null

    @XmlAttribute
    val needsExecutionTool: Boolean = false

    @XmlAttribute
    val completedIntegratorStepNotNeeded: Boolean = false

    @XmlAttribute
    val canBeInstantiatedOnlyOncePerProcess: Boolean = false

    @XmlAttribute
    val canNotUseMemoryManagementFunctions: Boolean = false

    @XmlAttribute
    val canGetAndSetFMUstate: Boolean = false

    @XmlAttribute
    val canSerializeFMUstate: Boolean = false

    @XmlAttribute
    val providesDirectionalDerivative: Boolean = false

    @XmlElementWrapper(name = "SourceFiles")
    @XmlElement(name = "File")
    val sourceFiles: List<SourceFile>? = null

    override fun toString(): String {
        return "ModelExchangeInfo{modelIdentifier=$modelIdentifier, needsExecutionTool=$needsExecutionTool, completedIntegratorStepNotNeeded=$completedIntegratorStepNotNeeded, canBeInstantiatedOnlyOncePerProcess=$canBeInstantiatedOnlyOncePerProcess, canNotUseMemoryManagementFunctions=$canNotUseMemoryManagementFunctions, canGetAndSetFMUstate=$canGetAndSetFMUstate, canSerializeFMUstate=$canSerializeFMUstate, providesDirectionalDerivative=$providesDirectionalDerivative}"
    }

}


