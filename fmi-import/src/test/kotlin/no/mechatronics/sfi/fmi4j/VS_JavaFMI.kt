package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.fmu.FmuBuilder
import org.javafmi.wrapper.Simulation
import org.junit.Test
import java.io.File

class VS_JavaFMI {

    @Test
    fun test1() {

        val file = File(javaClass.classLoader.getResource("v2/cs/ControlledTemperature/ControlledTemperature.fmu").file)
        val fmu1 = FmuBuilder(file).asCoSimulationFmu().newInstance().also {
            it.init(0.0)
        }
        val var1 = fmu1.modelVariables.getByName("HeatCapacity1.T0").asRealVariable()

        val fmu2 =  Simulation(file.absolutePath).also {
            it.init(0.0)
        }
        val var2 = fmu2.read("HeatCapacity1.T0")

        val dt = 1.0/ 1000
        val stop1 = 10.0
        val stop2 = stop1 + 1000.0
        while (fmu1.currentTime < stop1) {
            fmu1.doStep(dt)
        }
        while (fmu2.currentTime < stop1) {
            fmu2.doStep(dt)
        }

        var t0 = System.currentTimeMillis()
        var t1_end: Long
        while (fmu1.currentTime < stop2) {
            fmu1.doStep(dt)
            val value = var1.value
        }
        t1_end = System.currentTimeMillis() - t0


        t0 = System.currentTimeMillis()
        var t2_end: Long
        while (fmu2.currentTime < stop2) {
            fmu2.doStep(dt)
            val value = var2.asDouble()
        }

        t2_end = System.currentTimeMillis() - t0

        println("$t1_end : $t2_end")

        fmu2.terminate()
        fmu1.terminate()


    }

}