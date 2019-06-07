package no.ntnu.ihb.fmi4j.importer

import no.ntnu.ihb.fmi4j.modeldescription.ModelDescription
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

    abstract val modelDescription: ModelDescription

    /**
     * Get the content of the modelDescription.xml file as a String
     */
    val modelDescriptionXml: String
        get() = modelDescriptionFile.readText()

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


    protected abstract fun terminateInstances()

    protected abstract fun disposeNativeLibraries()

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