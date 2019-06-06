package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.importer.fmi1.jni.Fmi1Library
import no.ntnu.ihb.fmi4j.util.OsUtil
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicBoolean

object FMI4j {

    val version = "1.20.0"

    private val initialized = AtomicBoolean(false)

    internal fun init() {
        if (!initialized.getAndSet(true)) {
            val fileName = "${OsUtil.libPrefix}fmi4j.${OsUtil.libExtension}"
            val copy = File(fileName).apply {
                deleteOnExit()
            }
            try {
                Fmi1Library::class.java.classLoader
                        .getResourceAsStream("native/fmi/${OsUtil.currentOS}/$fileName").use { `is` ->
                            FileOutputStream(copy).use { fos ->
                                `is`.copyTo(fos)
                            }
                        }
                System.load(copy.absolutePath)
            } catch (ex: Exception) {
                copy.delete()
                throw RuntimeException(ex)
            }
        }
    }

}
