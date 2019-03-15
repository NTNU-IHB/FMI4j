package no.ntnu.ihb.fmi4j.importer.me.vendors.openmodelica

import no.ntnu.ihb.fmi4j.common.FmiStatus
import no.ntnu.ihb.fmi4j.common.read
import no.ntnu.ihb.fmi4j.importer.TestFMUs
import no.ntnu.ihb.fmi4j.solvers.Solver
import no.ntnu.ihb.fmi4j.solvers.apache.ApacheSolver
import no.ntnu.ihb.fmi4j.solvers.apache.ApacheSolvers
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FmuExportCrossCompile {

    private companion object {

        private val LOG = LoggerFactory.getLogger(FmuExportCrossCompile::class.java)

        const val stop = 1.0
        const val macroStep = 1.0 / 10
        const val microStep = 1E-3

        val fmu = TestFMUs.fmi20().cs()
                .vendor("OpenModelica").version("v1.11.0")
                .name("FmuExportCrossCompile").fmu().asModelExchangeFmu()

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

            while (slave.simulationTime <= stop) {
                Assertions.assertTrue(slave.doStep(macroStep))
                h.read(slave).also {
                    Assertions.assertEquals(FmiStatus.OK, it.status)
                    LOG.info("t=${slave.simulationTime}, h=${it.value}")
                }
            }

        }

    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testEuler() {
        runFmu(ApacheSolvers.euler(microStep))
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testRungeKutta() {
        runFmu(ApacheSolvers.rk4(microStep))
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testLuther() {
        runFmu(ApacheSolvers.luther(microStep))
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testMidpoint() {
        runFmu(ApacheSolvers.midpoint(microStep))
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testDp() {
        runFmu(ApacheSolver(DormandPrince853Integrator(0.0, microStep, 1E-4, 1E-4)))
    }

}