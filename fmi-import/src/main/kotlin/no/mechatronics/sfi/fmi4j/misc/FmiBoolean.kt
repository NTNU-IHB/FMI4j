package no.mechatronics.sfi.fmi4j.misc

const val FmiTrue = 1
const val FmiFalse = 0

object FmiBoolean {

    fun convert(value: Boolean) = if (value) FmiTrue else FmiFalse
    fun convert(value: Int) = when (value) {
        FmiTrue -> true
        FmiFalse -> false
        else -> throw IllegalArgumentException()
    }

}