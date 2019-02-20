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

package no.ntnu.ihb.fmi4j.importer.jni

import no.ntnu.ihb.fmi4j.common.FmiStatus
import no.ntnu.ihb.fmi4j.common.ValueReferences
import no.ntnu.ihb.fmi4j.util.OsUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream

internal typealias NativeStatus = Int
internal typealias Fmi2Component = Long

/**
 * @author Lars Ivar Hatledal
 */
open class Fmi2Library(
        libName: String
) : Closeable {

    protected val p: Long = load(libName)
    private var isClosed = false

    override fun close() {
        if (!isClosed) {
            free(p).also {
                val msg = if (it) "successfully" else "unsuccessfully"
                LOG.debug("Freed native library resources $msg!")
            }
            isClosed = true
        }
    }

    protected fun finalize() {
        if (!isClosed) {
            LOG.warn("FMU was not closed prior do garbage collection! Doing it for you..")
            close()
        }
    }

    private external fun load(libName: String): Long

    private external fun free(p: Long): Boolean

    private external fun getVersion(p: Long): String

    private external fun getTypesPlatform(p: Long): String


    private external fun setDebugLogging(p: Long, c: Fmi2Component,
                                         loggingOn: Boolean, categories: Array<String>): NativeStatus

    private external fun setupExperiment(p: Long, c: Fmi2Component,
                                         tolerance: Double, startTime: Double, stopTime: Double): NativeStatus

    private external fun enterInitializationMode(p: Long, c: Fmi2Component): NativeStatus

    private external fun exitInitializationMode(p: Long, c: Fmi2Component): NativeStatus

    private external fun instantiate(p: Long, instanceName: String, type: Int, guid: String,
                                     resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long

    private external fun terminate(p: Long, c: Fmi2Component): NativeStatus

    private external fun reset(p: Long, c: Fmi2Component): NativeStatus

    private external fun freeInstance(p: Long, c: Fmi2Component)

    private external fun getDirectionalDerivative(p: Long, c: Fmi2Component,
                                                  vUnknown_ref: ValueReferences, vKnownRef: ValueReferences,
                                                  dvKnown: DoubleArray, dvUnknown: DoubleArray): NativeStatus


    private external fun getInteger(p: Long, c: Fmi2Component, vr: ValueReferences, ref: IntArray): NativeStatus
    private external fun getReal(p: Long, c: Fmi2Component, vr: ValueReferences, ref: DoubleArray): NativeStatus
    private external fun getString(p: Long, c: Fmi2Component, vr: ValueReferences, ref: Array<String>): NativeStatus
    private external fun getBoolean(p: Long, c: Fmi2Component, vr: ValueReferences, ref: BooleanArray): NativeStatus


    private external fun setInteger(p: Long, c: Fmi2Component, vr: ValueReferences, values: IntArray): NativeStatus
    private external fun setReal(p: Long, c: Fmi2Component, vr: ValueReferences, values: DoubleArray): NativeStatus
    private external fun setString(p: Long, c: Fmi2Component, vr: ValueReferences, values: Array<String>): NativeStatus
    private external fun setBoolean(p: Long, c: Fmi2Component, vr: ValueReferences, values: BooleanArray): NativeStatus


    private external fun getFMUstate(p: Long, c: Fmi2Component, state: LongByReference): NativeStatus
    private external fun setFMUstate(p: Long, c: Fmi2Component, state: Long): NativeStatus
    private external fun freeFMUstate(p: Long, c: Fmi2Component, state: Long): NativeStatus


    private external fun serializedFMUstateSize(
            p: Long, c: Fmi2Component,
            state: Long, size: IntByReference): NativeStatus

    private external fun serializeFMUstate(
            p: Long, c: Fmi2Component,
            state: Long, serializedState: ByteArray): NativeStatus

    private external fun deSerializeFMUstate(
            p: Long, c: Fmi2Component,
            state: LongByReference, serializedState: ByteArray): NativeStatus


    protected fun NativeStatus.transform(): FmiStatus {
        return FmiStatus.valueOf(this)
    }

    fun getVersion(): String {
        return getVersion(p)
    }

    fun getTypesPlatform(): String {
        return getTypesPlatform(p)
    }


    fun setDebugLogging(c: Fmi2Component, loggingOn: Boolean, categories: Array<String>): FmiStatus {
        return setDebugLogging(p, c, loggingOn, categories).transform()
    }

    fun setupExperiment(c: Fmi2Component,
                        tolerance: Double, startTime: Double, stopTime: Double): FmiStatus {
        return setupExperiment(p, c, tolerance, startTime, stopTime).transform()
    }

    fun enterInitializationMode(c: Fmi2Component): FmiStatus {
        return enterInitializationMode(p, c).transform()
    }

    fun exitInitializationMode(c: Fmi2Component): FmiStatus {
        return exitInitializationMode(p, c).transform()
    }

    fun instantiate(instanceName: String, type: Int, guid: String,
                    resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long {
        return instantiate(p, instanceName, type, guid, resourceLocation, visible, loggingOn)
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

    fun getDirectionalDerivative(c: Fmi2Component, vUnknown_ref: ValueReferences,
                                 vKnownRef: ValueReferences, dvKnown: DoubleArray, dvUnknown: DoubleArray): FmiStatus {
        return getDirectionalDerivative(p, c, vUnknown_ref, vKnownRef, dvKnown, dvUnknown).transform()
    }


    fun getInteger(c: Fmi2Component, vr: ValueReferences, ref: IntArray): FmiStatus {
        return getInteger(p, c, vr, ref).transform()
    }

    fun getReal(c: Fmi2Component, vr: ValueReferences, ref: DoubleArray): FmiStatus {
        return getReal(p, c, vr, ref).transform()
    }

    fun getString(c: Fmi2Component, vr: ValueReferences, ref: Array<String>): FmiStatus {
        return getString(p, c, vr, ref).transform()
    }

    fun getBoolean(c: Fmi2Component, vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return getBoolean(p, c, vr, ref).transform()
    }


    fun setInteger(c: Fmi2Component, vr: ValueReferences, values: IntArray): FmiStatus {
        return setInteger(p, c, vr, values).transform()
    }

    fun setReal(c: Fmi2Component, vr: ValueReferences, values: DoubleArray): FmiStatus {
        return setReal(p, c, vr, values).transform()
    }

    fun setString(c: Fmi2Component, vr: ValueReferences, values: Array<String>): FmiStatus {
        return setString(p, c, vr, values).transform()
    }

    fun setBoolean(c: Fmi2Component, vr: ValueReferences, values: BooleanArray): FmiStatus {
        return setBoolean(p, c, vr, values).transform()
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

        init {

            val fileName = "${OsUtil.libPrefix}fmi2_jni.${OsUtil.libExtension}"
            val copy = File(fileName).apply {
                deleteOnExit()
            }
            try {
                Fmi2Library::class.java.classLoader
                        .getResourceAsStream("native/fmi2/${OsUtil.currentOS}/$fileName").use { `is` ->
                            FileOutputStream(copy).use { fos ->
                                `is`.copyTo(fos)
                            }
                        }
                System.load(copy.absolutePath)
            } catch (ex: Exception) {
                copy.delete()
                throw RuntimeException(ex)
            }

        }

    }

}
