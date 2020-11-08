package no.ntnu.ihb.fmi4j.importer.fmi1.jni

import no.ntnu.ihb.fmi4j.*
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReference
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.util.ArrayBuffers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

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

    override fun readInteger(vr: ValueReferences, ref: IntArray): FmiStatus {
        return updateStatus(library.getInteger(c, vr, ref))
    }

    @Synchronized
    fun readReal(valueReference: ValueReference): RealRead {
        return with(buffers) {
            vr[0] = valueReference
            RealRead(rv[0], updateStatus(library.getReal(c, vr, rv)))
        }
    }

    override fun readReal(vr: ValueReferences, ref: DoubleArray): FmiStatus {
        return updateStatus(library.getReal(c, vr, ref))
    }

    fun readRealDirect(vr: ByteBuffer, ref: ByteBuffer): FmiStatus {
        return updateStatus(library.getRealDirect(c, vr, ref))
    }

    @Synchronized
    fun readString(valueReference: ValueReference): StringRead {
        return with(buffers) {
            vr[0] = valueReference
            StringRead(sv[0], updateStatus(library.getString(c, vr, sv)))
        }
    }

    override fun readString(vr: ValueReferences, ref: StringArray): FmiStatus {
        return updateStatus(library.getString(c, vr, ref))
    }

    @Synchronized
    fun readBoolean(valueReference: ValueReference): BooleanRead {
        return with(buffers) {
            vr[0] = valueReference
            BooleanRead(bv[0], updateStatus(library.getBoolean(c, vr, bv)))
        }
    }

    override fun readBoolean(vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return updateStatus(library.getBoolean(c, vr, ref))
    }

    @Synchronized
    fun writeInteger(valueReference: ValueReference, ref: Int): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            iv[0] = ref
            writeInteger(vr, iv)
        }
    }

    override fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus {
        return updateStatus((library.setInteger(c, vr, value)))
    }

    @Synchronized
    fun writeReal(valueReference: ValueReference, value: Double): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            rv[0] = value
            writeReal(vr, rv)
        }
    }

    override fun writeReal(vr: ValueReferences, value: DoubleArray): FmiStatus {
        return updateStatus((library.setReal(c, vr, value)))
    }

    fun writeRealDirect(vr: ByteBuffer, value: ByteBuffer): FmiStatus {
        return updateStatus(library.setRealDirect(c, vr, value))
    }

    @Synchronized
    fun writeString(valueReference: ValueReference, value: String): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            sv[0] = value
            writeString(vr, sv)
        }
    }

    override fun writeString(vr: ValueReferences, value: StringArray): FmiStatus {
        return updateStatus((library.setString(c, vr, value)))
    }

    @Synchronized
    fun writeBoolean(valueReference: ValueReference, value: Boolean): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            bv[0] = value
            writeBoolean(vr, bv)
        }
    }

    override fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus {
        return updateStatus(library.setBoolean(c, vr, value))
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(Fmi1LibraryWrapper::class.java)
    }

}
