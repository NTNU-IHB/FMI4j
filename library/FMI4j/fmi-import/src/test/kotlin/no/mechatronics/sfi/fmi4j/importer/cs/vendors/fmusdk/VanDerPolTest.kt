package no.mechatronics.sfi.fmi4j.importer.cs.vendors.fmusdk

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.importer.Fmu
import no.mechatronics.sfi.fmi4j.importer.me.vendors.fmusdk.VanDerPolTest
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
            "FMI_2.0/CoSimulation/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu"))

    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    @Test
    fun testInstance() {

        fmu.asCoSimulationFmu().newInstance().use { slave ->

            slave.init()

            val variableName = "x0"
            val x0 = slave.modelVariables
                    .getByName(variableName).asRealVariable()

            val stop = 1.0
            val macroStep = 1E-2
            while (slave.simulationTime <= stop) {
                val read = x0.read()
                Assertions.assertTrue(read.status === FmiStatus.OK)
                LOG.info("t=${slave.simulationTime}, $variableName=${read.value}")
                slave.doStep(macroStep)
            }

        }

    }

}