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

import no.mechatronics.sfi.fmi4j.misc.SourceFile
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationInfo
import no.mechatronics.sfi.fmi4j.modeldescription.log.Category
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeInfo
import no.mechatronics.sfi.fmi4j.modeldescription.structure.ModelStructure
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.StringReader
import java.lang.IllegalArgumentException
import java.net.URL
import java.nio.charset.Charset
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.xml.bind.JAXB
import javax.xml.bind.annotation.*

private const val MODEL_DESC_FILE = "modelDescription.xml"


object ModelDescriptionParser {

    @JvmStatic
    fun parseModelDescription(xml: String): IModelDescription = parse(xml, ModelDescriptionXmlTemplate::class.java).generate()
    @JvmStatic
    fun parseModelDescription(url: URL): IModelDescription = parse(url.openStream(), ModelDescriptionXmlTemplate::class.java).generate()
    @JvmStatic
    fun parse(file: File): IModelDescription = parse(FileInputStream(file), ModelDescriptionXmlTemplate::class.java).generate()
    @JvmStatic
    fun parseModelDescription(inputStream: InputStream): IModelDescription = parse(inputStream, ModelDescriptionXmlTemplate::class.java).generate()

    internal fun <T: ModelDescriptionXmlTemplate> parse(xml: String, type: Class<T>): T = JAXB.unmarshal(StringReader(xml), type)
    internal fun <T : ModelDescriptionXmlTemplate> parse(stream: InputStream, type: Class<T>): T = exctractModelDescriptionXml(stream).let { parse(it, type) }

    @JvmStatic
    fun exctractModelDescriptionXml(stream: InputStream): String {

        ZipInputStream(stream).use {

            var nextEntry: ZipEntry? = it.nextEntry
            while (nextEntry != null) {

                val name = nextEntry.name
                if (name == MODEL_DESC_FILE) {
                    return IOUtils.toString(it, Charset.forName("UTF-8"))
                }

                nextEntry = it.nextEntry
            }

        }

        throw IllegalArgumentException("Input is not an valid FMU! No $MODEL_DESC_FILE present!")

    }


}


interface IModelDescription {

    /**
     * Version of “FMI for Model Exchange or Co-Simulation” that was used to
     * generate the XML file. The value for this version is “2.0”. Future minor
     * revisions are denoted as “2.0.1”, “2.0.2”, …
     */
    val fmiVersion: String
    /**
     * The name of the model as used in the modeling environment that
     * generated the XML file, such as
     * “Modelica.Mechanics.Rotational.Examples.CoupledClutches”.
     */
    val modelName: String
    /**
     * The “Globally Unique IDentifier” is a string that is used to check that the
     * XML file is compatible with the C functions of the FMU. Typically when generating the XML file, a fingerprint of the “relevant” information is
     * stored as guid and in the generated C-function.
     */
    val guid: String
    /**
     * Optional information on the intellectual property licensing for this FMU.
     * [Example: license = “BSD license <license text or link to license>”].
     */
    val license: String?
    /**
     * Optional information on the intellectual property copyright for this FMU.
     * [Example: copyright = “© My Company 2011”].
     */
    val copyright: String?
    /**
     * Optional string with the name and organization of the model author.
     */
    val author: String?
    /**
     * Optional version of the model, for example “1.0”.
     */

    val version: String?
    /**
     * Optional string with a brief description of the model.
     */
    val description: String?
    /**
     * Optional name of the tool that generated the XML file.
     */
    val generationTool: String?
    /**
     * Defines whether the variable names in “ModelVariables /
     * ScalarVariable / name” and in “TypeDefinitions / Type /
     * name” follow a particular convention. For the details, see section 2.2.9.
     * Currently standardized are:
     * • “FLAT”: A list of strings (the default).
     * • “STRUCTURED“: Hierarchical names with “.” as hierarchy separator,
     * and with array elements and derivative characterization.
     */
    val variableNamingConvention: VariableNamingConvention?
    /**
     * Optional date and time when the XML file was generated. The format is
     * a subset of “xs:dateTime” and should be: “YYYY-MM-DDThh:mm:ssZ"
     * (with one “T” between date and time; “Z” characterizes the Zulu time
     * zone, in other words Greenwich meantime).
     * [Example: "2009-12-08T14:33:22Z"].
     */
    val generationDateAndTime: String?

    val defaultExperiment: DefaultExperiment?

    /**
     * The central FMU data structure defining all variables of the FMU that
     * are visible/accessible via the FMU functions.
     */
    val modelVariables: ModelVariables

    /**
     * Defines the structure of the model. Especially, the ordered lists of
     * outputs, continuous-time states and initial unknowns (the unknowns
     * during Initialization Mode) are defined here. Furthermore, the
     * dependency of the unkowns from the knowns can be optionally
     * defined. [This information can be, for example used to compute
     * efficiently a sparse Jacobian for simulation or to utilize the
     * input/output dependency in order to detect that in some cases there
     * are actually no algebraic loops when connecting FMUs together
     */
    val modelStructure: ModelStructure

    /**
     * A global list of log categories that can be set to define the log
     * information that is supported from the FMU.
     */
    val logCategories: List<Category>?


    val isCoSimulationFmu: Boolean

    val isMeSimulationFmu: Boolean

    /**
     * Short class name according to C syntax, for
     * example “A_B_C”. Used as prefix for FMI
     * functions if the functions are provided in C
     * source code or in static libraries, but not if
     * the functions are provided by a
     * DLL/SharedObject. modelIdentifier is
     * also used as name of the static library or
     * DLL/SharedObject . See also section 2.1.1.
     *
     * @return
     */
    val modelIdentifier: String

