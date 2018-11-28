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
import no.mechatronics.sfi.fmi4j.solvers.Solver
import no.sfi.mechatronics.fmi4j.me.ApacheSolvers
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import java.lang.RuntimeException
import java.text.DecimalFormat
import java.text.NumberFormat


/**
 * @author Lars Ivar Hatledal
 */
class FmuDriver(
        private val fmuPath: File,
        private val outputVariables: Array<String>,
        private var outputFolder: String? = null
) {

    private val LOG: Logger = LoggerFactory.getLogger(FmuDriver::class.java)

    var startTime: Double = 0.0
    var stopTime: Double = 10.0
    var stepSize: Double = 1E-3

    var modelExchange: Boolean = false
    var solver: Solver = ApacheSolvers.rk4(1E-3)

    fun run(): Boolean {

        Fmu.from(fmuPath).let {
            if (modelExchange) {
                it.asModelExchangeFmu().newInstance( solver )
            } else {
                it.asCoSimulationFmu().newInstance()
            }
        }.use { slave ->

            slave.simpleSetup(startTime, stopTime)

            val sb = StringBuilder()
            val printer = CSVPrinter(sb, CSVFormat.DEFAULT.withQuote('"').withHeader("Time", *outputVariables))

            val outputVariables = outputVariables.map {
                slave.modelVariables.getByName(it)
            }

            LOG.info("Simulating FMU '$fmuPath', with startTime=$startTime, stopTime=$stopTime, stepSize=$stepSize")

            fun record() {
                printer.printRecord(slave.simulationTime, *outputVariables.map { it.read(slave).value }.toTypedArray())
            }

            var success = true
            while (slave.simulationTime <= (stopTime - stepSize) && success) {
                record()
                if (!slave.doStep(stepSize)) {
                    success = false
                    LOG.warn("doStep returned false, breaking..")
                }
            }

            if (success && outputFolder != null) {

               File(outputFolder).apply {
                   if (!exists()) {
                       mkdirs()
                   }
               }

               File(outputFolder, "${fmuPath.nameWithoutExtension}_out.csv").apply {
                   writeText(sb.toString())
                   LOG.info("Wrote results to file $absoluteFile")
               }
            }

            return success

        }

    }

}
