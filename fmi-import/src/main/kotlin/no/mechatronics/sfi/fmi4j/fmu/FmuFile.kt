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

import com.sun.jna.Native
import com.sun.jna.Platform
import com.sun.jna.Pointer
import no.mechatronics.sfi.fmi4j.misc.FmiBoolean
import no.mechatronics.sfi.fmi4j.misc.LibraryProvider
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import no.mechatronics.sfi.fmi4j.proxy.v2.Fmi2Library
import no.mechatronics.sfi.fmi4j.proxy.v2.Fmi2Type
import no.mechatronics.sfi.fmi4j.proxy.v2.cs.CoSimulationLibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.v2.cs.Fmi2CoSimulationLibrary
import no.mechatronics.sfi.fmi4j.proxy.v2.me.Fmi2ModelExchangeLibrary
import no.mechatronics.sfi.fmi4j.proxy.v2.me.ModelExchangeLibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.v2.structs.Fmi2CallbackFunctions
import java.io.File
import java.io.IOException
import java.net.URL
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

private const val LIBRARY_PATH = "jna.library.path"

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
    private val instances = mutableListOf<AbstractFmu<*, *>>()
    private val libraries = mutableListOf<LibraryProvider<*>>()

    @Throws(IOException::class)
    constructor(file: File) {
        this.fmuFile = extractToTempFolder(file)
    }

    @Throws(IOException::class)
    constructor(url: URL) {
        this.fmuFile = extractToTempFolder(url)
    }

    init {

        Runtime.getRuntime().addShutdownHook(Thread {
            instances.forEach {
                if (!it.isTerminated) {
                    it.terminate()
                }
            }
            libraries.forEach {
                it.disposeLibrary()
            }
            for (file in map.values) {
                if (file.deleteRecursively()) {
                    LOG.debug("Deleted fmu folder: $file")
                } else {
                    LOG.debug("Failed to delete fmu folder: $file")
                }
            }
        })

    }


    /**
     * Get the content of the modelDescription.xml file as a String
     */
    val modelDescriptionXml: String by lazy {
        FileUtils.readFileToString(modelDescriptionFile, Charset.forName("UTF-8"))
    }

    val modelDescription by lazy {
        ModelDescriptionParser.parse(modelDescriptionXml)
    }

    private val platformBitness: String
        get() = if (Platform.is64Bit()) "64" else "32"

    /**
     * Get the file handle for the modelDescription.xml file
     */
    private val modelDescriptionFile: File
        get() = File(fmuFile, MODEL_DESC)

    private val libraryFolderName: String
        get() =  when {
            Platform.isWindows() -> WINDOWS_FOLDER
            Platform.isLinux() -> LINUX_FOLDER
            Platform.isMac() -> MAC_OS_FOLDER
            else -> throw UnsupportedOperationException("OS '${Platform.ARCH}' is unsupported!")
    }

    private val libraryExtension: String
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

    private val coSimulationBuilder: CoSimulationFmuBuilder? by lazy {
        if (supportsCoSimulation) CoSimulationFmuBuilder() else null
    }

    private val modelExchangeBuilder: ModelExchangeFmuBuilder? by lazy {
        if (supportsModelExchange) ModelExchangeFmuBuilder() else null
    }

    val supportsCoSimulation: Boolean
        get() = modelDescription.supportsCoSimulation

    val supportsModelExchange: Boolean
        get() = modelDescription.supportsModelExchange

    @Throws(IllegalStateException::class)
    fun asCoSimulationFmu(): CoSimulationFmuBuilder
            = coSimulationBuilder ?: throw IllegalStateException("FMU does not support Co-Simulation!")

    @Throws(IllegalStateException::class)
    fun asModelExchangeFmu(): ModelExchangeFmuBuilder
            = modelExchangeBuilder ?: throw IllegalStateException("FMU does not support Model Exchange!")


    override fun toString(): String {
        return "FmuFile(fmuFile=${fmuFile.absolutePath})"
    }

    private val that = this

    inner class CoSimulationFmuBuilder internal constructor() {
        private val modelDescription
            get() = that.modelDescription.asCoSimulationModelDescription()

        private val libraryCache: LibraryProvider<Fmi2CoSimulationLibrary> by lazy {
            loadLibrary()
        }

        private fun loadLibrary(): LibraryProvider<Fmi2CoSimulationLibrary>
                = loadLibrary(that, modelDescription, Fmi2CoSimulationLibrary::class.java).also { libraries.add(it) }

        @JvmOverloads
        fun newInstance(visible: Boolean = false, loggingOn: Boolean = false) : CoSimulationFmu {
            val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
            val c = instantiate(that, modelDescription, lib.get(), Fmi2Type.CoSimulation, visible, loggingOn)
            val wrapper = CoSimulationLibraryWrapper(c, lib)
            return CoSimulationFmu(that, wrapper).also { instances.add(it) }
        }

    }

    inner class ModelExchangeFmuBuilder() {

        private val modelDescription
            get() = that.modelDescription.asModelExchangeModelDescription()

        private val libraryCache: LibraryProvider<Fmi2ModelExchangeLibrary> by lazy {
            loadLibrary()
        }

        private fun loadLibrary(): LibraryProvider<Fmi2ModelExchangeLibrary>
                = loadLibrary(that, modelDescription, Fmi2ModelExchangeLibrary::class.java).also { libraries.add(it) }

        @JvmOverloads
        fun newInstance(visible: Boolean = false, loggingOn: Boolean = false) : ModelExchangeFmu {
            val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
            val c = instantiate(that, modelDescription, lib.get(), Fmi2Type.ModelExchange, visible, loggingOn)
            val wrapper = ModelExchangeLibraryWrapper(c, lib)
            return ModelExchangeFmu(that, wrapper).also { instances.add(it) }
        }

        @JvmOverloads
        fun newInstance(integrator: FirstOrderIntegrator, visible: Boolean = false, loggingOn: Boolean = false) : ModelExchangeFmuWithIntegrator {
            val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
            val c = instantiate(that, modelDescription, lib.get(), Fmi2Type.ModelExchange, visible, loggingOn)
            val wrapper = ModelExchangeLibraryWrapper(c, lib)
            return ModelExchangeFmuWithIntegrator(ModelExchangeFmu(that, wrapper), integrator).also { instances.add(it.fmu) }
        }

    }


    private companion object {

        val LOG: Logger = LoggerFactory.getLogger(FmuFile::class.java)
        val map = mutableMapOf<String, File>()

//        init {
//
//            Runtime.getRuntime().addShutdownHook(Thread {
//                for (file in map.values) {
//                    if (file.deleteRecursively()) {
//                        LOG.debug("Deleted fmu folder: $file")
//                    } else {
//                        LOG.warn("Failed to delete fmu folder: $file")
//                    }
//                }
//            })
//        }


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
                LOG.debug("Re-using previously extracted FMU with name ${modelDescription.modelName}")
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
                    if (!zipEntry.isDirectory) {
                        val child = File(dir, zipEntry.name)
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

            LOG.debug("Extracted fmu into location $dir")
        }

    }

}


private fun <E: Fmi2Library> loadLibrary(fmuFile: FmuFile, modelDescription: ModelDescription, type: Class<E>): LibraryProvider<E> {
    System.setProperty(LIBRARY_PATH, fmuFile.libraryFolderPath)
    return LibraryProvider(Native.loadLibrary(fmuFile.getLibraryName(modelDescription), type))
}

private fun instantiate(fmuFile: FmuFile, modelDescription: ModelDescription, library: Fmi2Library, fmiType: Fmi2Type, visible: Boolean, loggingOn: Boolean) : Pointer {
    return library.fmi2Instantiate(modelDescription.modelIdentifier,
            fmiType.code, modelDescription.guid,
            fmuFile.resourcesPath, Fmi2CallbackFunctions(),
            FmiBoolean.convert(visible), FmiBoolean.convert(loggingOn) )
            ?: throw AssertionError("Unable to instantiate FMU. Returned pointer is null!")
}

