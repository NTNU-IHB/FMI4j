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

package no.mechatronics.sfi.fmi4j.fmu

import com.sun.jna.Platform
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import java.io.File
import java.io.IOException
import java.net.URL
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

private const val RESOURCES_FOLDER = "resources"
private const val BINARIES_FOLDER = "binaries"
private const val MAC_OS_FOLDER = "darwin"
private const val WINDOWS_FOLDER = "win"
private const val LINUX_FOLDER = "linux"
private const val MAC_OS_LIBRARY_EXTENSION = ".dylib"
private const val WINDOWS_LIBRARY_EXTENSION = ".dll"
private const val LINUX_LIBRARY_EXTENSION = ".so"

private const val FMI4J_FILE_PREFIX = "fmi4j_"
private const val MODEL_DESC = "modelDescription.xml"

/**
 *
 * @author Lars Ivar Hatledal
 */
class FmuFile {
    private val fmuFile: File

    @Throws(IOException::class)
    constructor(file: File) {
        this.fmuFile = extractToTempFolder(file)
    }

    @Throws(IOException::class)
    constructor(url: URL) {
        this.fmuFile = extractToTempFolder(url)
    }

    private val platformBitness: String
        get() = if (Platform.is64Bit()) "64" else "32"

    val fmuPath: String
        get() = "file:///${fmuFile.absolutePath.replace("\\", "/")}"

    /**
     * Get the file handle for the modelDescription.xml file
     */
    private val modelDescriptionFile: File
        get() = File(fmuFile, MODEL_DESC)


    /**
     * Get the content of the modelDescription.xml file as a String
     */
    val modelDescriptionXml: String by lazy {
         FileUtils.readFileToString(modelDescriptionFile, Charset.forName("UTF-8"))
    }

    val libraryFolderName: String
        get() =  when {
            Platform.isWindows() -> WINDOWS_FOLDER
            Platform.isLinux() -> LINUX_FOLDER
            Platform.isMac() -> MAC_OS_FOLDER
            else -> throw UnsupportedOperationException("OS '${Platform.ARCH}' is unsupported!")
    }

    val libraryExtension: String
        get() =  when {
            Platform.isWindows() -> WINDOWS_LIBRARY_EXTENSION
            Platform.isLinux() -> LINUX_LIBRARY_EXTENSION
            Platform.isMac() -> MAC_OS_LIBRARY_EXTENSION
            else ->  throw UnsupportedOperationException("OS '${Platform.ARCH}' is unsupported!")
    }

    val libraryFolderPath: String
        get() = File(fmuFile,BINARIES_FOLDER + File.separator
                + libraryFolderName + platformBitness).absolutePath

    val resourcesPath: String
        get() = "file:///${File(fmuFile,
                RESOURCES_FOLDER).absolutePath.replace("\\", "/")}"

    fun getLibraryName(desc: ModelDescription): String {
        return "${desc.modelIdentifier}${libraryExtension}"
    }

    fun getFullLibraryPath(desc: ModelDescription): String {
        return File(fmuFile,BINARIES_FOLDER + File.separator + libraryFolderName + platformBitness
                        + File.separator + desc.modelIdentifier + libraryExtension).absolutePath
    }

    override fun toString(): String {
        return "FmuFile(fmuFile=${fmuFile.absolutePath})"
    }


    private companion object {

        val LOG: Logger = LoggerFactory.getLogger(FmuFile::class.java)
        val map: MutableMap<String, File> = HashMap()

        init {

            fun deleteFile(fmuFile: File) {
                LOG.debug("Preparing to delete extracted FMU folder and all its contents: {}", fmuFile)
                var tries = 0
                val maxTries = 5
                var deletedSuccessfully = false
                do  {

                    try {
                        deletedSuccessfully = fmuFile.deleteRecursively()
                    }catch (ex: Exception){
                        Thread.sleep(100)
                    }


                } while(!deletedSuccessfully && tries++ < maxTries)

                if (deletedSuccessfully) {
                    LOG.debug("Deleted fmu folder: {}", fmuFile)
                } else {
                    LOG.warn("Failed to delete fmu folder after {} unsuccessful attempts: {}", maxTries, fmuFile)
                }
            }

            Runtime.getRuntime().addShutdownHook(Thread {
                for (file in map.values) {
                    deleteFile(file)
                }
            })
        }


        @Throws(IOException::class)
        private fun extractToTempFolder(url: URL): File {

            val modelDescription = ModelDescriptionParser.parse(url)
            val guid = modelDescription.guid

            if (guid in map) {
                LOG.debug("Re-using previously extracted FMU with name {}", modelDescription.modelName)
                return map[guid]!!
            }
            val baseName = FilenameUtils.getBaseName(url.toString())
            val tmp = Files.createTempFile(FMI4J_FILE_PREFIX + baseName, ".fmu").toFile()
            val data = IOUtils.toByteArray(url)
            FileUtils.writeByteArrayToFile(tmp, data)

            LOG.debug("Copied fmu from url into {}", tmp)
            val extractToTempFolder = extractToTempFolder(tmp)

            Files.deleteIfExists(tmp.toPath())
            LOG.debug("Deleted temp fmu file retrieved from url {}", tmp)

            return extractToTempFolder.also {
                map[guid] = it
            }

        }

        @Throws(IOException::class)
        private fun extractToTempFolder(fmuFile: File): File {

            val modelDescription = ModelDescriptionParser.parse(fmuFile)
            val guid = modelDescription.guid
            if (guid in map) {
                LOG.debug("Re-using previously extracted FMU with name {}", modelDescription.modelName)
                return map[guid]!!
            }

            val baseName = FilenameUtils.getBaseName(fmuFile.name).replace(FMI4J_FILE_PREFIX, "")
            return Files.createTempDirectory(FMI4J_FILE_PREFIX + baseName).toFile().also {
                extractTo(fmuFile, it)
                map[guid] = it
            }


        }

        @Throws(IOException::class)
        private fun extractTo(fmuFile: File, dir: File) {
            ZipFile(fmuFile).use { zipFile ->
                val enu = zipFile.entries()

                while (enu.hasMoreElements()) {
                    val zipEntry = enu.nextElement() as ZipEntry
                    val name = zipEntry.name

                    if (!zipEntry.isDirectory) {
                        val child = File(dir, name)
                        val data = IOUtils.toByteArray(zipFile.getInputStream(zipEntry))
                        FileUtils.writeByteArrayToFile(child, data)
                    }
                }
            }

            File(dir, "resources").apply {
                if (!exists()) {
                    mkdir()
                }
            }

            LOG.debug("Extracted fmu into location {}", dir)
        }

    }

}