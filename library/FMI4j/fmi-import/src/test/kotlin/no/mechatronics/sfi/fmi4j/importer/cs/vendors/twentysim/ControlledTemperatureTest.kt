package no.mechatronics.sfi.fmi4j.importer.cs.vendors.twentysim

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.common.currentOS
import no.mechatronics.sfi.fmi4j.importer.Fmu
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class ControlledTemperatureTest {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest::class.java)
    }

    @Test
    fun test() {

        val file = File(TestUtils.getTEST_FMUs(),
                "2.0/cs/$currentOS" +
                        "/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")

        Fmu.from(file).use { fmu ->

            fmu.asCoSimulationFmu().newInstance(loggingOn = true).use { slave ->

                Assertions.assertEquals("2.0", slave.modelDescription.fmiVersion)

                val startTemp = slave.modelDescription
                        .getVariableByName("HeatCapacity1.T0")
                        .asRealVariable().start
                Assertions.assertNotNull(startTemp)
                Assertions.assertEquals(298.0, startTemp!!)

                slave.setupExperiment()
                slave.enterInitializationMode()
                slave.exitInitializationMode()

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
