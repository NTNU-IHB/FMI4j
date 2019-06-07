package no.ntnu.ihb.fmi4j.importer.fmi2

import no.ntnu.ihb.fmi4j.importer.TestFMUs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException

class TestIllegalFmuInstanceType {

    @Test
    fun testNewInstanceME() {
        Assertions.assertThrows(IllegalStateException::class.java) {
            TestFMUs.fmi20().cs()
                    .vendor("20sim").version("4.6.4.8004")
                    .name("ControlledTemperature").fmu().use {
                it.asModelExchangeFmu()
            }
        }
    }

//    @Test
//    fun testNewInstanceCS() {
//        Assertions.assertThrows(IllegalStateException::class.java) {
//            TestFMUs.fmi20().me()
//                    .vendor("SystemModeler").version("5.0")
//                    .name("ControlledTemperature").fmu().use {
//                it.asCoSimulationFmu()
//            }
//        }
//    }

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