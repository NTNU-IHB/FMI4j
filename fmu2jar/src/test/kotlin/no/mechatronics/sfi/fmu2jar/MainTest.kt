package no.mechatronics.sfi.fmu2jar

import org.junit.After
import org.junit.Assert
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

    lateinit var out: File

    @Before
    fun setUp() {
        out = Files.createTempDirectory("fmu2jar_").toFile()
    }

    @After
    fun tearDown() {
        if(out.deleteRecursively()) {
            LOG.info("Deleted generated folder and all it's contents: ${out.absolutePath}")
        }
    }

    @Test
    fun main() {

        val fmu = File("src\\test\\resources\\ControlledTemperature.fmu")
        Assert.assertTrue(fmu.exists())
        val args = arrayOf<String>(
                "-fmu",
                fmu.absolutePath,
                "-out",
                out.absolutePath,
                "-mavenLocal"
        )

        ApplicationStarter.main(args)

        val generatedFile = out.listFiles()[0]
        Assert.assertTrue(generatedFile.name.endsWith(".jar"))
        Assert.assertTrue(generatedFile.length() > 0)

    }
}

