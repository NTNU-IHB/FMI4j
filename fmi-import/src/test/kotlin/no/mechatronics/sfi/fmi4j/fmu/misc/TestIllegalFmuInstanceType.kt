package no.mechatronics.sfi.fmi4j.fmu.misc

import no.mechatronics.sfi.fmi4j.fmu.Fmu
import no.mechatronics.sfi.fmi4j.fmu.TEST_FMUs
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException


class TestIllegalFmuInstanceType {

    @Test(expected = IllegalStateException::class)
    fun testNewInstanceME() {
        val file = File(TEST_FMUs, "FMI_2.0/CoSimulation/win64/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")
        Assert.assertTrue(file.exists())
        Fmu.from(file).use {
            it.asModelExchangeFmu()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testNewInstanceCS() {
        val file = File(TEST_FMUs, "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu")
        Assert.assertTrue(file.exists())
        Fmu.from(file).use {
            it.asCoSimulationFmu()
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testWrongExtension() {
        Fmu.from(File("test.dummy"))
    }

    @Test(expected = FileNotFoundException::class)
    fun testNoSuchFile() {
        Fmu.from(File("test.fmu"))
    }

}