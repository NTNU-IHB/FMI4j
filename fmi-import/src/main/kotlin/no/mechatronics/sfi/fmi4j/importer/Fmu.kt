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

package no.mechatronics.sfi.fmi4j.importer

import com.sun.jna.Native
import com.sun.jna.Platform
import com.sun.jna.Pointer
import no.mechatronics.sfi.fmi4j.importer.cs.CoSimulationFmuInstance
import no.mechatronics.sfi.fmi4j.importer.me.ModelExchangeFmuInstance
import no.mechatronics.sfi.fmi4j.importer.me.ModelExchangeFmuStepper
import no.mechatronics.sfi.fmi4j.importer.misc.FmiBoolean
import no.mechatronics.sfi.fmi4j.importer.misc.LibraryProvider
import no.mechatronics.sfi.fmi4j.importer.misc.extractTo
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.FmiLibrary
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.FmiType
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.cs.CoSimulationLibraryWrapper
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.cs.FmiCoSimulationLibrary
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.me.FmiModelExchangeLibrary
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.me.ModelExchangeLibraryWrapper
import no.mechatronics.sfi.fmi4j.importer.proxy.v2.structs.FmiCallbackFunctions
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionProvider
import no.mechatronics.sfi.fmi4j.modeldescription.SpecificModelDescription
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL
import java.nio.file.Files

private const val LIBRARY_PATH = "jna.library.path"

private const val RESOURCES_FOLDER = "resources"
private const val BINARIES_FOLDER = "binaries"
private const val MAC_OS_FOLDER = "darwin"
private const val WINDOWS_FOLDER = "win"
private const val LINUX_FOLDER = "linux"
private const val MAC_OS_LIBRARY_EXTENSION = ".dylib"
private const val WINDOWS_LIBRARY_EXTENSION = ".dll"
private const val LINUX_LIBRARY_EXTENSION = ".so"

private const val FMU_EXTENSION = "fmu"
internal const val FMI4J_FILE_PREFIX = "fmi4j_"

private const val MODEL_DESC = "modelDescription.xml"

/**
 *
 * @author Lars Ivar Hatledal
 */
