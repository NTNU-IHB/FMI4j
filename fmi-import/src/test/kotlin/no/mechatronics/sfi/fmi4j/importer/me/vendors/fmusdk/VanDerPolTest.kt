package no.mechatronics.sfi.fmi4j.importer.me.vendors.fmusdk

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.importer.Fmu
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator
import org.apache.commons.math3.ode.nonstiff.LutherIntegrator
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.LoggerFactory
import java.io.File

@EnabledOnOs(OS.WINDOWS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class VanDerPolTest {

    private companion object {
        private val LOG = LoggerFactory.getLogger(VanDerPolTest::class.java)
    }

    private val fmu = Fmu.from(File(TestUtils.getTEST_FMUs(),
            "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu"))

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

        fmu.asModelExchangeFmu().newInstance(integrator).use { instance ->

            val variableName = "x0"
            val x0 = instance.modelVariables
                    .getByName(variableName).asRealVariable()

            instance.init()

            val macroStep = 1.0 / 10
            while (instance.currentTime < 1) {
                val read = x0.read()
                Assertions.assertTrue(read.status === FmiStatus.OK)
                LOG.info("t=${instance.currentTime}, $variableName=${read.value}")
                instance.doStep(macroStep)
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