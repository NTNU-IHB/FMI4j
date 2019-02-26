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

}

/**
 * Represents the content within the <ModelExchange></> XML element
 *
 * @author Lars Ivar Hatledal
 */
interface ModelExchangeAttributes : CommonFmuAttributes {

    val completedIntegratorStepNotNeeded: Boolean

}
