package no.mechatronics.sfi.fmi4j.importer.me.vendors.fmusdk

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.importer.Fmu
import no.mechatronics.sfi.fmi4j.importer.me.Solver
import no.sfi.mechatronics.fmi4j.me.ApacheSolvers
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
class BouncingBallTest {

    private companion object {
        private val LOG = LoggerFactory.getLogger(BouncingBallTest::class.java)
    }

    private val fmu = Fmu.from(File(TestUtils.getTEST_FMUs(),
            "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/bouncingBall/bouncingBall.fmu"))

    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    @Test
    fun testVersion() {
        Assertions.assertEquals("2.0", fmu.modelDescription.fmiVersion)
    }

    private fun runFmu(solver: Solver) {

        LOG.info("Using solver: '${solver.name}'")

        fmu.asModelExchangeFmu().newInstance(solver).use { fmu ->

            val h = fmu.modelVariables
                    .getByName("h").asRealVariable()

            fmu.init()

            val macroStep = 1.0 / 10
            while (fmu.currentTime < 1) {
                val read = h.read()
                Assertions.assertTrue(read.status === FmiStatus.OK)
                LOG.info("t=${fmu.currentTime}, h=${read.value}")
                fmu.doStep(macroStep)
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

}