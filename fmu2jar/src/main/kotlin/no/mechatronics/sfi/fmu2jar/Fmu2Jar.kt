

package no.mechatronics.sfi.fmu2jar

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ScalarVariable
import no.mechatronics.sfi.fmu2jar.templates.CodeGeneration
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class GenerateOptions(
        val mavenLocal: Boolean,
        var outputFolder: File?
) {
    init {
        if (outputFolder == null && !mavenLocal) {
            outputFolder = File("").absoluteFile
        }
    }
}

class Fmu2Jar(
        private val file:File
) {

    private val modelDescription: ModelDescription

    init {

        if (!file.name.endsWith(".fmu", true)) {
            throw IllegalArgumentException("File '${file.absolutePath}' is not and FMU!")
        }

        modelDescription = ModelDescription.Companion.parseModelDescription(file)
    }

    private fun copyBuildFile(parentDir: File) {
        IOUtils.copy(javaClass.classLoader.getResourceAsStream("build.gradle"), FileOutputStream(File(parentDir, "build.gradle")))
    }

    private fun copyGradleWrapper(parentDir: File) {
        val zipStream = javaClass.classLoader.getResourceAsStream("myzip.zip")
        ZipInputStream(zipStream).use { zis ->
            var nextEntry: ZipEntry? = zis.nextEntry
            while (nextEntry != null) {

                if (!nextEntry.isDirectory) {
                    File(parentDir, nextEntry.name).also { file ->

                        if (!file.exists()) {
                            if (!file.parentFile.exists()) {
                                Files.createDirectories(file.parentFile.toPath())
                            }

                            FileOutputStream(file).use { fis ->
                                IOUtils.copy(zis, fis)
                            }
                        }

                    }
                }
                nextEntry = zis.nextEntry
            }
        }
    }


     private fun generateInstanceMethods() :String {
        val sb = StringBuilder()
        modelDescription.modelVariables.variables.forEach({

            CodeGeneration.generateGet(it, sb)
            CodeGeneration.generateSet(it, sb)


        })
        return sb.toString()
    }

    private fun copySourceFile(parentDir: File) {
        val src = CodeGeneration.generateBody(modelDescription.modelName, file.name, generateInstanceMethods())

        File(parentDir, "src/main/kotlin/no/mechatronics/sfi/fmu2jar/${modelDescription.modelName}.kt").apply {
            if (!parentFile.exists()) {
                Files.createDirectories(file.parentFile.toPath())
            }
            FileUtils.writeStringToFile(this, src, Charset.forName("UTF-8"))
        }

    }

    private fun copyFmuFile(parentDir: File) {
        File(parentDir, "src/main/resources").apply {
            if (!parentDir.exists()) {
                Files.createDirectories(file.parentFile.toPath())
            }
            FileUtils.copyFileToDirectory(file, this)
        }

    }

    fun generateJar(options: GenerateOptions) {

        val tempDirectory = Files.createTempDirectory("fmu2jar_").toFile()
        try {
            val parentDir = File(tempDirectory, modelDescription.modelName)
            parentDir.mkdir()

           // println(parentDir.absolutePath)

            copyFmuFile(parentDir)
            copyBuildFile(parentDir)
            copyGradleWrapper(parentDir)
            copySourceFile(parentDir)


            val cmd = mutableListOf("${parentDir.absolutePath}/gradlew.bat", "clean", "build")
            if (options.mavenLocal) {
                cmd.add("publishToMavenLocal")
            }

            ProcessBuilder()
                    .directory(parentDir)
                    .command(cmd)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .start()
                    .waitFor()

            options.outputFolder?.let { outputFolder ->
                File(parentDir, "build/libs/${modelDescription.modelName}-1.0-SNAPSHOT.jar").let { src ->
                    if (src.exists()) {
                        FileUtils.copyFileToDirectory(src.absoluteFile, outputFolder.absoluteFile)
                        println("Wrote FMU to directory '${outputFolder.absolutePath}'")
                    }
                }
            }
        } finally {
            if(tempDirectory.deleteRecursively()) {
                println("Deleted folder '${tempDirectory.absolutePath}'")
            }
        }

    }


}