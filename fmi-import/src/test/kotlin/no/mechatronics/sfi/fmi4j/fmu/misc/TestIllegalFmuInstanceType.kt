package no.mechatronics.sfi.fmi4j.fmu.misc

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.fmu.Fmu
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
        val file = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/${TestUtils.getOs()}/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")
        Assertions.assertTrue(file.exists())
        Assertions.assertThrows(IllegalStateException::class.java, {
            Fmu.from(file).use {
                it.asModelExchangeFmu()
            }
        })
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testNewInstanceCS() {
        val file = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu")
        Assertions.assertTrue(file.exists())
        Assertions.assertThrows(IllegalStateException::class.java, {
            Fmu.from(file).use {
                it.asCoSimulationFmu()
            }
        })
    }

    @Test
    fun testWrongExtension() {
        Assertions.assertThrows(IllegalArgumentException::class.java, {
            Fmu.from(File("test.dummy"))
        })
    }

    @Test
    fun testNoSuchFile() {
        Assertions.assertThrows(FileNotFoundException::class.java, {
            Fmu.from(File("test.fmu"))
        })
    }

}