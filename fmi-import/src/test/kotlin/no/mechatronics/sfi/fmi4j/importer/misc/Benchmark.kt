package no.mechatronics.sfi.fmi4j.importer.misc


import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.importer.Fmu
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable
import org.javafmi.wrapper.Simulation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.measureTimeMillis


data class TestOptions(
        val fmuPath: String,
        val stepSize: Double,
        val stopTime: Double,
        val vr: Int
) {

    val fmuName = File(fmuPath).nameWithoutExtension

}

private val options = listOf(
        TestOptions(
                fmuPath = "${TestUtils.getTEST_FMUs()}/FMI_2.0/CoSimulation/win64/FMUSDK/2.0.4/BouncingBall/bouncingBall.fmu",
                stepSize = 1E-2,
                stopTime = 100.0,
                vr = 0),
        TestOptions(
                fmuPath = "${TestUtils.getTEST_FMUs()}/FMI_2.0/CoSimulation/${TestUtils.getOs()}/20sim/4.6.4.8004/TorsionBar/TorsionBar.fmu",
                stepSize = 1E-5,
                stopTime = 12.0,
                vr = 2),
        TestOptions(
                fmuPath = "${TestUtils.getTEST_FMUs()}/FMI_2.0/CoSimulation/${TestUtils.getOs()}/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu",
                stepSize = 1E-4,
                stopTime = 10.0,
                vr = 46))

object Benchmark {

    private val LOG: Logger = LoggerFactory.getLogger(Benchmark::class.java)

    @JvmStatic
    fun main(args: Array<String>) {

        for (option in intArrayOf(2).map { options[it] }) {

            LOG.info("Running FMU '${option.fmuName}'")

            runJavaFMI(option)
            // System.gc()
            runFmi4j(option)
            //System.gc()
            println()

        }

    }

    fun runFmi4j(option: TestOptions) {

        Fmu.from(File(option.fmuPath)).use { fmu ->

            val iter = 1
            var total = 0.0
            for (i in 0..iter) {

                fmu.asCoSimulationFmu().newInstance(loggingOn = false).use { instance ->

                    val h = instance.modelVariables.getByValueReference(option.vr)[0] as RealVariable
                    instance.init(0.0, option.stopTime)


                    var j = 0
                    var sum = 0.0
                    measureTimeMillis {
                        while (instance.currentTime < option.stopTime - option.stepSize) {
                            !instance.doStep(option.stepSize)
                            sum += h.read().value
                            j += 1
                        }
                    }.also { total = it.toDouble() }

                    if (i == iter) {
                        println("FMI4j: ${total}ms")
                        println("sum=$sum, iter=$j")
                    }

                }

            }

        }

    }

    fun runJavaFMI(option: TestOptions) {

        val iter = 1
        var total = 0.0
        for (i in 0..iter) {

            Simulation(option.fmuPath).apply {

                val h = read(modelDescription.getModelVariable(option.vr).name)
                init(0.0, option.stopTime)

                var j = 0
                var sum = 0.0
                measureTimeMillis {
                    while (currentTime < option.stopTime - option.stepSize) {
                        doStep(option.stepSize)
                        sum += h.asDouble()
                        j++
                    }
                }.also { total = it.toDouble() }

                if (i == iter) {
                    println("JavaFMI: ${total}ms")
                    println("sum=$sum, iter=$j")
                }

                try {
                    terminate()
                } catch (ex: Error) {

                }

            }

        }

    }

}