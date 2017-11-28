package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2EventInfo
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.wrapper.Fmi2ModelExchangeWrapper
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import java.io.File
import java.net.URL


private class ModelExchangeHelper(
        fmuFile: FmuFile,
        visible: Boolean,
        loggingOn: Boolean
) : FmuHelper<Fmi2ModelExchangeWrapper, ModelExchangeModelDescription>(fmuFile, Fmi2Type.ModelExchange, visible, loggingOn) {

    override val wrapper: Fmi2ModelExchangeWrapper by lazy {
        Fmi2ModelExchangeWrapper(fmuFile.getLibraryFolderPath(), fmuFile.getLibraryName(modelDescription))
    }

    override val modelDescription: ModelExchangeModelDescription by lazy {
        ModelExchangeModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())
    }
}


open class ModelExchangeFmu @JvmOverloads constructor(

        fmuFile: FmuFile,
        visible: Boolean = false,
        loggingOn: Boolean = false

) : Fmu<Fmi2ModelExchangeWrapper, ModelExchangeModelDescription>(ModelExchangeHelper(fmuFile, visible, loggingOn)) {

    @JvmOverloads
    constructor(file: File, visible: Boolean = false, loggingOn: Boolean = false) : this(FmuFile(file), visible, loggingOn)

    @JvmOverloads
    constructor(url: URL, visible: Boolean = false, loggingOn: Boolean = false) : this(FmuFile(url), visible, loggingOn)

    private val eventInfo: Fmi2EventInfo by lazy {
        Fmi2EventInfo()
    }

    val ode: FirstOrderDifferentialEquations by lazy {
        object : FirstOrderDifferentialEquations {
            override fun getDimension(): Int =  modelDescription.numberOfContinuousStates

            override fun computeDerivatives(t: Double, y: DoubleArray?, yDot: DoubleArray?) {
                getDerivatives(yDot!!)
            }
        }
    }

    fun setTime(time: Double) {
        currentTime = time
        wrapper.setTime(currentTime)
    }

    fun setContinousStates(x: DoubleArray) = wrapper.setContinousStates(x)

    fun enterEventMode() = wrapper.enterEventMode()

    fun enterContinuousTimeMode() = wrapper.enterContinuousTimeMode()

    fun newDiscreteStates(eventInfo: Fmi2EventInfo) = wrapper.newDiscreteStates(eventInfo)

    fun completedIntegratorStep(): Pair<Boolean, Boolean> = wrapper.completedIntegratorStep()

    fun getDerivatives(derivatives: DoubleArray) = wrapper.getDerivatives(derivatives)

    fun getEventIndicators(eventIndicators: DoubleArray) = wrapper.getEventIndicators(eventIndicators)

    fun getContinuousStates(x: DoubleArray) = wrapper.getContinuousStates(x)

    fun getNominalsOfContinuousStates(x_nominal: DoubleArray) = wrapper.getNominalsOfContinuousStates(x_nominal)

}


