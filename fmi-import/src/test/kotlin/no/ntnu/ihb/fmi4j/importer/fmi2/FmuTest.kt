package no.ntnu.ihb.fmi4j.importer.fmi2

import no.ntnu.ihb.fmi4j.TestFMUs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException

class FmuTest {

    @Test
    fun testWrongExtension() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Fmu.from(File("wrong_extension.fm"))
        }
    }

    @Test
    fun testMissingFile() {
        Assertions.assertThrows(FileNotFoundException::class.java) {
            Fmu.from(File("missing_file.fmu"))
        }
    }

    @Test
    fun testFromBinary() {
        val fmuName = "ControlledTemperature"
        val file = TestFMUs.get("2.0/cs/20sim/4.6.4.8004/$fmuName/${fmuName}.fmu")

        val bytes = file.readBytes()
        Fmu.from(fmuName, bytes).use {
            Assertions.assertEquals("2.0", it.modelDescription.fmiVersion)
        }
    }

    @Test
    fun testFromUrl() {
        val fmuName = "ControlledTemperature"
        Fmu.from(TestFMUs.get("2.0/cs/20sim/4.6.4.8004/$fmuName/${fmuName}.fmu")).use {
            Assertions.assertEquals("ControlledTemperature", it.modelDescription.modelName)
        }
    }

    @Test
    fun testIllegalFmu() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Fmu.from(FmuTest::class.java.classLoader.getResource("illegal.fmu")!!)
        }
    }

}
