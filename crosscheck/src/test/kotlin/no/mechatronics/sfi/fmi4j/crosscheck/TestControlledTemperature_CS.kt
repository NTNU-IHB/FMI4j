package no.mechatronics.sfi.fmi4j.crosscheck

import no.mechatronics.sfi.fmi4j.TestUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class TestControlledTemperature_CS {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(TestControlledTemperature_CS::class.java)
    }

    @Test
    fun test() {

        val path = "${TestUtils.getTEST_FMUs()}/FMI_2.0/CoSimulation/${TestUtils.getOs()}/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"
        Assertions.assertTrue(File(path).exists())

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