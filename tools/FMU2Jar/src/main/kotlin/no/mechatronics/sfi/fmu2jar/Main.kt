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

package no.mechatronics.sfi.fmu2jar

import picocli.CommandLine
import java.io.File

/**
 * @author Lars Ivar Hatledal
 */
class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            CommandLine.run(Args(), System.out, *args)
        }

    }

}

class GenerateOptions(
        val mavenLocal: Boolean,
        var outputFolder: File?
) {
    init {
        if (outputFolder == null && !mavenLocal) {
            outputFolder = File(".").absoluteFile
        }
    }
}

@CommandLine.Command(name = "fmu2jar")
class Args : Runnable {

    @CommandLine.Option(names = ["-h", "--help"], description = ["Print this message and quits."], usageHelp = true)
    private var showHelp = false

    @CommandLine.Option(names = ["-fmu", "--fmuPath"], description = ["Path to the FMU."], required = true)
    private lateinit var fmuPath: File

    @CommandLine.Option(names = ["-mvn", "--maven"], description = ["Should the .jar be published to maven local?"], required = false)
    private var mvn: Boolean = false

    @CommandLine.Option(names = ["-out", "--output"], description = ["Specify where to copy the generated .jar. Not needed if [-mvn --maven] has been specified."], required = false)
    private var outputFolder: File? = null

    override fun run() {

        fmuPath.apply {
            if (!(exists() && name.endsWith(".fmu", true))) {
                error("Not a valid file: $absolutePath")
            }
        }

        try {

            Fmu2Jar(fmuPath.absoluteFile).apply {
                generateJar(GenerateOptions(
                        mavenLocal = mvn,
                        outputFolder = outputFolder?.absoluteFile
                ))
            }

        } catch (ex: Exception) {
            ex.printStackTrace(System.err)
            error("Application error..")
        }

    }
}



