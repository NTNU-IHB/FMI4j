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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import no.mechatronics.sfi.fmi4j.modeldescription.log.LogCategories
import no.mechatronics.sfi.fmi4j.modeldescription.log.LogCategoriesImpl
import no.mechatronics.sfi.fmi4j.modeldescription.misc.DefaultExperiment
import no.mechatronics.sfi.fmi4j.modeldescription.misc.SourceFile
import no.mechatronics.sfi.fmi4j.modeldescription.misc.VariableNamingConvention
import no.mechatronics.sfi.fmi4j.modeldescription.structure.ModelStructure
import no.mechatronics.sfi.fmi4j.modeldescription.structure.ModelStructureImpl
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import java.io.*
import java.lang.IllegalArgumentException
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.xml.bind.JAXB
import javax.xml.bind.annotation.*



/**
 * Static information related to an FMU
 *
 * @author Lars Ivar Hatledal
 */
interface CommonModelDescription {

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
     * The central FMU data structure defining all categories of the FMU that
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
    val logCategories: LogCategories?

    /**
     * The number of continuous states
     * @see ModelStructure.derivatives
     */
    val numberOfContinuousStates: Int
        get() = modelStructure.derivatives.size

    /**
     * Does this FMU implement Model Exchange.
     * That is, does the modelDescription.xml contain the <ModelExchange></ModelExchange> xml node?
     */
    val supportsModelExchange: Boolean

    /**
     * Does this FMU implement Co-Simulation.
     * That is, does the modelDescription.xml contain the <CoSimulation></CoSimulation> xml node?
     */
    val supportsCoSimulation: Boolean

}


/**
 * @author Lars Ivar Hatledal
 */
interface SpecificModelDescription : CommonModelDescription {

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


    /**
     * The source files
     */
    val sourceFiles: List<SourceFile>

}

interface ModelDescriptionProvider: CommonModelDescription {

    fun asCoSimulationModelDescription(): CoSimulationModelDescription

    fun asModelExchangeModelDescription() : ModelExchangeModelDescription

}

/**
 * @author Lars Ivar Hatledal
 */
@XmlRootElement(name = "fmiModelDescription")
@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "fmiModelDescription")
class ModelDescriptionImpl : CommonModelDescription, ModelDescriptionProvider, Serializable {

    @XmlAttribute
    @JacksonXmlProperty()
    override lateinit var fmiVersion: String

    @XmlAttribute
    @JacksonXmlProperty()
    override lateinit var modelName: String

    @XmlAttribute
    @JacksonXmlProperty()
    override lateinit var guid: String

    @XmlAttribute
    @JacksonXmlProperty()
    override val license: String? = null

    @XmlAttribute
    @JacksonXmlProperty()
    override val copyright: String? = null

    @XmlAttribute
    @JacksonXmlProperty()
    override val author: String? = null

    @XmlAttribute
    @JacksonXmlProperty()
    override val version: String? = null

    @XmlAttribute
    @JacksonXmlProperty()
    override val description: String? = null

    @XmlAttribute
    @JacksonXmlProperty()
    override val generationTool: String? = null

    @XmlAttribute
    @JacksonXmlProperty()
    override val variableNamingConvention: VariableNamingConvention? = null

    @XmlAttribute
    @JacksonXmlProperty()
    override val generationDateAndTime: String? = null

    @XmlElement(name = "DefaultExperiment")
    @JacksonXmlProperty(localName = "DefaultExperiment")
    override val defaultExperiment: DefaultExperiment? = null

    /**
     * The (fixed) number of event indicators for an FMU based on FMI for
     * Model Exchange.
     * For Co-Simulation, this value is ignored
     */
    @XmlAttribute
    @JacksonXmlProperty()
    val numberOfEventIndicators: Int = 0

    @JacksonXmlProperty(localName = "ModelVariables")
    override lateinit var modelVariables: ModelVariablesImpl

    @XmlElement(name = "ModelStructure")
    @JacksonXmlProperty(localName = "ModelStructure")
    override lateinit var modelStructure: ModelStructureImpl

    @XmlElement(name = "LogCategories")
    @JacksonXmlProperty(localName = "LogCategories")
    private val _logCategories: LogCategoriesImpl? = null

    override val logCategories: LogCategories?
        get() = _logCategories

    @XmlElement(name = "CoSimulation")
    @JacksonXmlProperty(localName = "CoSimulation")
    private val cs: CoSimulationDataImpl? = null

    @XmlElement(name = "ModelExchange")
    @JacksonXmlProperty(localName = "ModelExchange")
    private val me: ModelExchangeDataImpl? = null

