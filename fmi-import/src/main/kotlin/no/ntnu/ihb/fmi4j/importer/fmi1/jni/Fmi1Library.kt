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

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.util.OsUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream

internal typealias NativeStatus = Int
internal typealias FmiComponent = Long

/**
 * @author Lars Ivar Hatledal
 */
open class Fmi1Library(
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

    private external fun setupExperiment(p: Long, c: FmiComponent,
                                         tolerance: Double, startTime: Double, stopTime: Double): NativeStatus

    private external fun enterInitializationMode(p: Long, c: FmiComponent): NativeStatus

    private external fun exitInitializationMode(p: Long, c: FmiComponent): NativeStatus

    private external fun instantiate(p: Long, instanceName: String, type: Int, guid: String,
                                     resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long

    private external fun terminate(p: Long, c: FmiComponent): NativeStatus

    private external fun reset(p: Long, c: FmiComponent): NativeStatus

    private external fun freeInstance(p: Long, c: FmiComponent)

    private external fun getInteger(p: Long, c: FmiComponent, vr: ValueReferences, ref: IntArray): NativeStatus
    private external fun getReal(p: Long, c: FmiComponent, vr: ValueReferences, ref: DoubleArray): NativeStatus
    private external fun getString(p: Long, c: FmiComponent, vr: ValueReferences, ref: Array<String>): NativeStatus
    private external fun getBoolean(p: Long, c: FmiComponent, vr: ValueReferences, ref: BooleanArray): NativeStatus


    private external fun setInteger(p: Long, c: FmiComponent, vr: ValueReferences, values: IntArray): NativeStatus
    private external fun setReal(p: Long, c: FmiComponent, vr: ValueReferences, values: DoubleArray): NativeStatus
    private external fun setString(p: Long, c: FmiComponent, vr: ValueReferences, values: Array<String>): NativeStatus
    private external fun setBoolean(p: Long, c: FmiComponent, vr: ValueReferences, values: BooleanArray): NativeStatus


    protected fun NativeStatus.transform(): FmiStatus {
        return FmiStatus.valueOf(this)
    }

    fun getVersion(): String {
        return getVersion(p)
    }

    fun getTypesPlatform(): String {
        return getTypesPlatform(p)
    }


//    fun setupExperiment(c: FmiComponent,
//                        tolerance: Double, startTime: Double, stopTime: Double): FmiStatus {
//        return setupExperiment(p, c, tolerance, startTime, stopTime).transform()
//    }
//
//    fun enterInitializationMode(c: FmiComponent): FmiStatus {
//        return enterInitializationMode(p, c).transform()
//    }
//
//    fun exitInitializationMode(c: FmiComponent): FmiStatus {
//        return exitInitializationMode(p, c).transform()
//    }

    fun instantiate(instanceName: String, type: Int, guid: String,
                    resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long {
        return instantiate(p, instanceName, type, guid, resourceLocation, visible, loggingOn)
    }

    fun terminate(c: FmiComponent): FmiStatus {
        return terminate(p, c).transform()
    }

    fun reset(c: FmiComponent): FmiStatus {
        return reset(p, c).transform()
    }

    fun freeInstance(c: FmiComponent) {
        freeInstance(p, c)
    }

    fun getInteger(c: FmiComponent, vr: ValueReferences, ref: IntArray): FmiStatus {
        return getInteger(p, c, vr, ref).transform()
    }

    fun getReal(c: FmiComponent, vr: ValueReferences, ref: DoubleArray): FmiStatus {
        return getReal(p, c, vr, ref).transform()
    }

    fun getString(c: FmiComponent, vr: ValueReferences, ref: Array<String>): FmiStatus {
        return getString(p, c, vr, ref).transform()
    }

    fun getBoolean(c: FmiComponent, vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return getBoolean(p, c, vr, ref).transform()
    }


    fun setInteger(c: FmiComponent, vr: ValueReferences, values: IntArray): FmiStatus {
        return setInteger(p, c, vr, values).transform()
    }

    fun setReal(c: FmiComponent, vr: ValueReferences, values: DoubleArray): FmiStatus {
        return setReal(p, c, vr, values).transform()
    }

    fun setString(c: FmiComponent, vr: ValueReferences, values: Array<String>): FmiStatus {
        return setString(p, c, vr, values).transform()
    }

    fun setBoolean(c: FmiComponent, vr: ValueReferences, values: BooleanArray): FmiStatus {
        return setBoolean(p, c, vr, values).transform()
    }


    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Fmi1Library::class.java)

        init {

            val fileName = "${OsUtil.libPrefix}fmi2_jni.${OsUtil.libExtension}"
            val copy = File(fileName).apply {
                deleteOnExit()
            }
            try {
                Fmi1Library::class.java.classLoader
                        .getResourceAsStream("native/fmi/${OsUtil.currentOS}/$fileName").use { `is` ->
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
