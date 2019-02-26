package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationAttributes
import no.ntnu.ihb.fmi4j.modeldescription.CommonFmuAttributes
import no.ntnu.ihb.fmi4j.modeldescription.ModelExchangeAttributes


/**
 * @author Lars Ivar Hatledal
 */
sealed class JacksonCommonFmuAttributes : CommonFmuAttributes {

    @JacksonXmlProperty
    override val modelIdentifier: String = ""

    @JacksonXmlProperty
    override val needsExecutionTool: Boolean = false

    @JacksonXmlProperty
    override val canNotUseMemoryManagementFunctions: Boolean = false

    @JacksonXmlProperty
    override val canGetAndSetFMUstate: Boolean = false

    @JacksonXmlProperty
    override val canSerializeFMUstate: Boolean = false

    @JacksonXmlProperty
    override val providesDirectionalDerivative: Boolean = false

    @JacksonXmlProperty
    override val canBeInstantiatedOnlyOncePerProcess: Boolean = false

    @JacksonXmlElementWrapper(localName = "SourceFiles")
    @JacksonXmlProperty(localName = "File")
    override val sourceFiles: JacksonSourceFiles = emptyList()

}

/**
 * @author Lars Ivar Hatledal
 */
data class JacksonCoSimulationAttributes(

        @JacksonXmlProperty
        override val canHandleVariableCommunicationStepSize: Boolean = false,

        @JacksonXmlProperty
        override val canInterpolateInputs: Boolean = false,

        @JacksonXmlProperty
        override val maxOutputDerivativeOrder: Int = 0,

        @JacksonXmlProperty
        override val canRunAsynchronuously: Boolean = false

) : JacksonCommonFmuAttributes(), CoSimulationAttributes

/**
 * @author Lars Ivar Hatledal
 */
data class JacksonModelExchangeAttributes(

        @JacksonXmlProperty
        override val completedIntegratorStepNotNeeded: Boolean = false

) : JacksonCommonFmuAttributes(), ModelExchangeAttributes