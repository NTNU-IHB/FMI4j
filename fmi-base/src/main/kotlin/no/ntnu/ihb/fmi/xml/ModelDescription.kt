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

package no.ntnu.ihb.fmi.xml

import no.ntnu.ihb.fmi.common.ValueReference
import no.ntnu.ihb.fmi.xml.logging.LogCategories
import no.ntnu.ihb.fmi.xml.misc.DefaultExperiment
import no.ntnu.ihb.fmi.xml.misc.SourceFile
import no.ntnu.ihb.fmi.xml.misc.TypeDefinitions
import no.ntnu.ihb.fmi.xml.misc.UnitDefinitions
import no.ntnu.ihb.fmi.xml.structure.ModelStructure
import no.ntnu.ihb.fmi.xml.variables.ModelVariables
import no.ntnu.ihb.fmi.xml.variables.ScalarVariable
import java.util.*

/**
 * Static information related to an FMU
 *
 * @author Lars Ivar Hatledal
 */
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
     * “FLAT”: A list of strings (the default).
     * “STRUCTURED“: Hierarchical names with “.” as hierarchy separator,
     * and with array elements and derivative characterization.
     */
    val variableNamingConvention: String?

    /**
     * Optional date and time when the XML file was generated. The format is
     * a subset of “xs:dateTime” and should be: “YYYY-MM-DDThh:mm:ssZ"
     * (with one “T” between date and time; “Z” characterizes the Zulu time
     * zone, in other words Greenwich meantime).
     * [Example: "2009-12-08T14:33:22Z"].
     */
    val generationDateAndTime: GregorianCalendar?

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
     * dependency of the unknowns from the knowns can be optionally
     * defined. [This information can be, for example used to compute
     * efficiently a sparse Jacobian for simulation or to utilize the
     * input/output dependency in order to detect that in some cases there
     * are actually no algebraic loops when connecting FMUs together
     */
    val modelStructure: ModelStructure

    /**
     * A global list of unit and display unit definitions [for example to convert
     * display units into the units used in the model equations]. These
     * definitions are used in the XML element “ModelVariables”.
     */
    val unitDefinitions: UnitDefinitions?

    /**
     * A global list of type definitions that are utilized in “ModelVariables”.
     */
    val typeDefinitions: TypeDefinitions?

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

    fun getVariableByName(name: String): ScalarVariable {
        return modelVariables.getByName(name)
    }

    fun getValueReference(name: String): ValueReference {
        return getVariableByName(name).valueReference;
    }

}

interface ModelDescriptionProvider: ModelDescription {

    val supportsCoSimulation: Boolean
    val supportsModelExchange: Boolean

    fun asCoSimulationModelDescription(): CoSimulationModelDescription
    fun asModelExchangeModelDescription(): ModelExchangeModelDescription

}


interface CommonAttributes {

    val modelIdentifier: String
    val needsExecutionTool: Boolean
    val canBeInstantiatedOnlyOncePerProcess: Boolean
    val canNotUseMemoryManagementFunctions: Boolean
    val canGetAndSetFMUstate: Boolean
    val canSerializeFMUstate: Boolean
    val providesDirectionalDerivative: Boolean
    val sourceFiles: List<SourceFile>

}

interface CoSimulationAttributes: CommonAttributes {

    val maxOutputDerivativeOrder: Int?
    val canInterpolateInputs: Boolean
    val canRunAsynchronuously: Boolean
    val canProvideMaxStepSize: Boolean
    val canHandleVariableCommunicationStepSize: Boolean

}

interface ModelExchangeAttributes: CommonAttributes {

    val completedIntegratorStepNotNeeded: Boolean

}

interface CommonModelDescription: ModelDescription, CommonAttributes
interface CoSimulationModelDescription: CommonModelDescription, CoSimulationAttributes
interface ModelExchangeModelDescription: CommonModelDescription, ModelExchangeAttributes {

    /**
     * The (fixed) number of event indicators for an FMU based on FMI for Model Exchange.
     * For Co-Simulation, this value is ignored
     */
    val numberOfEventIndicators: Int

}

