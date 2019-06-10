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

import no.ntnu.ihb.fmi4j.Model
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
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

    abstract fun asCoSimulationFmu(): Model

    abstract fun asModelExchangeFmu(): Model


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

    protected companion object {

        private val LOG: Logger = LoggerFactory.getLogger(AbstractFmu::class.java)

        const val FMU_EXTENSION = "fmu"
        private const val FMI4J_FILE_PREFIX = "fmi4j_"

        const val BINARIES_FOLDER = "binaries"
        const val MODEL_DESC = "modelDescription.xml"

        val fmus: MutableList<AbstractFmu> = Collections.synchronizedList(mutableListOf<AbstractFmu>())

        init {
            Runtime.getRuntime().addShutdownHook(Thread {
                synchronized(fmus) {
                    fmus.toMutableList().forEach {
                        //mutableList because the list is modified during call
                        it.close()
                    }
                }
            })
        }

        fun createTempDir(fmuName: String): File {
            return Files.createTempDirectory(FMI4J_FILE_PREFIX + fmuName).toFile()
        }

    }

}
