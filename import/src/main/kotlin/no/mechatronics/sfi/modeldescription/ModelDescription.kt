package no.mechatronics.sfi.modeldescription

import no.mechatronics.sfi.modeldescription.cs.CoSimulationInfo
import no.mechatronics.sfi.modeldescription.log.Category
import no.mechatronics.sfi.modeldescription.me.ModelExchangeInfo
import java.io.StringReader
import javax.xml.bind.JAXB
import javax.xml.bind.annotation.*

@XmlRootElement(name = "fmiModelDescription")
@XmlSeeAlso(
        CoSimulationModelDescription::class,
        ModelExchangeModelDescription::class
)
@XmlAccessorType(XmlAccessType.FIELD)
open class ModelDescription {

    companion object {
        fun parseModelDescription(xml: String) : ModelDescription {
            return JAXB.unmarshal(StringReader(xml), ModelDescription::class.java)
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
    generated the XML file, such as
    “Modelica.Mechanics.Rotational.Examples.CoupledClutches”.
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
    [Example: copyright = “© My Company 2011”].
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
    outputs, continuous-time states and initial unknowns (the unknowns
    during Initialization Mode) are defined here. Furthermore, the
    dependency of the unkowns from the knowns can be optionally
    defined. [This information can be, for example used to compute
    efficiently a sparse Jacobian for simulation or to utilize the
    input/output dependency in order to detect that in some cases there
    are actually no algebraic loops when connecting FMUs together
     */
    @XmlElement(name = "ModelStructure")
    lateinit var modelStructure: ModelStructure
    private set

    /**
     * A global list of log categories that can be set to define the log
    information that is supported from the FMU.
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

    val numberOfContinuousStates: Int
        get() = modelStructure.derivatives.size

    override fun toString(): String {
        return "ModelDescription{fmiVersion=$fmiVersion, modelName=$modelName, guid=$guid, license=$license, copyright=$copyright, author=$author, version=$version, description=$description, generationTool=$generationTool, variableNamingConvention=$variableNamingConvention, generationDateAndTime=$generationDateAndTime}"
    }

}

@XmlRootElement(name = "fmiModelDescription")
class CoSimulationModelDescription : ModelDescription() {

    companion object {
        fun parseModelDescription(xml: String) : CoSimulationModelDescription {
            return JAXB.unmarshal(StringReader(xml), CoSimulationModelDescription::class.java)
        }
    }

    /**
     * The slave is able to provide derivatives of
     * outputs with maximum order. Calling of
     * fmi2GetRealOutputDerivatives(...) is
     * allowed up to the order defined by
     * maxOutputDerivativeOrder.
     *
     * @return
     */
    val maxOutputDerivativeOrder: Int
        get() = cs!!.maxOutputDerivativeOrder

    /**
     * If true, a tool is needed to execute the
     * model. The FMU just contains the
     * communication to this tool (see Figure 8).
     * [Typically, this information is only utilized for
     * information purposes. For example a
     * co-simulation master can inform the user
     * that a tool has to be available on the
     * computer where the slave is instantiated.
     * The name of the tool can be taken from
     * attribute generationTool of
     * fmiModelDescription. ]
     *
     * @return
     */
    fun needsExecutionTool(): Boolean {
        return cs!!.needsExecutionTool
    }

    /**
     * The slave can handle variable
     * communication step size. The
     * communication step size (parameter
     * communicationStepSize of
     * fmi2DoStep(...) ) has not to be constant
     * for each call.
     *
     * @return
     */
    fun canHandleVariableCommunicationStepSize(): Boolean {
        return cs!!.canHandleVariableCommunicationStepSize
    }

    /**
     * The slave is able to interpolate continuous
     * inputs. Calling of
     * fmi2SetRealInputDerivatives(...) has
     * an effect for the slave.
     *
     * @return
     */
    fun canInterpolateInputs(): Boolean {
        return cs!!.canInterpolateInputs
    }

    /**
     * This flag describes the ability to carry out the
     * fmi2DoStep(...) call asynchronously.
     *
     * @return
     */
    fun canRunAsynchronuosly(): Boolean {
        return cs!!.canRunAsynchronuosly
    }

    /**
     * This flag indicates cases (especially for
     * embedded code), where only one instance
     * per FMU is possible
     * (multiple instantiation is default = false; if
     * multiple instances are needed, the FMUs
     * must be instantiated in different processes).
     *
     * @return
     */
    fun canBeInstantiatedOnlyOncePerProcess(): Boolean {
        return cs!!.canBeInstantiatedOnlyOncePerProcess
    }

    /**
     * If true, the slave uses its own functions for
     * memory allocation and freeing only. The
     * callback functions allocateMemory and
     * freeMemory given in fmi2Instantiate are
     * ignored.
     *
     * @return
     */
    fun canNotUseMemoryManagementFunctions(): Boolean {
        return cs!!.canNotUseMemoryManagementFunctions
    }

    /**
     * If true, the environment can inquire the
     * internal FMU state and can restore it. That
     * is, fmi2GetFMUstate, fmi2SetFMUstate,
     * and fmi2FreeFMUstate are supported by
     * the FMU.
     *
     * @return
     */
    fun canGetAndSetFMUstate(): Boolean {
        return cs!!.canGetAndSetFMUstate
    }

    /**
     * If true, the environment can serialize the
     * internal FMU state, in other words
     * fmi2SerializedFMUstateSize,
     * fmi2SerializeFMUstate,
     * fmi2DeSerializeFMUstate are supported
     * by the FMU. If this is the case, then flag
     * canGetAndSetFMUstate must be true as
     * well.
     *
     * @return
     */
    fun canSerializeFMUstate(): Boolean {
        return cs!!.canSerializeFMUstate
    }

    /**
     * If true, the directional derivative of the
     * equations at communication points can be
     * computed with
     * fmi2GetDirectionalDerivative(..)
     *
     * @return
     */
    fun providesDirectionalDerivative(): Boolean {
        return cs!!.providesDirectionalDerivative
    }

}

/**
 *
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
@XmlRootElement(name = "fmiModelDescription")
class ModelExchangeModelDescription : ModelDescription() {

    companion object {
        fun parseModelDescription(xml: String) : ModelExchangeModelDescription {
            return JAXB.unmarshal(StringReader(xml), ModelExchangeModelDescription::class.java)
        }
    }

    /**
     * The (fixed) number of event indicators for an FMU based on FMI for
     * Model Exchange.
     */
    @XmlAttribute
    val numberOfEventIndicators: Int = 0

    fun needsExecutionTool(): Boolean {
        return me!!.needsExecutionTool
    }

    fun completedIntegratorStepNotNeeded(): Boolean {
        return me!!.completedIntegratorStepNotNeeded
    }

    fun canBeInstantiatedOnlyOncePerProcess(): Boolean {
        return me!!.canBeInstantiatedOnlyOncePerProcess
    }

    fun canNotUseMemoryManagementFunctions(): Boolean {
        return me!!.canNotUseMemoryManagementFunctions
    }

    fun canGetAndSetFMUstate(): Boolean {
        return me!!.canGetAndSetFMUstate
    }

    fun canSerializeFMUstate(): Boolean {
        return me!!.canSerializeFMUstate
    }

    fun providesDirectionalDerivative(): Boolean {
        return me!!.providesDirectionalDerivative
    }

}
