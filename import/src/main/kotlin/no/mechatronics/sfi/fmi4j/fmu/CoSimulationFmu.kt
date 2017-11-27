package no.mechatronics.sfi.fmi4j.fmu

import com.sun.jna.Pointer
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2StatusKind
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.jna.lib.wrapper.Fmi2CoSimulationLibraryWrapper
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import java.io.File
import java.net.URL


private class CoSimulationHelper(
        fmuFile: FmuFile,
        visible: Boolean,
        loggingOn: Boolean
) : FmuHelper<Fmi2CoSimulationLibraryWrapper, CoSimulationModelDescription>(fmuFile, Fmi2Type.CoSimulation, visible, loggingOn) {

    override val wrapper: Fmi2CoSimulationLibraryWrapper by lazy {
        Fmi2CoSimulationLibraryWrapper(fmuFile.getLibraryFolderPath(), fmuFile.getLibraryName(modelDescription))
    }

    override val modelDescription: CoSimulationModelDescription by lazy {
        CoSimulationModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())
    }
}



class CoSimulationFmu @JvmOverloads constructor(

        fmuFile: FmuFile,
        visible: Boolean = false,
        loggingOn: Boolean = false


) : Fmu<Fmi2CoSimulationLibraryWrapper, CoSimulationModelDescription>(CoSimulationHelper(fmuFile, visible, loggingOn)) {


    @JvmOverloads
    constructor(file: File, visible: Boolean = false, loggingOn: Boolean = false) : this(FmuFile(file), visible, loggingOn)
    @JvmOverloads
    constructor(url: URL, visible: Boolean = false, loggingOn: Boolean = false) : this(FmuFile(url), visible, loggingOn)


    fun doStep(dt: Double) : Fmi2Status {
        val status = wrapper.doStep(currentTime, dt, true)
        currentTime += dt
        return status
    }

    fun cancelStep() = wrapper.cancelStep()

    fun setRealInputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.setRealInputDerivatives(vr, order, value)

    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.getRealOutputDerivatives(vr, order, value)

    fun getStatus(c: Pointer, s: Fmi2StatusKind) = wrapper.getStatus(c, s)
    fun getRealStatus(c: Pointer, s: Fmi2StatusKind): Double = wrapper.getRealStatus(c, s)
    fun getIntegerStatus(c: Pointer, s: Fmi2StatusKind): Int = wrapper.getIntegerStatus(c, s)
    fun getBooleanStatus(c: Pointer, s: Fmi2StatusKind): Boolean = wrapper.getBooleanStatus(c, s)
    fun getStringStatus(c: Pointer, s: Fmi2StatusKind): String = wrapper.getStringStatus(c, s)

}


