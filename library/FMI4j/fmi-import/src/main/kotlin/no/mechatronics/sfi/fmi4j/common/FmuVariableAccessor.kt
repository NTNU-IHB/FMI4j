/*
 * The MIT License
 *
 * Copyright 2017-2018 Norwegian University of Technology
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

@file:Suppress("NAME_SHADOWING")

package no.mechatronics.sfi.fmi4j.common

import no.mechatronics.sfi.fmi4j.modeldescription.variables.TypedScalarVariable
import java.util.*

/**
 * @author Lars Ivar Hatledal
 */
interface FmuVariableAccessor {

    fun getVariableByName(name: String): TypedScalarVariable<*>

    @JvmDefault
    fun readInteger(name: String) = readInteger(getOrFindValueReference(name))
    @JvmDefault
    fun readInteger(vr: ValueReference): FmuIntegerRead {
        val vr = longArrayOf(vr)
        val values = IntArray(1)
        return readInteger(vr, values).let {
            FmuIntegerRead(values[0], it)
        }
    }
    fun readInteger(vr: ValueReferences, value: IntArray): FmiStatus

    @JvmDefault
    fun readReal(name: String) = readReal(getOrFindValueReference(name))
    @JvmDefault
    fun readReal(vr: ValueReference): FmuRealRead {
        val vr = longArrayOf(vr)
        val values = RealArray(1)
        return readReal(vr, values).let {
            FmuRealRead(values[0], it)
        }
    }
    fun readReal(vr: ValueReferences, value: RealArray): FmiStatus

    @JvmDefault
    fun readString(name: String) = readString(getOrFindValueReference(name))
    @JvmDefault
    fun readString(vr: ValueReference): FmuStringRead {
        val vr = longArrayOf(vr)
        val values = StringArray(1) {""}
        return readString(vr, values).let {
            FmuStringRead(values[0], it)
        }
    }
    fun readString(vr: ValueReferences, value: StringArray): FmiStatus

    @JvmDefault
    fun readBoolean(name: String) = readBoolean(getOrFindValueReference(name))
    @JvmDefault
    fun readBoolean(vr: ValueReference): FmuBooleanRead {
        val vr = longArrayOf(vr)
        val values = BooleanArray(1)
        return readBoolean(vr, values).let {
            FmuBooleanRead(values[0], it)
        }
    }
    fun readBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus

    @JvmDefault
    fun writeInteger(name: String, value: Int) = writeInteger(getOrFindValueReference(name), value)
    @JvmDefault
    fun writeInteger(vr: ValueReference, value: Int): FmiStatus {
        return writeInteger(longArrayOf(vr), intArrayOf(value))
    }
    fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus

    @JvmDefault
    fun writeReal(name: String, value: Real) = writeReal(getOrFindValueReference(name), value)
    @JvmDefault
    fun writeReal(vr: ValueReference, value: Real): FmiStatus {
        return writeReal(longArrayOf(vr), doubleArrayOf(value))
    }
    fun writeReal(vr: ValueReferences, value: RealArray): FmiStatus

    @JvmDefault
    fun writeString(name: String, value: String) = writeString(getOrFindValueReference(name), value)
    @JvmDefault
    fun writeString(vr: ValueReference, value: String): FmiStatus {
        return writeString(longArrayOf(vr), StringArray(1) {value})
    }
    fun writeString(vr: ValueReferences, value: StringArray): FmiStatus

    @JvmDefault
    fun writeBoolean(name: String, value: Boolean) = writeBoolean(getOrFindValueReference(name), value)
    @JvmDefault
    fun writeBoolean(vr: ValueReference, value: Boolean): FmiStatus {
        return writeBoolean(longArrayOf(vr), booleanArrayOf(value))
    }
    fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus

    private fun getOrFindValueReference(name: String): ValueReference {
        return cache.getOrPut(name) {
            getVariableByName(name).valueReference
        }
    }

    private companion object {
        val cache: MutableMap<String, ValueReference> = Collections.synchronizedMap(mutableMapOf<String, ValueReference>())
            @Synchronized get

    }

}