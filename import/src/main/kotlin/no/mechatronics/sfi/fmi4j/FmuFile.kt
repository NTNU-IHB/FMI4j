/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
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

package no.mechatronics.sfi.fmi4j

import com.sun.jna.Platform
import java.io.File
import java.io.IOException
import java.net.URL
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
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

class FmuFile {

    private companion object {

         val LOG = LoggerFactory.getLogger(FmuFile::class.java)

         val FMI4J_PREFIX = "fmi4j_"
         val map: MutableMap<String, File> = HashMap()


        init {

            fun deleteFile(fmuFile: File) {
                LOG.debug("Preparing to delete extracted FMU folder and all its contents: {}", fmuFile)
                var tries = 0
                val maxTries = 5
                var deletedSucessfully = false
                do  {

                    try {
                        deletedSucessfully = fmuFile.deleteRecursively()
                    }catch (ex: Exception){
                        Thread.sleep(100)
                    }


                } while(!deletedSucessfully && tries++ < maxTries)

                if (deletedSucessfully) {
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


            val modelDescription = ModelDescription.parseModelDescription(url)
            val guid = modelDescription.guid

            if (guid in map) {
                LOG.debug("Re-using previously extracted FMU with name {}", modelDescription.modelName)
                return map[guid]!!
            }
            val baseName = FilenameUtils.getBaseName(url.toString())
            val tmp = Files.createTempFile(FMI4J_PREFIX + baseName, ".fmu").toFile()
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

            val modelDescription = ModelDescription.parseModelDescription(fmuFile)
            val guid = modelDescription.guid
            if (guid in map) {
                LOG.debug("Re-using previously extracted FMU with name {}", modelDescription.modelName)
                return map[guid]!!
            }

            val baseName = FilenameUtils.getBaseName(fmuFile.name).replace(FMI4J_PREFIX, "")
            return Files.createTempDirectory(FMI4J_PREFIX + baseName).toFile().also {
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

            val res = File(dir, "resources")
            if (!res.exists()) {
                res.mkdir()
            }

            LOG.debug("Extracted fmu into location {}", dir)
        }

    }



    private val fmuFile: File

    @Throws(IOException::class)
    constructor(file: File) {
        this.fmuFile = extractToTempFolder(file)
    }

    @Throws(IOException::class)
    constructor(url: URL) {
        this.fmuFile = extractToTempFolder(url)
    }

    fun getFmuPath(): String {
        return "file:///" + fmuFile.getAbsolutePath().replace("\\", "/")
    }

    fun getModelDescriptionFile(): File {
        return File(fmuFile, "modelDescription.xml")
    }

    @Throws(IOException::class)
    fun getModelDescriptionXml(): String {
        return FileUtils.readFileToString(getModelDescriptionFile(), Charset.forName("UTF-8"))
    }

    fun getLibraryFolderName(): String {
        return if (Platform.isWindows()) {
            WINDOWS_FOLDER
        } else if (Platform.isLinux()) {
            LINUX_FOLDER
        } else if (Platform.isMac()) {
            MAC_OS_FOLDER
        } else {
            throw UnsupportedOperationException("OS '${Platform.ARCH}' is unsupported!")
        }
    }

    fun getLibraryExtension(): String {
        return if (Platform.isWindows()) {
            WINDOWS_LIBRARY_EXTENSION
        } else if (Platform.isLinux()) {
            LINUX_LIBRARY_EXTENSION
        } else if (Platform.isMac()) {
            MAC_OS_LIBRARY_EXTENSION
        } else {
            throw UnsupportedOperationException("OS '${Platform.ARCH}' is unsupported!")
        }
    }

    fun getBitness(): String {
        return if (Platform.is64Bit()) "64" else "32"
    }

    fun getLibraryName(desc: ModelDescription): String {
        return "${desc.modelIdentifier}${getLibraryExtension()}"
    }

    fun getLibraryFolderPath(): String {
        return File(fmuFile, BINARIES_FOLDER + File.separator + getLibraryFolderName() + getBitness()).absolutePath
    }

    fun getFullLibraryPath(desc: ModelDescription): String {
        return File(fmuFile, BINARIES_FOLDER + File.separator + getLibraryFolderName() + getBitness() + File.separator + desc.modelIdentifier + getLibraryExtension()).absolutePath
    }

    fun getResourcesPath(): String {
        return "file:///" + File(fmuFile, RESOURCES_FOLDER).absolutePath.replace("\\", "/")
    }

    override fun toString(): String {
        return "FmuFile(fmuFile=${fmuFile.absolutePath})"
    }


}