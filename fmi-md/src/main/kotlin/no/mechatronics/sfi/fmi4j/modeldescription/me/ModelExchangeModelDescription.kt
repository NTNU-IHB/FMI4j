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

package no.mechatronics.sfi.fmi4j.modeldescription.me

import no.mechatronics.sfi.fmi4j.modeldescription.*


interface ModelExchangeModelDescription : ExtendedModelDescription {

    val completedIntegratorStepNotNeeded: Boolean

}

/**
 *
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
class ModelExchangeModelDescriptionImpl(
         private val modelDescription: ModelDescription,
         private val me: ModelExchangeXmlNode
) : ModelDescription by modelDescription, ModelExchangeModelDescription {


    override val numberOfEventIndicators: Int
        get() = modelDescription.numberOfEventIndicators
    override val modelIdentifier: String
        get() = me.modelIdentifier
    override val needsExecutionTool: Boolean
        get() = me.needsExecutionTool
    override val canBeInstantiatedOnlyOncePerProcess: Boolean
        get() = me.canBeInstantiatedOnlyOncePerProcess
    override val canNotUseMemoryManagementFunctions: Boolean
        get() = me.canNotUseMemoryManagementFunctions
    override val canGetAndSetFMUstate: Boolean
        get() = me.canGetAndSetFMUstate
    override val canSerializeFMUstate: Boolean
        get() = me.canSerializeFMUstate
    override val providesDirectionalDerivative: Boolean
        get() = me.providesDirectionalDerivative
    override val sourceFiles: List<SourceFile>
        get() = me.sourceFiles
    override val completedIntegratorStepNotNeeded: Boolean
        get() = me.completedIntegratorStepNotNeeded

}

