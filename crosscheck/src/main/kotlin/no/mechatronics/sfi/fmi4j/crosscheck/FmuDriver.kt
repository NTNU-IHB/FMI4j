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

import no.mechatronics.sfi.fmi4j.fmu.Fmu
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File


object FmuDriver {

    private val LOG: Logger = LoggerFactory.getLogger(FmuDriver::class.java)


    @CommandLine.Command(name = "fmudriver")
    class Args : Runnable {

        @CommandLine.Option(names = ["-h", "--help"], description = ["Print this message and quits."], usageHelp = true)
        private var showHelp = false

        @CommandLine.Option(names = ["-fmu", "--fmuPath"], description = ["Path to the FMU."], required = true)
        private lateinit var fmuPath: File

        @CommandLine.Option(names = ["-out", "--outputFolder"], description = ["Folder to store .csv result."], required = false)
        private var outputFolder: String? = null

        @CommandLine.Option(names = ["-dt", "--stepSize"], description = ["Step-size."], required = false)
        private var dt: Double = 1E-3

        @CommandLine.Option(names = ["-start", "--startTime"], description = ["Start time."], required = false)
        private var startTime: Double = 0.0

        @CommandLine.Option(names = ["-stop", "--stopTime"], description = ["Stop time."], required = false)
        private var stopTime: Double = 10.0

        @CommandLine.Option(names = ["-me"], description = ["Stop time."], required = false)
        private var modelExchange: Boolean = false

        @CommandLine.Option(names = ["-vars", "--variables"], description = ["Variables to print"], split = ", ")
        private var outputVariables: Array<String> = arrayOf()

        override fun run() {

            Fmu.from(fmuPath).let {
                if (modelExchange) it.asModelExchangeFmu().newInstance(EulerIntegrator(1E-3)) else it.asCoSimulationFmu().newInstance()
            }.use { slave ->

                slave.init(startTime, stopTime)

                val sb = StringBuilder()
                val format = CSVFormat.DEFAULT.withHeader("Time", *outputVariables)
                val printer = CSVPrinter(sb, format)

                val outputVariables = outputVariables.map {
                    slave.modelVariables.getByName(it)
                }

                while (slave.currentTime <= stopTime) {
                    printer.printRecord(slave.currentTime, *outputVariables.map { it.read().value }.toTypedArray())
                    if (!slave.doStep(dt)) {
                        break
                    }
                }

                File(outputFolder, "${slave.modelName}_out.csv").apply {
                    writeText(sb.toString())
                    LOG.info("Wrote results to file $absoluteFile")
                }

            }

        }

    }

    @JvmStatic
    fun main(args: Array<String>) {
        CommandLine.run(Args(), System.out, *args)
    }


}