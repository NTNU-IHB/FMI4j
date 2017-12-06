package no.mechatronics.sfi.fmi4j.misc

internal class ArrayBuffers {

    val vr: IntArray by lazy {
        IntArray(1)
    }

    val iv: IntArray by lazy {
        IntArray(1)
    }

    val rv: DoubleArray by lazy {
        DoubleArray(1)
    }

    val sv: Array<String> by lazy {
        Array<String>(1, {""})
    }

    val bv: ByteArray by lazy {
        ByteArray(1)
    }

}