    @delegate:Transient
    private val coSimulationModelDescription: CoSimulationModelDescription? by lazy {
        cs?.let { CoSimulationModelDescriptionImpl(this, it) }
    }

    @delegate:Transient
    private val modelExchangeModelDescription: ModelExchangeModelDescription? by lazy {
        me?.let { ModelExchangeModelDescriptionImpl(this, it) }
    }

    override val supportsModelExchange: Boolean
        get() = me != null

    override val supportsCoSimulation: Boolean
        get() = cs != null

    /**
     * @throws IllegalStateException if FMU does not support Co-Simulation
     */
    override fun asCoSimulationModelDescription(): CoSimulationModelDescription
            = coSimulationModelDescription ?: throw IllegalStateException("FMU does not support Co-Simulation: modelDescription.xml does not contain a <CoSimulation> tag!")

    /**
     * @throws IllegalStateException if FMU does not support Model Exchange
     */
    override fun asModelExchangeModelDescription(): ModelExchangeModelDescription
            = modelExchangeModelDescription ?: throw IllegalStateException("FMU does not support Model Exchange: modelDescription.xml does not contain a <ModelExchange> tag!")

    internal val stringContent: String
        get() = listOfNotNull(
                "fmiVersion=$fmiVersion",
                "modelName=$modelName",
                "guid=$guid",
                license?.let {"license=$license" },
                copyright?.let { "copyright=$copyright" },
                author?.let { "author=$author" },
                version?.let { "version=$version" },
                description?.let { "description=$description" },
                generationTool?.let { "generationTool=$generationTool" },
                variableNamingConvention?.let { "variableNamingConvention=$variableNamingConvention" },
                generationDateAndTime?.let { "generationDateAndTime=$generationDateAndTime" },
                defaultExperiment?.let { "defaultExperiment=$defaultExperiment" },
                numberOfEventIndicators.let { "numberOfEventIndicators=$numberOfEventIndicators" }
        ).joinToString ("\n")


    override fun toString(): String {
        return "ModelDescriptionImpl(\n$stringContent\n)"
    }

}


/**
 * @author Lars Ivar Hatledal
 */
interface CoSimulationModelDescription : SpecificModelDescription {

    /**
     * The slave is able to provide derivatives of
     * outputs with maximum order. Calling of
     * fmi2GetRealOutputDerivatives(...) is
     * allowed up to the order defined by
     * maxOutputDerivativeOrder.
     *
     */
    val maxOutputDerivativeOrder: Int


    /**
     * The slave can handle variable
     * communication step size. The
     * communication step size (parameter
     * communicationStepSize of
     * fmi2DoStep(...) ) has not to be constant
     * for each call.
     *
     */
    val canHandleVariableCommunicationStepSize: Boolean

    /**
     * The slave is able to interpolate continuous
     * inputs. Calling of
     * fmi2SetRealInputDerivatives(...) has
     * an effect for the slave.
     *
     */
    val canInterpolateInputs: Boolean

    /**
     * This flag describes the ability to carry out the
     * fmi2DoStep(...) call asynchronously.
     *
     */
    val canRunAsynchronuously: Boolean

}

/**
 * @author Lars Ivar Hatledal
 */
class CoSimulationModelDescriptionImpl(
        private val modelDescription: ModelDescriptionImpl,
        private val cs: CoSimulationData
) : CommonModelDescription by modelDescription, CoSimulationModelDescription, CoSimulationData by cs, Serializable {

    override fun toString(): String {
        return "CoSimulationModelDescriptionImpl(\n${modelDescription.stringContent}\n)"
    }

}


/**
 * @author Lars Ivar Hatledal
 */
interface ModelExchangeModelDescription : SpecificModelDescription {

    /**
     * The (fixed) number of event indicators for an FMU based on FMI for
     * Model Exchange.
     */
    val numberOfEventIndicators: Int

    /**
     * If true, function
     * fmi2CompletedIntegratorStep need not to
     * be called (which gives a slightly more efficient
     * integration). If it is called, it has no effect.
     * If false (the default), the function must be called
     * after every completed integrator step, see
     * section 3.2.2.
     */
    val completedIntegratorStepNotNeeded: Boolean
}

/**
 *
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
class ModelExchangeModelDescriptionImpl(
        private val modelDescription: ModelDescriptionImpl,
        private val me: ModelExchangeData
) : CommonModelDescription by modelDescription, ModelExchangeModelDescription, ModelExchangeData by me, Serializable {

    override val numberOfEventIndicators: Int
        get() = modelDescription.numberOfEventIndicators

    override fun toString(): String {
        return "ModelExchangeModelDescriptionImpl(\n${modelDescription.stringContent}\nnumberOfEventIndicators=$numberOfEventIndicators\n)"
    }

}

