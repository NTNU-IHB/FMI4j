package no.mechatronics.sfi.modeldescription.me

import javax.xml.bind.annotation.XmlAttribute


internal class ModelExchangeInfo {

    @XmlAttribute
    val modelIdentifier: String? = null

    @XmlAttribute
    val needsExecutionTool: Boolean = false

    @XmlAttribute
    val completedIntegratorStepNotNeeded: Boolean = false

    @XmlAttribute
    val canBeInstantiatedOnlyOncePerProcess: Boolean = false

    @XmlAttribute
    val canNotUseMemoryManagementFunctions: Boolean = false

    @XmlAttribute
    val canGetAndSetFMUstate: Boolean = false

    @XmlAttribute
    val canSerializeFMUstate: Boolean = false

    @XmlAttribute
    val providesDirectionalDerivative: Boolean = false

    override fun toString(): String {
        return "ModelExchangeInfo{modelIdentifier=$modelIdentifier, needsExecutionTool=$needsExecutionTool, completedIntegratorStepNotNeeded=$completedIntegratorStepNotNeeded, canBeInstantiatedOnlyOncePerProcess=$canBeInstantiatedOnlyOncePerProcess, canNotUseMemoryManagementFunctions=$canNotUseMemoryManagementFunctions, canGetAndSetFMUstate=$canGetAndSetFMUstate, canSerializeFMUstate=$canSerializeFMUstate, providesDirectionalDerivative=$providesDirectionalDerivative}"
    }

}


