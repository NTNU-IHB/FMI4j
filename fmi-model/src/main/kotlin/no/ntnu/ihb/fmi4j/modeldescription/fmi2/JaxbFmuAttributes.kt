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

package no.ntnu.ihb.fmi4j.modeldescription.fmi2

import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationAttributes
import no.ntnu.ihb.fmi4j.modeldescription.ModelExchangeAttributes

fun FmiModelDescription.CoSimulation.convert(): CoSimulationAttributes {

    return CoSimulationAttributes(
            modelIdentifier = this@convert.modelIdentifier,
            needsExecutionTool = this@convert.isNeedsExecutionTool,
            canBeInstantiatedOnlyOncePerProcess = this@convert.isCanBeInstantiatedOnlyOncePerProcess,
            canNotUseMemoryManagementFunctions = this@convert.isCanNotUseMemoryManagementFunctions,
            canGetAndSetFMUstate = this@convert.isCanGetAndSetFMUstate,
            canSerializeFMUstate = this@convert.isCanSerializeFMUstate,
            providesDirectionalDerivative = this@convert.isProvidesDirectionalDerivative,
            canHandleVariableCommunicationStepSize = this@convert.isCanHandleVariableCommunicationStepSize,
            canRunAsynchronuously = this@convert.isCanRunAsynchronuously,
            canInterpolateInputs = this@convert.isCanInterpolateInputs,
            maxOutputDerivativeOrder = this@convert.getMaxOutputDerivativeOrder().toInt(),
            sourceFiles = this@convert.sourceFiles?.file?.map { it.convert() } ?: emptyList()
    )

}

fun FmiModelDescription.ModelExchange.convert(): ModelExchangeAttributes {
    return ModelExchangeAttributes(
            modelIdentifier = this@convert.modelIdentifier,
            needsExecutionTool = this@convert.isNeedsExecutionTool,
            canBeInstantiatedOnlyOncePerProcess = this@convert.isCanBeInstantiatedOnlyOncePerProcess,
            canNotUseMemoryManagementFunctions = this@convert.isCanNotUseMemoryManagementFunctions,
            canGetAndSetFMUstate = this@convert.isCanGetAndSetFMUstate,
            canSerializeFMUstate = this@convert.isCanSerializeFMUstate,
            providesDirectionalDerivative = this@convert.isProvidesDirectionalDerivative,
            completedIntegratorStepNotNeeded = this@convert.isCompletedIntegratorStepNotNeeded,
            sourceFiles = this@convert.sourceFiles?.file?.map { it.convert() } ?: emptyList()
    )

}
