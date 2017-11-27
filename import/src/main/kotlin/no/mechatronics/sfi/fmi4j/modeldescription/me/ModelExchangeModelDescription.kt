package no.mechatronics.sfi.fmi4j.modeldescription.me

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import java.io.File
import java.io.FileInputStream
import java.net.URL
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement


/**
 *
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
@XmlRootElement(name = "fmiModelDescription")
class ModelExchangeModelDescription : ModelDescription() {

    companion object {
        @JvmStatic
        fun parseModelDescription(xml: String) : ModelExchangeModelDescription = ModelDescription.parseModelDescription(xml, ModelExchangeModelDescription::class.java)
        @JvmStatic
        fun parseModelDescription(url: URL): ModelExchangeModelDescription = ModelDescription.parseModelDescription(url.openStream(), ModelExchangeModelDescription::class.java)
        @JvmStatic
        fun parseModelDescription(file: File): ModelExchangeModelDescription = ModelDescription.parseModelDescription(FileInputStream(file), ModelExchangeModelDescription::class.java)
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
