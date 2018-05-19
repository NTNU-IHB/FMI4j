package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator
import org.apache.commons.math3.ode.nonstiff.LutherIntegrator
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ModelExchangeTest_kt {

    private companion object {
        private val LOG = LoggerFactory.getLogger(ModelExchangeTest_java::class.java)
    }


    private val fmu: Fmu

    init {
        val file = File(TEST_FMUs, "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu")
        Assertions.assertTrue(file.exists())
        fmu = Fmu.from(file)
    }

    @AfterAll
    fun tearDown() {
        fmu.close()
    }


    @Test
    fun testVersion() {
        Assertions.assertEquals("2.0", fmu.modelDescription.fmiVersion)
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
                Assertions.assertTrue(read.status === FmiStatus.OK)
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