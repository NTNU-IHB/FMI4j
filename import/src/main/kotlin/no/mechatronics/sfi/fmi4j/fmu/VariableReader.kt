package no.mechatronics.sfi.fmi4j.fmu



class VariableReader internal constructor(
        private val fmu: Fmu<*, *>,
        private val valueReference: Int
) {

    fun asInteger() : Int = fmu.getInteger(valueReference)
    fun asReal() : Double = fmu.getReal(valueReference)
    fun asString() : String = fmu.getString(valueReference)
    fun asBoolean() : Boolean = fmu.getBoolean(valueReference)

}

class VariablesReader internal constructor(
        private val fmu: Fmu<*, *>,
        private val valueReference: IntArray
) {

    fun asInteger() : IntArray = fmu.getInteger(valueReference)
    fun asReal() : DoubleArray = fmu.getReal(valueReference)
    fun asString() : Array<String> = fmu.getString(valueReference)
    fun asBoolean() : BooleanArray = fmu.getBoolean(valueReference)

}