package no.mechatronics.sfi.fmi4j.wrapper

import org.junit.Test

import org.junit.Assert.*

class FmiStateTest {
    @Test
    fun isCallLegalDuringState() {

        assertFalse(FmiState.START.isCallLegalDuringState(FmiMethod.fmi2SetDebugLogging))
        assertFalse(FmiState.START.isCallLegalDuringState(FmiMethod.fmi2Reset))
        assertTrue(FmiState.START.isCallLegalDuringState(FmiMethod.fmi2GetTypesPlatform))

    }



}