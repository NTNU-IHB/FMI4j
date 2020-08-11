package no.ntnu.ihb.fmi4j.importer.fmi2.cs.vendors.twentysim

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import no.ntnu.ihb.fmi4j.read
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class ControlledTemperatureTest {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest::class.java)
    }

    @Test
    fun test() {

        TestFMUs.get("2.0/cs/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu").let {
            Fmu.from(it).asCoSimulationFmu().use { fmu ->

                fmu.newInstance(fmu.modelDescription.attributes.modelIdentifier, loggingOn = true).use { slave ->

                    Assertions.assertEquals("2.0", slave.modelDescription.fmiVersion)

                    val startTemp = slave.modelDescription
                            .getVariableByName("HeatCapacity1.T0")
                            .asRealVariable().start
                    Assertions.assertNotNull(startTemp)
                    Assertions.assertEquals(298.0, startTemp!!)

                    Assertions.assertTrue(slave.simpleSetup())

                    val heatCapacity1_C = slave.modelDescription
                            .getVariableByName("HeatCapacity1.C").asRealVariable()
                    Assertions.assertEquals(0.1, heatCapacity1_C.start!!)
                    LOG.debug("heatCapacity1_C=${heatCapacity1_C.read(slave).value}")

                    val temperatureRoom = slave.modelDescription
                            .getVariableByName("Temperature_Room").asRealVariable()

                    val dt = 1.0 / 100
                    for (i in 0..4) {
                        Assertions.assertTrue(slave.doStep(dt))
                        Assertions.assertTrue(slave.lastStatus === FmiStatus.OK)

                        val read = temperatureRoom.read(slave)
                        Assertions.assertTrue(read.status == FmiStatus.OK)
                        val value = read.value

                        LOG.info("t=${slave.simulationTime}, Temperature_Room=$value")
                    }

                }

            }

        }
    }
}
