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

package no.ntnu.ihb.fmi4j.importer.fmi1

import no.ntnu.ihb.fmi4j.importer.fmi1.jni.Fmi1Library
import no.ntnu.ihb.fmi4j.modeldescription.CommonModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionProvider
import no.ntnu.ihb.fmi4j.modeldescription.fmi1.JaxbModelDescriptionParser
import no.ntnu.ihb.fmi4j.util.OsUtil
import no.ntnu.ihb.fmi4j.util.extractContentTo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import java.net.URL
import java.nio.file.Files
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Represents an FMU
 *
 * @author Lars Ivar Hatledal
 */
class Fmu private constructor(
        val name: String,
        private val extractedFmu: File
) : Closeable {

    private var isClosed = AtomicBoolean(false)
    private val libraries = mutableListOf<Fmi1Library>()
    private val instances = mutableListOf<AbstractFmuInstance<*, *>>()

    private val coSimulationFmu by lazy {
        CoSimulationFmu(this)
    }

    init {
        if (!File(extractedFmu, MODEL_DESC).exists()) {
            deleteExtractedFmuFolder().also {
                throw IllegalStateException("FMU is invalid, no $MODEL_DESC present!")
            }
        }

        synchronized(fmus) {
            fmus.add(this)
        }
    }

    val guid: String
        get() = modelDescription.guid

    val modelName: String
        get() = modelDescription.modelName

    /**
     * Get the content of the modelDescription.xml file as a String
     */
    val modelDescriptionXml: String
        get() = modelDescriptionFile.readText()

    val modelDescription: ModelDescriptionProvider by lazy {
        JaxbModelDescriptionParser.parse(modelDescriptionXml)
    }

    /**
     * Does the FMU support Co-simulation?
     */
    val supportsCoSimulation: Boolean
        get() = modelDescription.supportsCoSimulation

    /**
     * Does the FMU support Model Exchange?
     */
    val supportsModelExchange: Boolean
        get() = modelDescription.supportsModelExchange

    fun asCoSimulationFmu(): CoSimulationFmu {
        if (!supportsCoSimulation) {
            throw IllegalStateException("FMU does not support Co-simulation!")
        }
        return coSimulationFmu
    }

    /**
     * Get the file handle for the modelDescription.xml file
     */
    private val modelDescriptionFile: File
        get() = File(extractedFmu, MODEL_DESC)

    private val fmuPath: String
        get() = "file:///${extractedFmu.absolutePath.replace("\\", "/")}"

    /**
     * Get the absolute name of the native library on the form "C://folder/name.extension"
     */
    fun getAbsoluteLibraryPath(modelIdentifier: String): String {
        return File(extractedFmu, BINARIES_FOLDER + File.separator + OsUtil.libraryFolderName + OsUtil.platformBitness
                + File.separator + modelIdentifier + "." + OsUtil.libExtension).absolutePath
    }

    internal fun registerLibrary(library: Fmi1Library) {
        libraries.add(library)
    }

    internal fun registerInstance(instance: AbstractFmuInstance<*, *>) {
        instances.add(instance)
    }

    internal fun instantiate(modelDescription: CommonModelDescription, library: Fmi1Library, visible: Boolean, interactive: Boolean, loggingOn: Boolean): Long {
        LOG.trace("Calling instantiate: visible=$visible, interactive=$interactive, loggingOn=$loggingOn")

        return library.instantiate(modelDescription.attributes.modelIdentifier,
                modelDescription.guid, fmuPath, visible, interactive, loggingOn)
    }

    override fun close() {
        if (!isClosed.getAndSet(true)) {

            LOG.debug("Closing FMU '$extractedFmu'..")

            terminateInstances()
            disposeNativeLibraries()
            deleteExtractedFmuFolder()

            fmus.remove(this)
        }
    }

    private fun disposeNativeLibraries() {
        libraries.forEach {
            it.close()
        }
        libraries.clear()
        System.gc()
    }

    private fun terminateInstances() {
        instances.forEach { instance ->
            if (!instance.isTerminated) {
                instance.terminate()
            }
            if (!instance.wrapper.isInstanceFreed) {
                instance.wrapper.freeInstance()
            }
        }
        instances.clear()
    }

    /**
     * Deletes the temporary folder where the FMU was extracted
     */
    private fun deleteExtractedFmuFolder(): Boolean {

        if (extractedFmu.exists()) {
            return extractedFmu.deleteRecursively().also { success ->
                if (success) {
                    LOG.debug("Deleted extracted FMU contents: $extractedFmu")
                } else {
                    LOG.debug("Failed to delete extracted FMU contents: $extractedFmu")
                }
            }
        }
        return true
    }

    override fun toString(): String {
        return "Fmu(fmu=${extractedFmu.absolutePath})"
    }

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Fmu::class.java)

        private const val RESOURCES_FOLDER = "resources"
        private const val BINARIES_FOLDER = "binaries"

        private const val FMU_EXTENSION = "fmu"
        private const val FMI4J_FILE_PREFIX = "fmi4j_"

        private const val MODEL_DESC = "modelDescription.xml"

        private val fmus = Collections.synchronizedList(mutableListOf<Fmu>())

        init {
            Runtime.getRuntime().addShutdownHook(Thread {
                synchronized(fmus) {
                    fmus.toMutableList().forEach {
                        //mutableList because the list is modified during call
                        it?.close()
                    }
                }
            })
        }

        private fun createTempDir(fmuName: String): File {
            return Files.createTempDirectory(FMI4J_FILE_PREFIX + fmuName).toFile().also {
                File(it, RESOURCES_FOLDER).apply {
                    if (!exists()) {
                        mkdir()
                    }
                }
            }
        }

        /**
         * Creates an FMU from the provided File
         */
        @JvmStatic
        @Throws(IOException::class, FileNotFoundException::class)
        fun from(file: File): Fmu {

            val extension = file.extension.toLowerCase()
            if (extension != FMU_EXTENSION) {
                throw IllegalArgumentException("File '${file.absolutePath}' is not an FMU! Invalid extension found: .$extension")
            }

            if (!file.exists()) {
                throw FileNotFoundException("No such file: $file!")
            }

            return createTempDir(file.nameWithoutExtension).let { temp ->
                file.extractContentTo(temp)
                Fmu(file.nameWithoutExtension, temp)
            }

        }

        /**
         * Creates an FMU from the provided URL.
         */
        @JvmStatic
        @Throws(IOException::class)
        fun from(url: URL): Fmu {

            val extension = File(url.file).extension
            if (extension != FMU_EXTENSION) {
                throw IllegalArgumentException("URL '$url' does not point to an FMU! Invalid extension found: .$extension")
            }

            val fmuName = File(url.file).nameWithoutExtension
            return createTempDir(fmuName).let { temp ->
                url.extractContentTo(temp)
                Fmu(fmuName, temp)
            }
        }


        /**
         * Creates an FMU from the provided name and byte array.
         */
        @Throws(IOException::class)
        fun from(name: String, data: ByteArray): Fmu {
            return createTempDir(name).let { temp ->
                ByteArrayInputStream(data).extractContentTo(temp)
                Fmu(name, temp)
            }
        }

    }

}
