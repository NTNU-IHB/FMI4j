package no.ntnu.ihb.fmu2jar.cli

import no.ntnu.ihb.fmu2jar.Fmu2Jar
import picocli.CommandLine
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

@CommandLine.Command(name = "fmu2jar")
class Args : Runnable {

    @CommandLine.Option(names = ["-h", "--help"], description = ["Print this message and quits."], usageHelp = true)
    private var showHelp = false

    @CommandLine.Option(names = ["-f", "--fmu"], description = ["Path to the FMU."], required = true)
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
