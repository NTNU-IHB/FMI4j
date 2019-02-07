package no.ntnu.ihb.fmu2jar

import no.ntnu.ihb.fmi4j.common.FmuSlave
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(MainTest::class.java)
    }

    private val out = Files.createTempDirectory("fmu2jar_test_").toFile().also {
        LOG.info("Created out dir: $it.absolutePath")
    }

    @AfterAll
    fun tearDown() {
        if (out.deleteRecursively()) {
            LOG.info("Deleted generated folder and all it's contents: ${out.absolutePath}")
        }
    }

    @Test
    fun testMain() {

        val fmuName = "ControlledTemperature"
        val file = File("../fmus/2.0/cs/20sim/4.6.4.8004/$fmuName/$fmuName.fmu")
        Assertions.assertTrue(file.exists())

        val args = arrayOf<String>(
                "--fmu", file.absolutePath,
                "-out", out.absolutePath,
                "-mvn")

        LOG.debug(args.joinToString(" "))

        Fmu2Jar.main(args)

        val myJar = out.listFiles()[0]
        Assertions.assertTrue(myJar.name.endsWith(".jar"))
        Assertions.assertTrue(myJar.length() > 0)

        val child = URLClassLoader(arrayOf(myJar.toURI().toURL()), this.javaClass.classLoader)
        val classToLoad = Class.forName("no.ntnu.ihb.fmu2jar.$fmuName", true, child)

        val method = classToLoad.getDeclaredMethod("newInstance")
        (method.invoke(null) as FmuSlave).use { slave ->

            slave.simpleSetup()

            val stop = 1.0
            val stepSize = 1.0/100
            while (slave.simulationTime <= stop) {
                Assertions.assertTrue(slave.doStep(stepSize))
            }

        }

    }

}

