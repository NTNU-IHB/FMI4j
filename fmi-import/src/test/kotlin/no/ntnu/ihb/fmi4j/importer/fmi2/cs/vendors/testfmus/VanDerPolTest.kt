package no.ntnu.ihb.fmi4j.importer.fmi2.cs.vendors.testfmus

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
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

        val fmu = TestFMUs.get("2.0/both/Test-FMUs/0.0.1/VanDerPol/VanDerPol.fmu").let {
            Fmu.from(it).asCoSimulationFmu()
        }

        fmu.newInstance(fmu.modelDescription.attributes.modelIdentifier, loggingOn = false).use { slave ->

            Assertions.assertTrue(slave.simpleSetup())

            val variableName = "x0"
            val x0 = slave.modelVariables
                    .getByName(variableName).asRealVariable()

            var t = 0.0
            val stop = 1.0
            val dt = 1E-2
            while (t <= stop) {
                x0.read(slave).also { read ->
                    Assertions.assertEquals(FmiStatus.OK, read.status)
                    LOG.info("t=$t, $variableName=${read.value}")
                }
                Assertions.assertTrue(slave.doStep(t, dt))
                t += dt
            }

        }

        fmu.close()

    }

}
