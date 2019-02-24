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

import no.ntnu.ihb.fmi.fmi2.xml.Fmi2ModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.misc.SourceFile
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

    val maxOutputDerivativeOrder: Int?
    val canInterpolateInputs: Boolean
    val canRunAsynchronuously: Boolean
    val canProvideMaxStepSize: Boolean
    val canHandleVariableCommunicationStepSize: Boolean

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
class CoSimulationAttributesImpl(
        md: Fmi2ModelDescription
) : CommonFmuAttributes, CoSimulationAttributes, Serializable  {

    private val cs: Fmi2ModelDescription.CoSimulation = md.modelExchangeAndCoSimulation.find {
        it is Fmi2ModelDescription.CoSimulation
    } as Fmi2ModelDescription.CoSimulation

    override val modelIdentifier: String
        get() = cs.modelIdentifier

    override val needsExecutionTool: Boolean
        get() = cs.isNeedsExecutionTool

    override val canBeInstantiatedOnlyOncePerProcess: Boolean
        get() = cs.isCanBeInstantiatedOnlyOncePerProcess

    override val canNotUseMemoryManagementFunctions: Boolean
        get() = cs.isCanNotUseMemoryManagementFunctions

    override val canGetAndSetFMUstate: Boolean
        get() = cs.isCanGetAndSetFMUstate

    override val canSerializeFMUstate: Boolean
        get() = cs.isCanSerializeFMUstate

    override val providesDirectionalDerivative: Boolean
        get() = cs.isProvidesDirectionalDerivative

    override val sourceFiles: List<SourceFile>
        get() = cs.sourceFiles.file.map { SourceFile(it.name) }

    override val canHandleVariableCommunicationStepSize: Boolean
        get() = cs.isCanHandleVariableCommunicationStepSize

    override val canInterpolateInputs: Boolean
        get() = cs.isCanInterpolateInputs

    override val maxOutputDerivativeOrder: Int
        get() = cs.maxOutputDerivativeOrder.toInt()

    override val canRunAsynchronuously: Boolean
        get() = cs.isCanRunAsynchronuously

    override val canProvideMaxStepSize: Boolean
        get() = cs.isCanHandleVariableCommunicationStepSize

}


/**
 * @author Lars Ivar Hatledal
 */
class ModelExchangeAttributesImpl(
        md: Fmi2ModelDescription
) : CommonFmuAttributes, ModelExchangeAttributes, Serializable {

    private val me: Fmi2ModelDescription.ModelExchange = md.modelExchangeAndCoSimulation.find {
        it is Fmi2ModelDescription.ModelExchange
    } as Fmi2ModelDescription.ModelExchange

    override val modelIdentifier: String
        get() = me.modelIdentifier

    override val needsExecutionTool: Boolean
        get() = me.isNeedsExecutionTool

    override val canBeInstantiatedOnlyOncePerProcess: Boolean
        get() = me.isCanBeInstantiatedOnlyOncePerProcess

    override val canNotUseMemoryManagementFunctions: Boolean
        get() = me.isCanNotUseMemoryManagementFunctions

    override val canGetAndSetFMUstate: Boolean
        get() = me.isCanGetAndSetFMUstate

    override val canSerializeFMUstate: Boolean
        get() = me.isCanSerializeFMUstate

    override val providesDirectionalDerivative: Boolean
        get() = me.isProvidesDirectionalDerivative

    override val sourceFiles: List<SourceFile>
        get() = me.sourceFiles.file.map { SourceFile(it.name) }

    override val completedIntegratorStepNotNeeded: Boolean
        get() = me.isCompletedIntegratorStepNotNeeded

}