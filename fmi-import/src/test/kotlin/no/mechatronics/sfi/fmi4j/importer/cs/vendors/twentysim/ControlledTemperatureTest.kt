package no.mechatronics.sfi.fmi4j.importer.cs.vendors.twentysim

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.importer.AbstractFmuInstance
import no.mechatronics.sfi.fmi4j.importer.Fmu
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class ControlledTemperatureTest {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest::class.java)
    }

    @Test
    fun test() {

        val file = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/${TestUtils.getOs()}/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")

        Fmu.from(file).use { fmu ->

            fmu.asCoSimulationFmu().newInstance(loggingOn = true).use { instance ->

                Assertions.assertEquals("2.0", instance.modelDescription.fmiVersion)

                val startTemp = instance.getVariableByName("HeatCapacity1.T0")
                        .asRealVariable().start
                Assertions.assertNotNull(startTemp)
                Assertions.assertEquals(298.0, startTemp!!)

                instance.init()
                Assertions.assertTrue(instance.lastStatus === FmiStatus.OK)

                val heatCapacity1_C = instance.getVariableByName("HeatCapacity1.C")
                        .asRealVariable()
                Assertions.assertEquals(0.1, heatCapacity1_C.start!!)
                LOG.debug("heatCapacity1_C=${heatCapacity1_C.read().value}")

                val temperatureRoom = instance.getVariableByName("Temperature_Room")
                        .asRealVariable()

                val dt = 1.0 / 100
                for (i in 0..4) {
                    Assertions.assertTrue(instance.doStep(dt))
                    Assertions.assertTrue(instance.lastStatus === FmiStatus.OK)

                    val read = temperatureRoom.read()
                    Assertions.assertTrue(read.status == FmiStatus.OK)
                    val value = read.value

                    LOG.info("t=${instance.currentTime}, Temperature_Room=$value")
                }

            }

        }

    }
}
