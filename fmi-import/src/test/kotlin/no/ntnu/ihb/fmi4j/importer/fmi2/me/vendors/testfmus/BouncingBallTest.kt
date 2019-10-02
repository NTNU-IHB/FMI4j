package no.ntnu.ihb.fmi4j.importer.fmi2.me.vendors.testfmus

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.importer.TestFMUs
import no.ntnu.ihb.fmi4j.importer.fmi2.me.vendors.fmusdk.BouncingBallTest
import no.ntnu.ihb.fmi4j.read
import no.ntnu.ihb.fmi4j.solvers.Solver
import no.ntnu.ihb.fmi4j.solvers.apache.ApacheSolver
import no.ntnu.ihb.fmi4j.solvers.apache.ApacheSolvers
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BouncingBallTest {

    private companion object {
        private val LOG = LoggerFactory.getLogger(BouncingBallTest::class.java)

        private val fmu = TestFMUs.fmi20().both()
                .vendor("Test-FMUs").version("0.0.1")
                .name("BouncingBall").fmu().asModelExchangeFmu()

    }

    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    @Test
    fun test() {
        fmu.modelDescription.modelVariables.getByName("h").asRealVariable().also {
            Assertions.assertEquals(1.0, it.start)
        }
    }

    private fun runFmu(solver: Solver) {

        LOG.info("Using solver: '${solver.name}'")

        fmu.newInstance(solver).use { slave ->

            val h = slave.modelVariables
                    .getByName("h").asRealVariable()

            Assertions.assertTrue(slave.simpleSetup())

            val macroStep = 1.0 / 10
            while (slave.simulationTime <= 1) {
                Assertions.assertTrue(slave.doStep(macroStep))
                h.read(slave).also {
                    Assertions.assertEquals(FmiStatus.OK, it.status)
                    LOG.info("t=${slave.simulationTime}, h=${it.value}")
                }
            }

        }

    }

    @Test
    fun testEuler() {
        runFmu(ApacheSolvers.euler(1E-3))
    }

    @Test
    fun testRungeKutta() {
        runFmu(ApacheSolvers.rk4(1E-3))
    }

    @Test
    fun testLuther() {
        runFmu(ApacheSolvers.luther(1E-3))
    }

    @Test
    fun testMidpoint() {
        runFmu(ApacheSolvers.midpoint(1E-3))
    }

    @Test
    fun testDp() {
        runFmu(ApacheSolver(DormandPrince853Integrator(0.0, 1E-3, 1E-4, 1E-4)))
    }

}
