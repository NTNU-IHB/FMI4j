package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.importer.TestFMUs
import no.ntnu.ihb.fmi4j.importer.Fmu
import no.ntnu.ihb.fmi4j.modeldescription.variables.RealVariable
import org.javafmi.wrapper.Simulation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.measureTimeMillis

data class TestOptions(
        val fmuFile: File,
        val stepSize: Double,
        val stopTime: Double,
        val vr: Long
)

private val options = listOf(
        TestOptions(
                fmuFile = TestFMUs.fmi20().cs()
                        .vendor("FMUSDK").version("2.0.4")
                        .name("bouncingBall").file(),
                stepSize = 1E-2,
                stopTime = 100.0,
                vr = 0),
        TestOptions(
                fmuFile = TestFMUs.fmi20().cs()
                        .vendor("20sim").version("4.6.4.8004")
                        .name("TorsionBar").file(),
                stepSize = 1E-5,
                stopTime = 12.0,
                vr = 2),
        TestOptions(
                fmuFile = TestFMUs.fmi20().cs()
                        .vendor("20sim").version("4.6.4.8004")
                        .name("ControlledTemperature").file(),
                stepSize = 1E-4,
                stopTime = 10.0,
                vr = 46))

object Benchmark {

    private val LOG: Logger = LoggerFactory.getLogger(Benchmark::class.java)

    @JvmStatic
    fun main(args: Array<String>) {

        for (option in intArrayOf(1).map { options[it] }) {

            LOG.info("Running FMU '${option.fmuFile}'")

            runJavaFMI(option)
            // System.gc()
            runFmi4j(option)
            //System.gc()
            println()

        }

    }

    fun runFmi4j(option: TestOptions) {

        Fmu.from(option.fmuFile).use { fmu ->

            val iter = 1
            var elapsed: Long
            for (i in 0..iter) {

                fmu.asCoSimulationFmu().newInstance(loggingOn = false).use { slave ->

                    val h = slave.modelVariables.getByValueReference(option.vr)[0] as RealVariable
                    slave.simpleSetup(0.0, option.stopTime)

                    var j = 0
                    var sum = 0.0
                    measureTimeMillis {
                        while (slave.simulationTime < option.stopTime - option.stepSize) {
                            !slave.doStep(option.stepSize)
                            sum += h.read(slave).value
                            j += 1
                        }
                    }.also { elapsed = it }

                    if (i == iter) {
                        println("FMI4j: ${elapsed}ms")
                        println("sum=$sum, iter=$j")
                    }

                }

            }

        }

    }

    fun runJavaFMI(option: TestOptions) {

        val iter = 1
        var elapsed: Long
        for (i in 0..iter) {

            Simulation(option.fmuFile.absolutePath).apply {

                val h = read(modelDescription.getModelVariable(option.vr.toInt()).name)
                init(0.0, option.stopTime)

                var j = 0
                var sum = 0.0
                measureTimeMillis {
                    while (currentTime < option.stopTime - option.stepSize) {
                        doStep(option.stepSize)
                        sum += h.asDouble()
                        j++
                    }
                }.also { elapsed = it }

                if (i == iter) {
                    println("JavaFMI: ${elapsed}ms")
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