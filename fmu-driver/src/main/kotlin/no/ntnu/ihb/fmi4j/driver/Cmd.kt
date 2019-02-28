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

import picocli.CommandLine
import java.io.File

/**
 * @author Lars Ivar Hatledal
 */
internal object Cmd {

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
