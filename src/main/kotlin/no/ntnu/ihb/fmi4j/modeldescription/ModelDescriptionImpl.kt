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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import no.ntnu.ihb.fmi4j.modeldescription.logging.LogCategories
import no.ntnu.ihb.fmi4j.modeldescription.misc.DefaultExperimentImpl
import no.ntnu.ihb.fmi4j.modeldescription.misc.TypeDefinitions
import no.ntnu.ihb.fmi4j.modeldescription.misc.UnitDefinitions
import no.ntnu.ihb.fmi4j.modeldescription.structure.ModelStructure
import no.ntnu.ihb.fmi4j.modeldescription.structure.ModelStructureImpl
import no.ntnu.ihb.fmi4j.modeldescription.variables.ModelVariablesImpl
import java.io.Serializable


/**
 * Default ModelDescription implementation
 *
 * @author Lars Ivar Hatledal
 */
@JacksonXmlRootElement(localName = "fmiModelDescription")
@JsonIgnoreProperties(ignoreUnknown = true)
class ModelDescriptionImpl(

        @JacksonXmlProperty
        override val fmiVersion: String,

        @JacksonXmlProperty
        override val modelName: String,

        @JacksonXmlProperty
        override val guid: String,

        @JacksonXmlProperty
        override val license: String? = null,

        @JacksonXmlProperty
        override val copyright: String? = null,

        @JacksonXmlProperty
        override val author: String? = null,

        @JacksonXmlProperty
        override val version: String? = null,

        @JacksonXmlProperty
        override val description: String? = null,

        @JacksonXmlProperty
        override val generationTool: String? = null,

        @JacksonXmlProperty
        override val generationDateAndTime: String? = null,

        @JacksonXmlProperty
        override val variableNamingConvention: String? = null,

        @JacksonXmlProperty(localName = "DefaultExperiment")
        override val defaultExperiment: DefaultExperimentImpl? = null,

        @JacksonXmlProperty(localName = "ModelVariables")
        override val modelVariables: ModelVariablesImpl,

        @JacksonXmlProperty(localName = "LogCategories")
        override val logCategories: LogCategories? = null,

        @JacksonXmlProperty(localName = "UnitDefinitions")
        override val unitDefinitions: UnitDefinitions? = null,

        @JacksonXmlProperty(localName = "TypeDefinitions")
        override val typeDefinitions: TypeDefinitions? = null,

        @JacksonXmlProperty(localName = "CoSimulation")
        private val coSimulationAttributes: CoSimulationAttributesImpl? = null,

        @JacksonXmlProperty(localName = "ModelExchange")
        private val modelExchangeAttributes: ModelExchangeAttributesImpl? = null,

        @JacksonXmlProperty
        private val numberOfEventIndicators: Int = 0


) : ModelDescriptionProvider, Serializable {

    @JacksonXmlProperty(localName = "ModelStructure")
    private val _modelStructure: ModelStructureImpl? = null

    override val modelStructure: ModelStructure
        get() =_modelStructure!!

    override val supportsCoSimulation: Boolean
        get() = coSimulationAttributes != null

    override val supportsModelExchange: Boolean
        get() = modelExchangeAttributes != null

    override fun asCoSimulationModelDescription(): CoSimulationModelDescription {
        return CoSimulationModelDescriptionImpl(this, coSimulationAttributes
                ?: throw IllegalStateException("No CoSimulation attributes present in ModelDescription!"))
    }

    override fun asModelExchangeModelDescription(): ModelExchangeModelDescription {
        return ModelExchangeModelDescriptionImpl(this, modelExchangeAttributes
                ?: throw IllegalStateException("No ModelExchange attributes in ModelDescription!"),
                numberOfEventIndicators)
    }

    private val stringContent: String
        get() = listOfNotNull(
                "fmiVersion=$fmiVersion",
                "modelName=$modelName",
                "guid=$guid",
                license?.let { "license=$license" },
                copyright?.let { "copyright=$copyright" },
                author?.let { "author=$author" },
                version?.let { "version=$version" },
                description?.let { "description=$description" },
                generationTool?.let { "generationTool=$generationTool" },
                variableNamingConvention?.let { "variableNamingConvention=$variableNamingConvention" },
                generationDateAndTime?.let { "generationDateAndTime=$generationDateAndTime" },
                defaultExperiment?.let { "defaultExperiment=$defaultExperiment" }
        ).joinToString("\n")

    override fun toString(): String {
        return "ModelDescriptionImpl(\n$stringContent\n)"
    }

}

class CoSimulationModelDescriptionImpl(
        md: ModelDescription,
        cs: CoSimulationAttributes
): CoSimulationModelDescription, ModelDescription by md, CoSimulationAttributes by cs


class ModelExchangeModelDescriptionImpl(
        md: ModelDescription,
        me: ModelExchangeAttributes,
        override val numberOfEventIndicators: Int = 0

): ModelExchangeModelDescription, ModelDescription by md, ModelExchangeAttributes by me