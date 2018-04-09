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

package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.common.FmuVariableAccessor
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.variables.ModelVariables
import no.mechatronics.sfi.fmi4j.modeldescription.variables.TypedScalarVariable
import java.io.Closeable

/**
 * @author Lars Ivar Hatledal
 */
interface Fmu : Closeable {

    /**
     * @see ModelDescription.guid
     */
    val guid: String
        get() = modelDescription.guid

    /**
     * @see ModelDescription.modelName
     */
    val modelName: String
        get() = modelDescription.modelName

    /**
     * @see ModelDescription.modelVariables
     */
    val modelVariables: ModelVariables
        get() = modelDescription.modelVariables

    val modelDescription: ModelDescription
    val variableAccessor: FmuVariableAccessor

    val lastStatus: FmiStatus
    val isInitialized: Boolean
    val isTerminated: Boolean

    /**
     * Call init with 0.0 as start.
     */
    fun init()

    /**
     * Call init with start and default stop time
     */
    fun init(start: Double)

    /**
     * Initialise FMU with the provided start and stop value
     *
     * @param start FMU start time
     * @param stop FMU stop time. If start > stop then stop is ignored
     */
    fun init(start: Double, stop: Double)

    /**
     * @see no.mechatronics.sfi.fmi4j.fmu.proxy.v2.FmiLibrary.fmi2Reset
     */
    fun reset(): Boolean

    /**
     * @see no.mechatronics.sfi.fmi4j.fmu.proxy.v2.FmiLibrary.fmi2Terminate
     */
    fun terminate(): Boolean

    /**
     * Calls terminate()
     *
     * @see Closeable
     */
    override fun close() {
        terminate()
    }

    /**
     * @see ModelVariables.getByName
     */
    fun getVariableByName(name: String): TypedScalarVariable<*>
            = modelVariables.getByName(name)

}