package no.mechatronics.sfi.fmi4j.importer.misc

import no.mechatronics.sfi.fmi4j.TestFMUs
import no.mechatronics.sfi.fmi4j.importer.Fmu
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import java.io.File
import java.io.FileNotFoundException

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class TestIllegalFmuInstanceType {

    @Test
    fun testNewInstanceME() {
        val file = TestFMUs.fmi20().cs()
                .vendor("20sim").version("4.6.4.8004").file("ControlledTemperature")
        Assertions.assertTrue(file.exists())
        Assertions.assertThrows(IllegalStateException::class.java) {
            Fmu.from(file).use {
                it.asModelExchangeFmu()
            }
        }
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testNewInstanceCS() {
        val file = TestFMUs.fmi20().cs()
                .vendor("FMUSDK").version("2.0.4").file("vanDerPol")
        Assertions.assertTrue(file.exists())
        Assertions.assertThrows(IllegalStateException::class.java) {
            Fmu.from(file).use {
                it.asCoSimulationFmu()
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