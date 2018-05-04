package no.mechatronics.sfi.fmi4j.crosscheck

import org.junit.Assert
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class TestBouncingBall_ME_win64 {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(TestBouncingBall_ME_win64::class.java)
    }

    @Test
    fun setup() {

        val name = "bouncingBall"
        val path = "$TEST_FMUs/FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/bouncingBall/$name.fmu"
        Assert.assertTrue(File(path).exists())

        val args = arrayOf(
                "-fmu", "\"$path\"",
                "-dt", "0.01",
                "-stop", "4",
                "-me",
                "-vars", "h, der(h), v, der(v), g, e"
        )

        FmuDriver.main(args)

        val fileName = "${name}_out.csv"
        File(fileName).apply {
            if (exists()) {
                delete()
                LOG.info("Deleted $fileName")
            }
        }

    }

}