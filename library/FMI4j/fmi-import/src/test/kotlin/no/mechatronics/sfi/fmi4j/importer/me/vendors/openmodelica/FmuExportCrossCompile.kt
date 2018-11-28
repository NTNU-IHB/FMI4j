package no.mechatronics.sfi.fmi4j.importer.me.vendors.openmodelica

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.common.currentOS
import no.mechatronics.sfi.fmi4j.importer.Fmu
import no.mechatronics.sfi.fmi4j.solvers.Solver
import no.sfi.mechatronics.fmi4j.me.ApacheSolver
import no.sfi.mechatronics.fmi4j.me.ApacheSolvers
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator
import org.junit.jupiter.api.*
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.LoggerFactory
import java.io.File

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class FmuExportCrossCompile {

    private companion object {
        private val LOG = LoggerFactory.getLogger(FmuExportCrossCompile::class.java)

        const val stop = 1.0
        const val macroStep = 1.0 / 10
        const val microStep = 1E-3

        val fmu = Fmu.from(File(TestUtils.getTEST_FMUs(),
                "2.0/me/$currentOS/OpenModelica/v1.11.0/" +
                        "FmuExportCrossCompile/FmuExportCrossCompile.fmu")).asModelExchangeFmu()

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

            slave.simpleSetup()

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
    fun testEuler() {
        runFmu(ApacheSolvers.euler(microStep))
    }
//
    @Test
    fun testRungeKutta() {
        runFmu(ApacheSolvers.rk4(microStep))
    }

    @Test
    fun testLuther() {
        runFmu(ApacheSolvers.luther(microStep))
    }

    @Test
    fun testMidpoint() {
        runFmu(ApacheSolvers.midpoint(microStep))
    }

    @Test
    fun testDp() {
        runFmu(ApacheSolver(DormandPrince853Integrator(0.0, microStep, 1E-4, 1E-4)))
    }

}