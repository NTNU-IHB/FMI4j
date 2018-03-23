package no.mechatronics.sfi.fmi4j.fmu

import org.junit.Assert
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException


class TestIllegalFmuType {

    @Test(expected = IllegalStateException::class)
    fun testNewInstanceME() {
        val path = "../test/fmi2/cs/win64/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"
        val file = File(path)
        Assert.assertNotNull(file)
        FmuFile.from(file).use {
            it.asModelExchangeFmu()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testNewInstanceCS() {
        val path = "../test/fmi2/me/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu"
        val file = File(path)
        Assert.assertNotNull(file)
        FmuFile.from(file).use {
            it.asCoSimulationFmu()
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testWrongExtension() {
        FmuFile.from(File("test.dummy"))
    }

    @Test(expected = FileNotFoundException::class)
    fun testNoSuchFile() {
        FmuFile.from(File("test.fmu"))
    }

}