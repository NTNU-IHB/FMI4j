package no.mechatronics.sfi.fmi4j.misc

open class DirectionalDerivatives(
        size: Int
) {

    val vUnknown_ref: IntArray
    val vKnown_ref: IntArray
    val dvKnown: DoubleArray
    val dvUnknown: DoubleArray

    init {
        vUnknown_ref = IntArray(size)
        vKnown_ref = IntArray(size)
        dvKnown = DoubleArray(size)
        dvUnknown = DoubleArray(size)
    }

}