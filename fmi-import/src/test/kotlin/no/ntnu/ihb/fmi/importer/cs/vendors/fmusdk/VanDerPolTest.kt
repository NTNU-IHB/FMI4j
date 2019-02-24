package no.ntnu.ihb.fmi.importer.cs.vendors.fmusdk

import no.ntnu.ihb.fmi.common.Status
import no.ntnu.ihb.fmi.importer.TestFMUs
import no.ntnu.ihb.fmi.importer.me.vendors.fmusdk.VanDerPolTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.LoggerFactory

@EnabledOnOs(OS.WINDOWS)
class VanDerPolTest {

    private companion object {
        private val LOG = LoggerFactory.getLogger(VanDerPolTest::class.java)
    }

    @Test
    fun testInstance() {

        val fmu = TestFMUs.fmi20().cs()
                .vendor("FMUSDK").version("2.0.4")
                .name("vanDerPol").fmu().asCoSimulationFmu()

        fmu.newInstance().use { slave ->

            Assertions.assertTrue(slave.simpleSetup())

            val variableName = "x0"
            val x0 = slave.modelVariables
                    .getByName(variableName).asRealVariable()

            val stop = 1.0
            val macroStep = 1E-2
            while (slave.simulationTime <= stop) {
                x0.read(slave).also { read ->
                    Assertions.assertTrue(read.status === Status.OK)
                    LOG.info("t=${slave.simulationTime}, $variableName=${read.value}")
                }
                Assertions.assertTrue(slave.doStep(macroStep))
            }

        }

        fmu.close()

    }

}