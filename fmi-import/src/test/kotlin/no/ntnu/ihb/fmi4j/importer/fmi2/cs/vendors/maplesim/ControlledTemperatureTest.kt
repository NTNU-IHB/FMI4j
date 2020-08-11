package no.ntnu.ihb.fmi4j.importer.fmi2.cs.vendors.maplesim

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import no.ntnu.ihb.fmi4j.read
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@EnabledOnOs(OS.WINDOWS)
class ControlledTemperatureTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest::class.java)
    }

    @Test
    fun test() {

        TestFMUs.get("2.0/cs/MapleSim/2017/ControlledTemperature/ControlledTemperature.fmu").let {
            Fmu.from(it).asCoSimulationFmu().use { fmu ->

                fmu.newInstance(fmu.modelDescription.attributes.modelIdentifier).use { slave ->

                    Assertions.assertEquals("2.0", slave.modelDescription.fmiVersion)

                    val heatCapacitorT = slave.modelDescription
                            .getVariableByName("heatCapacitor.T").asRealVariable()
                    Assertions.assertEquals(2.93149999999999980e+02, heatCapacitorT.start!!)

                    Assertions.assertTrue(slave.simpleSetup())
                    Assertions.assertTrue(slave.lastStatus === FmiStatus.OK)

                    LOG.debug("heatCapacitor_T=${heatCapacitorT.read(slave).value}")

                    val tempInputValue = slave.modelDescription
                            .getVariableByName("outputs[2]").asRealVariable()

                    val stepSize = 1.0 / 100
                    for (i in 0..4) {
                        Assertions.assertTrue(slave.doStep(stepSize))
                        Assertions.assertEquals(slave.lastStatus, FmiStatus.OK)

                        tempInputValue.read(slave).also {
                            Assertions.assertTrue(it.status == FmiStatus.OK)
                            LOG.info("t=${slave.simulationTime}, outputs[2]=${it.value}")
                        }

                    }

                }

            }

        }
    }
}
