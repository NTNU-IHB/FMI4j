@file:JvmName("OSUtil")

package no.mechatronics.sfi.fmi4j.importer.misc

val currentOS: String by lazy {

    val os: String = System.getProperty("os.name")
    val arch: String = System.getProperty("sun.arch.data.model")

    when {
        os.contains("linux", true) -> "linux$arch"
        os.contains("win", true) -> "win$arch"
        else -> throw RuntimeException("Unsupported OS: $os")
    }

}