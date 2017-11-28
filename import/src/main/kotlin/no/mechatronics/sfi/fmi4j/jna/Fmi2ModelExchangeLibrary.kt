package no.mechatronics.sfi.fmi4j.jna

import com.sun.jna.Pointer
import com.sun.jna.ptr.ByteByReference
import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2EventInfo


interface Fmi2ModelExchangeLibrary : Fmi2Library {

    fun fmi2SetTime(c: Pointer, time: Double): Int

    fun fmi2SetContinuousStates(c: Pointer, x: DoubleArray, nx: Int): Int

    fun fmi2EnterEventMode(c: Pointer): Int

    fun fmi2EnterContinuousTimeMode(c: Pointer): Int

    fun fmi2NewDiscreteStates(c: Pointer, eventInfo: Fmi2EventInfo): Int

    fun fmi2CompletedIntegratorStep(c: Pointer,
                                    noSetFMUStatePriorToCurrentPoint: Byte,
                                    enterEventMode: ByteByReference,
                                    terminateSimulation: ByteByReference): Int

    fun fmi2GetDerivatives(c: Pointer, derivatives: DoubleArray, nx: Int): Int

    fun fmi2GetEventIndicators(c: Pointer, eventIndicators: DoubleArray, ni: Int): Int

    fun fmi2GetContinuousStates(c: Pointer, x: DoubleArray, nx: Int): Int

    fun fmi2GetNominalsOfContinuousStates(c: Pointer, x_nominal: DoubleArray, nx: Int): Int

}