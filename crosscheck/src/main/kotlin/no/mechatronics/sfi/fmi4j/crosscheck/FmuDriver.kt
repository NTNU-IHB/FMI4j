package no.mechatronics.sfi.fmi4j.crosscheck

import no.mechatronics.sfi.fmi4j.FmiSimulation
import no.mechatronics.sfi.fmi4j.fmu.FmuFile
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.io.FileUtils
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.Charset

const val STEP_SIZE = "stepSize"
const val START_TIME = "startTime"
const val STOP_TIME = "stopTime"
const val OUTPUT_VARIABLES = "outputs"
const val FMU = "fmu"
const val HELP = "help"
const val OUT_DIR = "outputDirectory"
const val CS = "cs"
const val ME = "me"

class SimulationOptions(
        private val cmd: CommandLine
) {

    val me: Boolean = cmd.hasOption(ME)
    val fmu: FmiSimulation = FmuFile.from(File(cmd.getOptionValue(FMU))).let {
        if (cmd.hasOption(ME)) {
            it.asModelExchangeFmu().newInstance(EulerIntegrator(1E-3))
        } else {
            it.asCoSimulationFmu().newInstance()
        }
    }
    val stepSize: Double = cmd.getOptionValue(STEP_SIZE)?.toDouble() ?: 1E-3
    val startTime: Double = cmd.getOptionValue(START_TIME)?.toDouble() ?: 0.0
    val stopTime: Double = cmd.getOptionValue(STOP_TIME)?.toDouble() ?: 10.0
    val outputVariables: List<String> = cmd.getOptionValue(OUTPUT_VARIABLES)?.split(" ") ?: emptyList()
    val outputDirectory: String? = cmd.getOptionValue(OUT_DIR)


}

object FmuDriver {

    private val LOG: Logger = LoggerFactory.getLogger(FmuDriver::class.java)

    @JvmStatic
    fun main(args: Array<String>) {

        Options().apply {

            addOption(HELP, false, "Prints this message")
            addOption(FMU, true, "path to the FMU")
            addOption(STEP_SIZE, true, "The stepsize")
            addOption(STOP_TIME, true, "The stoptime")
            addOption(OUTPUT_VARIABLES, true, "The variables to print")
            addOption(OUT_DIR, true, "output folder")
            addOption(ME, false, "Model Exchange?")

        }.also {
            val simOptions = DefaultParser().parse(it, args).let (::SimulationOptions)
            val result = simulate(simOptions)
            simOptions.outputDirectory?.also { parent ->
                val file = File(parent,"${simOptions.fmu.modelDescription.modelName}_out.csv")
                FileUtils.write(file, result, Charset.forName("UTF-8"))
                LOG.info("Wrote results to file ${file.absolutePath}")
            }

        }

    }


    private fun simulate(options: SimulationOptions): String {

        val sb = StringBuilder()
        options.fmu.use { fmu ->
            if (fmu.init(options.startTime, options.stopTime)) {

                val format = CSVFormat.DEFAULT.withHeader("Time", *options.outputVariables.toTypedArray())
                val printer = CSVPrinter(sb, format)

                val outputVariables = options.outputVariables.map {
                    fmu.modelVariables.getByName(it)
                }

                val dt = options.stepSize
                while (fmu.currentTime < options.stopTime) {
                    printer.printRecord(fmu.currentTime, *outputVariables.map { it.read().value }.toTypedArray())
                    fmu.doStep(dt)
                }

            }
        }

        return sb.toString()

    }


}