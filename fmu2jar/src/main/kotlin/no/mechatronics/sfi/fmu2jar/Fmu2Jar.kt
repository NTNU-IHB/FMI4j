

package no.mechatronics.sfi.fmu2jar

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import no.mechatronics.sfi.fmu2jar.templates.CodeGeneration
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class Fmu2Jar(
        private val file: File
) {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(Fmu2Jar::class.java)
    }

    private val modelDescription: ModelDescription

    init {
        if (!file.name.endsWith(".fmu", true)) {
            throw IllegalArgumentException("File '${file.absolutePath}' is not and FMU!")
        }
        modelDescription = ModelDescriptionParser.parse(file).asCS()
    }

    private fun copyBuildFile(parentDir: File) {
        FileOutputStream(File(parentDir, "build.gradle")).use {
            IOUtils.copy(javaClass.classLoader.getResourceAsStream("build.gradle"), it)
        }
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

    private fun copySourceFile(parentDir: File) {
        File(parentDir, "src/main/kotlin/no/mechatronics/sfi/fmu2jar/${modelDescription.modelName}.kt").apply {
            if (!parentFile.exists()) {
                Files.createDirectories(file.parentFile.toPath())
            }
            val src = CodeGeneration.generateWrapper(modelDescription)
            FileUtils.writeStringToFile(this, src, Charset.forName("UTF-8"))
        }
    }

    private fun copyFmuFile(parentDir: File) {
        File(parentDir, "src/main/resources").apply {
            if (!parentDir.exists()) {
                Files.createDirectories(file.parentFile.toPath())
            }
            FileInputStream(file).use { fis ->
                FileOutputStream(File(parentDir, "${modelDescription.modelName}.fmu")).use { fos ->
                    IOUtils.copy(fis, fos)
                }
            }
        }
    }

    fun generateJar(options: GenerateOptions) {

        val tempDirectory = Files.createTempDirectory("fmu2jar_").toFile()
        val parentDir = File(tempDirectory, modelDescription.modelName)
        parentDir.mkdir()

        copyFmuFile(parentDir)
        copyBuildFile(parentDir)
        copyGradleWrapper(parentDir)
        copySourceFile(parentDir)

        try {

            val cmd = mutableListOf("${parentDir.absolutePath}/gradlew.bat", "clean", "build")
            if (options.mavenLocal) {
                cmd.add("publishToMavenLocal")
            }

            val status = ProcessBuilder()
                    .directory(parentDir)
                    .command(cmd)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .start()
                    .waitFor()

            if (status == 0) {
                options.outputFolder?.let { outputFolder ->
                    File(parentDir, "build/libs/${modelDescription.modelName}-1.0-SNAPSHOT.jar").let { src ->
                        if (src.exists()) {
                            FileUtils.copyFileToDirectory(src.absoluteFile, outputFolder.absoluteFile)
                            LOG.info("Wrote FMU to directory: {}", outputFolder.absolutePath)
                        }
                    }
                }
            } else {
                LOG.error("Gradle process returned with an error ($status). Unable to generate jar!")
            }

        } finally {
            if(tempDirectory.exists() && tempDirectory.deleteRecursively()) {
                LOG.info("Deleted temp folder: {}", tempDirectory.absolutePath)
            }
        }

    }


}