class Fmu private constructor(
        private val fmuFile: File
): Closeable {

    var isClosed = false
        private set

    var hasDeletedExtractedFmuFolder = false
        private set

    private val instances = mutableListOf<no.mechatronics.sfi.fmi4j.importer.AbstractFmuInstance<*, *>>()
    private val libraries = mutableListOf<LibraryProvider<*>>()


    override fun close() {
        if (!isClosed) {

            LOG.debug("Closing FMU '${modelDescription.modelName}'..")

            terminateInstances()
            disposeNativeLibraries()
            deleteExtractedFmuFolder()

            files.remove(this)
            isClosed = true
        }
    }

    private fun disposeNativeLibraries() {
        libraries.forEach {
            it.disposeLibrary()
        }
        libraries.clear()
    }

    private fun terminateInstances() {
        instances.forEach {
            if (!it.isTerminated) {
                it.terminate()
            }
            if (!it.wrapper.isInstanceFreed) {
                it.wrapper.freeInstance()
            }
        }
        instances.clear()
    }

    fun deleteExtractedFmuFolder(): Boolean {

        if (!hasDeletedExtractedFmuFolder) {
            return if (fmuFile.deleteRecursively()) {
                LOG.debug("Deleted extracted FMU contents: $fmuFile")
                hasDeletedExtractedFmuFolder = true
                true
            } else {
                LOG.debug("Failed to delete extracted FMU contents: $fmuFile")
                false
            }
        }
        return true
    }

    /**
     * Get the content of the modelDescription.xml file as a String
     */
    val modelDescriptionXml: String by lazy {
        modelDescriptionFile.readText()
    }

    val modelDescription: ModelDescriptionProvider by lazy {
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

    fun getLibraryName(desc: SpecificModelDescription): String {
        return "${desc.modelIdentifier}$libraryExtension"
    }

    fun getFullLibraryPath(desc: SpecificModelDescription): String {
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
        return "Fmu(fmu=${fmuFile.absolutePath})"
    }

    inner class CoSimulationFmuBuilder internal constructor() {

        private val modelDescription
            get() = this@Fmu.modelDescription.asCoSimulationModelDescription()

        private val libraryCache: LibraryProvider<FmiCoSimulationLibrary> by lazy {
            loadLibrary()
        }

        private fun loadLibrary(): LibraryProvider<FmiCoSimulationLibrary> {
            return loadLibrary(this@Fmu, modelDescription, FmiCoSimulationLibrary::class.java).also {
                libraries.add(it)
            }
        }

        @JvmOverloads
        fun newInstance(visible: Boolean = false, loggingOn: Boolean = false) : CoSimulationFmuInstance {
            val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
            val c = instantiate(this@Fmu, modelDescription, lib.get(), FmiType.CoSimulation, visible, loggingOn)
            val wrapper = CoSimulationLibraryWrapper(c, lib)
            return CoSimulationFmuInstance(this@Fmu, wrapper).also { instances.add(it) }
        }

    }

    inner class ModelExchangeFmuBuilder {

        private val modelDescription
            get() = this@Fmu.modelDescription.asModelExchangeModelDescription()

        private val libraryCache: LibraryProvider<FmiModelExchangeLibrary> by lazy {
            loadLibrary()
        }

        private fun loadLibrary(): LibraryProvider<FmiModelExchangeLibrary> {
            return loadLibrary(this@Fmu, modelDescription, FmiModelExchangeLibrary::class.java).also {
                libraries.add(it)
            }
        }

        @JvmOverloads
        fun newInstance(visible: Boolean = false, loggingOn: Boolean = false) : ModelExchangeFmuInstance {
            val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
            val c = instantiate(this@Fmu, modelDescription, lib.get(), FmiType.ModelExchange, visible, loggingOn)
            val wrapper = ModelExchangeLibraryWrapper(c, lib)
            return ModelExchangeFmuInstance(this@Fmu, wrapper).also { instances.add(it) }
        }

        @JvmOverloads
        fun newInstance(integrator: FirstOrderIntegrator, visible: Boolean = false, loggingOn: Boolean = false): ModelExchangeFmuStepper {
            val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
            val c = instantiate(this@Fmu, modelDescription, lib.get(), FmiType.ModelExchange, visible, loggingOn)
            val wrapper = ModelExchangeLibraryWrapper(c, lib)
            return ModelExchangeFmuStepper(ModelExchangeFmuInstance(this@Fmu, wrapper), integrator).also { instances.add(it.fmuInstance) }
        }

    }

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Fmu::class.java)

        private val files = mutableListOf<Fmu>()

        init {
            Runtime.getRuntime().addShutdownHook(Thread {
                files.toMutableList().forEach{ it.close() }
            })
        }

        private fun createTempDir(fmuName: String): File {
            return Files.createTempDirectory(FMI4J_FILE_PREFIX + fmuName).toFile().also {
                File(it, "resources").apply {
                    if (!exists()) {
                        mkdir()
                    }
                }
            }
        }

        @JvmStatic
        @Throws(IOException::class, FileNotFoundException::class)
        fun from (file: File): Fmu {

            val extension = file.extension.toLowerCase()
            if (extension != FMU_EXTENSION) {
                throw IllegalArgumentException("File is not an FMU! Invalid extension found: .$extension")
            }

            if (!file.exists()) {
                throw FileNotFoundException("No such file: $file!")
            }

            val temp = createTempDir(file.nameWithoutExtension)
            file.extractTo(temp)

            return Fmu(temp).also {
                files.add(it)
            }

        }

        @JvmStatic
        @Throws(IOException::class)
        fun from(url: URL): Fmu {

            val extension = File(url.file).extension
            if (extension != FMU_EXTENSION) {
                throw IllegalArgumentException("URL does not point to an FMU! Invalid extension found: .$extension")
            }

            val temp = createTempDir(File(url.file).nameWithoutExtension)
            url.extractTo(temp)

            return Fmu(temp).also {
                files.add(it)
            }

        }

        private fun <E: FmiLibrary> loadLibrary(fmu: Fmu, modelDescription: SpecificModelDescription, type: Class<E>): LibraryProvider<E> {
            return LibraryProvider { Native.loadLibrary(fmu.getFullLibraryPath(modelDescription), type) }
        }

        private fun instantiate(fmu: Fmu, modelDescription: SpecificModelDescription, library: FmiLibrary, fmiType: FmiType, visible: Boolean, loggingOn: Boolean) : Pointer {
            LOG.trace("Calling instantiate: visible=$visible, loggingOn=$loggingOn")
            return library.fmi2Instantiate(modelDescription.modelIdentifier,
                    fmiType.code, modelDescription.guid,
                    fmu.resourcesPath, FmiCallbackFunctions.byValue(),
                    FmiBoolean.convert(visible), FmiBoolean.convert(loggingOn) )
                    ?: throw IllegalStateException("Unable to instantiate FMU. Returned pointer is null!")
        }

    }

}




