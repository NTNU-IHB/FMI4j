/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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

package no.ntnu.ihb.fmi4j.importer

import no.ntnu.ihb.fmi4j.CoSimulationModel
import no.ntnu.ihb.fmi4j.ModelExchangeModel
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionProvider
import no.ntnu.ihb.fmi4j.modeldescription.util.FmiModelDescriptionUtil
import no.ntnu.ihb.fmi4j.util.extractContentTo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import java.net.URL
import java.nio.file.Files
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class AbstractFmu internal constructor(
    val name: String,
    protected val extractedFmu: File
) : Closeable {

    val guid: String
        get() = modelDescription.guid

    val modelName: String
        get() = modelDescription.modelName

    private var isClosed = AtomicBoolean(false)

    abstract val modelDescription: ModelDescriptionProvider

    /**
     * Get the content of the modelDescription.xml file as a String
     */
    val modelDescriptionXml: String
        get() = modelDescriptionFile.readText()

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

    /**
     * Get the file handle for the modelDescription.xml file
     */
    private val modelDescriptionFile: File
        get() = File(extractedFmu, MODEL_DESC)

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

    abstract fun asCoSimulationFmu(): CoSimulationModel

    abstract fun asModelExchangeFmu(): ModelExchangeModel


    protected abstract fun terminateInstances()

    protected abstract fun disposeNativeLibraries()

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


    override fun close() {
        if (!isClosed.getAndSet(true)) {

            LOG.debug("Closing FMU '$extractedFmu'..")

            terminateInstances()
            disposeNativeLibraries()
            deleteExtractedFmuFolder()

            fmus.remove(this)
        }
    }

    override fun toString(): String {
        return "Fmu(fmu=${extractedFmu.absolutePath})"
    }

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(AbstractFmu::class.java)

        private const val FMU_EXTENSION = "fmu"
        private const val FMI4J_FILE_PREFIX = "fmi4j_"

        internal const val BINARIES_FOLDER = "binaries"
        internal const val MODEL_DESC = "modelDescription.xml"

        internal val fmus: MutableList<AbstractFmu> = Collections.synchronizedList(mutableListOf<AbstractFmu>())

        init {
            Runtime.getRuntime().addShutdownHook(Thread {
                synchronized(fmus) {
                    fmus.toMutableList().forEach {
                        //mutableList because the list is modified during call
                        it.close()
                    }
                    fmus.clear()
                }
            })
        }

        private fun createTempDir(fmuName: String): File {
            return Files.createTempDirectory(FMI4J_FILE_PREFIX + fmuName).toFile()
        }

        private fun getModelDescriptionFileFromExtractedFmuDir(folder: File): File {
            return folder.listFiles()?.find {
                it.name == MODEL_DESC
            } ?: throw IllegalArgumentException("Folder '$folder' does not contain a file named '$MODEL_DESC'!")
        }

        /**
         * Creates an FMU from the provided File
         */
        @JvmStatic
        @Throws(IOException::class, FileNotFoundException::class)
        fun from(file: File): AbstractFmu {

            val extension = file.extension.lowercase(Locale.getDefault())
            require(extension == FMU_EXTENSION) { "File '${file.absolutePath}' is not an FMU! Invalid extension found: .$extension" }

            if (!file.exists()) {
                throw FileNotFoundException("No such file: '${file.absolutePath}'!")
            }

            return createTempDir(file.nameWithoutExtension).let { temp ->
                file.extractContentTo(temp)
                returnCorrectFmuType(file.nameWithoutExtension, temp)
            }

        }

        /**
         * Creates an FMU from the provided URL.
         */
        @JvmStatic
        @Throws(IOException::class)
        fun from(url: URL): AbstractFmu {

            val extension = File(url.file).extension
            require(extension == FMU_EXTENSION) { "URL '$url' does not point to an FMU! Invalid extension found: .$extension" }

            val fmuName = File(url.file).nameWithoutExtension
            return createTempDir(fmuName).let { temp ->
                url.extractContentTo(temp)
                returnCorrectFmuType(fmuName, temp)
            }
        }

        /**
         * Creates an FMU from the provided name and byte array.
         */
        @Throws(IOException::class)
        fun from(fmuName: String, data: ByteArray): AbstractFmu {
            return createTempDir(fmuName).let { temp ->
                ByteArrayInputStream(data).extractContentTo(temp)
                returnCorrectFmuType(fmuName, temp)
            }
        }

        private fun returnCorrectFmuType(fmuName: String, temp: File): AbstractFmu {
            return when (val version =
                FmiModelDescriptionUtil.extractVersion(getModelDescriptionFileFromExtractedFmuDir(temp).readText())) {
                "1.0" -> no.ntnu.ihb.fmi4j.importer.fmi1.Fmu(fmuName, temp)
                "2.0" -> no.ntnu.ihb.fmi4j.importer.fmi2.Fmu(fmuName, temp)
                else -> {
                    temp.deleteRecursively()
                    throw UnsupportedOperationException("Unsupported FMI version: '$version'")
                }
            }
        }
    }

}
