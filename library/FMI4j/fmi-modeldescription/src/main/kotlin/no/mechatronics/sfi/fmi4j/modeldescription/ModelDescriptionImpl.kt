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

package no.mechatronics.sfi.fmi4j.modeldescription

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescriptionImpl
import no.mechatronics.sfi.fmi4j.modeldescription.logging.LogCategories
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescriptionImpl
import no.mechatronics.sfi.fmi4j.modeldescription.misc.*
import no.mechatronics.sfi.fmi4j.modeldescription.structure.ModelStructureImpl
import no.mechatronics.sfi.fmi4j.modeldescription.variables.ModelVariablesImpl
import java.io.Serializable


/**
 * Default ModelDescription implementation
 *
 * @author Lars Ivar Hatledal
 */
@JacksonXmlRootElement(localName = "fmiModelDescription")
class ModelDescriptionImpl(

        @JacksonXmlProperty()
        override var fmiVersion: String,

        @JacksonXmlProperty()
        override val modelName: String,

        @JacksonXmlProperty()
        override val guid: String,

        @JacksonXmlProperty()
        override val license: String? = null,

        @JacksonXmlProperty()
        override val copyright: String? = null,

        @JacksonXmlProperty()
        override val author: String? = null,

        @JacksonXmlProperty()
        override val version: String? = null,

        @JacksonXmlProperty()
        override val description: String? = null,

        @JacksonXmlProperty()
        override val generationTool: String? = null,

        @JacksonXmlProperty()
        override val generationDateAndTime: String? = null,

        @JacksonXmlProperty()
        override val variableNamingConvention: VariableNamingConvention? = null,

        @JacksonXmlProperty(localName = "DefaultExperiment")
        override val defaultExperiment: DefaultExperiment? = null,

        @JacksonXmlProperty(localName = "ModelVariables")
        override var modelVariables: ModelVariablesImpl,

        @JacksonXmlProperty(localName = "ModelStructure")
        override var modelStructure: ModelStructureImpl,

        @JacksonXmlProperty(localName = "LogCategories")
        override val logCategories: LogCategories? = null,

        @JacksonXmlProperty(localName = "UnitDefinitions")
        override val unitDefinitions: UnitDefinitions? = null,

        @JacksonXmlProperty(localName = "TypeDefinitions")
        override val typeDefinitions: TypeDefinitions? = null,

        @JacksonXmlProperty(localName = "CoSimulation")
        private val cs: CoSimulationDataImpl? = null,

        @JacksonXmlProperty(localName = "ModelExchange")
        private val me: ModelExchangeDataImpl? = null,

        /**
         * The (fixed) number of event indicators for an FMU based on FMI for Model Exchange.
         * For Co-Simulation, this value is ignored
         */
        val numberOfEventIndicators: Int = 0


) : ModelDescription, Serializable {

    @Transient
    private var _modelExchangeModelDescription: ModelExchangeModelDescription? = null

    private val modelExchangeModelDescription: ModelExchangeModelDescription?
        get() {
            if (_modelExchangeModelDescription == null && supportsModelExchange) {
                _modelExchangeModelDescription = ModelExchangeModelDescriptionImpl(this, me!!)
            }
            return _modelExchangeModelDescription
        }

    @Transient
    private var _coSimulationModelDescription: CoSimulationModelDescription? = null

    private val coSimulationModelDescription: CoSimulationModelDescription?
        get() {
            if (_coSimulationModelDescription == null && supportsCoSimulation) {
                _coSimulationModelDescription = CoSimulationModelDescriptionImpl(this, cs!!)
            }
            return _coSimulationModelDescription
        }

    override val supportsModelExchange: Boolean
        get() = me != null

    override val supportsCoSimulation: Boolean
        get() = cs != null

    /**
     * @throws IllegalStateException if FMU does not support Co-Simulation
     */
    override fun asCoSimulationModelDescription(): CoSimulationModelDescription = coSimulationModelDescription
            ?: throw IllegalStateException("FMU does not support Co-Simulation: modelDescription.xml does not contain a <CoSimulation> tag!")

    /**
     * @throws IllegalStateException if FMU does not support Model Exchange
     */
    override fun asModelExchangeModelDescription(): ModelExchangeModelDescription = modelExchangeModelDescription
            ?: throw IllegalStateException("FMU does not support Model Exchange: modelDescription.xml does not contain a <ModelExchange> tag!")

    internal val stringContent: String
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
                defaultExperiment?.let { "defaultExperiment=$defaultExperiment" },
                unitDefinitions?.let { "unitDefinitions=$unitDefinitions" }
        ).joinToString("\n")


    override fun toString(): String {
        return "ModelDescriptionImpl(\n$stringContent\n)"
    }

}
