
package no.mechatronics.sfi.fmu2jar

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import java.io.File


class Main  {

  companion object {

      const val FMU_FILE = "fmu"
      const val OUTPUT_FOLDER = "outputFolder"
      const val MAVEN_LOCAL_OPT = "mavenLocal"


      @JvmStatic
      public fun main(args: Array<String>) {

          if (args.size == 0) {
              println("No input.. exiting")
          }

          try {
              val options = Options().apply {
                  addOption(FMU_FILE, true, "Path to the FMU")
                  addOption(MAVEN_LOCAL_OPT, false, "Should the .jar be published to maven local?")
                  addOption(OUTPUT_FOLDER, true, "Specify where to copy the generated .jar. Not needed if $MAVEN_LOCAL_OPT=true")
              }

              val parser = DefaultParser()
              val cmd = parser.parse(options, args)

              var outputFolder: File? = null
              var mavenLocal: Boolean = false
              var fmuFile: File? = null

              with(cmd) {

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
