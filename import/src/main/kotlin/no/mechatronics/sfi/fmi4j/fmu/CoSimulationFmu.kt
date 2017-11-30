package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.Fmi2Simulation
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2StatusKind
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.wrapper.Fmi2CoSimulationWrapper
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import java.io.File
import java.net.URL


private class CoSimulationHelper(
        fmuFile: FmuFile,
        visible: Boolean,
        loggingOn: Boolean
) : FmuHelper<Fmi2CoSimulationWrapper, CoSimulationModelDescription>(fmuFile, Fmi2Type.CoSimulation, visible, loggingOn) {

    override val wrapper: Fmi2CoSimulationWrapper by lazy {
        Fmi2CoSimulationWrapper(fmuFile.getLibraryFolderPath(), fmuFile.getLibraryName(modelDescription))
    }

    override val modelDescription: CoSimulationModelDescription by lazy {
        CoSimulationModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())
    }
}

class CoSimulationFmu @JvmOverloads constructor(

        fmuFile: FmuFile,
        visible: Boolean = false,
        loggingOn: Boolean = false


) : Fmu<Fmi2CoSimulationWrapper, CoSimulationModelDescription>(CoSimulationHelper(fmuFile, visible, loggingOn)), Fmi2Simulation {


    @JvmOverloads
    constructor(file: File, visible: Boolean = false, loggingOn: Boolean = false) : this(FmuFile(file), visible, loggingOn)
    @JvmOverloads
    constructor(url: URL, visible: Boolean = false, loggingOn: Boolean = false) : this(FmuFile(url), visible, loggingOn)


   override fun doStep(dt: Double) : Boolean {
        val status = wrapper.doStep(currentTime, dt, true)
        currentTime += dt
        return status == Fmi2Status.OK
    }

    fun cancelStep() = wrapper.cancelStep()

    fun setRealInputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.setRealInputDerivatives(vr, order, value)

    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.getRealOutputDerivatives(vr, order, value)

    fun getStatus(s: Fmi2StatusKind) = wrapper.getStatus(s)
    fun getRealStatus(s: Fmi2StatusKind): Double = wrapper.getRealStatus(s)
    fun getIntegerStatus(s: Fmi2StatusKind): Int = wrapper.getIntegerStatus(s)
    fun getBooleanStatus(s: Fmi2StatusKind): Boolean = wrapper.getBooleanStatus(s)
    fun getStringStatus(s: Fmi2StatusKind): String = wrapper.getStringStatus(s)

}


