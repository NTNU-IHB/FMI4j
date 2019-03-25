package no.ntnu.ihb.fmi4j.driver

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File


class TestBouncingBall {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(TestBouncingBall::class.java)
    }

    @Test
    fun test() {

        val name = "BouncingBall"
        val path = "../fmus/2.0/Test-FMUs/0.0.1/$name/$name.fmu"
        Assertions.assertTrue(File(path).exists())

        val args = arrayOf(
                "-f", "\"$path\"",
                "-dt", "0.01",
                "-stop", "4",
                "-me",
                "h", "v"
        )

        Cmd.main(args)

        val fileName = "${name}_out.csv"
        File(fileName).apply {
            if (exists()) {
                delete()
                LOG.info("Deleted $fileName")
            }
        }

    }

}