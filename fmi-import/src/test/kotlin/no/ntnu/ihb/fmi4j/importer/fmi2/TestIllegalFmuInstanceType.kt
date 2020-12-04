package no.ntnu.ihb.fmi4j.importer.fmi2

import no.ntnu.ihb.fmi4j.TestFMUs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException

class TestIllegalFmuInstanceType {

    @Test
    fun testNewInstanceME() {
        Assertions.assertThrows(IllegalStateException::class.java) {
            TestFMUs.get("2.0/cs/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu").let {
                Fmu.from(it).use { fmu ->
                    fmu.asModelExchangeFmu()
                }
            }
        }
    }

    @Test
    fun testWrongExtension() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Fmu.from(File("test.dummy"))
        }
    }

    @Test
    fun testNoSuchFile() {
        Assertions.assertThrows(FileNotFoundException::class.java) {
            Fmu.from(File("test.fmu"))
        }
    }

}
