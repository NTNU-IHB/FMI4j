/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
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

@XmlRootElement(name = "fmiModelDescription")
@XmlAccessorType(XmlAccessType.FIELD)
open class ModelDescription {

    companion object {

        internal fun <T: ModelDescription> parseModelDescription(xml: String, type: Class<T>): T {
            return JAXB.unmarshal(StringReader(xml), type)
        }

        @JvmStatic
        fun parseModelDescription(xml: String): ModelDescription = parseModelDescription(xml, ModelDescription::class.java)
        @JvmStatic
        fun parseModelDescription(url: URL): ModelDescription = parseModelDescription(url.openStream(), ModelDescription::class.java)
        @JvmStatic
        fun parseModelDescription(file: java.io.File): ModelDescription = parseModelDescription(FileInputStream(file), ModelDescription::class.java)
        @JvmStatic
        fun parseModelDescription(inputStream: InputStream): ModelDescription = parseModelDescription(inputStream, ModelDescription::class.java)


        internal fun <T : ModelDescription> parseModelDescription(stream: InputStream, type: Class<T>): T {

            var modelDescription: T? = null
            ZipInputStream(stream).use {

                var nextEntry: ZipEntry? = it.nextEntry
                while (nextEntry != null) {

                    val name = nextEntry.name
                    if (name == MODEL_DESC_FILE) {
                        modelDescription = parseModelDescription(IOUtils.toString(it, Charset.forName("UTF-8")), type)
                    }

                    nextEntry = it.nextEntry
                }

            }

            if (modelDescription == null) {
                throw IllegalArgumentException("Input is not an valid FMU! No $MODEL_DESC_FILE present!")
            }

            return modelDescription!!

        }

    }

    /**
     * Version of “FMI for Model Exchange or Co-Simulation” that was used to
     * generate the XML file. The value for this version is “2.0”. Future minor
     * revisions are denoted as “2.0.1”, “2.0.2”, …
     */
    @XmlAttribute
    lateinit var fmiVersion: String
    private set
    /**
     * The name of the model as used in the modeling environment that
     * generated the XML file, such as
     * “Modelica.Mechanics.Rotational.Examples.CoupledClutches”.
     */
    @XmlAttribute
    lateinit var modelName: String
    private set
    /**
     * The “Globally Unique IDentifier” is a string that is used to check that the
     * XML file is compatible with the C functions of the FMU. Typically when generating the XML file, a fingerprint of the “relevant” information is
     * stored as guid and in the generated C-function.
     */
    @XmlAttribute
    lateinit var guid: String
    private set
    /**
     * Optional information on the intellectual property licensing for this FMU.
     * [Example: license = “BSD license <license text or link to license>”].
     */
    @XmlAttribute
    val license: String? = null
    /**
     * Optional information on the intellectual property copyright for this FMU.
     * [Example: copyright = “© My Company 2011”].
     */
    @XmlAttribute
    val copyright: String? = null
    /**
     * Optional string with the name and organization of the model author.
     */
    @XmlAttribute
    val author: String? = null
    /**
     * Optional version of the model, for example “1.0”.
     */
    @XmlAttribute
    val version: String? = null
    /**
     * Optional string with a brief description of the model.
     */
    @XmlAttribute
    val description: String? = null
    /**
     * Optional name of the tool that generated the XML file.
     */
    @XmlAttribute
    val generationTool: String? = null
    /**
     * Defines whether the variable names in “ModelVariables /
     * ScalarVariable / name” and in “TypeDefinitions / Type /
     * name” follow a particular convention. For the details, see section 2.2.9.
     * Currently standardized are:
     * • “flat”: A list of strings (the default).
     * • “structured“: Hierarchical names with “.” as hierarchy separator,
     * and with array elements and derivative characterization.
     */
    @XmlAttribute
    val variableNamingConvention: String? = null
    /**
     * Optional date and time when the XML file was generated. The format is
     * a subset of “xs:dateTime” and should be: “YYYY-MM-DDThh:mm:ssZ"
     * (with one “T” between date and time; “Z” characterizes the Zulu time
     * zone, in other words Greenwich meantime).
     * [Example: "2009-12-08T14:33:22Z"].
     */
    @XmlAttribute
    val generationDateAndTime: String = ""

    @XmlElement(name = "DefaultExperiment")
    val defaultExperiment: DefaultExperiment? = null

    /**
     * The central FMU data structure defining all variables of the FMU that
     * are visible/accessible via the FMU functions.
     */
    @XmlElement(name = "ModelVariables")
    lateinit var modelVariables: ModelVariables
    private set

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
    @XmlElement(name = "ModelStructure")
    lateinit var modelStructure: ModelStructure
    private set

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
        get() {
            if (cs != null) {
                return cs.modelIdentifier!!
            } else if (me != null) {
                return me.modelIdentifier!!
            }
            throw IllegalStateException()
        }

    val needsExecutionTool: Boolean
        get() {
            if (cs != null) {
                return cs.needsExecutionTool
            } else if (me != null) {
                return me.needsExecutionTool
            }
            throw IllegalStateException()
        }

    val canBeInstantiatedOnlyOncePerProcess: Boolean
        get() {
            if (cs != null) {
                return cs.canBeInstantiatedOnlyOncePerProcess
            } else if (me != null) {
                return me.canBeInstantiatedOnlyOncePerProcess
            }
            throw IllegalStateException()
        }

    val canNotUseMemoryManagementFunctions: Boolean
        get() {
            if (cs != null) {
                return cs.canNotUseMemoryManagementFunctions
            } else if (me != null) {
                return me.canNotUseMemoryManagementFunctions
            }
            throw IllegalStateException()
        }

    val canGetAndSetFMUstate: Boolean
        get() {
            if (cs != null) {
                return cs.canGetAndSetFMUstate
            } else if (me != null) {
                return me.canGetAndSetFMUstate
            }
            throw IllegalStateException()
        }

    val canSerializeFMUstate: Boolean
        get() {
            if (cs != null) {
                return cs.canSerializeFMUstate
            } else if (me != null) {
                return me.canSerializeFMUstate
            }
            throw IllegalStateException()
        }

    val providesDirectionalDerivative: Boolean
        get() {
            if (cs != null) {
                return cs.providesDirectionalDerivative
            } else if (me != null) {
                return me.providesDirectionalDerivative
            }
            throw IllegalStateException()
        }

    val sourceFiles: List<SourceFile>
        get() {
            if (cs != null) {
                return cs.sourceFiles ?: emptyList()
            } else if (me != null) {
                return me.sourceFiles ?: emptyList()
            }
            throw IllegalStateException()
        }

    val numberOfContinuousStates: Int
        get() = modelStructure.derivatives.size

    override fun toString(): String {
        return "ModelDescription{fmiVersion=$fmiVersion, modelName=$modelName, guid=$guid, license=$license, copyright=$copyright, author=$author, version=$version, description=$description, generationTool=$generationTool, variableNamingConvention=$variableNamingConvention, generationDateAndTime=$generationDateAndTime}"
    }

}
