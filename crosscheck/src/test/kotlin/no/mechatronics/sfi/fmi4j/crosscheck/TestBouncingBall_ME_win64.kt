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

        val path = "../test/fmi2/me/win64/FMUSDK/2.0.4/bouncingBall/bouncingBall.fmu"
        Assert.assertTrue(File(path).exists())

        val args = arrayOf(
                "-$FMU", "\"$path\"",
                "-$STEP_SIZE", "0.01",
                "-$STOP_TIME", "4",
                "-$OUTPUT_VARIABLES", "h der(h) v der(v) g e",
                "-$OUT_DIR", ".",
                "-$ME"
        )

        FmuDriver.main(args)

        File("bouncingBall_out.csv").apply {
            if (exists()) {
                delete()
            }
        }

    }

}