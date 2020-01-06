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

import no.ntnu.ihb.fmi4j.*
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReference
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.util.ArrayBuffers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File

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

        val LOG: Logger = LoggerFactory.getLogger(Fmi1Library::class.java)

        init {
            FMI4j.init()
        }

    }

}

/**
 * @author Lars Ivar Hatledal
 */
abstract class Fmi1LibraryWrapper<E : Fmi1Library>(
        protected var c: Long,
        library: E
) : VariableAccessor {

    private val buffers: ArrayBuffers by lazy {
        ArrayBuffers()
    }

    private var _library: E? = library

    protected val library: E
        get() = _library ?: throw IllegalAccessException("Library is no longer accessible!")

    val isInstanceFreed: Boolean
        get() = _library == null

    /**
     * The status returned from the last call to a FMU function
     */
    var lastStatus: FmiStatus = FmiStatus.NONE
        internal set

    /**
     * Has terminate been called on the FMU?
     */
    var isTerminated: Boolean = false
        protected set


    protected fun updateStatus(status: FmiStatus): FmiStatus {
        return status.also { lastStatus = it }
    }

    val typesPlatform: String
        get() = library.getTypesPlatform()

    val version: String
        get() = library.getVersion()

    fun terminate(): FmiStatus {
        if (isTerminated) {
            return FmiStatus.OK
        } else {
            return try {
                updateStatus(library.terminate(c))
            } catch (ex: Error) {
                LOG.error("Error caught on fmi2Terminate: ${ex.javaClass.simpleName}")
                updateStatus(FmiStatus.OK)
            } finally {
                isTerminated = true
            }

        }
    }

    internal fun freeInstance() {
        if (!isInstanceFreed) {
            var success = false
            try {
                library.freeInstance(c)
                success = true
            } catch (ex: Error) {
                LOG.error("Error caught on fmiFreeInstance: ${ex.javaClass.simpleName}")
            } finally {
                val msg = if (success) "successfully" else "unsuccessfully"
                LOG.debug("FMU instance '${library.instanceName}' freed $msg!")
                _library = null
                System.gc()
            }
        }
    }


    @Synchronized
    fun readInteger(valueReference: ValueReference): IntegerRead {
        return with(buffers) {
            vr[0] = valueReference
            IntegerRead(iv[0], updateStatus(library.getInteger(c, vr, iv)))
        }
    }

    override fun read(vr: ValueReferences, ref: IntArray): FmiStatus {
        return updateStatus(library.getInteger(c, vr, ref))
    }

    @Synchronized
    fun readReal(valueReference: ValueReference): RealRead {
        return with(buffers) {
            vr[0] = valueReference
            RealRead(rv[0], updateStatus(library.getReal(c, vr, rv)))
        }
    }

    override fun read(vr: ValueReferences, ref: DoubleArray): FmiStatus {
        return updateStatus(library.getReal(c, vr, ref))
    }

    @Synchronized
    fun readString(valueReference: ValueReference): StringRead {
        return with(buffers) {
            vr[0] = valueReference
            StringRead(sv[0], updateStatus(library.getString(c, vr, sv)))
        }
    }

    override fun read(vr: ValueReferences, ref: StringArray): FmiStatus {
        return updateStatus(library.getString(c, vr, ref))
    }

    @Synchronized
    fun readBoolean(valueReference: ValueReference): BooleanRead {
        return with(buffers) {
            vr[0] = valueReference
            BooleanRead(bv[0], updateStatus(library.getBoolean(c, vr, bv)))
        }
    }

    override fun read(vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return updateStatus(library.getBoolean(c, vr, ref))
    }

    @Synchronized
    fun writeInteger(valueReference: ValueReference, ref: Int): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            iv[0] = ref
            write(vr, iv)
        }
    }

    override fun write(vr: ValueReferences, value: IntArray): FmiStatus {
        return updateStatus((library.setInteger(c, vr, value)))
    }

    @Synchronized
    fun writeReal(valueReference: ValueReference, value: Double): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            rv[0] = value
            write(vr, rv)
        }
    }

    override fun write(vr: ValueReferences, value: DoubleArray): FmiStatus {
        return updateStatus((library.setReal(c, vr, value)))
    }


    @Synchronized
    fun writeString(valueReference: ValueReference, value: String): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            sv[0] = value
            write(vr, sv)
        }
    }

    override fun write(vr: ValueReferences, value: StringArray): FmiStatus {
        return updateStatus((library.setString(c, vr, value)))
    }

    @Synchronized
    fun writeBoolean(valueReference: ValueReference, value: Boolean): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            bv[0] = value
            write(vr, bv)
        }
    }

    override fun write(vr: ValueReferences, value: BooleanArray): FmiStatus {
        return updateStatus(library.setBoolean(c, vr, value))
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(Fmi1LibraryWrapper::class.java)
    }

}
