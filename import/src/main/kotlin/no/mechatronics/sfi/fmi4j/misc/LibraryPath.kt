package no.mechatronics.sfi.fmi4j.misc

import com.sun.jna.Native
import org.slf4j.Logger
import org.slf4j.LoggerFactory


private const val LIBRARY_PATH = "jna.library.path"

class LibraryPath<E>(
        private val dir: String,
        private val name: String,
        private val type: Class<E>
) {

    var library: E? = null
    var isDisposed: Boolean = false
        private set

    private companion object {

        val LOG : Logger = LoggerFactory.getLogger(LibraryPath::class.java)

    }

    init {
        System.setProperty(LIBRARY_PATH,dir)
        library = Native.loadLibrary(name, type)
        LOG.debug("Loaded native library '{}'", name)
    }

    fun dispose() {
        if (!isDisposed) {
            library = null
            System.gc()
            isDisposed = true
        }
    }

}
