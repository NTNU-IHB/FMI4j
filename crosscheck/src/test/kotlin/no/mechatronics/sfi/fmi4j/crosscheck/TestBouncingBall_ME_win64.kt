package no.mechatronics.sfi.fmi4j.crosscheck

import org.junit.Assert
import org.junit.Test
import java.io.File

class TestBouncingBall_ME_win64 {

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

    }

}