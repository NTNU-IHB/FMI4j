package no.mechatronics.sfi.fmi4j.fmudriver

import no.mechatronics.sfi.fmu2jar.TEST_FMUs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@EnabledOnOs(OS.WINDOWS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class TestBouncingBall_ME_win64 {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(TestBouncingBall_ME_win64::class.java)
    }

    @Test
    fun test() {

        val name = "bouncingBall"
        val path = "$TEST_FMUs/2.0/me/win64/" +
                "FMUSDK/2.0.4/bouncingBall/$name.fmu"
        Assertions.assertTrue(File(path).exists())

        val args = arrayOf(
                "-f", "\"$path\"",
                "-dt", "0.01",
                "-stop", "4",
                "-me",
                "h", "der(h)", "v", "der(v)", "g", "e"
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