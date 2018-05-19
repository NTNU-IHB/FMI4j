package no.mechatronics.sfi.fmi4j.fmu.misc

import no.mechatronics.sfi.fmi4j.fmu.Fmu
import no.mechatronics.sfi.fmi4j.fmu.TEST_FMUs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException


class TestIllegalFmuInstanceType {

    @Test
    fun testNewInstanceME() {
        val file = File(TEST_FMUs, "FMI_2.0/CoSimulation/win64/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")
        Assertions.assertTrue(file.exists())
        Assertions.assertThrows(IllegalStateException::class.java, {
            Fmu.from(file).use {
                it.asModelExchangeFmu()
            }
        })
    }

    @Test
    fun testNewInstanceCS() {
        val file = File(TEST_FMUs, "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu")
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