package no.mechatronics.sfi.fmi4j.importer.misc

/**
 * @author Lars Ivar Hatledal
 */
enum class FmiType(
        val code: Int
) {

    MODEL_EXCHANGE(0),
    CO_SIMULATION(1)

}

/**
 *
 * @author Lars Ivar Hatledal
 */
internal class ArrayBuffers {

    val vr = IntArray(1)
    val iv = IntArray(1)
    val rv = DoubleArray(1)
    val bv = BooleanArray(1)
    val sv = arrayOf("")

}