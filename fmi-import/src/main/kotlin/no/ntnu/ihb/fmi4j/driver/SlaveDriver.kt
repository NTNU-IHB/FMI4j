/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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

package no.ntnu.ihb.fmi4j.driver

import no.ntnu.ihb.fmi4j.common.FmuSlave
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

class SlaveDriver(
        private val slave: FmuSlave,
        private val options: DriverOptions,
        private val fmuName: String = slave.modelDescription.modelName
) {

    fun run() {
        slave.use { slave ->

            val startTime = options.startTime
            val stopTime = options.stopTime
            val stepSize = options.stepSize

            LOG.info("Simulating FMU '$fmuName', with startTime=$startTime, stopTime=$stopTime, stepSize=$stepSize")

            if (!slave.simpleSetup(options.startTime)) {
                throw Failure("Failed to initialize FMU!")
            }

            val recorder = Recorder(slave)
            recorder.record()
            while (slave.simulationTime <= stopTime) {
                if (!slave.doStep(stepSize)) {
                    throw Failure("doStep failed, simulation terminated prematurely!")
                }
                recorder.record()
            }

            recorder.writeData()
        }
    }

    private inner class Recorder(
            private val slave: FmuSlave
    ) {

        private var last = -1.0
        private val maxFrequency = 1e-3

        private val sb = StringBuilder()

        private val outputVariables = options.outputVariables.map {
            slave.modelVariables.getByName(it)
        }

        init {
            appendData("\"Time\"", *options.outputVariables.map { "\"$it\"" }.toTypedArray())
        }

        fun appendData(vararg data: Any?) {
            sb.append(data.joinToString(",")).append("\n")
        }

        fun record() {
            val t = slave.simulationTime
            if (last == -1.0 || (t - last) > maxFrequency) {
                appendData(String.format(Locale.US, "%.3f", slave.simulationTime), *outputVariables.map {
                    when (val value = it.read(slave).value) {
                        is Double -> String.format(Locale.US, "%.5f", value)
                        else -> value
                    }
                }.toTypedArray())
                last = t
            }
        }

        fun writeData() {
            options.outputFolder?.also { outputFolder ->
                File(outputFolder).apply {
                    if (!exists()) {
                        mkdirs()
                    }
                }

                val data = sb.toString().trim()
                data.toByteArray().size.also { size ->
                    if (options.failOnLargeSize && (size > 1e6)) {
                        throw Rejection("Generated CSV larger than 1MB. Was: ${size / 1e6}MB!")
                    }
                }

                File(outputFolder, "${fmuName}_out.csv").apply {
                    writeText(data)
                    LOG.info("Wrote results to file $absoluteFile..")
                }
            }
        }

    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(SlaveDriver::class.java)
    }

}
