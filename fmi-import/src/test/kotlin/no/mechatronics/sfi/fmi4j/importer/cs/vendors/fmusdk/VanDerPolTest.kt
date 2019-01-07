package no.mechatronics.sfi.fmi4j.importer.cs.vendors.fmusdk

import no.mechatronics.sfi.fmi4j.TestFMUs
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.importer.me.vendors.fmusdk.VanDerPolTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.LoggerFactory

@EnabledOnOs(OS.WINDOWS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class VanDerPolTest {

    private companion object {
        private val LOG = LoggerFactory.getLogger(VanDerPolTest::class.java)
    }

    private val fmu = TestFMUs.fmi20().cs()
            .vendor("FMUSDK").version("2.0.4").fmu("vanDerPol")

    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    @Test
    fun testInstance() {

        fmu.asCoSimulationFmu().newInstance().use { slave ->

            slave.simpleSetup()

            val variableName = "x0"
            val x0 = slave.modelVariables
                    .getByName(variableName).asRealVariable()

            val stop = 1.0
            val macroStep = 1E-2
            while (slave.simulationTime <= stop) {
                x0.read(slave.variableAccessor).also { read ->
                    Assertions.assertTrue(read.status === FmiStatus.OK)
                    LOG.info("t=${slave.simulationTime}, $variableName=${read.value}")
                }
                Assertions.assertTrue(slave.doStep(macroStep))
            }

        }

    }

}