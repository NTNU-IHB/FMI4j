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

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import java.io.File

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


class Main {

  companion object {

      private const val HELP = "help"
      private const val FMU_FILE = "fmu"
      private const val OUTPUT_FOLDER = "out"
      private const val MAVEN_LOCAL_OPT = "mavenLocal"

      @JvmStatic
      fun main(args: Array<String>) {

          val options = Options().apply {
              addOption(HELP, false, "Prints this message and quits")
              addOption(FMU_FILE, true, "Path to the FMU")
              addOption(MAVEN_LOCAL_OPT, false, "Should the .jar be published to maven local? (optional)")
              addOption(OUTPUT_FOLDER, true, "Specify where to copy the generated .jar. Not needed if '-$MAVEN_LOCAL_OPT true'")
          }

          val cmd = DefaultParser().parse(options, args)
          if (args.isEmpty() || cmd.hasOption(HELP)) {
              HelpFormatter().printHelp("fmu2jar", options)
          }

          try {

              cmd.apply {

                  getOptionValue(FMU_FILE)?.also { path ->
                      val file = File(path.replace("\\", "/")).absoluteFile
                      if (!(file.exists() && file.name.endsWith(".fmu", true))) {
                          error("Not a valid file: ${file.absolutePath}")
                      }

                      var outputFolder: File? = getOptionValue(OUTPUT_FOLDER)?.let {
                          File(it.replace("\\", "/")).absoluteFile
                      }

                      Fmu2Jar(file).apply {
                          generateJar(GenerateOptions(
                                  mavenLocal = hasOption(MAVEN_LOCAL_OPT),
                                  outputFolder = outputFolder))
                      }

                  }

              }

          } catch(ex: Exception) {
              ex.printStackTrace(System.err)
              error("Application error..")
          }

      }

  }

}
