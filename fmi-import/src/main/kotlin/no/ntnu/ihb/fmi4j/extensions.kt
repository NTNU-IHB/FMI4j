@file:JvmName("Fmi4jVariableUtils")

/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.modeldescription.*
import no.ntnu.ihb.fmi4j.modeldescription.variables.*


fun VariableReader.readInteger(vr: ValueReference): IntegerRead {
    val values = IntArray(1)
    return readInteger(longArrayOf(vr), values).let {
        IntegerRead(values[0], it)
    }
}

fun SimpleModelInstance.readInteger(name: String): IntegerRead {
    return readInteger(modelVariables.getValueReference(name))
}

fun VariableReader.readReal(vr: ValueReference): RealRead {
    val values = RealArray(1)
    return readReal(longArrayOf(vr), values).let {
        RealRead(values[0], it)
    }
}

fun SimpleModelInstance.readReal(name: String): RealRead {
    return readReal(modelVariables.getValueReference(name))
}

fun VariableReader.readString(vr: ValueReference): StringRead {
    val values = StringArray(1) { "" }
    return this.readString(longArrayOf(vr), values).let {
        StringRead(values[0], it)
    }
}

fun SimpleModelInstance.readString(name: String): StringRead {
    return this.readString(modelVariables.getValueReference(name))
}

fun VariableReader.readBoolean(vr: ValueReference): BooleanRead {
    val values = BooleanArray(1)
    return readBoolean(longArrayOf(vr), values).let {
        BooleanRead(values[0], it)
    }
}

fun SimpleModelInstance.readBoolean(name: String): BooleanRead {
    return readBoolean(modelVariables.getValueReference(name))
}

/////////////////////////////////////////////////////////////////////////////

fun VariableWriter.writeInteger(vr: ValueReference, value: Int): FmiStatus {
    return this.writeInteger(longArrayOf(vr), intArrayOf(value))
}

fun SimpleModelInstance.writeInteger(name: String, value: Int): FmiStatus {
    return writeInteger(modelVariables.getValueReference(name), value)
}

fun VariableWriter.writeReal(vr: ValueReference, value: Real): FmiStatus {
    return writeReal(longArrayOf(vr), realArrayOf(value))
}

fun SimpleModelInstance.writeReal(name: String, value: Real): FmiStatus {
    return writeReal(modelVariables.getValueReference(name), value)
}

fun VariableWriter.writeString(vr: ValueReference, value: String): FmiStatus {
    return this.writeString(longArrayOf(vr), stringArrayOf(value))
}

fun SimpleModelInstance.writeString(name: String, value: String): FmiStatus {
    return writeString(modelVariables.getValueReference(name), value)
}

fun VariableWriter.writeBoolean(vr: ValueReference, value: Boolean): FmiStatus {
    return this.writeBoolean(longArrayOf(vr), booleanArrayOf(value))
}

fun SimpleModelInstance.writeBoolean(name: String, value: Boolean): FmiStatus {
    return writeBoolean(modelVariables.getValueReference(name), value)
}

/////////////////////////////////////////////////////////////////////////////////////

fun IntegerVariable.read(reader: VariableReader): VariableRead<Int> {
    return reader.readInteger(valueReference)
}

fun RealVariable.read(reader: VariableReader): VariableRead<Real> {
    return reader.readReal(valueReference)
}

fun StringVariable.read(reader: VariableReader): VariableRead<String> {
    return reader.readString(valueReference)
}

fun BooleanVariable.read(reader: VariableReader): VariableRead<Boolean> {
    return reader.readBoolean(valueReference)
}

fun EnumerationVariable.read(reader: VariableReader): VariableRead<Int> {
    return reader.readInteger(valueReference)
}

fun TypedScalarVariable<*>.read(reader: VariableReader): VariableRead<*> {

    return when (this) {
        is IntegerVariable -> asIntegerVariable().read(reader)
        is RealVariable -> asRealVariable().read(reader)
        is StringVariable -> asStringVariable().read(reader)
        is BooleanVariable -> asBooleanVariable().read(reader)
        is EnumerationVariable -> asEnumerationVariable().read(reader)
        else -> throw AssertionError("Internal error!")
    }

}

fun IntegerVariable.write(writer: VariableWriter, value: Int): FmiStatus {
    return writer.writeInteger(valueReference, value)
}

fun RealVariable.write(writer: VariableWriter, value: Real): FmiStatus {
    return writer.writeReal(valueReference, value)
}

fun StringVariable.write(writer: VariableWriter, value: String): FmiStatus {
    return writer.writeString(valueReference, value)
}

fun BooleanVariable.write(writer: VariableWriter, value: Boolean): FmiStatus {
    return writer.writeBoolean(valueReference, value)
}

fun EnumerationVariable.write(writer: VariableWriter, value: Int): FmiStatus {
    return writer.writeInteger(valueReference, value)
}

fun TypedScalarVariable<*>.write(writer: VariableWriter, value: Any): FmiStatus {

    return when (this) {
        is IntegerVariable -> asIntegerVariable().write(writer, value as Int)
        is RealVariable -> asRealVariable().write(writer, value as Real)
        is StringVariable -> asStringVariable().write(writer, value as String)
        is BooleanVariable -> asBooleanVariable().write(writer, value as Boolean)
        is EnumerationVariable -> asEnumerationVariable().write(writer, value as Int)
        else -> throw AssertionError("Internal error!")
    }

}