    val needsExecutionTool: Boolean

    val canBeInstantiatedOnlyOncePerProcess: Boolean

    val canNotUseMemoryManagementFunctions: Boolean

    val canGetAndSetFMUstate: Boolean

    val canSerializeFMUstate: Boolean

    val providesDirectionalDerivative: Boolean

    val sourceFiles: List<SourceFile>

    val numberOfContinuousStates: Int

}

@XmlRootElement(name = "fmiModelDescription")
@XmlAccessorType(XmlAccessType.FIELD)
internal open class ModelDescriptionXmlTemplate {

    @XmlAttribute
    lateinit var fmiVersion: String

    @XmlAttribute
    lateinit var modelName: String

    @XmlAttribute
    lateinit var guid: String

    @XmlAttribute
    val license: String? = null

    @XmlAttribute
    val copyright: String? = null

    @XmlAttribute
    val author: String? = null

    @XmlAttribute
    val version: String? = null

    @XmlAttribute
    val description: String? = null

    @XmlAttribute
    val generationTool: String? = null

    @XmlAttribute
    val variableNamingConvention: VariableNamingConvention? = null

    @XmlAttribute
    val generationDateAndTime: String? = null

    @XmlElement(name = "DefaultExperiment")
    val defaultExperiment: DefaultExperiment? = null

    /**
     * The central FMU data structure defining all variables of the FMU that
     * are visible/accessible via the FMU functions.
     */
    @XmlElement(name = "ModelVariables")
    lateinit var modelVariables: ModelVariables

    @XmlElement(name = "ModelStructure")
    lateinit var modelStructure: ModelStructure

    /**
     * A global list of log categories that can be set to define the log
     * information that is supported from the FMU.
     */
    @XmlElementWrapper(name = "LogCategories")
    @XmlElement(name = "Category")
    val logCategories: List<Category>? = null


    @XmlElement(name = "CoSimulation")
    internal val cs: CoSimulationInfo? = null

    @XmlElement(name = "ModelExchange")
    internal val me: ModelExchangeInfo? = null

    open fun generate(): IModelDescription = ModelDescriptionImpl()

    private val that = this

    inner class ModelDescriptionImpl : IModelDescription {
        override val fmiVersion: String
            get() = that.fmiVersion
        override val modelName: String
            get() = that.modelName
        override val guid: String
            get() = that.guid
        override val license: String?
            get() = that.license
        override val copyright: String?
            get() = that.copyright
        override val author: String?
            get() = that.author
        override val version: String?
            get() = that.version
        override val description: String?
            get() = that.description
        override val generationTool: String?
            get() = that.generationTool
        override val variableNamingConvention: VariableNamingConvention?
            get() = that.variableNamingConvention
        override val generationDateAndTime: String?
            get() = that.generationDateAndTime
        override val defaultExperiment: DefaultExperiment?
            get() = that.defaultExperiment
        override val modelVariables: ModelVariables
            get() = that.modelVariables
        override val modelStructure: ModelStructure
            get() = that.modelStructure
        override val logCategories: List<Category>?
            get() = that.logCategories
        override val isCoSimulationFmu
            get() = cs != null
        override val isMeSimulationFmu: Boolean
            get() =  me != null
        override val modelIdentifier: String
            get() {
                if (cs != null) {
                    return cs.modelIdentifier!!
                } else if (me != null) {
                    return me.modelIdentifier!!
                }
                throw IllegalStateException()
            }

        override val needsExecutionTool: Boolean
            get() {
                if (cs != null) {
                    return cs.needsExecutionTool
                } else if (me != null) {
                    return me.needsExecutionTool
                }
                throw IllegalStateException()
            }

        override val canBeInstantiatedOnlyOncePerProcess: Boolean
            get() {
                if (cs != null) {
                    return cs.canBeInstantiatedOnlyOncePerProcess
                } else if (me != null) {
                    return me.canBeInstantiatedOnlyOncePerProcess
                }
                throw IllegalStateException()
            }

        override val canNotUseMemoryManagementFunctions: Boolean
            get() {
                if (cs != null) {
                    return cs.canNotUseMemoryManagementFunctions
                } else if (me != null) {
                    return me.canNotUseMemoryManagementFunctions
                }
                throw IllegalStateException()
            }

        override val canGetAndSetFMUstate: Boolean
            get() {
                if (cs != null) {
                    return cs.canGetAndSetFMUstate
                } else if (me != null) {
                    return me.canGetAndSetFMUstate
                }
                throw IllegalStateException()
            }

        override val canSerializeFMUstate: Boolean
            get() {
                if (cs != null) {
                    return cs.canSerializeFMUstate
                } else if (me != null) {
                    return me.canSerializeFMUstate
                }
                throw IllegalStateException()
            }

        override val providesDirectionalDerivative: Boolean
            get() {
                if (cs != null) {
                    return cs.providesDirectionalDerivative
                } else if (me != null) {
                    return me.providesDirectionalDerivative
                }
                throw IllegalStateException()
            }

        override val sourceFiles: List<SourceFile>
            get() {
                if (cs != null) {
                    return cs.sourceFiles ?: emptyList()
                } else if (me != null) {
                    return me.sourceFiles ?: emptyList()
                }
                throw IllegalStateException()
            }

        override val numberOfContinuousStates: Int
            get() = modelStructure.derivatives.size

        override fun toString(): String {
            return "ModelDescriptionImpl{fmiVersion=$fmiVersion, modelName=$modelName, guid=$guid, license=$license, copyright=$copyright, author=$author, version=$version, description=$description, generationTool=$generationTool, variableNamingConvention=$variableNamingConvention, generationDateAndTime=$generationDateAndTime}"
        }
    }




}
