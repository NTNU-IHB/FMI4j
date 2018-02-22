//package no.mechatronics.sfi.fmi4j.misc
//
//import no.mechatronics.sfi.fmi4j.fmu.AbstractFmu
//import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
//
//interface SingleRead {
//
//    fun asInt(): Int
//    fun asReal(): Double
//    fun asString(): String
//    fun asBoolean(): Boolean
//
//}
//
//
//interface BulkRead {
//
//    fun asInt(): IntArray
//    fun asReal(): DoubleArray
//    fun asString(): Array<String>
//    fun asBoolean(): BooleanArray
//
//}
//
//interface VariableReader {
//
//    fun read(valueReference: Int): SingleRead
//    fun read(variableName: String): SingleRead
//
//    fun read(valueReferences: IntArray): BulkRead
//    fun read(valueReferences: Collection<Int>): BulkRead
//    fun read(variableNames: Array<String>): BulkRead
//
//}
//
//class VariableReaderImpl(
//        fmu: AbstractFmu<*, *>
//): VariableReader {
//
//    private val wrapper = fmu.wrapper
//    private val modelVariables = fmu.modelVariables
//
//    override fun read(valueReference: Int) = SingleReadImpl(valueReference)
//    override fun read(variableName: String) = SingleReadImpl(variableName)
//
//    override fun read(valueReferences: IntArray) = BulkReadImpl(valueReferences)
//    override fun read(valueReferences: Collection<Int>) = BulkReadImpl(valueReferences)
//    override fun read(variableNames: Array<String>) = BulkReadImpl(variableNames)
//
//    inner class SingleReadImpl(
//            private val valueReference: Int
//    ): SingleRead{
//
//        constructor(variableName: String) : this(modelVariables.getValueReference(variableName))
//
//        override fun asInt(): Int {
//            val variable = modelVariables.getByValueReference(valueReference)
//            if (variable is IntegerVariable) {
//                return wrapper.readInteger(valueReference)
//            } else {
//                throw IllegalStateException("$variable is not of type Int!")
//            }
//        }
//        override fun asReal(): Double {
//            val variable = modelVariables.getByValueReference(valueReference)
//            if (variable is RealVariable) {
//                return wrapper.readReal(valueReference)
//            } else {
//                throw IllegalStateException("$variable is not of type Real!")
//            }
//        }
//        override fun asString() : String {
//            val variable = modelVariables.getByValueReference(valueReference)
//            if (variable is StringVariable) {
//                return wrapper.readString(valueReference)
//            } else {
//                throw IllegalStateException("$variable is not of type String!")
//            }
//        }
//
//        override fun asBoolean(): Boolean {
//            val variable = modelVariables.getByValueReference(valueReference)
//            if (variable is BooleanVariable) {
//                return wrapper.readBoolean(valueReference)
//            } else {
//                throw IllegalStateException("$variable is not of type Boolean!")
//            }
//        }
//
//    }
//
//    inner class BulkReadImpl(
//            private val valueReferences: IntArray
//    ): BulkRead {
//
//        constructor(valueReferences: Collection<Int>) : this (valueReferences.toIntArray())
//        constructor(variableName: Array<String>) : this(modelVariables.getValueReferences(variableName))
//
//        override fun asInt() = wrapper.readInteger(valueReferences)
//        override fun asReal() = wrapper.readReal(valueReferences)
//        override fun asString() = wrapper.readString(valueReferences)
//        override fun asBoolean() = wrapper.readBoolean(valueReferences)
//
//    }
//
//}

