//package no.mechatronics.sfi.fmi4j.misc
//
//import no.mechatronics.sfi.fmi4j.Fmi2Status
//import no.mechatronics.sfi.fmi4j.fmu.AbstractFmu
//import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
//
//interface SingleWrite {
//
//    fun with(value: Int): Fmi2Status
//    fun with(value: Double): Fmi2Status
//    fun with(value: String): Fmi2Status
//    fun with(value: Boolean): Fmi2Status
//
//}
//
//interface BulkWrite {
//
//    fun with(values: IntArray): Fmi2Status
//    fun with(values: DoubleArray): Fmi2Status
//    fun with(values: Array<String>): Fmi2Status
//    fun with(values: BooleanArray): Fmi2Status
//
//}
//
//interface VariableWriter {
//
//    fun write(valueReference: Int): SingleWrite
//    fun write(variableName: String): SingleWrite
//
//    fun write(valueReferences: IntArray): BulkWrite
//    fun write(valueReferences: Collection<Int>): BulkWrite
//    fun write(variableNames: Array<String>): BulkWrite
//
//}
//
//
//class VariableWriterImpl(
//        val fmu: AbstractFmu<*, *>
//): VariableWriter {
//
//    private val wrapper = fmu.wrapper
//    private val modelVariables = fmu.modelVariables
//
//    override fun write(valueReference: Int) = SingleWriteImpl(valueReference)
//    override fun write(variableName: String) = SingleWriteImpl(variableName)
//
//    override fun write(valueReferences: IntArray) = BulkWriteImpl(valueReferences)
//    override fun write(valueReferences: Collection<Int>) = BulkWriteImpl(valueReferences)
//    override fun write(variableNames: Array<String>) = BulkWriteImpl(variableNames)
//
//    inner class SingleWriteImpl(
//            private val valueReference: Int
//    ): SingleWrite{
//
//        constructor(variableName: String): this(modelVariables.getValueReference(variableName))
//
//        override fun with(value: Int): Fmi2Status {
//            val variable = modelVariables.getByValueReference(valueReference)
//            if (variable is IntegerVariable) {
//                return wrapper.setInteger(valueReference, value)
//            } else {
//                throw IllegalStateException("$variable is not of type Int!")
//            }
//        }
//
//        override fun with(value: Double): Fmi2Status {
//            val variable = modelVariables.getByValueReference(valueReference)
//            if (variable is RealVariable) {
//                return wrapper.setReal(valueReference, value)
//            } else {
//                throw IllegalStateException("$variable is not of type Real!")
//            }
//        }
//
//        override fun with(value: String): Fmi2Status {
//            val variable = modelVariables.getByValueReference(valueReference)
//            if (variable is StringVariable) {
//                return wrapper.setString(valueReference, value)
//            } else {
//                throw IllegalStateException("$variable is not of type String!")
//            }
//        }
//        override fun with(value: Boolean): Fmi2Status {
//            val variable = modelVariables.getByValueReference(valueReference)
//            if (variable is BooleanVariable) {
//                return wrapper.setBoolean(valueReference, value)
//            } else {
//                throw IllegalStateException("$variable is not of type Boolean!")
//            }
//        }
//
//    }
//
//    inner class BulkWriteImpl(
//            private val valueReferences: IntArray
//    ): BulkWrite {
//
//        constructor(valueReferences: Collection<Int>) : this (valueReferences.toIntArray())
//        constructor(variableName: Array<String>) : this(modelVariables.getValueReferences(variableName))
//
//        override fun with(values: IntArray) = wrapper.setInteger(valueReferences, values)
//        override fun with(values: DoubleArray) =  wrapper.setReal(valueReferences, values)
//        override fun with(values: Array<String>) = wrapper.setString(valueReferences, values)
//        override fun with(values: BooleanArray) = wrapper.setBoolean(valueReferences, values)
//
//    }
//
//}