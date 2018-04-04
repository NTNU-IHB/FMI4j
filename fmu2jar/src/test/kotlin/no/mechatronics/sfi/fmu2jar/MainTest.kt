package no.mechatronics.sfi.fmu2jar


import no.mechatronics.sfi.fmi4j.fmu.FmiSimulation
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Files


class MainTest {

    companion object {

        val LOG: Logger = LoggerFactory.getLogger(MainTest::class.java)

        lateinit var out: File

        @JvmStatic
        @BeforeClass
        fun setUp() {
            out = Files.createTempDirectory("fmu2jar_test_").toFile()
            LOG.info("Created out dir: ${out.absolutePath}")
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            if (out.deleteRecursively()) {
                LOG.info("Deleted generated folder and all it's contents: ${out.absolutePath}")
            }
        }

    }

    @Test
    fun testMain() {

        val fmuName = "ControlledTemperature"

        val path = "../test/fmi2/cs/win64/20sim/4.6.4.8004/ControlledTemperature/$fmuName.fmu"
        val file = File(path)
        Assert.assertTrue(file.exists())
        val args = arrayOf<String>(
                "-fmu", file.absolutePath,
                "-out", out.absolutePath,
                "-mavenLocal"
        )

        LOG.info(args.joinToString(" "))

        Main.main(args)

        val myJar = out.listFiles()[0]
        Assert.assertTrue(myJar.name.endsWith(".jar"))
        Assert.assertTrue(myJar.length() > 0)

        val child = URLClassLoader(arrayOf(myJar.toURI().toURL()), this.javaClass.classLoader)
        val classToLoad = Class.forName("no.mechatronics.sfi.fmu2jar.${fmuName.toLowerCase()}.$fmuName", true, child)

        val method = classToLoad.getDeclaredMethod("newInstance")
        val instance = method.invoke(null) as FmiSimulation

        instance.use { fmu ->
            val dt = 1.0/100
            Assert.assertTrue(fmu.init())
            while (instance.currentTime < 5) {
                Assert.assertTrue(fmu.doStep(dt))
            }
        }

    }
}

