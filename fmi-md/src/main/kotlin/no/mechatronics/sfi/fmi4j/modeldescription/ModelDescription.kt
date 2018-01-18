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
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationDataImpl
import no.mechatronics.sfi.fmi4j.modeldescription.log.Category
import no.mechatronics.sfi.fmi4j.modeldescription.log.CategoryImpl
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescriptionImpl
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeDataImpl
import no.mechatronics.sfi.fmi4j.modeldescription.misc.DefaultExperiment
import no.mechatronics.sfi.fmi4j.modeldescription.misc.DefaultExperimentImpl
import no.mechatronics.sfi.fmi4j.modeldescription.misc.SourceFile
import no.mechatronics.sfi.fmi4j.modeldescription.misc.VariableNamingConvention
import no.mechatronics.sfi.fmi4j.modeldescription.structure.ModelStructure
import no.mechatronics.sfi.fmi4j.modeldescription.structure.ModelStructureImpl
import java.io.Serializable
import javax.xml.bind.annotation.*



/**
 * Static information related to an FMU
 *
 * @author Lars Ivar Hatledal
 */
interface SimpleModelDescription {

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

    /**
     * Provides default settings for the integrator, such as stop time and
     * relative tolerance.
     */
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
    val logCategories: List<Category>

    val numberOfContinuousStates: Int
        get() = modelStructure.derivatives.size

}


/**
 * @author Lars Ivar Hatedal
 */
interface ModelDescription : SimpleModelDescription {


    /**
     * The source files
     */
    val sourceFiles: List<SourceFile>

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

    /**
     * If true, a tool is needed to execute the model and
     * the FMU just contains the communication to this
     * tool. [Typically, this information is only utilized for
     * information purposes. For example when loading
     * an FMU with needsExecutionTool = true,
     * the environment can inform the user that a tool
     * has to be available on the computer where the
     * model is instantiated. The name of the tool can
     * be taken from attribute generationTool of
     * fmiModelDescription.]
     */
    val needsExecutionTool: Boolean

    /**
     * This flag indicates cases (especially for
     * embedded code), where only one instance per
     * FMU is possible
     * (multiple instantiation is default = false; if
     * multiple instances are needed and this flag =
     * true, the FMUs must be instantiated in different
     * processes).
     */
    val canBeInstantiatedOnlyOncePerProcess: Boolean

    /**
     * If true, the FMU uses its own functions for
     * memory allocation and freeing only. The callback
     * functions allocateMemory and freeMemory
     * given in fmi2Instantiate are ignored.
     */
    val canNotUseMemoryManagementFunctions: Boolean

    /**
     * If true, the environment can inquire the internal
     * FMU state and can restore it. That is, functions
     * fmi2GetFMUstate, fmi2SetFMUstate, and
     * fmi2FreeFMUstate are supported by the FMU.
     */
    val canGetAndSetFMUstate: Boolean

    /**
     * If true, the environment can serialize the internal
     * FMU state, in other words functions
     * fmi2SerializedFMUstateSize,
     * fmi2SerializeFMUstate,
     * fmi2DeSerializeFMUstate are supported by
     * the FMU. If this is the case, then flag
     * canGetAndSetFMUstate must be true as well
     */
    val canSerializeFMUstate: Boolean

    /**
     * If true, the directional derivative of the equations
     * can be computed with
     * fmi2GetDirectionalDerivative(..)
     */
    val providesDirectionalDerivative: Boolean

}


interface ModelDescriptionProvider : SimpleModelDescription {

    fun asCS(): CoSimulationModelDescription

    fun asME(): ModelExchangeModelDescription

}

/**
 * @author Lars Ivar Hatedal
 */
@XmlRootElement(name = "fmiModelDescription")
@XmlAccessorType(XmlAccessType.FIELD)
class ModelDescriptionImpl : SimpleModelDescription, ModelDescriptionProvider, Serializable {

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override lateinit var fmiVersion: String

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override lateinit var modelName: String

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override lateinit var guid: String

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val license: String? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val copyright: String? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val author: String? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val version: String? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val description: String? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val generationTool: String? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val variableNamingConvention: VariableNamingConvention? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val generationDateAndTime: String? = null

    /**
     * @inheritDoc
     */
    @XmlElement(name = "DefaultExperiment")
    override val defaultExperiment: DefaultExperimentImpl? = null

    /**
     * @inheritDoc
     */
    @XmlAttribute
    override val numberOfEventIndicators: Int = 0

    /**
     * @inheritDoc
     */
    @XmlElement(name = "ModelVariables")
    override lateinit var modelVariables: ModelVariablesImpl

    /**
     * @inheritDoc
     */
    @XmlElement(name = "ModelStructure")
    override lateinit var modelStructure: ModelStructureImpl

    /**
     * @inheritDoc
     */
    @XmlElementWrapper(name = "LogCategories")
    @XmlElement(name = "Category")
    var _logCategories: List<CategoryImpl>? = null

    override val logCategories: List<Category>
        get() = _logCategories ?: emptyList()

    @XmlElement(name = "CoSimulation")
    private var cs: CoSimulationDataImpl? = null

    @XmlElement(name = "ModelExchange")
    private var me: ModelExchangeDataImpl? = null

    override fun asCS(): CoSimulationModelDescription
            = CoSimulationModelDescriptionImpl(this, cs ?: throw IllegalStateException("modelDescription.xml does not contain a <CoSimulation> tag!"))

    override fun asME(): ModelExchangeModelDescription
            = ModelExchangeModelDescriptionImpl(this, me ?: throw IllegalStateException("modelDescription.xml does not contain a <ModelExchange> tag!"))

}
