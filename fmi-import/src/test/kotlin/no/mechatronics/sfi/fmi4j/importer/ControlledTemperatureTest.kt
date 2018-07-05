package no.mechatronics.sfi.fmi4j.importer


import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.FmiStatus
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class ControlledTemperatureTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest::class.java)
    }

    private val fmu: Fmu

    init {
        val file = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/${TestUtils.getOs()}/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")
        Assertions.assertTrue(file.exists())
        fmu = Fmu.from(file)
    }

    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    @Test
    fun test() {

        fmu.asCoSimulationFmu().newInstance(loggingOn = true).use { instance ->

            Assertions.assertEquals("2.0", instance.modelDescription.fmiVersion)

            val startTemp = instance.getVariableByName("HeatCapacity1.T0").asRealVariable().start
            Assertions.assertNotNull(startTemp)
            Assertions.assertEquals(298.0, startTemp!!)

            instance.init()
            Assertions.assertTrue(instance.lastStatus === FmiStatus.OK)

            val heatCapacity1_C = instance.getVariableByName("HeatCapacity1.C").asRealVariable()
            Assertions.assertEquals(0.1, heatCapacity1_C.start!!)
            LOG.debug("heatCapacity1_C=${heatCapacity1_C.read().value}")

            val temperatureRoom = instance.getVariableByName("Temperature_Room").asRealVariable()

            var first1 = java.lang.Double.NaN

            val dt = 1.0 / 100
            for (i in 0..4) {
                instance.doStep(dt)
                Assertions.assertTrue(instance.lastStatus === FmiStatus.OK)

                val read = temperatureRoom.read()
                Assertions.assertTrue(read.status == FmiStatus.OK)
                val value = read.value

                if (java.lang.Double.isNaN(first1)) {
                    first1 = value
                }
                LOG.info("Temperature_Room=$value")
            }

            Assertions.assertTrue((instance as AbstractFmuInstance<*, *>).reset(false))

            val first = AtomicBoolean(true)
            while (instance.currentTime < 5) {
                instance.doStep(dt)
                Assertions.assertTrue(instance.lastStatus === FmiStatus.OK)

                val read = temperatureRoom.read()
                Assertions.assertTrue(read.status == FmiStatus.OK)
                val value = read.value

                if (first.getAndSet(false)) {
                    Assertions.assertEquals(first1, value)
                }
                LOG.info("Temperature_Room=$value")
            }

            fmu.asCoSimulationFmu().newInstance().use { fmu2 ->
                fmu2.init()
                LOG.info("Temperature_Room=${fmu2.variableAccessor.readReal(temperatureRoom.valueReference)}")
            }

        }

    }
}
