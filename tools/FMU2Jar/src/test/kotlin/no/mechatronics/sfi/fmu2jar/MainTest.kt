package no.mechatronics.sfi.fmu2jar

import no.mechatronics.sfi.fmi4j.common.FmiSimulation
import no.mechatronics.sfi.fmu2jar.util.TEST_FMUs
import no.mechatronics.sfi.fmu2jar.util.currentOS
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
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
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
        val file = File(TEST_FMUs +
                "/FMI_2.0/CoSimulation/$currentOS/20sim/4.6.4.8004/ControlledTemperature/$fmuName.fmu")
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
        val classToLoad = Class.forName("no.mechatronics.sfi.fmu2jar.$fmuName", true, child)

        val method = classToLoad.getDeclaredMethod("newInstance")
        val instance = method.invoke(null) as FmiSimulation

        instance.use { fmu ->
            val dt = 1.0/100
            fmu.init()
            while (instance.currentTime < 5) {
                Assertions.assertTrue(fmu.doStep(dt))
            }
        }

    }
}

