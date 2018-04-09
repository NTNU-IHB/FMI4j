package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator
import org.apache.commons.math3.ode.nonstiff.LutherIntegrator
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException

class ModelExchangeTest2_kt {

    private companion object {

        private val LOG = LoggerFactory.getLogger(ModelExchangeTest_java::class.java)

        private lateinit var fmuFile: FmuFile

        @JvmStatic
        @BeforeClass
        @Throws(IOException::class)
        fun setUp() {
            val path = "../test/fmi2/me/win64/FMUSDK/2.0.4/bouncingBall/bouncingBall.fmu"
            val file = File(path)
            Assert.assertNotNull(file)
            fmuFile = FmuFile.from(file)
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            fmuFile.close()
        }

    }

    @Test
    fun testVersion() {
        Assert.assertEquals("2.0", fmuFile.modelDescription.fmiVersion)
    }

    private fun runFmu(integrator: FirstOrderIntegrator) {

        LOG.info("Using integrator: ${integrator.javaClass.simpleName}")

        fmuFile.asModelExchangeFmu().newInstance(integrator).use { fmu ->

            val h = fmu.modelVariables
                    .getByName("h").asRealVariable()

           fmu.init()

            val macroStep = 1.0 / 10
            while (fmu.currentTime < 1) {
                val read = h.read()
                Assert.assertTrue(read.status === FmiStatus.OK)
                LOG.info("t=${fmu.currentTime}, h=${read.value}")
                fmu.doStep(macroStep)
            }

        }

    }

    @Test
    fun testEuler() {
        runFmu(EulerIntegrator(1E-3))
    }

    @Test
    fun testRungeKutta() {
        runFmu(ClassicalRungeKuttaIntegrator(1E-3))
    }

    @Test
    fun testLuther() {
        runFmu(LutherIntegrator(1E-3))
    }
}