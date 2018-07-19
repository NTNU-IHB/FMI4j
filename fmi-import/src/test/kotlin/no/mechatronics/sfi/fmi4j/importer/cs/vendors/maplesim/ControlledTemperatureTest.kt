package no.mechatronics.sfi.fmi4j.importer.cs.vendors.maplesim

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
        val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest::class.java)
    }

    @Test
    fun test() {

        val file = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/${TestUtils.getOs()}/MapleSim/2017/ControlledTemperature/ControlledTemperature.fmu")

        Fmu.from(file).use { fmu ->

            fmu.asCoSimulationFmu().newInstance(loggingOn = true).use { instance ->

                Assertions.assertEquals("2.0", instance.modelDescription.fmiVersion)

                val heatCapacitor_T = instance
                        .getVariableByName("heatCapacitor.T").asRealVariable()
                Assertions.assertEquals(2.93149999999999980e+02, heatCapacitor_T.start!!)

                instance.init()
                Assertions.assertTrue(instance.lastStatus === FmiStatus.OK)

                LOG.debug("heatCapacitor_T=${heatCapacitor_T.read().value}")

                val tempInputValue = instance
                        .getVariableByName("outputs[2]").asRealVariable()

                val dt = 1.0 / 100
                for (i in 0..4) {
                    instance.doStep(dt)
                    Assertions.assertTrue(instance.lastStatus === FmiStatus.OK)

                    val read = tempInputValue.read()
                    Assertions.assertTrue(read.status == FmiStatus.OK)
                    val value = read.value

                    LOG.info("TempInput.value=$value")
                }

            }

        }

    }
}
