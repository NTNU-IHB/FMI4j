package no.ntnu.ihb.fmi4j.fmudriver

import picocli.CommandLine
import java.io.File

/**
 * @author Lars Ivar Hatledal
 */
object Cmd {

    @CommandLine.Command(name = "fmudriver")
    class Args : Runnable {

        @CommandLine.Option(names = ["-h", "--help"], description = ["Print this message and quits."], usageHelp = true)
        var showHelp = false

        @CommandLine.Option(names = ["-f", "--fmu"], description = ["Path to the FMU."], required = true)
        lateinit var fmuPath: String

        @CommandLine.Option(names = ["-out", "--outputFolder"], description = ["Folder to store xc result."], required = false)
        var outputFolder: String = "."

        @CommandLine.Option(names = ["-dt", "--stepSize"], description = ["Step-size."], required = false)
        var stepSize: Double = 1E-3

        @CommandLine.Option(names = ["-start", "--startTime"], description = ["Start time."], required = false)
        var startTime: Double = 0.0

        @CommandLine.Option(names = ["-stop", "--stopTime"], description = ["Stop time."], required = false)
        var stopTime: Double = 10.0

        @CommandLine.Option(names = ["-reltol", "--relativeTolerance"], description = ["Relative tolerance."], required = false)
        var relTol: Double = 0.0

        @CommandLine.Option(names = ["-me"], description = ["Treat FMU as a Model Exchange."], required = false)
        var modelExchange: Boolean = false

        @CommandLine.Parameters(arity = "1..*", paramLabel = "variables", description = ["Variables to print."])
        lateinit var outputVariables: List<String>

        override fun run() {

            val options = DriverOptions(
                    startTime = this@Args.startTime,
                    stopTime = this@Args.stopTime,
                    stepSize = this@Args.stepSize,

                    outputVariables = outputVariables,
                    outputFolder = outputFolder
            )

            FmuDriver(File(fmuPath.removeSurrounding("\"")), options).run()
        }

    }

    @JvmStatic
    fun main(args: Array<String>) {
        CommandLine.run(Args(), System.out, *args)
    }

}





