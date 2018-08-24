@file:JvmName("OSUtil")

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

package no.mechatronics.sfi.fmi4j.common

val osName: String = System.getProperty("os.name")
val platformBitness: String = System.getProperty("sun.arch.data.model")

val is32Bit: Boolean
    get() = platformBitness == "32"

val is64Bit: Boolean
    get() = platformBitness == "64"

val isWindows: Boolean
    get() = osName.startsWith("Windows")

val isLinux: Boolean
    get() = osName.startsWith("Linux")

val isMac: Boolean
    get() = osName.startsWith("Mac") || osName.startsWith("Darwin")


val currentOS: String
    get() {
        return when {
            isMac -> "darwin$platformBitness"
            isLinux -> "linux$platformBitness"
            isWindows -> "win$platformBitness"
            else -> throw RuntimeException("Unsupported OS: $osName")
        }
    }

val libPrefix: String
    get() {
        return when {
            isMac -> "" // NOT SURE IF THIS IS CORRECT!
            isLinux -> "lib"
            isWindows -> ""
            else -> throw RuntimeException("Unsupported OS: $osName")
        }
    }

val libExtension: String
    get() {
        return when {
            isMac -> "dylib"
            isLinux -> "so"
            isWindows -> "dll"
            else -> throw RuntimeException("Unsupported OS: $osName")
        }
    }
