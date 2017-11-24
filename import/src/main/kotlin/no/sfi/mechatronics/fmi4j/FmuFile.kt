package no.sfi.mechatronics.fmi4j

import com.sun.jna.Platform
import java.io.File
import java.io.IOException
import java.net.URL
import no.sfi.mechatronics.fmi4j.modeldescription.ModelDescription
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


class FmuFile {

    private val LOG = LoggerFactory.getLogger(FmuFile::class.java)

    private val RESOURCES_FOLDER = "resources"
    private val BINARIES_FOLDER = "binaries"
    private val MAC_OS_FOLDER = "darwin"
    private val WINDOWS_FOLDER = "win"
    private val LINUX_FOLDER = "linux"
    private val MAC_OS_LIBRARY_EXTENSION = ".dylib"
    private val WINDOWS_LIBRARY_EXTENSION = ".dll"
    private val LINUX_LIBRARY_EXTENSION = ".so"

    private val FMI4J_PREFIX = "fmi4j_"

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

    fun dispose() {
        if (fmuFile.exists()) {
            var count = 0
            var success = false
            while (!success) {
                try {
                    FileUtils.deleteDirectory(fmuFile)
                    success = true
                    LOG.debug("Deleted fmu folder: {}", fmuFile)
                } catch (ex: IOException) {
                    LOG.warn("Failed to delete fmu folder: {}", fmuFile)
                    try {
                        Thread.sleep(10)
                    } catch (ex1: InterruptedException) {
                        LOG.error("Interrupted", ex)
                    }

                }

                if (count++ > 1) {
                    Runtime.getRuntime().addShutdownHook(Thread {

                        if (fmuFile.exists()) {
                            try {
                                FileUtils.deleteDirectory(fmuFile)
                                LOG.debug("Deleted fmu folder: {}", fmuFile)
                            } catch (ex: IOException) {
                                LOG.warn("Failed to delete fmu folder: {}", fmuFile)
                            }

                        }

                    })
                    break
                }
            }

        }
    }


    @Throws(IOException::class)
    private fun extractToTempFolder(url: URL): File {

        val basename = FilenameUtils.getBaseName(url.toString())
        val tmp = Files.createTempFile(FMI4J_PREFIX + basename, ".fmu").toFile()
        val data = IOUtils.toByteArray(url)
        FileUtils.writeByteArrayToFile(tmp, data)

        LOG.debug("Copied fmu from url into {}", tmp)

        val extractToTempFolder = extractToTempFolder(tmp)

        Files.deleteIfExists(tmp.toPath())
        LOG.debug("Deleted temp fmu file retrieved from url {}", tmp)

        return extractToTempFolder

    }

    @Throws(IOException::class)
    private fun extractToTempFolder(fmuFile: File): File {

        val baseName = FilenameUtils.getBaseName(fmuFile.name).replace(FMI4J_PREFIX, "")
        val tmpFolder = Files.createTempDirectory(FMI4J_PREFIX + baseName).toFile()
        extractTo(fmuFile, tmpFolder)
        return tmpFolder

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