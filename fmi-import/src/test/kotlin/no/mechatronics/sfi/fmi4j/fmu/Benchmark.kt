package no.mechatronics.sfi.fmi4j.fmu


import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable
import org.javafmi.wrapper.Simulation
import java.io.File
import java.time.Duration
import java.time.Instant

data class TestOptions(
        val fmuPath: String,
        val stepSize: Double,
        val stopTime: Double,
        val vr: Int
)


private val options = listOf(
        TestOptions(
            fmuPath = "test/fmi2/cs/win64/FMUSDK/2.0.4/BouncingBall/bouncingBall.fmu",
            stepSize = 1E-3,
            stopTime = 100.0,
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

val OPTION = options[2]

fun main(args: Array<String>) {

    runJavaFMI()
    runFmi4j()

}


fun runFmi4j() {

    Fmu.from(File(OPTION.fmuPath)).use { fmu ->

        val iter = 0
        for (i in 0 .. iter) {

            fmu.asCoSimulationFmu().newInstance(loggingOn = false).use { instance ->

                val h = instance.modelVariables.getByValueReference(OPTION.vr)[0] as RealVariable
                instance.init(0.0, OPTION.stopTime)

                val start = Instant.now()
                var sum = 0.0
                var j = 0
                while (instance.currentTime < OPTION.stopTime - OPTION.stepSize) {
                    !instance.doStep(OPTION.stepSize)
                    sum += h.read().value
                    j+=1
                }

                val end = Instant.now()

                if (i == iter) {
                    println("FMI4j: ${Duration.between(start, end).toMillis()}ms")
                    println("sum=$sum, iter=$j")
                }

            }
           // System.gc()
        }

    }

    println("")

}

fun runJavaFMI() {

    val iter = 0
    for (i in 0 .. iter) {

        Simulation(OPTION.fmuPath).apply {

            val h = read(modelDescription.getModelVariable(OPTION.vr).name)
            init(0.0, OPTION.stopTime)

            val start = Instant.now()
            var sum = 0.0
            while (currentTime < OPTION.stopTime) {
                doStep(OPTION.stepSize)
                sum += h.asDouble()
            }

            val end = Instant.now()

            if (i == iter) {
                println("JavaFMI: ${Duration.between(start, end).toMillis()}ms")
                println("sum=$sum")
            }

           try {
               terminate()
           } catch (ex: Error) {

           }
           // System.gc()
        }

    }

}