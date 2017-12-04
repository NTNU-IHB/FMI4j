/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
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

    private companion object {
        val LOG = LoggerFactory.getLogger(Fmi2CallbackFunctions::class.java)
        val POINTERS : MutableSet<Pointer> = HashSet()
    }


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
           // LOG.debug("CallbackAllocateMemoryImpl, {}", size)
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

           // LOG.debug("CallbackFreeMemoryImpl")

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
