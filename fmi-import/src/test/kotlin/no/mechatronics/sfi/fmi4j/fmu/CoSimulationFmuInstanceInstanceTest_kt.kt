package no.mechatronics.sfi.fmi4j.fmu


import no.mechatronics.sfi.fmi4j.common.FmiStatus
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class CoSimulationFmuInstanceInstanceTest_kt {

    companion object {

        val LOG: Logger = LoggerFactory.getLogger(CoSimulationFmuInstanceInstanceTest_kt::class.java)

        private lateinit var fmu: Fmu

        @JvmStatic
        @BeforeClass
        fun setUp() {

            val path = "../test/fmi2/cs/win64/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"
            val file = File(path)
            Assert.assertNotNull(file)
            fmu = Fmu.from(file)

        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            fmu.close()
        }

    }

    @Test
    @Throws(Exception::class)
    fun test() {

        fmu.asCoSimulationFmu().newInstance(loggingOn = true).use { instance ->

            Assert.assertEquals("2.0", instance.modelDescription.fmiVersion)

            val startTemp = instance.getVariableByName("HeatCapacity1.T0").asRealVariable().start
            Assert.assertNotNull(startTemp)
            Assert.assertEquals(298.0, startTemp!!, 0.0)

            instance.init()
            Assert.assertTrue(instance.lastStatus === FmiStatus.OK)

            val heatCapacity1_C = instance.getVariableByName("HeatCapacity1.C").asRealVariable()
            Assert.assertEquals(0.1, heatCapacity1_C.start!!, 0.0)
            println(heatCapacity1_C.read().value)

            val temperatureRoom = instance.getVariableByName("Temperature_Room").asRealVariable()

            var first1 = java.lang.Double.NaN

            val dt = 1.0 / 100
            for (i in 0..4) {
                instance.doStep(dt)
                Assert.assertTrue(instance.lastStatus === FmiStatus.OK)

                val read = temperatureRoom.read()
                Assert.assertTrue(read.status == FmiStatus.OK)
                val value = read.value

                if (java.lang.Double.isNaN(first1)) {
                    first1 = value
                }
                LOG.info("Temperature_Room=$value")
            }

            Assert.assertTrue((instance as AbstractFmu<*, *>).reset(false))

            val first = AtomicBoolean(true)
            while (instance.currentTime < 5) {
                instance.doStep(dt)
                Assert.assertTrue(instance.lastStatus === FmiStatus.OK)

                val read = temperatureRoom.read()
                Assert.assertTrue(read.status == FmiStatus.OK)
                val value = read.value

                if (first.getAndSet(false)) {
                    Assert.assertEquals(first1, value, 0.0)
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
