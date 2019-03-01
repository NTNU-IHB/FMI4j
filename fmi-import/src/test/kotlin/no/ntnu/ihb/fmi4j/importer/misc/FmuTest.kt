package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.importer.Fmu
import no.ntnu.ihb.fmi4j.importer.TestFMUs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream
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

        val file = TestFMUs.fmi20().cs()
                .vendor("20sim").version("4.6.4.8004")
                .name(fmuName).file()


        val bytes = FileInputStream(file).use {
            it.readBytes()
        }

        Fmu.from(fmuName, bytes).use {
            Assertions.assertEquals("2.0", it.modelDescription.fmiVersion)
        }


    }

}