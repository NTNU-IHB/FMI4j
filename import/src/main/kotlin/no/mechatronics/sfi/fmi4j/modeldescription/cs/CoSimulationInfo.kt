package no.mechatronics.sfi.fmi4j.modeldescription.cs

import javax.xml.bind.annotation.XmlAttribute

internal class CoSimulationInfo (

        @XmlAttribute
        val modelIdentifier: String? = null,

        @XmlAttribute
        val needsExecutionTool: Boolean = false,

        @XmlAttribute
        val canHandleVariableCommunicationStepSize: Boolean = false,

        @XmlAttribute
        val canInterpolateInputs: Boolean = false,

        @XmlAttribute
        val maxOutputDerivativeOrder: Int = 0,

        @XmlAttribute
        val canRunAsynchronuosly: Boolean = false,

        @XmlAttribute
        val canBeInstantiatedOnlyOncePerProcess: Boolean = false,

        @XmlAttribute
        val canNotUseMemoryManagementFunctions: Boolean = false,

        @XmlAttribute
        val canGetAndSetFMUstate: Boolean = false,

        @XmlAttribute
        val canSerializeFMUstate: Boolean = false,

        @XmlAttribute
        val providesDirectionalDerivative: Boolean = false
){
    override fun toString(): String {
        return "CoSimulationInfo{modelIdentifier=$modelIdentifier, needsExecutionTool=$needsExecutionTool, canHandleVariableCommunicationStepSize=$canHandleVariableCommunicationStepSize, canInterpolateInputs=$canInterpolateInputs, maxOutputDerivativeOrder=$maxOutputDerivativeOrder, canRunAsynchronuosly=$canRunAsynchronuosly, canBeInstantiatedOnlyOncePerProcess=$canBeInstantiatedOnlyOncePerProcess, canNotUseMemoryManagementFunctions=$canNotUseMemoryManagementFunctions, canGetAndSetFMUstate=$canGetAndSetFMUstate, canSerializeFMUstate=$canSerializeFMUstate, providesDirectionalDerivative=$providesDirectionalDerivative}"
    }

}