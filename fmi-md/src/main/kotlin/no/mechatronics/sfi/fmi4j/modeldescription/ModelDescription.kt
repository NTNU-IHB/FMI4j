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

import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescriptionImpl
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationXmlNode
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationXmlNodeImpl
import no.mechatronics.sfi.fmi4j.modeldescription.log.Category
import no.mechatronics.sfi.fmi4j.modeldescription.log.CategoryImpl
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescriptionImpl
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeXmlNode
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeXmlNodeImpl
import no.mechatronics.sfi.fmi4j.modeldescription.structure.ModelStructure
import no.mechatronics.sfi.fmi4j.modeldescription.structure.ModelStructureImpl
import java.io.Serializable
import javax.xml.bind.annotation.*

interface ModelDescription {

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
     * The (fixed) number of event indicators for an FMU based on FMI for
     * Model Exchange.
     * For Co-Simulation, this value is ignored.
     */
    val numberOfEventIndicators: Int

    /**
     * A global list of log categories that can be set to define the log
     * information that is supported from the FMU.
     */
    val logCategories: List<Category>?

    val numberOfContinuousStates: Int
        get() = modelStructure.derivatives.size

    fun asCS(): CoSimulationModelDescription

    fun asME(): ModelExchangeModelDescription


}

interface ExtendedModelDescription: ModelDescription {
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
}

@XmlRootElement(name = "fmiModelDescription")
@XmlAccessorType(XmlAccessType.FIELD)
class ModelDescriptionImpl : ModelDescription, Serializable {

    @XmlAttribute
    override lateinit var fmiVersion: String

    @XmlAttribute
    override lateinit var modelName: String

    @XmlAttribute
    override lateinit var guid: String

    @XmlAttribute
    override val license: String? = null

    @XmlAttribute
    override val copyright: String? = null

    @XmlAttribute
    override val author: String? = null

    @XmlAttribute
    override val version: String? = null

    @XmlAttribute
    override val description: String? = null

    @XmlAttribute
    override val generationTool: String? = null

    @XmlAttribute
    override val variableNamingConvention: VariableNamingConvention? = null

    @XmlAttribute
    override val generationDateAndTime: String? = null

    @XmlElement(name = "DefaultExperiment")
    override val defaultExperiment: DefaultExperimentImpl? = null

    @XmlAttribute
    override val numberOfEventIndicators: Int = 0

    /**
     * The central FMU data structure defining all variables of the FMU that
     * are visible/accessible via the FMU functions.
     */
    @XmlElement(name = "ModelVariables")
    override lateinit var modelVariables: ModelVariablesImpl

    @XmlElement(name = "ModelStructure")
    override lateinit var modelStructure: ModelStructureImpl

    /**
     * A global list of log categories that can be set to define the log
     * information that is supported from the FMU.
     */
    @XmlElementWrapper(name = "LogCategories")
    @XmlElement(name = "Category")
    override val logCategories: List<CategoryImpl>? = null

    @XmlElement(name = "CoSimulation")
    private var cs: CoSimulationXmlNodeImpl? = null

    @XmlElement(name = "ModelExchange")
    private var me: ModelExchangeXmlNodeImpl? = null

    override fun asCS(): CoSimulationModelDescription
            = CoSimulationModelDescriptionImpl(this, cs ?: throw IllegalStateException("modelDescription.xml does not contain a <CoSimulation> tag!"))

    override fun asME(): ModelExchangeModelDescription
            = ModelExchangeModelDescriptionImpl(this, me ?: throw IllegalStateException("modelDescription.xml does not contain a <ModelExchange> tag!"))

}
