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

class ModelExchangeTest_kt {

    private companion object {

        private val LOG = LoggerFactory.getLogger(ModelExchangeTest_java::class.java)

        private lateinit var fmu: Fmu

        @JvmStatic
        @BeforeClass
        @Throws(IOException::class)
        fun setUp() {
            val file = File(TEST_FMUs, "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu")
            Assert.assertTrue(file.exists())
            fmu = Fmu.from(file)
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            fmu.close()
        }

    }

    @Test
    fun testVersion() {
        Assert.assertEquals("2.0", fmu.modelDescription.fmiVersion)
    }

    private fun runFmu(integrator: FirstOrderIntegrator) {

        LOG.info("Using integrator: ${integrator.javaClass.simpleName}")

        fmu.asModelExchangeFmu().newInstance(integrator, loggingOn = true).use { fmu ->

            val x0 = fmu.modelVariables
                    .getByName("x0").asRealVariable()

            fmu.init(0.0, 0.0)

            val macroStep = 1.0 / 10
            while (fmu.currentTime < 1) {
                val read = x0.read()
                Assert.assertTrue(read.status === FmiStatus.OK)
                LOG.info("t=${fmu.currentTime}, x0=${read.value}")
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