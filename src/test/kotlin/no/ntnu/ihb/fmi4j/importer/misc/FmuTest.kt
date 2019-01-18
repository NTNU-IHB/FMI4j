package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.importer.Fmu
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

}