package no.ntnu.ihb.fmi4j.importer.fmi2.jni

import no.ntnu.ihb.fmi4j.*
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReference
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.util.ArrayBuffers
import no.ntnu.ihb.fmi4j.util.IntByReference
import no.ntnu.ihb.fmi4j.util.LongByReference
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

/**
 * @author Lars Ivar Hatledal
 */
abstract class Fmi2LibraryWrapper<E : Fmi2Library>(
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
        private set

    /**
     * Has terminate been called on the FMU?
     */
    var isTerminated: Boolean = false
        private set


    protected fun updateStatus(status: FmiStatus): FmiStatus {
        return status.also { lastStatus = it }
    }

    /**
     * @see Fmi2Library.getTypesPlatform
     */
    val typesPlatform: String
        get() = library.getTypesPlatform()

    /**
     *
     * @see Fmi2Library.getVersion()
     */
    val version: String
        get() = library.getVersion()


    /**
     * @see Fmi2Library.setDebugLogging
     */
    fun setDebugLogging(loggingOn: Boolean, categories: StringArray): FmiStatus {
        return updateStatus(library.setDebugLogging(c, loggingOn, categories))
    }

    /**
     * @see Fmi2Library.setupExperiment
     */
    fun setupExperiment(tolerance: Double, startTime: Double, stopTime: Double): FmiStatus {
        return updateStatus(library.setupExperiment(c, tolerance, startTime, stopTime))
    }

    /**
     * @see Fmi2Library.enterInitializationMode
     */
    fun enterInitializationMode(): FmiStatus {
        return updateStatus(library.enterInitializationMode(c))
    }

    /**
     * @see Fmi2Library.exitInitializationMode
     */
    fun exitInitializationMode(): FmiStatus {
        return updateStatus(library.exitInitializationMode(c))
    }

    /**
     * @see Fmi2Library.terminate
     */
    fun terminate(): FmiStatus {
        return if (isTerminated) {
            return FmiStatus.OK
        } else {
            try {
                updateStatus(library.terminate(c))
            } catch (ex: Error) {
                LOG.error("Error caught on fmi2Terminate: ${ex.javaClass.simpleName}")
                updateStatus(FmiStatus.OK)
            } finally {
                isTerminated = true
            }
        }
    }

    /**
     * @see Fmi2Library.freeInstance
     */
    internal fun freeInstance() {
        if (!isInstanceFreed) {
            var success = false
            try {
                library.freeInstance(c)
                success = true
            } catch (ex: Error) {
                LOG.error("Error caught on fmi2FreeInstance: ${ex.javaClass.simpleName}")
            } finally {
                val msg = if (success) "successfully" else "unsuccessfully"
                LOG.debug("FMU instance '${library.instanceNames[c]}' freed $msg!")
                _library = null
                System.gc()
            }
        }
    }

    /**
     * @see Fmi2Library.reset
     */
    fun reset(): FmiStatus {
        return updateStatus(library.reset(c)).also { status ->
            if (status == FmiStatus.OK) {
                isTerminated = false
            }
        }
    }

    /**
     * @see Fmi2Library.getInteger
     */
    @Synchronized
    fun readInteger(valueReference: ValueReference): IntegerRead {
        return with(buffers) {
            vr[0] = valueReference
            IntegerRead(iv[0], updateStatus(library.getInteger(c, vr, iv)))
        }
    }

    /**
     * @see Fmi2Library.getInteger
     */
    override fun readInteger(vr: ValueReferences, ref: IntArray): FmiStatus {
        return updateStatus(library.getInteger(c, vr, ref))
    }

    /**
     * @see Fmi2Library.getReal
     */
    @Synchronized
    fun readReal(valueReference: ValueReference): RealRead {
        return with(buffers) {
            vr[0] = valueReference
            RealRead(rv[0], updateStatus(readReal(vr, rv)))
        }
    }

    /**
     * @see Fmi2Library.getReal
     */
    override fun readReal(vr: ValueReferences, ref: DoubleArray): FmiStatus {
        return updateStatus(library.getReal(c, vr, ref))
    }

    fun readRealDirect(vr: ByteBuffer, ref: ByteBuffer): FmiStatus {
        return updateStatus(library.getRealDirect(c, vr, ref))
    }

    /**
     * @see Fmi2Library.getString
     */
    @Synchronized
    fun readString(valueReference: ValueReference): StringRead {
        return with(buffers) {
            vr[0] = valueReference
            StringRead(sv[0], updateStatus(library.getString(c, vr, sv)))
        }
    }

    /**
     * @see Fmi2Library.getString
     */
    override fun readString(vr: ValueReferences, ref: StringArray): FmiStatus {
        return updateStatus(library.getString(c, vr, ref))
    }

    /**
     * @see Fmi2Library.getBoolean
     */
    @Synchronized
    fun readBoolean(valueReference: ValueReference): BooleanRead {
        return with(buffers) {
            vr[0] = valueReference
            BooleanRead(bv[0], updateStatus(library.getBoolean(c, vr, bv)))
        }
    }

    /**
     * @see Fmi2Library.getBoolean
     */
    override fun readBoolean(vr: ValueReferences, ref: BooleanArray): FmiStatus {
        return updateStatus(library.getBoolean(c, vr, ref))
    }

    override fun readAll(
        intVr: ValueReferences?, intRefs: IntArray?,
        realVr: ValueReferences?, realRefs: DoubleArray?,
        boolVr: ValueReferences?, boolRefs: BooleanArray?,
        strVr: ValueReferences?, strRefs: StringArray?
    ): FmiStatus {
        return updateStatus(library.getAll(c, intVr, intRefs, realVr, realRefs, boolVr, boolRefs, strVr, strRefs))
    }

    /**
     * @see Fmi2Library.setInteger
     */
    @Synchronized
    fun writeInteger(valueReference: ValueReference, ref: Int): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            iv[0] = ref
            writeInteger(vr, iv)
        }
    }

    /**
     * @see Fmi2Library.setInteger
     */
    override fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus {
        return updateStatus((library.setInteger(c, vr, value)))
    }

    /**
     * @see Fmi2Library.setReal
     */
    @Synchronized
    fun writeReal(valueReference: ValueReference, value: Double): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            rv[0] = value
            writeReal(vr, rv)
        }
    }

    /**
     * @see Fmi2Library.setReal
     */
    override fun writeReal(vr: ValueReferences, value: DoubleArray): FmiStatus {
        return updateStatus((library.setReal(c, vr, value)))
    }

    fun writeRealDirect(vr: ByteBuffer, value: ByteBuffer): FmiStatus {
        return updateStatus(library.setRealDirect(c, vr, value))
    }

    /**
     * @see Fmi2Library.setString
     */
    @Synchronized
    fun writeString(valueReference: ValueReference, value: String): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            sv[0] = value
            writeString(vr, sv)
        }
    }

    /**
     * @see Fmi2Library.setString
     */
    override fun writeString(vr: ValueReferences, value: StringArray): FmiStatus {
        return updateStatus((library.setString(c, vr, value)))
    }

    /**
     * @see Fmi2Library.setBoolean
     */
    @Synchronized
    fun writeBoolean(valueReference: ValueReference, value: Boolean): FmiStatus {
        return with(buffers) {
            vr[0] = valueReference
            bv[0] = value
            writeBoolean(vr, bv)
        }
    }

    /**
     * @see Fmi2Library.setBoolean
     */
    override fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus {
        return updateStatus(library.setBoolean(c, vr, value))
    }

    override fun writeAll(
        intVr: ValueReferences?, intValues: IntArray?,
        realVr: ValueReferences?, realValues: DoubleArray?,
        boolVr: ValueReferences?, boolValues: BooleanArray?,
        strVr: ValueReferences?, strValues: StringArray?
    ): FmiStatus {
        return updateStatus(
            library.setAll(
                c,
                intVr, intValues,
                realVr, realValues,
                boolVr, boolValues,
                strVr, strValues
            )
        )
    }

    /**
     * @see Fmi2Library.getDirectionalDerivative
     */
    fun getDirectionalDerivative(
        vUnknown_ref: ValueReferences, vKnown_ref: ValueReferences,
        dvKnown: DoubleArray, dvUnknown: DoubleArray
    ): FmiStatus {
        return updateStatus(
            library.getDirectionalDerivative(
                c, vUnknown_ref, vKnown_ref, dvKnown, dvUnknown
            )
        )
    }

    /**
     * @see Fmi2Library.getFMUstate
     */
    fun getFMUState(): FmuState {
        return LongByReference().also {
            updateStatus(library.getFMUstate(c, it))
        }.value
    }

    /**
     * @see Fmi2Library.getFMUstate
     */
    fun setFMUState(fmuState: FmuState): FmiStatus {
        return updateStatus(library.setFMUstate(c, fmuState))
    }

    /**
     * @see Fmi2Library.freeFMUstate
     */
    fun freeFMUState(fmuState: FmuState): FmiStatus {
        return updateStatus(library.freeFMUstate(c, fmuState))
    }

    /**
     * @see Fmi2Library.serializedFMUstateSize
     */
    fun serializedFMUStateSize(fmuState: FmuState): Int {
        return IntByReference().let {
            updateStatus(library.serializedFMUstateSize(c, fmuState, it))
            it.value
        }
    }

    /**
     * @see Fmi2Library.serializeFMUstate
     */
    fun serializeFMUState(fmuState: FmuState): ByteArray {
        val size = serializedFMUStateSize(fmuState)
        return ByteArray(size).also {
            updateStatus(library.serializeFMUstate(c, fmuState, it))
        }
    }

    /**
     * @see Fmi2Library.deSerializeFMUstate
     */
    fun deSerializeFMUState(serializedState: ByteArray): FmuState {
        return LongByReference().also { state ->
            updateStatus(library.deSerializeFMUstate(c, state, serializedState))
        }.value
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(Fmi2LibraryWrapper::class.java)
    }

}
