package no.mechatronics.sfi.fmi4j.importer.cs.vendors.maplesim

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
        val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest::class.java)
    }

    @Test
    fun test() {

        val file = File(TestUtils.getTEST_FMUs(),
                "2.0/cs/$currentOS" +
                        "/MapleSim/2017/ControlledTemperature/ControlledTemperature.fmu")

        Fmu.from(file).use { fmu ->

            fmu.asCoSimulationFmu().newInstance().use { slave ->

                Assertions.assertEquals("2.0", slave.modelDescription.fmiVersion)

                val heatCapacitor_T = slave.modelDescription
                        .getVariableByName("heatCapacitor.T").asRealVariable()
                Assertions.assertEquals(2.93149999999999980e+02, heatCapacitor_T.start!!)

                slave.setupExperiment()
                slave.enterInitializationMode()
                slave.exitInitializationMode()
                Assertions.assertTrue(slave.lastStatus === FmiStatus.OK)

                LOG.debug("heatCapacitor_T=${heatCapacitor_T.read(slave).value}")

                val tempInputValue = slave.modelDescription
                        .getVariableByName("outputs[2]").asRealVariable()

                val dt = 1.0 / 100
                for (i in 0..4) {
                    slave.doStep(dt)
                    Assertions.assertTrue(slave.lastStatus === FmiStatus.OK)

                    tempInputValue.read(slave).also {
                        Assertions.assertTrue(it.status == FmiStatus.OK)
                        LOG.info("t=${slave.simulationTime}, outputs[2]=${it.value}")
                    }

                }

            }

        }

    }
}
