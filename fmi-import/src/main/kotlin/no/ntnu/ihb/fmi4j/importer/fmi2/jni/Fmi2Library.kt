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

package no.ntnu.ihb.fmi4j.importer.fmi2.jni

import no.ntnu.ihb.fmi4j.FMI4j
import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.FmiComponent
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.modeldescription.stringArrayOf
import no.ntnu.ihb.fmi4j.util.IntByReference
import no.ntnu.ihb.fmi4j.util.LongByReference
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import java.nio.ByteBuffer

internal typealias NativeStatus = Int
internal typealias Fmi2Component = Long

/**
 * @author Lars Ivar Hatledal
 */
open class Fmi2Library(
    lib: File
) : Closeable {

    private var isClosed = false
    internal val instanceNames: MutableMap<Long, String> = mutableMapOf()
    protected val p: Long = load(lib.parent, lib.name)

    override fun close() {
        if (!isClosed) {
            free(p).also {
                val msg = if (it) "successfully" else "unsuccessfully"
                LOG.debug("Freed native library resources $msg!")
            }
            isClosed = true
        }
    }

    private external fun load(dir: String, libName: String): Long
    private external fun free(p: Long): Boolean

    private external fun getVersion(p: Long): String
    private external fun getTypesPlatform(p: Long): String

    private external fun instantiate(
        p: Long, instanceName: String, type: Int, guid: String,
        resourceLocation: String, visible: Boolean, loggingOn: Boolean
    ): Long

    private external fun setDebugLogging(
        p: Long, c: Fmi2Component,
        loggingOn: Boolean, categories: Array<String>
    ): NativeStatus

    private external fun setupExperiment(
        p: Long, c: Fmi2Component,
        tolerance: Double, startTime: Double, stopTime: Double
    ): NativeStatus

    private external fun enterInitializationMode(p: Long, c: Fmi2Component): NativeStatus
    private external fun exitInitializationMode(p: Long, c: Fmi2Component): NativeStatus

    private external fun reset(p: Long, c: Fmi2Component): NativeStatus
    private external fun terminate(p: Long, c: Fmi2Component): NativeStatus
    private external fun freeInstance(p: Long, c: Fmi2Component)

    private external fun getDirectionalDerivative(
        p: Long, c: Fmi2Component,
        vUnknown_ref: ValueReferences, vKnownRef: ValueReferences,
        dvKnown: DoubleArray, dvUnknown: DoubleArray
    ): NativeStatus


    private external fun getInteger(
        p: Long, c: Fmi2Component,
        vr: ValueReferences, nvr: Int, ref: IntArray
    ): NativeStatus

    private external fun getReal(
        p: Long, c: Fmi2Component,
        vr: ValueReferences, nvr: Int, ref: DoubleArray
    ): NativeStatus

    private external fun getRealDirect(
        p: Long, c: Fmi2Component,
        vr: ByteBuffer, nvr: Int, ref: ByteBuffer
    ): NativeStatus

    private external fun getString(
        p: Long, c: Fmi2Component,
        vr: ValueReferences, nvr: Int, ref: Array<String>
    ): NativeStatus

    private external fun getBoolean(
        p: Long, c: Fmi2Component,
        vr: ValueReferences, nvr: Int, ref: BooleanArray
    ): NativeStatus

    private external fun getAll(
        p: Long, c: FmiComponent,
        intVr: ValueReferences, nIntvr: Int, intRef: IntArray,
        realVr: ValueReferences, nRealVr: Int, realRef: DoubleArray,
        boolVr: ValueReferences, nBoolVr: Int, boolRef: BooleanArray,
        strVr: ValueReferences, nStrvr: Int, strRef: StringArray
    ): NativeStatus


    private external fun setInteger(
        p: Long, c: Fmi2Component,
        vr: ValueReferences, nvr: Int, values: IntArray
    ): NativeStatus

    private external fun setReal(
        p: Long, c: Fmi2Component,
        vr: ValueReferences, nvr: Int, values: DoubleArray
    ): NativeStatus

    private external fun setRealDirect(
        p: Long, c: Fmi2Component,
        vr: ByteBuffer, nvr: Int, values: ByteBuffer
    ): NativeStatus

    private external fun setString(
        p: Long, c: Fmi2Component,
        vr: ValueReferences, nvr: Int, values: Array<String>
    ): NativeStatus

    private external fun setBoolean(
        p: Long, c: Fmi2Component,
        vr: ValueReferences, nvr: Int, values: BooleanArray
    ): NativeStatus

    private external fun setAll(
        p: Long, c: FmiComponent,
        intVr: ValueReferences, nIntvr: Int, intValues: IntArray,
        realVr: ValueReferences, nRealVr: Int, realValues: DoubleArray,
        boolVr: ValueReferences, nBoolVr: Int, boolValues: BooleanArray,
        strVr: ValueReferences, nStrVr: Int, strValues: StringArray
    ): NativeStatus

    private external fun getFMUstate(p: Long, c: Fmi2Component, state: LongByReference): NativeStatus
    private external fun setFMUstate(p: Long, c: Fmi2Component, state: Long): NativeStatus
    private external fun freeFMUstate(p: Long, c: Fmi2Component, state: Long): NativeStatus


    private external fun serializedFMUstateSize(
        p: Long, c: Fmi2Component,
        state: Long, size: IntByReference
    ): NativeStatus

    private external fun serializeFMUstate(
        p: Long, c: Fmi2Component,
        state: Long, serializedState: ByteArray
    ): NativeStatus

    private external fun deSerializeFMUstate(
        p: Long, c: Fmi2Component,
        state: LongByReference, serializedState: ByteArray
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

    fun instantiate(
        instanceName: String, type: Int, guid: String,
        resourceLocation: String, visible: Boolean, loggingOn: Boolean
    ): Long {
        return instantiate(p, instanceName, type, guid, resourceLocation, visible, loggingOn).also {
            instanceNames[it] = instanceName
        }
    }

    fun setDebugLogging(c: Fmi2Component, loggingOn: Boolean, categories: Array<String>): FmiStatus {
        return setDebugLogging(p, c, loggingOn, categories).transform()
    }

    fun setupExperiment(
        c: Fmi2Component,
        tolerance: Double, startTime: Double, stopTime: Double
    ): FmiStatus {
        return setupExperiment(p, c, tolerance, startTime, stopTime).transform()
    }

    fun enterInitializationMode(c: Fmi2Component): FmiStatus {
        return enterInitializationMode(p, c).transform()
    }

    fun exitInitializationMode(c: Fmi2Component): FmiStatus {
        return exitInitializationMode(p, c).transform()
    }

    fun terminate(c: Fmi2Component): FmiStatus {
        return terminate(p, c).transform()
    }

    fun reset(c: Fmi2Component): FmiStatus {
        return reset(p, c).transform()
    }

    fun freeInstance(c: Fmi2Component) {
        freeInstance(p, c)
    }

    fun getDirectionalDerivative(
        c: Fmi2Component,
        vUnknown_ref: ValueReferences, vKnownRef: ValueReferences,
        dvKnown: DoubleArray, dvUnknown: DoubleArray
    ): FmiStatus {
        return getDirectionalDerivative(p, c, vUnknown_ref, vKnownRef, dvKnown, dvUnknown).transform()
    }


    fun getInteger(c: Fmi2Component, vr: ValueReferences, ref: IntArray): FmiStatus {
        return getInteger(p, c, vr, vr.size, ref).transform()
    }

    fun getReal(c: Fmi2Component, vr: ValueReferences, ref: DoubleArray): FmiStatus {
        return getReal(p, c, vr, vr.size, ref).transform()
    }

    fun getRealDirect(c: Fmi2Component, vr: ByteBuffer, ref: ByteBuffer): FmiStatus {
        val size = vr.capacity() / Long.SIZE_BYTES
        return getRealDirect(p, c, vr, size, ref).transform()
    }

    fun getString(c: Fmi2Component, vr: ValueReferences, ref: Array<String>): FmiStatus {
        return getString(p, c, vr, vr.size, ref).transform()
    }

    fun getBoolean(c: Fmi2Component, vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return getBoolean(p, c, vr, vr.size, ref).transform()
    }

    fun getAll(
        c: Fmi2Component,
        intVr: ValueReferences?, intRefs: IntArray?,
        realVr: ValueReferences?, realRefs: DoubleArray?,
        boolVr: ValueReferences?, boolRefs: BooleanArray?,
        strVr: ValueReferences?, strRefs: StringArray?
    ): FmiStatus {
        return getAll(
            p, c,
            intVr ?: emptyLongArray, intVr?.size ?: 0, intRefs ?: emptyIntArray,
            realVr ?: emptyLongArray, realVr?.size ?: 0, realRefs ?: emptyDoubleArray,
            boolVr ?: emptyLongArray, boolVr?.size ?: 0, boolRefs ?: emptyBooleanArray,
            strVr ?: emptyLongArray, strVr?.size ?: 0, strRefs ?: emptyStringArray
        ).transform()
    }


    fun setInteger(c: Fmi2Component, vr: ValueReferences, values: IntArray): FmiStatus {
        return setInteger(p, c, vr, vr.size, values).transform()
    }

    fun setReal(c: Fmi2Component, vr: ValueReferences, values: DoubleArray): FmiStatus {
        return setReal(p, c, vr, vr.size, values).transform()
    }

    fun setRealDirect(c: Fmi2Component, vr: ByteBuffer, values: ByteBuffer): FmiStatus {
        val size = vr.capacity() / Long.SIZE_BYTES
        return setRealDirect(p, c, vr, size, values).transform()
    }

    fun setString(c: Fmi2Component, vr: ValueReferences, values: Array<String>): FmiStatus {
        return setString(p, c, vr, vr.size, values).transform()
    }

    fun setBoolean(c: Fmi2Component, vr: ValueReferences, values: BooleanArray): FmiStatus {
        return setBoolean(p, c, vr, vr.size, values).transform()
    }

    fun setAll(
        c: Fmi2Component,
        intVr: ValueReferences?, intValues: IntArray?,
        realVr: ValueReferences?, realValues: DoubleArray?,
        boolVr: ValueReferences?, boolValues: BooleanArray?,
        strVr: ValueReferences?, strValues: StringArray?
    ): FmiStatus {
        return setAll(
            p, c,
            intVr ?: emptyLongArray, intVr?.size ?: 0, intValues ?: emptyIntArray,
            realVr ?: emptyLongArray, realVr?.size ?: 0, realValues ?: emptyDoubleArray,
            boolVr ?: emptyLongArray, boolVr?.size ?: 0, boolValues ?: emptyBooleanArray,
            strVr ?: emptyLongArray, strVr?.size ?: 0, strValues ?: emptyStringArray
        ).transform()
    }


    fun getFMUstate(c: Fmi2Component, state: LongByReference): FmiStatus {
        return getFMUstate(p, c, state).transform()
    }

    fun setFMUstate(c: Fmi2Component, state: Long): FmiStatus {
        return setFMUstate(p, c, state).transform()
    }

    fun freeFMUstate(c: Fmi2Component, state: Long): FmiStatus {
        return freeFMUstate(p, c, state).transform()
    }


    fun serializedFMUstateSize(c: Fmi2Component, state: Long, size: IntByReference): FmiStatus {
        return serializedFMUstateSize(p, c, state, size).transform()
    }

    fun serializeFMUstate(c: Fmi2Component, state: Long, serializedState: ByteArray): FmiStatus {
        return serializeFMUstate(p, c, state, serializedState).transform()
    }

    fun deSerializeFMUstate(c: Fmi2Component, state: LongByReference, serializedState: ByteArray): FmiStatus {
        return deSerializeFMUstate(p, c, state, serializedState).transform()
    }


    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Fmi2Library::class.java)

        private val emptyLongArray = longArrayOf()
        private val emptyIntArray = intArrayOf()
        private val emptyDoubleArray = doubleArrayOf()
        private val emptyBooleanArray = booleanArrayOf()
        private val emptyStringArray = stringArrayOf()

        init {
            FMI4j.init()
        }

    }

}
