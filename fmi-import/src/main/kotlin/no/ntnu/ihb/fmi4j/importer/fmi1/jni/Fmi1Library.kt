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

package no.ntnu.ihb.fmi4j.importer.fmi1.jni

import no.ntnu.ihb.fmi4j.FMI4j
import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import java.nio.ByteBuffer

internal typealias NativeStatus = Int
internal typealias FmiComponent = Long

/**
 * @author Lars Ivar Hatledal
 */
abstract class Fmi1Library(
        lib: File,
        modelIdentifier: String
) : Closeable {

    internal lateinit var instanceName: String
    private var isClosed = false
    protected val p: Long = load(lib.parent, lib.name, modelIdentifier)

    override fun close() {
        if (!isClosed) {
            free(p).also {
                val msg = if (it) "successfully" else "unsuccessfully"
                LOG.debug("Freed native library resources $msg!")
            }
            isClosed = true
        }
    }

    private external fun load(dir: String, libName: String, modelIdentifier: String): Long

    private external fun free(p: Long): Boolean

    private external fun getVersion(p: Long): String

    private external fun getTypesPlatform(p: Long): String

    private external fun setDebugLogging(p: Long, c: FmiComponent, loggingOn: Boolean): FmiStatus

    private external fun getInteger(p: Long, c: FmiComponent, vr: ValueReferences, ref: IntArray): NativeStatus
    private external fun getReal(p: Long, c: FmiComponent, vr: ValueReferences, ref: DoubleArray): NativeStatus
    private external fun getRealDirect(p: Long, c: FmiComponent, size: Int, vr: ByteBuffer, ref: ByteBuffer): NativeStatus
    private external fun getString(p: Long, c: FmiComponent, vr: ValueReferences, ref: Array<String>): NativeStatus
    private external fun getBoolean(p: Long, c: FmiComponent, vr: ValueReferences, ref: BooleanArray): NativeStatus
    private external fun getAllVariables(
            p: Long, c: FmiComponent,
            intVr: ValueReferences, intRef: IntArray,
            realVr: ValueReferences, realRef: DoubleArray,
            strVr: ValueReferences, strRef: StringArray,
            boolVr: ValueReferences, boolRef: BooleanArray
    ): NativeStatus

    private external fun setInteger(p: Long, c: FmiComponent, vr: ValueReferences, values: IntArray): NativeStatus
    private external fun setReal(p: Long, c: FmiComponent, vr: ValueReferences, values: DoubleArray): NativeStatus
    private external fun setRealDirect(p: Long, c: FmiComponent, size: Int, vr: ByteBuffer, values: ByteBuffer): NativeStatus
    private external fun setString(p: Long, c: FmiComponent, vr: ValueReferences, values: Array<String>): NativeStatus
    private external fun setBoolean(p: Long, c: FmiComponent, vr: ValueReferences, values: BooleanArray): NativeStatus
    private external fun setAllVariables(
            p: Long, c: FmiComponent,
            intVr: ValueReferences, intValues: IntArray,
            realVr: ValueReferences, realValues: DoubleArray,
            strVr: ValueReferences, strValues: StringArray,
            boolVr: ValueReferences, boolValues: BooleanArray
    ): NativeStatus

    protected fun NativeStatus.transform(): FmiStatus {
        return FmiStatus.valueOf(this)
    }

    fun getVersion(): String {
        return getVersion(p)
    }

    fun getTypesPlatform(): String {
        return getTypesPlatform(p)
    }

    fun setDebugLogging(c: FmiComponent, loggingOn: Boolean): FmiStatus {
        return setDebugLogging(p, c, loggingOn)
    }

    abstract fun terminate(c: FmiComponent): FmiStatus

    abstract fun freeInstance(c: FmiComponent)

    fun getInteger(c: FmiComponent, vr: ValueReferences, ref: IntArray): FmiStatus {
        return getInteger(p, c, vr, ref).transform()
    }

    fun getReal(c: FmiComponent, vr: ValueReferences, ref: DoubleArray): FmiStatus {
        return getReal(p, c, vr, ref).transform()
    }

    fun getRealDirect(c: FmiComponent, vr: ByteBuffer, ref: ByteBuffer): FmiStatus {
        val size = vr.capacity() / Long.SIZE_BYTES
        return getRealDirect(p, c, size, vr, ref).transform()
    }

    fun getString(c: FmiComponent, vr: ValueReferences, ref: Array<String>): FmiStatus {
        return getString(p, c, vr, ref).transform()
    }

    fun getBoolean(c: FmiComponent, vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return getBoolean(p, c, vr, ref).transform()
    }

    fun getAllVariables(c: FmiComponent,
                        intVr: ValueReferences, intRefs: IntArray,
                        realVr: ValueReferences, realRefs: DoubleArray,
                        strVr: ValueReferences, strRefs: StringArray,
                        boolVr: ValueReferences, boolRefs: BooleanArray
    ): FmiStatus {
        return getAllVariables(c, p, intVr, intRefs, realVr, realRefs, strVr, strRefs, boolVr, boolRefs).transform()
    }

    fun setInteger(c: FmiComponent, vr: ValueReferences, values: IntArray): FmiStatus {
        return setInteger(p, c, vr, values).transform()
    }

    fun setReal(c: FmiComponent, vr: ValueReferences, values: DoubleArray): FmiStatus {
        return setReal(p, c, vr, values).transform()
    }

    fun setRealDirect(c: FmiComponent, vr: ByteBuffer, values: ByteBuffer): FmiStatus {
        val size = vr.capacity() / Long.SIZE_BYTES
        return setRealDirect(p, c, size, vr, values).transform()
    }

    fun setString(c: FmiComponent, vr: ValueReferences, values: Array<String>): FmiStatus {
        return setString(p, c, vr, values).transform()
    }

    fun setBoolean(c: FmiComponent, vr: ValueReferences, values: BooleanArray): FmiStatus {
        return setBoolean(p, c, vr, values).transform()
    }

    fun setAllVariables(c: FmiComponent,
                        intVr: ValueReferences, intValues: IntArray,
                        realVr: ValueReferences, realValues: DoubleArray,
                        strVr: ValueReferences, strValues: StringArray,
                        boolVr: ValueReferences, boolValues: BooleanArray
    ): FmiStatus {
        return setAllVariables(c, p, intVr, intValues, realVr, realValues, strVr, strValues, boolVr, boolValues).transform()
    }


    private companion object {

        val LOG: Logger = LoggerFactory.getLogger(Fmi1Library::class.java)

        init {
            FMI4j.init()
        }

    }

}

