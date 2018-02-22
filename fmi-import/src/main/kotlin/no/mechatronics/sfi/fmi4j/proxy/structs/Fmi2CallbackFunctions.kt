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

package no.mechatronics.sfi.fmi4j.proxy.structs


import com.sun.jna.Callback
import com.sun.jna.Memory
import com.sun.jna.Pointer
import com.sun.jna.Structure

import java.util.Arrays

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import no.mechatronics.sfi.fmi4j.common.FmiStatus

/**
 *
 * @author Lars Ivar Hatledal
 */
open class Fmi2CallbackFunctions : Structure() {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(Fmi2CallbackFunctions::class.java)
        val POINTERS : MutableMap<Pointer, Memory> = HashMap()
    }

    //class ByValue : Fmi2CallbackFunctions(), Structure.ByValue

    @JvmField
    internal var logger: CallbackLogger = FmiCallbackLoggerImpl()
    @JvmField
    internal var allocateMemory: CallbackAllocateMemory = CallbackAllocateMemoryImpl()
    @JvmField
    internal var freeMemory: CallbackFreeMemory = CallbackFreeMemoryImpl()
    @JvmField
    internal var stepFinished: StepFinished = StepFinishedImpl()

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

            LOG.info("InstanceName: {}, status: {}, category: {}, message: {}", instanceName, FmiStatus.valueOf(status), category, message)
        }

    }

    interface CallbackAllocateMemory : Callback {

        operator fun invoke(nobj: Int, size: Int): Pointer
    }

    inner class CallbackAllocateMemoryImpl : CallbackAllocateMemory {

        override fun invoke(nobj: Int, size: Int): Pointer {

            val bytes = (if (nobj <= 0) 1 else nobj) * size + 4;
            val memory = Memory(bytes.toLong())
            val aligned = memory.align(4)
            memory.clear()

            val pointer: Pointer = aligned.share(0)
            POINTERS.put(pointer, memory)

            return memory
        }

    }

    interface CallbackFreeMemory : Callback {

        operator fun invoke(pointer: Pointer)
    }

    inner class CallbackFreeMemoryImpl : CallbackFreeMemory {

        override fun invoke(pointer: Pointer) {

           // LOG.debug("CallbackFreeMemoryImpl")

           POINTERS.remove(pointer)?.apply {
               System.gc()
              // Native.free(Pointer.nativeValue(this))
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
