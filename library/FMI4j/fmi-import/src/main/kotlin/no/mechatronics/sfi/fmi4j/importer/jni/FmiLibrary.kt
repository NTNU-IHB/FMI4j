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

typealias NativeStatus = Int
private typealias FmiComponent = Long

/**
 * @author Lars Ivar Hatledal
 */
sealed class FmiLibrary(
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
            c: FmiComponent, loggingOn: Boolean, categories: Array<String>): NativeStatus

    external fun setupExperiment(
            c: FmiComponent, toleranceDefined: Boolean,
            tolerance: Double, startTime: Double, stopTime: Double): NativeStatus

    external fun enterInitializationMode(c: FmiComponent): NativeStatus

    external fun exitInitializationMode(c: FmiComponent): NativeStatus

    external fun instantiate(instanceName: String, type: Int, guid: String,
            resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long

    external fun terminate(c: FmiComponent): NativeStatus

    external fun reset(c: FmiComponent): NativeStatus

    external fun freeInstance(c: FmiComponent)

    //read
    external fun getInteger(c: FmiComponent, vr: IntArray, ref: IntArray): NativeStatus

    external fun getReal(c: FmiComponent, vr: IntArray, ref: DoubleArray): NativeStatus

    external fun getString(c: FmiComponent, vr: IntArray, ref: Array<String>): NativeStatus

    external fun getBoolean(c: FmiComponent, vr: IntArray, ref: BooleanArray): NativeStatus

    //write
    external fun setInteger(c: FmiComponent, vr: IntArray, values: IntArray): NativeStatus

    external fun setReal(c: FmiComponent, vr: IntArray, values: DoubleArray): NativeStatus

    external fun setString(c: FmiComponent, vr: IntArray, values: Array<String>): NativeStatus

    external fun setBoolean(c: FmiComponent, vr: IntArray, values: BooleanArray): NativeStatus

    external fun getDirectionalDerivative(c: FmiComponent, vUnknown_ref: IntArray,
                                          vKnownRef: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray): NativeStatus

    external fun getFMUstate(c: FmiComponent, state: LongByReference): NativeStatus

    external fun setFMUstate(c: FmiComponent, state: Long): NativeStatus

    external fun freeFMUstate(c: FmiComponent, state: Long): NativeStatus

    external fun serializedFMUstateSize(c: FmiComponent, state: Long, size: IntByReference): NativeStatus

    external fun serializeFMUstate(c: FmiComponent, state: Long, serializedState: ByteArray): NativeStatus

    external fun deSerializeFMUstate(c: FmiComponent, state: LongByReference, serializedState: ByteArray): NativeStatus

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(FmiLibrary::class.java)

        init {

            val fileName = libPrefix + "fmi_jni." + libExtension
            val copy = File(fileName).apply {
                deleteOnExit()
            }
            try {
                FmiLibrary::class.java.classLoader
                        .getResourceAsStream("native/fmi/$currentOS/$fileName").use { `is` ->
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

class FmiCoSimulationLibrary(
        libName: String
) : FmiLibrary(libName) {

    external fun step(c: FmiComponent, currentCommunicationPoint: Double,
                      communicationStepSize: Double, noSetFMUStatePriorToCurrentPoint: Boolean): NativeStatus

    external fun cancelStep(c: FmiComponent): NativeStatus

    external fun setRealInputDerivatives(c: FmiComponent, vr: IntArray, order: IntArray, value: DoubleArray): NativeStatus

    external fun getRealOutputDerivatives(c: FmiComponent, vr: IntArray, order: IntArray, value: DoubleArray): NativeStatus

    external fun getStatus(c: FmiComponent, s: Int, value: IntByReference): NativeStatus

    external fun getIntegerStatus(c: FmiComponent, s: Int, value: IntByReference): NativeStatus

    external fun getRealStatus(c: FmiComponent, s: Int, value: DoubleByReference): NativeStatus

    external fun getStringStatus(c: FmiComponent, s: Int, value: StringByReference): NativeStatus

    external fun getBooleanStatus(c: FmiComponent, s: Int, value: BooleanByReference): NativeStatus

    external fun getMaxStepSize(c: FmiComponent, stepSize: DoubleByReference): NativeStatus
}


class FmiModelExchangeLibrary(
        libName: String
) : FmiLibrary(libName) {

    external fun enterEventMode(c: FmiComponent): NativeStatus

    external fun newDiscreteStates(c: FmiComponent, ev: EventInfo): NativeStatus

    external fun enterContinuousTimeMode(c: FmiComponent): NativeStatus

    external fun setContinuousStates(c: FmiComponent, x: DoubleArray): NativeStatus

    external fun completedIntegratorStep(c: FmiComponent, noSetFMUStatePriorToCurrentPoint: Boolean,
                                         enterEventMode: BooleanByReference, terminateSimulation: BooleanByReference): NativeStatus

    external fun setTime(c: FmiComponent, time: Double): NativeStatus

    external fun getDerivatives(c: FmiComponent, derivatives: DoubleArray): NativeStatus

    external fun getEventIndicators(c: FmiComponent, eventIndicators: DoubleArray): NativeStatus

    external fun getContinuousStates(c: FmiComponent, x: DoubleArray): NativeStatus

    external fun getNominalsOfContinuousStates(c: FmiComponent, x_nominals: DoubleArray): NativeStatus

}
