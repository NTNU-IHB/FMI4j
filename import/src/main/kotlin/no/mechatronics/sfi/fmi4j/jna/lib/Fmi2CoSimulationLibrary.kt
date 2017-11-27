package no.mechatronics.sfi.fmi4j.jna.lib

import com.sun.jna.Pointer
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.DoubleByReference
import com.sun.jna.ptr.IntByReference
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2StatusKind


interface Fmi2CoSimulationLibrary : Fmi2Library {

    fun fmi2SetRealInputDerivatives(c: Pointer, vr: IntArray, nvr: Int, order: IntArray, value: DoubleArray): Int

    fun fmi2GetRealOutputDerivatives(c: Pointer, vr: IntArray, nvr: Int, order: IntArray, value: DoubleArray): Int

    fun fmi2DoStep(c: Pointer, currentCommunicationPoint: Double, communicationStepSize: Double, noSetFMUStatePriorToCurrent: Byte): Int

    fun fmi2CancelStep(c: Pointer): Int

    fun fmi2GetStatus(c: Pointer, s: Fmi2StatusKind, value: IntByReference): Int

    fun fmi2GetRealStatus(c: Pointer, s: Fmi2StatusKind, value: DoubleByReference): Int

    fun fmi2GetIntegerStatus(c: Pointer, s: Fmi2StatusKind, value: IntByReference): Int

    fun fmi2GetBooleanStatus(c: Pointer, s: Fmi2StatusKind, value: ByteByReference): Int

    fun fmi2GetStringStatus(c: Pointer, s: Fmi2StatusKind, value: StringByReference): Int

}