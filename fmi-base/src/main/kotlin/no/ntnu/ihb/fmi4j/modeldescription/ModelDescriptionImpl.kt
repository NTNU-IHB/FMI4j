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

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import no.ntnu.ihb.fmi.fmi2.xml.Fmi2ModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.logging.LogCategories
import no.ntnu.ihb.fmi4j.modeldescription.logging.LogCategory
import no.ntnu.ihb.fmi4j.modeldescription.misc.*
import no.ntnu.ihb.fmi4j.modeldescription.misc.Unit
import no.ntnu.ihb.fmi4j.modeldescription.structure.ModelStructure
import no.ntnu.ihb.fmi4j.modeldescription.structure.Unknown
import no.ntnu.ihb.fmi4j.modeldescription.variables.IntegerVariable
import no.ntnu.ihb.fmi4j.modeldescription.variables.ModelVariables
import no.ntnu.ihb.fmi4j.modeldescription.variables.ScalarVariableImpl
import java.io.Serializable
import java.util.*

/**
 * Default ModelDescription implementation
 *
 * @author Lars Ivar Hatledal
 */
class ModelDescriptionImpl(
        private val md: Fmi2ModelDescription
) : ModelDescriptionProvider, Serializable {

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

    override val generationDateAndTime: GregorianCalendar?
        get() = md.generationDateAndTime?.toGregorianCalendar()

    override val variableNamingConvention: String?
        get() = md.variableNamingConvention

    override val defaultExperiment: DefaultExperiment? =
            md.defaultExperiment?.let {
                DefaultExperiment(
                        startTime = it.startTime,
                        stopTime = it.stopTime,
                        tolerance = it.tolerance,
                        stepSize = it.stepSize
                )
            }


    override val modelVariables: ModelVariables =
            md.modelVariables.scalarVariable.map {
                ScalarVariableImpl(it)
            }.let {
                ModelVariables(it)
            }

    override val logCategories: LogCategories? =
            md.logCategories?.category?.map {
                LogCategory(
                        name = it.name,
                        description = it.description
                )
            }


    override val unitDefinitions: UnitDefinitions? =
            md.unitDefinitions?.let {
                it.unit?.map { u ->
                    Unit(
                            name = u.name,
                            baseUnit = u.baseUnit?.let { bu ->
                                BaseUnit(
                                        kg = bu.kg,
                                        K = bu.k,
                                        m = bu.m,
                                        mol = bu.mol,
                                        s = bu.s,
                                        A = bu.a,
                                        cd = bu.cd,
                                        rad = bu.rad,
                                        factor = bu.factor,
                                        offset = bu.offset

                                )
                            },
                            displayUnits = u.displayUnit?.map { du ->
                                DisplayUnit(
                                        name = du.name,
                                        factor = du.factor,
                                        offset = du.offset
                                )
                            }
                    )
                }
            }


    override val typeDefinitions: TypeDefinitions? =
            md.typeDefinitions?.simpleType?.map { type ->
                SimpleType(
                        name = type.name,
                        description = type.description
                )
            }


    override val modelStructure: ModelStructure =
            md.modelStructure.let { structure ->
                ModelStructure(
                        outputs = structure.outputs?.unknown?.map { unknown ->
                            Unknown(
                                    index = unknown.index.toInt(),
                                    dependencies = unknown.dependencies.map { it.toInt() },
                                    dependenciesKind = unknown.dependenciesKind
                            )
                        } ?: emptyList(),
                        derivatives = structure.derivatives?.unknown?.map { unknown ->
                            Unknown(
                                    index = unknown.index.toInt(),
                                    dependencies = unknown.dependencies.map { it.toInt() },
                                    dependenciesKind = unknown.dependenciesKind
                            )
                        } ?: emptyList(),
                        initialUnknowns = structure.derivatives?.unknown?.map { unknown ->
                            Unknown(
                                    index = unknown.index.toInt(),
                                    dependencies = unknown.dependencies.map { it.toInt() },
                                    dependenciesKind = unknown.dependenciesKind
                            )
                        } ?: emptyList()
                )
            }


    override val supportsCoSimulation: Boolean
        get() = md.modelExchangeAndCoSimulation.find { it is Fmi2ModelDescription.CoSimulation } != null

    override val supportsModelExchange: Boolean
        get() = md.modelExchangeAndCoSimulation.find { it is Fmi2ModelDescription.ModelExchange } != null

    override fun asCoSimulationModelDescription(): CoSimulationModelDescription {
        if (!supportsCoSimulation) {
            throw IllegalStateException("FMU does not support Co-simulation!")
        }
        return CoSimulationModelDescriptionImpl()
    }

    override fun asModelExchangeModelDescription(): ModelExchangeModelDescription {
        if (!supportsModelExchange) {
            throw IllegalStateException("FMU does not support Model Exchange!")
        }
        return ModelExchangeModelDescriptionImpl()
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

    fun toJson(): String {
        return GsonBuilder()
                .setPrettyPrinting()
                .create().toJson(md)
    }

    override fun toString(): String {
        return "ModelDescriptionImpl(\n$stringContent\n)"
    }

    inner class CoSimulationModelDescriptionImpl : CoSimulationModelDescription, ModelDescription by this, CoSimulationAttributes by CoSimulationAttributesImpl(md)

    inner class ModelExchangeModelDescriptionImpl : ModelExchangeModelDescription, ModelDescription by this, ModelExchangeAttributes by ModelExchangeAttributesImpl(md) {
        override val numberOfEventIndicators: Int = md.numberOfEventIndicators.toInt()
    }

    companion object {

        @JvmStatic
        fun fromJson(json: String): ModelDescription {
            return GsonBuilder()
                    .addDeserializationExclusionStrategy(object: ExclusionStrategy {

                        override fun shouldSkipField(f: FieldAttributes): Boolean {
                            return f.name == "generationDateAndTime"
                        }

                        override fun shouldSkipClass(clazz: Class<*>): Boolean {
                            return false
                        }
                    })
                    .create().fromJson(json, Fmi2ModelDescription::class.java).let {
                        ModelDescriptionImpl(it)
                    }
        }

    }

}

