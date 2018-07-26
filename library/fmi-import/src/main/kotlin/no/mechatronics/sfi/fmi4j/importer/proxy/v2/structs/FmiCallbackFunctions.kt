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

package no.mechatronics.sfi.fmi4j.importer.proxy.v2.structs


import com.sun.jna.Callback
import com.sun.jna.Memory
import com.sun.jna.Pointer
import com.sun.jna.Structure
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Arrays
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 *
 * @author Lars Ivar Hatledal
 */
open class FmiCallbackFunctions private constructor(
) : Structure() {

    internal companion object {
        private val LOG: Logger = LoggerFactory.getLogger(FmiCallbackFunctions::class.java)
        private val POINTERS: MutableMap<Pointer, Memory> = HashMap()

        fun byReference() = FmiCallbackFunctions()
        fun byValue() = FmiCallbackFunctions().ByValue()

    }

    inner class ByValue: FmiCallbackFunctions(), Structure.ByValue

    @JvmField
    internal var logger: CallbackLogger = FmiCallbackLoggerImpl()
    @JvmField
    internal var allocateMemory: CallbackAllocateMemory = CallbackAllocateMemoryImpl()
    @JvmField
    internal var freeMemory: CallbackFreeMemory = CallbackFreeMemoryImpl()
    @JvmField
    internal var stepFinished: StepFinished? = null

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

            val msg = "InstanceName: $instanceName, status: ${no.mechatronics.sfi.fmi4j.common.FmiStatus.valueOf(status)}, category: $category, message: $message"

            when  {
                category.contains("error", ignoreCase = true) -> LOG.error(msg)
                else -> LOG.info(msg)
            }

        }

    }

    interface CallbackAllocateMemory : Callback {

        operator fun invoke(nobj: Int, size: Int): Pointer
    }

    inner class CallbackAllocateMemoryImpl : CallbackAllocateMemory {

        /**
         * Pointer to a function that is called in the FMU if memory needs to be allocated. If attribute
         * “canNotUseMemoryManagementFunctions = true” in
         * <fmiModelDescription><ModelExchange / CoSimulation>, then function
         * allocateMemory is not used in the FMU and a void pointer can be provided. If this attribute
         * has a value of “false” (which is the default), the FMU must not use malloc, calloc or
         * other memory allocation functions. One reason is that these functions might not be available
         * for embedded systems on the target machine. Another reason is that the environment may
         * have optimized or specialized memory allocation functions. allocateMemory returns a
         * pointer to space for a vector of nobj objects, each of size “size” or NULL, if the request
         * cannot be satisfied. The space is initialized to zero bytes [(a simple implementation is to use
         * calloc from the C standard library)]
         */
        override fun invoke(nobj: Int, size: Int): Pointer {

            val bytes = (if (nobj <= 0) 1 else nobj) * size + 4
            return Memory(bytes.toLong()).also { memory ->
                val aligned = memory.align(4)
                memory.clear()

                aligned.share(0).also { pointer ->
                    POINTERS[pointer] = memory
                }
            }
        }

    }

    interface CallbackFreeMemory : Callback {

        /**
         * Pointer to a function that must be called in the FMU if memory is freed that has been
         * allocated with allocateMemory. If a null pointer is provided as input argument obj, the
         * function shall perform no action [(a simple implementation is to use free from the C
         * standard library; in ANSI C89 and C99, the null pointer handling is identical as defined
         * here)]. If attribute “canNotUseMemoryManagementFunctions = true” in
         * <fmiModelDescription><ModelExchange / CoSimulation>, then function
         * freeMemory is not used in the FMU and a null pointer can be provided.
         */
        fun invoke(pointer: Pointer)
    }

    inner class CallbackFreeMemoryImpl : CallbackFreeMemory {


        override fun invoke(pointer: Pointer) {
            LOG.trace("CallbackFreeMemory")
            POINTERS.remove(pointer)
        }

    }


    interface StepFinished : Callback {

        /**
         * Optional call back function to signal if the computation of a communication step of a co-simulation
         * slave is finished. A null pointer can be provided. In this case the master must
         * use fmiGetStatus(..) to query the status of fmi2DoStep. If a pointer to a function
         * is provided, it must be called by the FMU after a completed communication step.
         */
        fun invoke(c: Pointer?, status: Int)
    }

    inner class StepFinishedImpl : StepFinished {

        override fun invoke(c: Pointer?, status: Int) {
            LOG.debug("StepFinished")
        }

    }
}
