package no.mechatronics.sfi.fmi4j.misc

import no.mechatronics.sfi.fmi4j.proxy.Fmi2Library
import java.util.function.Supplier

class LibraryProvider<E: Fmi2Library> : Supplier<E> {

    private var library: E? = null

    constructor(library: E) {
        this.library = library
    }

    override fun get() : E = library!!

    fun disposeLibrary() {
        library = null
        System.gc()
    }

}