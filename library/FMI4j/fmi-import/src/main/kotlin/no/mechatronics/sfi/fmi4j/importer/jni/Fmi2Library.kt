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

package no.mechatronics.sfi.fmi4j.importer.jni

import no.mechatronics.sfi.fmi4j.common.currentOS
import no.mechatronics.sfi.fmi4j.common.libExtension
import no.mechatronics.sfi.fmi4j.common.libPrefix
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream

private typealias NativeStatus = Int
private typealias Fmi2Component = Long

/**
 * @author Lars Ivar Hatledal
 */
sealed class Fmi2Library(
        private val libName: String
) : Closeable {

    init {
        if (!load(libName)) {
            throw RuntimeException("Unable to load native library '$libName' !")
        }
    }

    private var isClosed = false

    override fun close() {
        if (!isClosed) {
            free().also {
                LOG.debug("Freed native library '${File(libName).name}' successfully: $it")
            }
            isClosed = true
        }
    }

    protected fun finalize() {
        if (!isClosed) {
            LOG.warn("FMU was not closed prior do garbage collection! Doing it for you..")
            close()
        }
    }

    private external fun load(libName: String): Boolean

    external fun free(): Boolean

    external fun getVersion(): String

    external fun getTypesPlatform(): String


    external fun setDebugLogging(
            c: Fmi2Component, loggingOn: Boolean, categories: Array<String>): NativeStatus

    external fun setupExperiment(
            c: Fmi2Component, toleranceDefined: Boolean,
            tolerance: Double, startTime: Double, stopTime: Double): NativeStatus

    external fun enterInitializationMode(c: Fmi2Component): NativeStatus

    external fun exitInitializationMode(c: Fmi2Component): NativeStatus

    external fun instantiate(instanceName: String, type: Int, guid: String,
            resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long

    external fun terminate(c: Fmi2Component): NativeStatus

    external fun reset(c: Fmi2Component): NativeStatus

    external fun freeInstance(c: Fmi2Component)

    external fun getDirectionalDerivative(
            c: Fmi2Component, vUnknown_ref: IntArray,
            vKnownRef: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray): NativeStatus


    external fun getInteger(c: Fmi2Component, vr: IntArray, ref: IntArray): NativeStatus

    external fun getReal(c: Fmi2Component, vr: IntArray, ref: DoubleArray): NativeStatus

    external fun getString(c: Fmi2Component, vr: IntArray, ref: Array<String>): NativeStatus

    external fun getBoolean(c: Fmi2Component, vr: IntArray, ref: BooleanArray): NativeStatus


    external fun setInteger(c: Fmi2Component, vr: IntArray, values: IntArray): NativeStatus

    external fun setReal(c: Fmi2Component, vr: IntArray, values: DoubleArray): NativeStatus

    external fun setString(c: Fmi2Component, vr: IntArray, values: Array<String>): NativeStatus

    external fun setBoolean(c: Fmi2Component, vr: IntArray, values: BooleanArray): NativeStatus


    external fun getFMUstate(c: Fmi2Component, state: LongByReference): NativeStatus

    external fun setFMUstate(c: Fmi2Component, state: Long): NativeStatus

    external fun freeFMUstate(c: Fmi2Component, state: Long): NativeStatus


    external fun serializedFMUstateSize(c: Fmi2Component, state: Long, size: IntByReference): NativeStatus

    external fun serializeFMUstate(c: Fmi2Component, state: Long, serializedState: ByteArray): NativeStatus

    external fun deSerializeFMUstate(
            c: Fmi2Component, state: LongByReference, serializedState: ByteArray): NativeStatus


    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Fmi2Library::class.java)

        init {

            val fileName = libPrefix + "fmi2_jni." + libExtension
            val copy = File(fileName).apply {
                deleteOnExit()
            }
            try {
                Fmi2Library::class.java.classLoader
                        .getResourceAsStream("native/fmi2/$currentOS/$fileName").use { `is` ->
                            FileOutputStream(copy).use { fos ->
                                `is`.copyTo(fos)
                            }
                        }
                System.load(copy.absolutePath)
            } catch (ex: Exception) {
                copy.delete()
                throw RuntimeException(ex)
            }

        }

    }

}

class Fmi2CoSimulationLibrary(
        libName: String
) : Fmi2Library(libName) {

    external fun step(c: Fmi2Component, currentCommunicationPoint: Double,
                      communicationStepSize: Double, noSetFMUStatePriorToCurrentPoint: Boolean): NativeStatus

    external fun cancelStep(c: Fmi2Component): NativeStatus

    external fun setRealInputDerivatives(c: Fmi2Component, vr: IntArray, order: IntArray,
                                         value: DoubleArray): NativeStatus

    external fun getRealOutputDerivatives(c: Fmi2Component, vr: IntArray, order: IntArray,
                                          value: DoubleArray): NativeStatus

    external fun getStatus(c: Fmi2Component, s: Int, value: IntByReference): NativeStatus

    external fun getIntegerStatus(c: Fmi2Component, s: Int, value: IntByReference): NativeStatus

    external fun getRealStatus(c: Fmi2Component, s: Int, value: DoubleByReference): NativeStatus

    external fun getStringStatus(c: Fmi2Component, s: Int, value: StringByReference): NativeStatus

    external fun getBooleanStatus(c: Fmi2Component, s: Int, value: BooleanByReference): NativeStatus

    external fun getMaxStepSize(c: Fmi2Component, stepSize: DoubleByReference): NativeStatus
}


class Fmi2ModelExchangeLibrary(
        libName: String
) : Fmi2Library(libName) {

    external fun enterEventMode(c: Fmi2Component): NativeStatus

    external fun newDiscreteStates(c: Fmi2Component, ev: EventInfo): NativeStatus

    external fun enterContinuousTimeMode(c: Fmi2Component): NativeStatus

    external fun setContinuousStates(c: Fmi2Component, x: DoubleArray): NativeStatus

    external fun completedIntegratorStep(
            c: Fmi2Component, noSetFMUStatePriorToCurrentPoint: Boolean,
            enterEventMode: BooleanByReference, terminateSimulation: BooleanByReference): NativeStatus

    external fun setTime(c: Fmi2Component, time: Double): NativeStatus

    external fun getDerivatives(c: Fmi2Component, derivatives: DoubleArray): NativeStatus

    external fun getEventIndicators(c: Fmi2Component, eventIndicators: DoubleArray): NativeStatus

    external fun getContinuousStates(c: Fmi2Component, x: DoubleArray): NativeStatus

    external fun getNominalsOfContinuousStates(c: Fmi2Component, x_nominals: DoubleArray): NativeStatus

}
