/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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

package no.ntnu.ihb.fmi4j.modeldescription.jaxb

import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationAttributes
import no.ntnu.ihb.fmi4j.modeldescription.ModelExchangeAttributes
import no.ntnu.ihb.fmi4j.modeldescription.SourceFile

class JaxbCoSimulationAttributes internal constructor(
        private val cs: FmiModelDescription.CoSimulation
): CoSimulationAttributes {

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
        get() = cs.sourceFiles?.file?.map { JaxbSourcefile(it) } ?: emptyList()
    override val canHandleVariableCommunicationStepSize: Boolean
        get() = cs.isCanHandleVariableCommunicationStepSize
    override val canInterpolateInputs: Boolean
        get() = cs.isCanInterpolateInputs
    override val maxOutputDerivativeOrder: Int
        get() = cs.maxOutputDerivativeOrder.toInt()
    override val canRunAsynchronuously: Boolean
        get() = cs.isCanRunAsynchronuously

}

class JaxbModelExchangeAttributes internal constructor(
        private val me: FmiModelDescription.ModelExchange
): ModelExchangeAttributes {

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
        get() = me.sourceFiles?.file?.map { JaxbSourcefile(it) } ?: emptyList()

    override val completedIntegratorStepNotNeeded: Boolean
        get() = me.isCompletedIntegratorStepNotNeeded
}