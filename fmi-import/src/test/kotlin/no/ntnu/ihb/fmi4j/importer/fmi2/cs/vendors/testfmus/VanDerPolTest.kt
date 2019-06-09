package no.ntnu.ihb.fmi4j.importer.fmi2.cs.vendors.testfmus

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.importer.TestFMUs
import no.ntnu.ihb.fmi4j.read
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class VanDerPolTest {

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(VanDerPolTest::class.java)
    }

    @Test
    fun testInstance() {

        val fmu = TestFMUs.fmi20().both()
                .vendor("Test-FMUs").version("0.0.1")
                .name("VanDerPol").fmu().asCoSimulationFmu()

        fmu.newInstance(loggingOn = false).use { slave ->

            Assertions.assertTrue(slave.simpleSetup())

            val variableName = "x0"
            val x0 = slave.modelVariables
                    .getByName(variableName).asRealVariable()

            val stop = 1.0
            val macroStep = 1E-2
            while (slave.simulationTime <= stop) {
                x0.read(slave).also { read ->
                    Assertions.assertEquals(FmiStatus.OK, read.status)
                    LOG.info("t=${slave.simulationTime}, $variableName=${read.value}")
                }
                Assertions.assertTrue(slave.doStep(macroStep))
            }

        }

        fmu.close()

    }

}