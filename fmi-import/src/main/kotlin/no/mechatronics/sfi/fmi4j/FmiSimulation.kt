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

package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.misc.VariableReader
import no.mechatronics.sfi.fmi4j.misc.VariableWriter
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelVariables

interface FmiSimulation : AutoCloseable {

    val fmuFile: FmuFile
    val modelDescription: ModelDescription
    val modelVariables: ModelVariables
    val currentTime: Double

    fun write(name: String) : VariableWriter
    fun read(name: String) : VariableReader

    fun write(vr: Int) : VariableWriter
    fun read(vr: Int) : VariableReader

    fun init() : Boolean
    fun init(start: Double) : Boolean
    fun init(start: Double, stop: Double): Boolean
    fun doStep(dt: Double) : Boolean

    fun reset() : Boolean
    fun reset(requireReinit: Boolean) : Boolean
    fun terminate() : Boolean

    fun isTerminated() : Boolean

    fun getLastStatus() : Fmi2Status

}

