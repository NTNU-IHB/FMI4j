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

        val path = "../test/fmi2/cs/win64/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"
        Assert.assertTrue(File(path).exists())

        val args = arrayOf(
                "-$FMU", "\"$path\"",
                "-$STEP_SIZE", "1E-4",
                "-$STOP_TIME", "5",
                "-$OUTPUT_VARIABLES", "Temperature_Reference Temperature_Room",
                "-$OUT_DIR", "."
        )

        FmuDriver.main(args)

        File("ControlledTemperature_out.csv").apply {
            if (exists()) {
                delete()
            }
        }

    }

}