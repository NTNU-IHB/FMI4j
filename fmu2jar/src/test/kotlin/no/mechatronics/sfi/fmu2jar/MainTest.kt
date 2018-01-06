package no.mechatronics.sfi.fmu2jar

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files

class MainTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(MainTest::class.java)
    }

    lateinit var tmp: File

    @Before
    fun setUp() {
        tmp = Files.createTempDirectory("fmu2jar_").toFile()
    }

    @After
    fun tearDown() {
        if(tmp.deleteRecursively()) {
            LOG.info("Deleted generated folder and all it's contents: ${tmp.absolutePath}")
        }
    }

    @Test
    fun main() {

        val args = arrayOf<String>(
                "-fmu",
                "C:\\Users\\laht\\IdeaProjects\\FMI4j\\fmi-import\\src\\test\\resources\\v2\\cs\\ControlledTemperature\\ControlledTemperature.fmu",
                "-out",
                tmp.absolutePath,
                "-mavenLocal"
        )

        Main.main(args)

    }
}

