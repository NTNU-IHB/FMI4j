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

import no.mechatronics.sfi.fmi4j.modeldescription.SpecificModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.variables.ModelVariables
import no.mechatronics.sfi.fmi4j.modeldescription.variables.TypedScalarVariable
import java.io.Closeable

typealias FmuState = Long

interface SimpleFmuInstance : FmuVariableAccessor, Closeable {


    /**
     * Has init been called?
     */
    val isInitialized: Boolean

    /**
     * Has terminate been called?
     */
    val isTerminated: Boolean

    /**
     * Current simulation time
     */
    val simulationTime: Double

    /**
     * The last status returned by the FMU
     */
    val lastStatus: FmiStatus

    /**
     * If true, the directional derivative of the equations
     * can be computed with getDirectionalDerivative(..)
     */
    val providesDirectionalDerivative: Boolean

    val modelDescription: SpecificModelDescription

    /**
     * @see SpecificModelDescription.modelVariables
     */
    @JvmDefault
    val modelVariables: ModelVariables
        get() = modelDescription.modelVariables

    /**
     * Call init with 0.0 as start.
     */
    @JvmDefault
    fun init() {
        init(0.0)
    }

    /**
     * Call init with start and default stop time (endless)
     */
    @JvmDefault
    fun init(start: Double) {
        init(start, 0.0)
    }

    /**
     * Initialise FMU with the provided start and stop value
     *
     * @param start FMU start time
     * @param stop FMU stop time. If start > stop then stop is ignored
     */
    fun init(start: Double, stop: Double)

    /**
     * @see no.mechatronics.sfi.fmi4j.importer.proxy.v2.FmiLibrary.fmi2Reset
     */
    fun reset(): Boolean

    /**
     * @see no.mechatronics.sfi.fmi4j.importer.proxy.v2.FmiLibrary.fmi2Terminate
     */
    fun terminate(): Boolean

    /**
     * Does this FMU instance support getting and setting the FMU state?
     */
    val canGetAndSetFMUstate: Boolean

    fun getFMUstate(): FmuState
    fun setFMUstate(state: FmuState): Boolean
    fun freeFMUstate(state: FmuState): Boolean

    /**
     * Is serialization supported by this FMU instance?
     */
    val canSerializeFMUstate: Boolean

    fun serializeFMUstate(state: FmuState): ByteArray
    fun deSerializeFMUstate(state: ByteArray): FmuState
    fun getDirectionalDerivative(vUnknownRef: IntArray, vKnownRef: IntArray, dvKnown: RealArray): RealArray

    /**
     * Calls terminate()
     *
     * @see Closeable
     */
    @JvmDefault
    override fun close() {
        if (!isTerminated) {
            terminate()
        }
    }

    /**
     * @see ModelVariables.getByName
     */
    @JvmDefault
    fun getVariableByName(name: String): TypedScalarVariable<*> {
        return modelVariables.getByName(name)
    }

}

/**
 * Represents a generic FMU instance
 *
 * @author Lars Ivar Hatledal
 */
interface FmuInstance<out E: SpecificModelDescription> : SimpleFmuInstance {

    override val modelDescription: E

}