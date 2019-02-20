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

package no.ntnu.ihb.fmi4j.fmudriver

import no.ntnu.ihb.fmi4j.common.FmuSlave
import no.ntnu.ihb.fmi4j.importer.Fmu
import no.ntnu.ihb.fmi4j.solvers.Solver
import no.ntnu.ihb.fmi4j.solvers.apache.ApacheSolvers
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.QuoteMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class Rejection(
        val reason: String
): Exception(reason)

class Failure(
        val reason: String
): Exception(reason)

data class DriverOptions(

        val startTime: Double = 0.0,
        val stopTime: Double = 0.0,
        val stepSize: Double = 1E-3,

        val modelExchange: Boolean = false,
        val solver: Solver = ApacheSolvers.rk4(1E-3),

        val failOnLargeSize: Boolean = false,

        val outputFolder: String? = null,
        val outputVariables: List<String>
)

/**
 * @author Lars Ivar Hatledal
 */
class FmuDriver(
        private val fmuPath: File,
        private val options: DriverOptions
) {

    private val LOG: Logger = LoggerFactory.getLogger(FmuDriver::class.java)

    fun run() {

        Fmu.from(fmuPath).also { fmu ->
            if (options.modelExchange) {

                if (options.modelExchange && !fmu.supportsModelExchange) {
                    throw Failure("FMU does not support Model Exchange!")
                }

                simulate(fmu.asModelExchangeFmu().newInstance( options.solver ))
            } else {

                if (!options.modelExchange && !fmu.supportsCoSimulation) {
                    throw Failure("FMU does not support Co-simulation!")
                }

                simulate(fmu.asCoSimulationFmu().newInstance())
            }
        }

    }

    @Suppress("NAME_SHADOWING")
    private fun simulate(slave: FmuSlave) {

        slave.use { slave ->

            val startTime = options.startTime
            val stopTime = options.stopTime
            val stepSize = options.stepSize

            slave.simpleSetup(options.startTime)

            val sb = StringBuilder()
            val header = arrayOf("\"Time\"", *options.outputVariables.map { '"' + it + '"' }.toTypedArray())
            val csvFormat = CSVFormat.DEFAULT
                    .withHeader(*header)
                    .withEscape('\\')
                    .withQuoteMode(QuoteMode.NONE)
            val printer = CSVPrinter(sb, csvFormat)

            val outputVariables = options.outputVariables.map {
                slave.modelVariables.getByName(it)
            }

            LOG.info("Simulating FMU '$fmuPath', with startTime=$startTime, stopTime=$stopTime, stepSize=$stepSize")

            fun record() {
                printer.printRecord(slave.simulationTime, *outputVariables.map { it.read(slave).value }.toTypedArray())
            }

            record()
            while (slave.simulationTime <= stopTime) {
                if (!slave.doStep(stepSize)) {
                    throw Failure("Simulation terminated prematurely!")
                }
                record()
            }

            if (options.outputFolder != null) {

                File(options.outputFolder).apply {
                    if (!exists()) {
                        mkdirs()
                    }
                }

                val data = sb.toString()
                data.toByteArray().size.also { size ->
                    if (options.failOnLargeSize && (size > 1e6)) {
                        throw Rejection("Generated CSV larger than 1MB. Was: ${size/1e6}MB!")
                    }
                }

                File(options.outputFolder, "${fmuPath.nameWithoutExtension}_out.csv").apply {
                    writeText(data)
                    LOG.info("Wrote results to file $absoluteFile..")
                }
            }

        }

    }


}
