package no.ntnu.ihb.fmi4j.importer

import no.ntnu.ihb.fmi4j.FmiStatus
import java.nio.ByteBuffer

interface DirectAccessor {

    fun readRealDirect(vr: ByteBuffer, ref: ByteBuffer): FmiStatus

    fun writeRealDirect(vr: ByteBuffer, value: ByteBuffer): FmiStatus

}
