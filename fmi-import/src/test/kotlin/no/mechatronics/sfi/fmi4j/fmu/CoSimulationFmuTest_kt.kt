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

class CoSimulationFmuTest_kt {

    companion object {

        val LOG: Logger = LoggerFactory.getLogger(CoSimulationFmuTest_kt::class.java)

        private lateinit var fmuFile: FmuFile

        @JvmStatic
        @BeforeClass
        fun setUp() {

            val path = "../test/fmi2/cs/win64/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"
            val file = File(path)
            Assert.assertNotNull(file)
            fmuFile = FmuFile.from(file)

        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            fmuFile.close()
        }

    }

    @Test
    @Throws(Exception::class)
    fun test() {

        fmuFile.asCoSimulationFmu().newInstance(loggingOn = true).use { fmu ->

            Assert.assertEquals("2.0", fmu.modelDescription.fmiVersion)

            val startTemp = fmu.getVariableByName("HeatCapacity1.T0").asRealVariable().start
            Assert.assertNotNull(startTemp)
            Assert.assertEquals(298.0, startTemp!!, 0.0)

            Assert.assertTrue(fmu.init() == FmiStatus.OK)
            Assert.assertTrue(fmu.lastStatus === FmiStatus.OK)

            val heatCapacity1_C = fmu.getVariableByName("HeatCapacity1.C").asRealVariable()
            Assert.assertEquals(0.1, heatCapacity1_C.start!!, 0.0)
            println(heatCapacity1_C.read().value)

            val temperatureRoom = fmu.getVariableByName("Temperature_Room").asRealVariable()

            var first1 = java.lang.Double.NaN

            val dt = 1.0 / 100
            for (i in 0..4) {
                fmu.doStep(dt)
                Assert.assertTrue(fmu.lastStatus === FmiStatus.OK)

                val read = temperatureRoom.read()
                Assert.assertTrue(read.status == FmiStatus.OK)
                val value = read.value

                if (java.lang.Double.isNaN(first1)) {
                    first1 = value
                }
                LOG.info("Temperature_Room=$value")

            }

            (fmu as AbstractFmu<*, *>).reset(false)

            Assert.assertTrue(fmu.lastStatus === FmiStatus.OK)

            val first = AtomicBoolean(true)
            while (fmu.currentTime < 5) {
                fmu.doStep(dt)
                Assert.assertTrue(fmu.lastStatus === FmiStatus.OK)

                val read = temperatureRoom.read()
                Assert.assertTrue(read.status == FmiStatus.OK)
                val value = read.value

                if (first.getAndSet(false)) {
                    Assert.assertEquals(first1, value, 0.0)
                }
                LOG.info("Temperature_Room=$value")

            }

            fmuFile.asCoSimulationFmu().newInstance().use { fmu2 ->
                Assert.assertTrue(fmu2.init() == FmiStatus.OK)
                LOG.info("Temperature_Room=${fmu2.variableAccessor.readReal(temperatureRoom.valueReference)}")
            }

        }

    }
}
