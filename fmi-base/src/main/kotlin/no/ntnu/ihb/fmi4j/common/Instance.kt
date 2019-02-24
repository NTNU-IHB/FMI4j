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

package no.ntnu.ihb.fmi4j.common

import no.ntnu.ihb.fmi4j.xml.CommonModelDescription
import no.ntnu.ihb.fmi4j.xml.ModelDescription
import no.ntnu.ihb.fmi4j.xml.variables.ModelVariables
import java.io.Closeable

typealias FmuState = Long

/**
 * @author Lars Ivar Hatledal
 */
interface SimpleFmuInstance : Closeable {

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
    val lastStatus: Status

    /**
     * The parsed content found in the modelDescription.xml
     */
    val modelDescription: ModelDescription

    /**
     * @see ModelDescription.modelVariables
     */
    val modelVariables: ModelVariables
        get() = modelDescription.modelVariables

    fun simpleSetup(): Boolean{
        return simpleSetup(0.0, 0.0, 0.0)
    }

    fun simpleSetup(start: Double = 0.0, stop: Double = 0.0, tolerance: Double = 0.0): Boolean {
        return setup(start, stop, tolerance) && enterInitializationMode() && exitInitializationMode()
    }

    fun setup(): Boolean {
        return setup(0.0, 0.0, 0.0)
    }

    fun setup(start: Double = 0.0, stop: Double = 0.0, tolerance: Double = 0.0): Boolean

    fun enterInitializationMode(): Boolean

    fun exitInitializationMode(): Boolean

    /**
     * @see no.ntnu.ihb.fmi4j.importer.jni.Fmi2Library.reset
     */
    fun reset(): Boolean

    /**
     * @see no.ntnu.ihb.fmi4j.importer.jni.Fmi2Library.terminate
     */
    fun terminate(): Boolean


    fun getFMUstate(): FmuState
    fun setFMUstate(state: FmuState): Boolean
    fun freeFMUstate(state: FmuState): Boolean

    fun serializeFMUstate(state: FmuState): ByteArray
    fun deSerializeFMUstate(state: ByteArray): FmuState
    fun getDirectionalDerivative(vUnknownRef: ValueReferences, vKnownRef: ValueReferences, dvKnown: RealArray): RealArray

    fun readInteger(vr: ValueReferences, ref: IntArray): Status
    fun readReal(vr: ValueReferences, ref: RealArray): Status
    fun readString(vr: ValueReferences, ref: StringArray): Status
    fun readBoolean(vr: ValueReferences, ref: BooleanArray): Status

    fun writeInteger(vr: ValueReferences, value: IntArray): Status
    fun writeReal(vr: ValueReferences, value: RealArray): Status
    fun writeString(vr: ValueReferences, value: StringArray): Status
    fun writeBoolean(vr: ValueReferences, value: BooleanArray): Status

    /**
     * Calls terminate()
     *
     * @see Closeable
     */
    override fun close() {
        if (!isTerminated) {
            terminate()
        }
    }

}

/**
 * Represents a generic FMU instance
 *
 * @author Lars Ivar Hatledal
 */
interface Instance<out E: CommonModelDescription> : SimpleFmuInstance {

    override val modelDescription: E

}
