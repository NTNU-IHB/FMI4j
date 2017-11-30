package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status


class VariableWriter internal constructor(
        private val fmu: Fmu<*, *>,
        private val valueReference: Int
) {

    fun with(data: Int) : Fmi2Status {
        return fmu.setInteger(valueReference, data)
    }

    fun with(data: Double) : Fmi2Status {
        return fmu.setReal(valueReference, data)
    }

    fun with(data: String) : Fmi2Status {
        return fmu.setString(valueReference, data)
    }

    fun with(data: Boolean) : Fmi2Status {
        return fmu.setBoolean(valueReference, data)
    }

}

class VariablesWriter internal constructor(
        private val fmu: Fmu<*, *>,
        private val valueReference: IntArray
) {

    fun with(data: IntArray) {
        fmu.setInteger(valueReference, data)
    }

    fun with(data: DoubleArray) {
        fmu.setReal(valueReference, data)
    }

    fun with(data: Array<String>) {
        fmu.setString(valueReference, data)
    }

    fun with(data: BooleanArray) {
        fmu.setBoolean(valueReference, data)
    }

}

