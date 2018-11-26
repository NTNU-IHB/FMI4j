/*
 * The MIT License
 *
 * Copyright 2017-2018 Norwegian University of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package no.mechatronics.sfi.fmi4j.crosscheck

import no.mechatronics.sfi.fmi4j.importer.Fmu
import no.sfi.mechatronics.fmi4j.me.ApacheSolvers
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * @author Lars Ivar Hatledal
 */
object FmuDriver {

    private const val FMI4j_VERSION = "0.12.2"
    private val LOG: Logger = LoggerFactory.getLogger(FmuDriver::class.java)

    @CommandLine.Command(name = "fmudriver")
    class Args : Runnable {

        @CommandLine.Option(names = ["-h", "--help"], description = ["Print this message and quits."], usageHelp = true)
        private var showHelp = false

        @CommandLine.Option(names = ["-f", "--fmu"], description = ["Path to the FMU."], required = true)
        private lateinit var fmuPath: File

        @CommandLine.Option(names = ["-out", "--outputFolder"], description = ["Folder to store xc result."], required = false)
        private var outputFolder: String = ""

        @CommandLine.Option(names = ["-dt", "--stepSize"], description = ["Step-size."], required = false)
        private var stepSize: Double = 1E-3

        @CommandLine.Option(names = ["-start", "--startTime"], description = ["Start time."], required = false)
        private var startTime: Double = 0.0

        @CommandLine.Option(names = ["-stop", "--stopTime"], description = ["Stop time."], required = false)
        private var stopTime: Double = 10.0

        @CommandLine.Option(names = ["-reltol", "--relativeTolerance"], description = ["Relative tolerance."], required = false)
        private var relTol: Double = 0.0

        @CommandLine.Option(names = ["-me"], description = ["Stop time."], required = false)
        private var modelExchange: Boolean = false

        @CommandLine.Parameters(arity = "1..*", paramLabel = "variables", description = ["Variables to print."])
        private lateinit var outputVariables: Array<String>

        private fun toFixed(len: Int, number: Double): Double {
            return DecimalFormat("#.###").let {
                NumberFormat.getInstance().parse(it.format(number)).toDouble()
            }
        }

        override fun run() {

            Fmu.from(fmuPath).let {
                if (modelExchange) {
                    it.asModelExchangeFmu().newInstance( ApacheSolvers.euler(1E-3))
                } else {
                    it.asCoSimulationFmu().newInstance()
                }
            }.use { slave ->

                slave.setupExperiment(startTime, stopTime, relTol)
                slave.enterInitializationMode()
                slave.exitInitializationMode()

                val sb = StringBuilder()
                val printer = CSVPrinter(sb, CSVFormat.DEFAULT.withHeader("Time", *outputVariables))

                val outputVariables = outputVariables.map {
                    slave.modelVariables.getByName(it)
                }

                LOG.info("Running crosscheck on FMU '${slave.modelDescription.modelName}', with startTime=$startTime, stopTime=$stopTime, stepSize=$stepSize")

                fun record() {
                    printer.printRecord(toFixed(4, slave.simulationTime), *outputVariables.map { it.read(slave).value }.toTypedArray())
                }

                record()
                while (slave.simulationTime <= (stopTime - stepSize)) {
                    if (!slave.doStep(stepSize)) {
                        break
                    }
                    record()
                }

                if (outputFolder.isEmpty()) {
                    outputFolder = getDefaultOutputDir()
                }

                File(outputFolder).apply {
                    if (!exists()) {
                        mkdirs()
                    }
                }

                File(outputFolder, "${slave.modelDescription.modelName}_out.csv").apply {
                    writeText(sb.toString())
                    LOG.info("Wrote results to file $absoluteFile")
                }

                File(outputFolder, "README.md").apply {
                   writeText(getReadme())
                }

                File(outputFolder, "passed").apply {
                    createNewFile()
                }

            }

        }

        private fun getDefaultOutputDir(): String {
            var file = fmuPath.parentFile
            var names = mutableListOf<String>()
            for (i in 0 .. 2) {
                names.add(file.name)
                file = file.parentFile
            }

            names.addAll(listOf(FMI4j_VERSION, "FMI4j"))

            for (i in 0 .. 2) {
                val name = when(file.name) {
                    "CoSimulation" -> "cs"
                    "ModelExchange" -> "me"
                    "FMI_2.0" -> "2.0"
                    else -> file.name
                }
                names.add(name)
                file = file.parentFile
            }
            return names.reverse().let{ names.joinToString("/") }
        }

        private fun getReadme(): String {

            return """

            The cross-check results have been generated with FMI4j's FmuDriver.
            To get more information download the 'fmudriver' tool from https://github.com/SFI-Mechatronics/FMI4j/releases and run:

            ```
            java -jar fmudriver.jar -h
            ```

            """.trimIndent()

        }

    }

    @JvmStatic
    fun main(args: Array<String>) {
        CommandLine.run(Args(), System.out, *args)
    }

}