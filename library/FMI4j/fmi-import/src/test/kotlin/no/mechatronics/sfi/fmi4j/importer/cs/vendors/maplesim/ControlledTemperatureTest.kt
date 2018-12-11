package no.mechatronics.sfi.fmi4j.importer.cs.vendors.maplesim

import no.mechatronics.sfi.fmi4j.TestFMUs
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class ControlledTemperatureTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest::class.java)
    }

    @Test
    fun test() {

        TestFMUs.fmi20().cs()
                .vendor("MapleSim").version("2017")
                .fmu("ControlledTemperature").use { fmu ->

                    fmu.asCoSimulationFmu().newInstance().use { slave ->

                        Assertions.assertEquals("2.0", slave.modelDescription.fmiVersion)

                        val heatCapacitorT = slave.modelDescription
                                .getVariableByName("heatCapacitor.T").asRealVariable()
                        Assertions.assertEquals(2.93149999999999980e+02, heatCapacitorT.start!!)

                        slave.simpleSetup()
                        Assertions.assertTrue(slave.lastStatus === FmiStatus.OK)

                        LOG.debug("heatCapacitor_T=${heatCapacitorT.read(slave).value}")

                        val tempInputValue = slave.modelDescription
                                .getVariableByName("outputs[2]").asRealVariable()

                        val dt = 1.0 / 100
                        for (i in 0..4) {
                            Assertions.assertTrue(slave.doStep(dt))
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
