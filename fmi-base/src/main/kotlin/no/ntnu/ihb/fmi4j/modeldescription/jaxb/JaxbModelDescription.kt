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

import no.ntnu.ihb.fmi4j.modeldescription.*
import no.ntnu.ihb.fmi4j.modeldescription.variables.ModelVariables

class JaxbModelDescription internal constructor(
        private val md: FmiModelDescription
): ModelDescriptionProvider {

    override val fmiVersion: String
        get() = md.fmiVersion
    override val modelName: String
        get() = md.modelName
    override val guid: String
        get() = md.guid
    override val license: String?
        get() = md.license
    override val copyright: String?
        get() = md.copyright
    override val author: String?
        get() = md.author
    override val version: String?
        get() = md.version
    override val description: String?
        get() = md.description
    override val generationTool: String?
        get() = md.generationTool
    override val variableNamingConvention: String?
        get() = md.variableNamingConvention
    override val generationDateAndTime: String?
        get() = md.generationDateAndTime
    override val defaultExperiment: DefaultExperiment?
        get() = md.defaultExperiment?.let { JaxbDefaultExperiment(it) }
    override val modelVariables: ModelVariables
        get() = JaxbModelVariables(md.modelVariables)
    override val modelStructure: ModelStructure
        get() = JaxbModelStructure(md.modelStructure)
    override val unitDefinitions: UnitDefinitions?
        get() = md.unitDefinitions?.unit?.map { JaxbUnit(it) }
    override val typeDefinitions: TypeDefinitions?
        get() = md.typeDefinitions?.simpleType?.map { JaxbSimpleType(it) }
    override val logCategories: JaxbLogCategories?
        get() = md.logCategories?.category?.map { JaxbLogCategory(it) }
    override val supportsCoSimulation: Boolean
        get() = md.coSimulation != null
    override val supportsModelExchange: Boolean
        get() = md.modelExchange != null

    override fun asCoSimulationModelDescription(): CoSimulationModelDescription {
        if (!supportsCoSimulation) {
            throw IllegalStateException()
        }
        return JaxbCoSimulationModelDescription(this, JaxbCoSimulationAttributes(md.coSimulation))
    }

    override fun asModelExchangeModelDescription(): ModelExchangeModelDescription {
        if (!supportsModelExchange) {
            throw IllegalStateException()
        }
        return JaxbModelExchangeModelDescription(this, JaxbModelExchangeAttributes(md.modelExchange))
    }

}

class JaxbCoSimulationModelDescription(
    md: JaxbModelDescription,
    override val attributes: CoSimulationAttributes
): CoSimulationModelDescription, ModelDescription by md

class JaxbModelExchangeModelDescription(
        md: JaxbModelDescription,
        override val attributes: ModelExchangeAttributes,
        override val numberOfEventIndicators: Int = 0
): ModelExchangeModelDescription, ModelDescription by md
