package no.mechatronics.sfi.fmi4j.misc

const val Fmi2True = 1
const val Fmi2False = 0

object Fmi2Boolean {

    fun convert(value: Boolean) = if (value) Fmi2True else Fmi2False
    fun convert(value: Int) = when (value) {
        Fmi2True -> true
        Fmi2False -> false
        else -> throw IllegalArgumentException()
    }

}