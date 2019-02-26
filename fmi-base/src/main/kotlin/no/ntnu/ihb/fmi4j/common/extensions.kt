@file:JvmName("Fmi4jVariableUtils")

package no.ntnu.ihb.fmi4j.common

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
    val values = StringArray(1) {""}
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