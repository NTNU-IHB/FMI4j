package no.mechatronics.sfi.fmi4j.crosscheck

import org.junit.Assert
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class TestControlledTemperature_CS_win64 {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(TestControlledTemperature_CS_win64::class.java)
    }

    @Test
    fun setup() {

        val path = "$TEST_FMUs/FMI_2.0/CoSimulation/win64/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"
        Assert.assertTrue(File(path).exists())

        val args = arrayOf(
                "-fmu", "\"$path\"",
                "-dt", "1E-4",
                "-stop", "5",
                "-vars", "Temperature_Reference, Temperature_Room"
        )

        FmuDriver.main(args)

        File("ControlledTemperature_out.csv").apply {
            if (exists()) {
                delete()
            }
        }

    }

}