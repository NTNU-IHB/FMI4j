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

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import no.mechatronics.sfi.fmi4j.modeldescription.SimpleModelDescription
import no.mechatronics.sfi.fmu2jar.templates.CodeGeneration
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * @author Lars Ivar Hatledal
 */
class Fmu2Jar(
        private val file: File
) {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(Fmu2Jar::class.java)
    }

    private val modelDescription: SimpleModelDescription

    init {
        if (!file.name.endsWith(".fmu", true)) {
            throw IllegalArgumentException("File '${file.absolutePath}' is not and FMU!")
        }
        modelDescription = ModelDescriptionParser.parse(file)
    }

    private fun copyBuildFile(parentDir: File) {
        FileOutputStream(File(parentDir, "build.gradle")).use {
            IOUtils.copy(javaClass.classLoader.getResourceAsStream("build.gradle"), it)
        }
        LOG.debug("Build file copied")
    }

    private fun copyGradleWrapper(parentDir: File) {
        val zipStream: InputStream = javaClass.classLoader.getResourceAsStream("myzip.zip")
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
        LOG.debug("Gradle wrapper copied")
    }

    private fun copySourceFile(parentDir: File) {
        File(parentDir, "src/main/kotlin/no/mechatronics/sfi/fmu2jar/${modelDescription.modelName}.kt").apply {
            if (!parentFile.exists()) {
                Files.createDirectories(file.parentFile.toPath())
            }
            val src = CodeGeneration.generateWrapper(modelDescription)
            FileUtils.writeStringToFile(this, src, Charset.forName("UTF-8"))
        }
        LOG.debug("Source file copied")
    }

    private fun copyFmuFile(parentDir: File) {
        File(parentDir, "src/main/resources").apply {
            if (!exists()) {
               mkdirs()
            }
            FileInputStream(file).use { fis ->
                FileOutputStream(File(this, "${modelDescription.modelName}.fmu")).use { fos ->
                    IOUtils.copy(fis, fos)
                }
            }
        }
        LOG.debug("FMU copied")
    }

    fun generateJar(options: GenerateOptions) {

        LOG.info("Generating .jar..")

        val tempDirectory = Files.createTempDirectory("fmu2jar_").toFile()
        val parentDir = File(tempDirectory, modelDescription.modelName).apply {
            mkdir()
        }

        copyFmuFile(parentDir)
        copyBuildFile(parentDir)
        copyGradleWrapper(parentDir)
        copySourceFile(parentDir)

        File(parentDir, "settings.gradle").apply {
            createNewFile()
        }

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
                            LOG.info("Generated .jar is located at: ${outputFolder.absolutePath}${File.separator}${src.name}")
                        }
                    }
                }
            } else {
                LOG.error("Gradle process returned with an error ($status). Unable to generate jar!")
            }

        } finally {
            if(tempDirectory.exists() && tempDirectory.deleteRecursively()) {
                LOG.debug("Deleted temp folder: {}", tempDirectory.absolutePath)
            }
        }

    }


}

