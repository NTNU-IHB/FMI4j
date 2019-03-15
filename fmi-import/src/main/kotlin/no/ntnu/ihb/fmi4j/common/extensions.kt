@file:JvmName("Fmi4jVariableUtils")

package no.ntnu.ihb.fmi4j.common

import no.ntnu.ihb.fmi4j.modeldescription.*
import no.ntnu.ihb.fmi4j.modeldescription.variables.*


fun FmuVariableReader.readInteger(vr: ValueReference): FmuIntegerRead {
    val values = IntArray(1)
    return readInteger(longArrayOf(vr), values).let {
        FmuIntegerRead(values[0], it)
    }
}

fun SimpleFmuInstance.readInteger(name: String): FmuIntegerRead {
    return readInteger(modelVariables.getValueReference(name))
}

fun FmuVariableReader.readReal(vr: ValueReference): FmuRealRead {
    val values = RealArray(1)
    return readReal(longArrayOf(vr), values).let {
        FmuRealRead(values[0], it)
    }
}

fun SimpleFmuInstance.readReal(name: String): FmuRealRead {
    return readReal(modelVariables.getValueReference(name))
}

fun FmuVariableReader.readString(vr: ValueReference): FmuStringRead {
    val values = StringArray(1) { "" }
    return readString(longArrayOf(vr), values).let {
        FmuStringRead(values[0], it)
    }
}

fun SimpleFmuInstance.readString(name: String): FmuStringRead {
    return readString(modelVariables.getValueReference(name))
}

fun FmuVariableReader.readBoolean(vr: ValueReference): FmuBooleanRead {
    val values = BooleanArray(1)
    return readBoolean(longArrayOf(vr), values).let {
        FmuBooleanRead(values[0], it)
    }
}

fun SimpleFmuInstance.readBoolean(name: String): FmuBooleanRead {
    return readBoolean(modelVariables.getValueReference(name))
}


fun FmuVariableWriter.writeInteger(vr: ValueReference, value: Int): FmiStatus {
    return writeInteger(longArrayOf(vr), intArrayOf(value))
}

fun FmuVariableWriter.writeReal(vr: ValueReference, value: Real): FmiStatus {
    return writeReal(longArrayOf(vr), realArrayOf(value))
}

fun FmuVariableWriter.writeString(vr: ValueReference, value: String): FmiStatus {
    return writeString(longArrayOf(vr), stringArrayOf(value))
}

fun FmuVariableWriter.writeBoolean(vr: ValueReference, value: Boolean): FmiStatus {
    return writeBoolean(longArrayOf(vr), booleanArrayOf(value))
}

fun IntegerVariable.read(reader: FmuVariableReader): FmuRead<Int> {
    return reader.readInteger(valueReference)
}

fun RealVariable.read(reader: FmuVariableReader): FmuRead<Real> {
    return reader.readReal(valueReference)
}

fun StringVariable.read(reader: FmuVariableReader): FmuRead<String> {
    return reader.readString(valueReference)
}

fun BooleanVariable.read(reader: FmuVariableReader): FmuRead<Boolean> {
    return reader.readBoolean(valueReference)
}

fun EnumerationVariable.read(reader: FmuVariableReader): FmuRead<Int> {
    return reader.readInteger(valueReference)
}

fun TypedScalarVariable<*>.read(reader: FmuVariableReader): FmuRead<*> {

    return when (this) {
        is IntegerVariable -> asIntegerVariable().read(reader)
        is RealVariable -> asRealVariable().read(reader)
        is StringVariable -> asStringVariable().read(reader)
        is BooleanVariable -> asBooleanVariable().read(reader)
        is EnumerationVariable -> asEnumerationVariable().read(reader)
        else -> throw AssertionError("Internal error!")
    }

}

fun IntegerVariable.write(writer: FmuVariableWriter, value: Int): FmiStatus {
    return writer.writeInteger(valueReference, value)
}

fun RealVariable.write(writer: FmuVariableWriter, value: Real): FmiStatus {
    return writer.writeReal(valueReference, value)
}

fun StringVariable.write(writer: FmuVariableWriter, value: String): FmiStatus {
    return writer.writeString(valueReference, value)
}

fun BooleanVariable.write(writer: FmuVariableWriter, value: Boolean): FmiStatus {
    return writer.writeBoolean(valueReference, value)
}

fun EnumerationVariable.write(writer: FmuVariableWriter, value: Int): FmiStatus {
    return writer.writeInteger(valueReference, value)
}

fun TypedScalarVariable<*>.write(writer: FmuVariableWriter, value: Any): FmiStatus {

    return when (this) {
        is IntegerVariable -> asIntegerVariable().write(writer, value as Int)
        is RealVariable -> asRealVariable().write(writer, value as Real)
        is StringVariable -> asStringVariable().write(writer, value as String)
        is BooleanVariable -> asBooleanVariable().write(writer, value as Boolean)
        is EnumerationVariable -> asEnumerationVariable().write(writer, value as Int)
        else -> throw AssertionError("Internal error!")
    }

}