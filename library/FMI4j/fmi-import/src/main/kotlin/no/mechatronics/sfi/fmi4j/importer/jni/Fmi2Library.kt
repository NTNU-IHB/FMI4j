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

package no.mechatronics.sfi.fmi4j.importer.jni

import no.mechatronics.sfi.fmi4j.common.currentOS
import no.mechatronics.sfi.fmi4j.common.libExtension
import no.mechatronics.sfi.fmi4j.common.libPrefix
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

    protected val p = load(libName)
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

    private external fun setupExperiment(p: Long, c: Fmi2Component, toleranceDefined: Boolean,
                                         tolerance: Double, startTime: Double, stopTime: Double): NativeStatus

    private external fun enterInitializationMode(p: Long, c: Fmi2Component): NativeStatus

    private external fun exitInitializationMode(p: Long, c: Fmi2Component): NativeStatus

    private external fun instantiate(p: Long, instanceName: String, type: Int, guid: String,
                                     resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long

    private external fun terminate(p: Long, c: Fmi2Component): NativeStatus

    private external fun reset(p: Long, c: Fmi2Component): NativeStatus

    private external fun freeInstance(p: Long, c: Fmi2Component)

    private external fun getDirectionalDerivative(p: Long, c: Fmi2Component,
                                                  vUnknown_ref: IntArray, vKnownRef: IntArray,
                                                  dvKnown: DoubleArray, dvUnknown: DoubleArray): NativeStatus


    private external fun getInteger(p: Long, c: Fmi2Component, vr: IntArray, ref: IntArray): NativeStatus
    private external fun getReal(p: Long, c: Fmi2Component, vr: IntArray, ref: DoubleArray): NativeStatus
    private external fun getString(p: Long, c: Fmi2Component, vr: IntArray, ref: Array<String>): NativeStatus
    private external fun getBoolean(p: Long, c: Fmi2Component, vr: IntArray, ref: BooleanArray): NativeStatus


    private external fun setInteger(p: Long, c: Fmi2Component, vr: IntArray, values: IntArray): NativeStatus
    private external fun setReal(p: Long, c: Fmi2Component, vr: IntArray, values: DoubleArray): NativeStatus
    private external fun setString(p: Long, c: Fmi2Component, vr: IntArray, values: Array<String>): NativeStatus
    private external fun setBoolean(p: Long, c: Fmi2Component, vr: IntArray, values: BooleanArray): NativeStatus


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


    fun getVersion() = getVersion(p)

    fun getTypesPlatform() = getTypesPlatform(p)


    fun setDebugLogging(c: Fmi2Component, loggingOn: Boolean, categories: Array<String>) = setDebugLogging(p, c, loggingOn, categories)

    fun setupExperiment(c: Fmi2Component, toleranceDefined: Boolean,
                        tolerance: Double, startTime: Double, stopTime: Double) = setupExperiment(p, c, toleranceDefined, tolerance, startTime, stopTime)

    fun enterInitializationMode(c: Fmi2Component) = enterInitializationMode(p, c)

    fun exitInitializationMode(c: Fmi2Component) = exitInitializationMode(p, c)

    fun instantiate(instanceName: String, type: Int, guid: String,
                    resourceLocation: String, visible: Boolean, loggingOn: Boolean) = instantiate(p, instanceName, type, guid, resourceLocation, visible, loggingOn)

    fun terminate(c: Fmi2Component) = terminate(p, c)

    fun reset(c: Fmi2Component) = reset(p, c)

    fun freeInstance(c: Fmi2Component) = freeInstance(p, c)

    fun getDirectionalDerivative(c: Fmi2Component, vUnknown_ref: IntArray,
                                 vKnownRef: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray) = getDirectionalDerivative(p, c, vUnknown_ref, vKnownRef, dvKnown, dvUnknown)


    fun getInteger(c: Fmi2Component, vr: IntArray, ref: IntArray) = getInteger(p, c, vr, ref)
    fun getReal(c: Fmi2Component, vr: IntArray, ref: DoubleArray) = getReal(p, c, vr, ref)
    fun getString(c: Fmi2Component, vr: IntArray, ref: Array<String>) = getString(p, c, vr, ref)
    fun getBoolean(c: Fmi2Component, vr: IntArray, ref: BooleanArray) = getBoolean(p, c, vr, ref)


    fun setInteger(c: Fmi2Component, vr: IntArray, values: IntArray) = setInteger(p, c, vr, values)
    fun setReal(c: Fmi2Component, vr: IntArray, values: DoubleArray) = setReal(p, c, vr, values)
    fun setString(c: Fmi2Component, vr: IntArray, values: Array<String>) = setString(p, c, vr, values)
    fun setBoolean(c: Fmi2Component, vr: IntArray, values: BooleanArray) = setBoolean(p, c, vr, values)


    fun getFMUstate(c: Fmi2Component, state: LongByReference) = getFMUstate(p, c, state)
    fun setFMUstate(c: Fmi2Component, state: Long) = setFMUstate(p, c, state)
    fun freeFMUstate(c: Fmi2Component, state: Long) = freeFMUstate(p, c, state)


    fun serializedFMUstateSize(c: Fmi2Component, state: Long, size: IntByReference) = serializedFMUstateSize(p, c, state, size)

    fun serializeFMUstate(c: Fmi2Component, state: Long, serializedState: ByteArray) = serializeFMUstate(p, c, state, serializedState)

    fun deSerializeFMUstate(c: Fmi2Component, state: LongByReference, serializedState: ByteArray) = deSerializeFMUstate(p, c, state, serializedState)


    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Fmi2Library::class.java)

        init {

            val fileName = libPrefix + "fmi2_jni." + libExtension
            val copy = File(fileName).apply {
                deleteOnExit()
            }
            try {
                Fmi2Library::class.java.classLoader
                        .getResourceAsStream("native/fmi2/$currentOS/$fileName").use { `is` ->
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
