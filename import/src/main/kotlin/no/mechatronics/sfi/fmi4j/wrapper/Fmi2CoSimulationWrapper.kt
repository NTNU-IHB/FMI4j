package no.mechatronics.sfi.fmi4j.wrapper

import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.DoubleByReference
import com.sun.jna.ptr.IntByReference
import no.mechatronics.sfi.fmi4j.jna.convert
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2StatusKind
import no.mechatronics.sfi.fmi4j.jna.Fmi2CoSimulationLibrary
import no.mechatronics.sfi.fmi4j.jna.StringByReference


class Fmi2CoSimulationWrapper(
        libraryFolder: String,
        libraryName: String
) : Fmi2Wrapper<Fmi2CoSimulationLibrary>(libraryFolder, libraryName, Fmi2CoSimulationLibrary::class.java) {

    /**
     * @see Fmi2CoSimulationlibrary.fmi2SetRealInputDerivatives
     */
    fun setRealInputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray) : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2SetRealInputDerivatives)
        return updateStatus(Fmi2Status.valueOf(library.fmi2SetRealInputDerivatives(c, vr, vr.size, order, value)))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetRealOutputDerivatives
     */
    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray) : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2GetRealOutputDerivatives)
        return updateStatus(Fmi2Status.valueOf(library.fmi2GetRealOutputDerivatives(c, vr, vr.size, order, value)))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2DoStep
     */
    fun doStep(t: Double, dt: Double, noSetFMUStatePriorToCurrent: Boolean) : Fmi2Status {
        return updateStatus(Fmi2Status.valueOf(library.fmi2DoStep(c, t, dt, convert(noSetFMUStatePriorToCurrent))))
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2CancelStep
     */
    fun cancelStep() : Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2CancelStep)
        return updateState(updateStatus(Fmi2Status.valueOf(library.fmi2CancelStep(c))), FmiState.STEP_CANCELED)
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetStatus
     */
    fun getStatus(s: Fmi2StatusKind): Fmi2Status {
        state.isCallLegalDuringState(FmiMethod.fmi2GetStatus)
        val i = IntByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetIntegerStatus(c, s, i)))
        return Fmi2Status.valueOf(i.value)
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetRealStatus
     */
    fun getRealStatus(s: Fmi2StatusKind): Double {
        state.isCallLegalDuringState(FmiMethod.fmi2GetRealStatus)
        val d = DoubleByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetRealStatus(c, s, d)))
        return d.value
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetIntegerStatus
     */
    fun getIntegerStatus(s: Fmi2StatusKind): Int {
        state.isCallLegalDuringState(FmiMethod.fmi2GetIntegerStatus)
        val i = IntByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetIntegerStatus(c, s, i)))
        return i.value
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetBooleanStatus
     */
    fun getBooleanStatus(s: Fmi2StatusKind): Boolean {
        state.isCallLegalDuringState(FmiMethod.fmi2GetBooleanStatus)
        val b = ByteByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetBooleanStatus(c, s, b)))
        return convert(b.value)
    }

    /**
     * @see Fmi2CoSimulationlibrary.fmi2GetStringStatus
     */
    fun getStringStatus(s: Fmi2StatusKind): String {
        state.isCallLegalDuringState(FmiMethod.fmi2GetStringStatus)
        val str = StringByReference()
        updateStatus(Fmi2Status.valueOf(library.fmi2GetStringStatus(c, s, str)))
        return str.value
    }

}
