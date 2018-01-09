
package no.mechatronics.sfi.fmu2jar

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import java.io.File


class ApplicationStarter {

  companion object {

      private const val HELP = "help"
      private const val FMU_FILE = "fmu"
      private const val OUTPUT_FOLDER = "out"
      private const val MAVEN_LOCAL_OPT = "mavenLocal"


      @JvmStatic
      fun main(args: Array<String>) {

          val options = Options().apply {
              addOption(HELP, false, "Prints this message")
              addOption(FMU_FILE, true, "Path to the FMU")
              addOption(MAVEN_LOCAL_OPT, false, "Should the .jar be published to maven local?")
              addOption(OUTPUT_FOLDER, true, "Specify where to copy the generated .jar. Not needed if $MAVEN_LOCAL_OPT=true")
          }

          val cmd = DefaultParser().parse(options, args)
          if (args.isEmpty() || cmd.hasOption(HELP)) {
              HelpFormatter().printHelp("fmu2jar", options)
          }

          try {

              var outputFolder: File? = null
              var mavenLocal: Boolean = false
              var fmuFile: File? = null

              cmd.apply {

                  getOptionValue(FMU_FILE)?.let { path ->
                      val file = File(path.replace("\\", "/"))
                      if (file.exists() && file.name.endsWith(".fmu", true)) {
                          fmuFile = file
                      } else {
                          error("Not a valid file: ${file.absolutePath}")
                      }
                  }

                  mavenLocal = hasOption(MAVEN_LOCAL_OPT)

                  getOptionValue(OUTPUT_FOLDER)?.let {
                      outputFolder = File(it.replace("\\", "/"))
                  }

              }

              fmuFile?.let {
                  with (Fmu2Jar(it)) {
                      generateJar(GenerateOptions(mavenLocal, outputFolder))
                  }
              }

          } catch(ex: Exception) {
              ex.printStackTrace(System.out)
              error("Application error..")
          }


      }

  }

}
