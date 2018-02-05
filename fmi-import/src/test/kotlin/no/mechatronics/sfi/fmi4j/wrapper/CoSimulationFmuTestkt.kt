package no.mechatronics.sfi.fmi4j.wrapper


import no.mechatronics.sfi.fmi4j.FmiSimulation
import no.mechatronics.sfi.fmi4j.fmu.AbstractFmu
import no.mechatronics.sfi.fmi4j.fmu.FmuBuilder
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status
import org.junit.After
import org.junit.Assert
import org.junit.Before
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

class CoSimulationFmuTest_java {

    private var builder: FmuBuilder? = null
    private var fmu: FmiSimulation? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        val url = CoSimulationFmuTest_java::class.java.classLoader
                .getResource("v2/cs/ControlledTemperature/ControlledTemperature.fmu")
        Assert.assertNotNull(url)

        builder = FmuBuilder(url!!)
        fmu = builder!!.asCoSimulationFmu().newInstance()

    }

    @After
    fun tearDown() {
        if (fmu != null) {
            fmu!!.terminate()
            Assert.assertTrue(fmu!!.lastStatus === Fmi2Status.OK)
        }
    }

    @org.junit.Test
    @Throws(Exception::class)
    fun test() {

        Assert.assertEquals("2.0", fmu!!.version)

        val startTemp = fmu!!.modelVariables.getByName("HeatCapacity1.T0").asRealVariable().start
        Assert.assertNotNull(startTemp)
        Assert.assertEquals(298.0, startTemp!!, 0.0)

        Assert.assertTrue(fmu!!.init())
        Assert.assertTrue(fmu!!.lastStatus === Fmi2Status.OK)

        val heatCapacity1_C = fmu!!.modelVariables.getByName("HeatCapacity1.C").asRealVariable()
        Assert.assertEquals(0.1, heatCapacity1_C.start!!, 0.0)
        println(heatCapacity1_C.value)

        val temperature_room = fmu!!.modelVariables.getByName("Temperature_Room").asRealVariable()

        var first1 = java.lang.Double.NaN

        val dt = 1.0 / 100
        for (i in 0..4) {
            fmu!!.doStep(dt)
            Assert.assertTrue(fmu!!.lastStatus === Fmi2Status.OK)
            val value = temperature_room.value
            Assert.assertTrue(fmu!!.lastStatus === Fmi2Status.OK)
            if (java.lang.Double.isNaN(first1)) {
                first1 = value
            }
            println(value)

        }

        (fmu as AbstractFmu<*, *>).reset(false)

        Assert.assertTrue(fmu!!.lastStatus === Fmi2Status.OK)

        val first = AtomicBoolean(true)
        while (fmu!!.currentTime < 5) {
            fmu!!.doStep(dt)
            Assert.assertTrue(fmu!!.lastStatus === Fmi2Status.OK)
            val value = temperature_room.value
            Assert.assertTrue(fmu!!.lastStatus === Fmi2Status.OK)
            if (first.getAndSet(false)) {
                Assert.assertEquals(first1, value, 0.0)
            }
            println(value)

        }

        builder!!.asCoSimulationFmu().newInstance().use { fmu2 ->
            fmu2.init()
            println(fmu2.variableAccessor.getReal(temperature_room.valueReference))
        }

    }
}
