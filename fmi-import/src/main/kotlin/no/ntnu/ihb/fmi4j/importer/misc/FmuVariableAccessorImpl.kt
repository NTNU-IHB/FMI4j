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

package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.common.*
import java.util.*

/**
 * @author Lars Ivar Hatledal
 */
class FmuVariableAccessorImpl(
        private val accessor: FmuVariableAccessorLite,
        private val valueReferenceLocator: (String) -> ValueReference
): FmuVariableAccessor {

    override fun readInteger(name: String): FmuIntegerRead {
        return readInteger(getOrFindValueReference(name))
    }

    override fun readInteger(vr: ValueReference): FmuIntegerRead {
        val values = IntArray(1)
        return readInteger(longArrayOf(vr), values).let {
            FmuIntegerRead(values[0], it)
        }
    }

    override fun readInteger(vr: ValueReferences, value: IntArray): FmiStatus {
        return accessor.readInteger(vr, value)
    }

    override fun readReal(name: String): FmuRealRead {
        return readReal(getOrFindValueReference(name))
    }

    override fun readReal(vr: ValueReference): FmuRealRead {
        val values = RealArray(1)
        return readReal(longArrayOf(vr), values).let {
            FmuRealRead(values[0], it)
        }
    }

    override fun readReal(vr: ValueReferences, value: RealArray): FmiStatus {
        return accessor.readReal(vr, value)
    }

    override fun readString(name: String): FmuStringRead {
        return readString(getOrFindValueReference(name))
    }

    override fun readString(vr: ValueReference): FmuStringRead {
        val values = StringArray(1) {""}
        return readString(longArrayOf(vr), values).let {
            FmuStringRead(values[0], it)
        }
    }

    override fun readString(vr: ValueReferences, value: StringArray): FmiStatus {
        return accessor.readString(vr, value)
    }

    override fun readBoolean(name: String): FmuBooleanRead {
        return readBoolean(getOrFindValueReference(name))
    }

    override fun readBoolean(vr: ValueReference): FmuBooleanRead {
        val values = BooleanArray(1)
        return readBoolean(longArrayOf(vr), values).let {
            FmuBooleanRead(values[0], it)
        }
    }

    override fun readBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus {
        return accessor.readBoolean(vr, value)
    }

    override fun writeInteger(name: String, value: Int): FmiStatus {
        return writeInteger(getOrFindValueReference(name), value)
    }

    override fun writeInteger(vr: ValueReference, value: Int): FmiStatus {
        return writeInteger(longArrayOf(vr), intArrayOf(value))
    }

    override fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus {
        return accessor.writeInteger(vr, value)
    }

    override fun writeReal(name: String, value: Real): FmiStatus {
        return writeReal(getOrFindValueReference(name), value)
    }

    override fun writeReal(vr: ValueReference, value: Real): FmiStatus {
        return writeReal(longArrayOf(vr), doubleArrayOf(value))
    }

    override fun writeReal(vr: ValueReferences, value: RealArray): FmiStatus {
        return accessor.writeReal(vr, value)
    }

    override fun writeString(name: String, value: String): FmiStatus {
        return writeString(getOrFindValueReference(name), value)
    }

    override fun writeString(vr: ValueReference, value: String): FmiStatus {
        return writeString(longArrayOf(vr), StringArray(1) {value})
    }

    override fun writeString(vr: ValueReferences, value: StringArray): FmiStatus {
        return accessor.writeString(vr, value)
    }

    override fun writeBoolean(name: String, value: Boolean): FmiStatus {
        return writeBoolean(getOrFindValueReference(name), value)
    }

    override fun writeBoolean(vr: ValueReference, value: Boolean): FmiStatus {
        return writeBoolean(longArrayOf(vr), booleanArrayOf(value))
    }

    override fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus {
        return accessor.writeBoolean(vr, value)
    }

    private fun getOrFindValueReference(name: String): ValueReference {
        return cache.getOrPut(name) {
           valueReferenceLocator(name)
        }
    }

    private companion object {
        val cache: MutableMap<String, ValueReference> = Collections.synchronizedMap(mutableMapOf<String, ValueReference>())
            @Synchronized get

    }

}