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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.misc.SourceFile
import no.ntnu.ihb.fmi4j.modeldescription.misc.SourceFileImpl
import java.io.Serializable

/**
 * Represents the common content within the <CoSimulation> and <ModelExchange> XML elements
 *
 * @author Lars Ivar Hatledal
 */
interface CommonFmuAttributes {

    val modelIdentifier: String
    val needsExecutionTool: Boolean
    val canBeInstantiatedOnlyOncePerProcess: Boolean
    val canNotUseMemoryManagementFunctions: Boolean
    val canGetAndSetFMUstate: Boolean
    val canSerializeFMUstate: Boolean
    val providesDirectionalDerivative: Boolean
    val sourceFiles: List<SourceFile>

}

/**
 * Represents the content within the <CoSimulation></> XML element
 *
 * @author Lars Ivar Hatledal
 */
interface CoSimulationAttributes : CommonFmuAttributes {

    val canHandleVariableCommunicationStepSize: Boolean
    val canInterpolateInputs: Boolean
    val maxOutputDerivativeOrder: Int
    val canRunAsynchronuously: Boolean
    val canProvideMaxStepSize: Boolean

}

/**
 * Represents the content within the <ModelExchange></> XML element
 *
 * @author Lars Ivar Hatledal
 */
interface ModelExchangeAttributes : CommonFmuAttributes {

    val completedIntegratorStepNotNeeded: Boolean

}

/**
 * @author Lars Ivar Hatledal
 */
sealed class CommonFmuAttributesImpl : CommonFmuAttributes, Serializable {

    @JacksonXmlProperty
    override val modelIdentifier: String = ""

    @JacksonXmlProperty
    override val needsExecutionTool: Boolean = false

    @JacksonXmlProperty
    override val canNotUseMemoryManagementFunctions: Boolean = false

    @JacksonXmlProperty
    override val canGetAndSetFMUstate: Boolean = false

    @JacksonXmlProperty
    override val canSerializeFMUstate: Boolean = false

    @JacksonXmlProperty
    override val providesDirectionalDerivative: Boolean = false

    @JacksonXmlProperty
    override val canBeInstantiatedOnlyOncePerProcess: Boolean = false

    @JacksonXmlElementWrapper(localName = "SourceFiles")
    @JacksonXmlProperty(localName = "File")
    override val sourceFiles: List<SourceFileImpl> = emptyList()

}

/**
 * @author Lars Ivar Hatledal
 */
data class CoSimulationAttributesImpl(

        @JacksonXmlProperty
        override val canHandleVariableCommunicationStepSize: Boolean = false,

        @JacksonXmlProperty
        override val canInterpolateInputs: Boolean = false,

        @JacksonXmlProperty
        override val maxOutputDerivativeOrder: Int = 0,

        @JacksonXmlProperty
        override val canRunAsynchronuously: Boolean = false,

        @JacksonXmlProperty
        override val canProvideMaxStepSize: Boolean = false


) : CommonFmuAttributesImpl(), CoSimulationAttributes

/**
 * @author Lars Ivar Hatledal
 */
data class ModelExchangeAttributesImpl(

        @JacksonXmlProperty
        override val completedIntegratorStepNotNeeded: Boolean = false

) : CommonFmuAttributesImpl(), ModelExchangeAttributes