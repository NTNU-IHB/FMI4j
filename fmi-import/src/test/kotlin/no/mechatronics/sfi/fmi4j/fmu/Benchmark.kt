package no.mechatronics.sfi.fmi4j.fmu


import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable
import org.javafmi.wrapper.Simulation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Duration
import java.time.Instant


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
            fmuPath = "test/fmi2/cs/win64/FMUSDK/2.0.4/BouncingBall/bouncingBall.fmu",
            stepSize = 1E-3,
                stopTime = 50.0,
            vr = 0),
        TestOptions(
                fmuPath = "C:\\Users\\laht\\Local Documents\\Vico\\Extra\\FMUs\\20161108\\HydraulicCylinderComplex.fmu",
                stepSize = 1E-4,
                stopTime = 20.0,
                vr = 155),
        TestOptions(
                fmuPath = "C:\\Users\\laht\\IdeaProjects\\FMI4j\\test\\fmi2\\cs\\win64\\20sim\\4.6.4.8004\\TorsionBar\\TorsionBar.fmu",
                stepSize = 1E-5,
                stopTime = 12.0,
                vr = 2))

object Benchmark {

    private val LOG: Logger = LoggerFactory.getLogger(Benchmark::class.java)

    @JvmStatic
    fun main(args: Array<String>) {

        for (option in intArrayOf(0).map { options[it] }) {

            LOG.info("Running FMU '${option.fmuName}'")

            runJavaFMI(option)
            System.gc()
            runFmi4j(option)

            println()

        }

    }


    fun runFmi4j(option: TestOptions) {

        Fmu.from(File(option.fmuPath)).use { fmu ->

            val iter = 3
            for (i in 0..iter) {

                fmu.asCoSimulationFmu().newInstance(loggingOn = false).use { instance ->

                    val h = instance.modelVariables.getByValueReference(option.vr)[0] as RealVariable
                    instance.init(0.0, option.stopTime)

                    val start = Instant.now()
                    var sum = 0.0
                    var j = 0
                    while (instance.currentTime < option.stopTime - option.stepSize) {
                        !instance.doStep(option.stepSize)
                        sum += h.read().value
                        j += 1
                    }

                    val end = Instant.now()

                    if (i == iter) {
                        println("FMI4j: ${Duration.between(start, end).toMillis()}ms")
                        println("sum=$sum, iter=$j")
                    }

                }
            }

        }


    }

    fun runJavaFMI(option: TestOptions) {

        val iter = 3
        for (i in 0..iter) {

            Simulation(option.fmuPath).apply {

                val h = read(modelDescription.getModelVariable(option.vr).name)
                init(0.0, option.stopTime)

                val start = Instant.now()
                var sum = 0.0
                var j = 0
                while (currentTime < option.stopTime - option.stepSize) {
                    doStep(option.stepSize)
                    sum += h.asDouble()
                    j++
                }

                val end = Instant.now()

                if (i == iter) {
                    println("JavaFMI: ${Duration.between(start, end).toMillis()}ms")
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