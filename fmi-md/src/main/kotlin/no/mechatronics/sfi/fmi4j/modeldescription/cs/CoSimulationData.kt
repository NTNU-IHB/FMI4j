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

package no.mechatronics.sfi.fmi4j.modeldescription.cs

import no.mechatronics.sfi.fmi4j.modeldescription.misc.SourceFile
import no.mechatronics.sfi.fmi4j.modeldescription.misc.SourceFileImpl
import java.io.Serializable
import javax.xml.bind.annotation.*

/**
 * @author Lars Ivar Hatledal
 */
interface CoSimulationData {
    
    val modelIdentifier: String
    val needsExecutionTool: Boolean
    val canHandleVariableCommunicationStepSize: Boolean
    val canInterpolateInputs: Boolean
    val maxOutputDerivativeOrder: Int
    val canRunAsynchronuosly: Boolean
    val canBeInstantiatedOnlyOncePerProcess: Boolean
    val canNotUseMemoryManagementFunctions: Boolean
    val canGetAndSetFMUstate: Boolean
    val canSerializeFMUstate: Boolean
    val providesDirectionalDerivative: Boolean
    val canGetAndSetFMUState: Boolean
    val sourceFiles: List<SourceFile>
    
}

/**
 * @author Lars Ivar Hatledal
 */
@XmlAccessorType(XmlAccessType.FIELD)
internal class CoSimulationDataImpl : CoSimulationData, Serializable {

    @XmlAttribute
    override lateinit var modelIdentifier: String

    @XmlAttribute
    override val needsExecutionTool: Boolean = false

    @XmlAttribute
    override val canHandleVariableCommunicationStepSize: Boolean = false

    @XmlAttribute
    override val canInterpolateInputs: Boolean = false

    @XmlAttribute
    override val maxOutputDerivativeOrder: Int = 0

    @XmlAttribute
    override val canRunAsynchronuosly: Boolean = false

    @XmlAttribute
    override val canBeInstantiatedOnlyOncePerProcess: Boolean = false

    @XmlAttribute
    override val canNotUseMemoryManagementFunctions: Boolean = false

    @XmlAttribute
    override val canGetAndSetFMUstate: Boolean = false

    @XmlAttribute
    override val canSerializeFMUstate: Boolean = false

    @XmlAttribute
    override val providesDirectionalDerivative: Boolean = false

    @XmlAttribute
    override val canGetAndSetFMUState: Boolean = false

    @XmlElementWrapper(name = "SourceFiles")
    @XmlElement(name = "File")
    private var _sourceFiles: List<SourceFileImpl>? = null

    @delegate:Transient
    override val sourceFiles: List<SourceFile> by lazy {
        _sourceFiles ?: emptyList()
    }

    override fun toString(): String {
        return "CoSimulationXmlNode{modelIdentifier=$modelIdentifier, needsExecutionTool=$needsExecutionTool, canHandleVariableCommunicationStepSize=$canHandleVariableCommunicationStepSize, canInterpolateInputs=$canInterpolateInputs, maxOutputDerivativeOrder=$maxOutputDerivativeOrder, canRunAsynchronuosly=$canRunAsynchronuosly, canBeInstantiatedOnlyOncePerProcess=$canBeInstantiatedOnlyOncePerProcess, canNotUseMemoryManagementFunctions=$canNotUseMemoryManagementFunctions, canGetAndSetFMUstate=$canGetAndSetFMUstate, canSerializeFMUstate=$canSerializeFMUstate, providesDirectionalDerivative=$providesDirectionalDerivative}"
    }

}