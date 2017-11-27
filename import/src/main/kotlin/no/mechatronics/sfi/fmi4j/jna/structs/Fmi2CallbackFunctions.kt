package no.mechatronics.sfi.fmi4j.jna.structs


import com.sun.jna.Callback
import com.sun.jna.Memory
import com.sun.jna.Pointer
import com.sun.jna.Structure
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import java.util.Arrays
import java.util.HashSet
import org.slf4j.LoggerFactory


open class Fmi2CallbackFunctions : Structure() {

    private val LOG = LoggerFactory.getLogger(Fmi2CallbackFunctions::class.java)

    object POINTERS : HashSet<Pointer>()
    class ByValue : Fmi2CallbackFunctions(), Structure.ByValue

    @JvmField
    var logger: CallbackLogger
    @JvmField
    var allocateMemory: CallbackAllocateMemory
    @JvmField
    var freeMemory: CallbackFreeMemory
    @JvmField
    var stepFinished: StepFinished

    init {
        this.logger = FmiCallbackLoggerImpl()
        this.allocateMemory = CallbackAllocateMemoryImpl()
        this.freeMemory = CallbackFreeMemoryImpl()
        this.stepFinished = StepFinishedImpl()
        setAlignType(Structure.ALIGN_GNUC)
    }

    override fun getFieldOrder(): List<String> {
        return Arrays.asList(
                "logger",
                "allocateMemory",
                "freeMemory",
                "stepFinished"
        )
    }

    interface CallbackLogger : Callback {

        operator fun invoke(c: Pointer?, instanceName: String, status: Int, category: String, message: String, args: Pointer?)
    }

    inner class FmiCallbackLoggerImpl : CallbackLogger {

        override fun invoke(c: Pointer?, instanceName: String, status: Int, category: String, message: String, args: Pointer?) {
            LOG.info("InstanceName: {}, status: {}, category: {}, message: {}", instanceName, Fmi2Status.valueOf(status), category, message)
        }

    }

    interface CallbackAllocateMemory : Callback {

        operator fun invoke(nobj: Int, size: Int): Pointer
    }

    inner class CallbackAllocateMemoryImpl : CallbackAllocateMemory {

        override fun invoke(nobj: Int, size: Int): Pointer {

            var nobj_ = nobj
            if (nobj_ <= 0) {
                nobj_ = 1
            }
            val malloc = (nobj_ * size).toLong();
            LOG.debug("CallbackAllocateMemoryImpl, {}", size)
            val memory = Memory(malloc)
            memory.align(Structure.ALIGN_GNUC)
            POINTERS.add(memory)
            return memory
        }

    }

    interface CallbackFreeMemory : Callback {

        operator fun invoke(pointer: Pointer)
    }

    inner class CallbackFreeMemoryImpl : CallbackFreeMemory {

        override fun invoke(pointer: Pointer) {

            LOG.debug("CallbackFreeMemoryImpl")

            if (!POINTERS.remove(pointer)) {
                LOG.warn("Failed to remove pointer!")
            }

        }

    }

    interface StepFinished : Callback {

        operator fun invoke(c: Pointer, status: Int)
    }

    inner class StepFinishedImpl : StepFinished {

        override fun invoke(c: Pointer, status: Int) {
            LOG.debug("StepFinished")
        }

    }
}
