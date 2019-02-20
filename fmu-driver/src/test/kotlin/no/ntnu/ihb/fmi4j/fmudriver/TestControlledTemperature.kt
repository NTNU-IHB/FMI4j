package no.ntnu.ihb.fmi4j.fmudriver

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class TestControlledTemperature {

    @Test
    fun test() {

        val name = "ControlledTemperature"
        val path = "../fmus/2.0/cs/20sim/4.6.4.8004/" +
                "$name/$name.fmu"
        Assertions.assertTrue(File(path).exists())

        val args = arrayOf(
                "-f", "\"$path\"",
                "-dt", "1E-4",
                "-stop", "5",
                "Temperature_Reference", "Temperature_Room"
        )

        Cmd.main(args)

        File("${name}_out.csv").apply {
            Assertions.assertTrue(exists())
            delete()
        }

    }

}