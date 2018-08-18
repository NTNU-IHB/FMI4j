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

import no.mechatronics.sfi.fmi4j.importer.misc.currentOS
import no.mechatronics.sfi.fmi4j.importer.misc.libExtension
import no.mechatronics.sfi.fmi4j.importer.misc.libPrefix
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream


/**
 * @author Lars Ivar Hatledal
 */
class FmiLibrary(
        private val libName: String
) : Closeable {

    init {
        if (!load(libName)) {
            throw RuntimeException("Unable to load native library '$libName' !")
        }
    }

    private var isFreed = false

    override fun close() {
        if (!isFreed.also { isFreed = true }) {
            free().also {
                LOG.debug("Freed native library '${File(libName).name}' successfully: $it")
            }
        }
    }

    protected fun finalize() {
        close()
    }

    private external fun load(libName: String): Boolean

    external fun free(): Boolean

    external fun getVersion(): String

    external fun getTypesPlatform(): String


    external fun setDebugLogging(
            c: Long, loggingOn: Boolean, categories: Array<String>): Int

    external fun setupExperiment(
            c: Long, toleranceDefined: Boolean,
            tolerance: Double, startTime: Double, stopTime: Double): Int

    external fun enterInitializationMode(c: Long): Int

    external fun exitInitializationMode(c: Long): Int

    external fun instantiate(
            instanceName: String, type: Int, guid: String,
            resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long

    external fun terminate(c: Long): Int

    external fun reset(c: Long): Int

    external fun freeInstance(c: Long)

    //read
    external fun getInteger(c: Long, vr: IntArray, ref: IntArray): Int

    external fun getReal(c: Long, vr: IntArray, ref: DoubleArray): Int

    external fun getString(c: Long, vr: IntArray, ref: Array<String>): Int

    external fun getBoolean(c: Long, vr: IntArray, ref: BooleanArray): Int

    //write
    external fun setInteger(c: Long, vr: IntArray, values: IntArray): Int

    external fun setReal(c: Long, vr: IntArray, values: DoubleArray): Int

    external fun setString(c: Long, vr: IntArray, values: Array<String>): Int

    external fun setBoolean(c: Long, vr: IntArray, values: BooleanArray): Int

    external fun getDirectionalDerivative(
            c: Long, vUnknown_ref: IntArray,
            vKnownRef: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray): Int


    external fun getFMUstate(c: Long, state: FmuState): Int

    external fun setFMUstate(c: Long, state: Long): Int

    external fun freeFMUstate(c: Long, state: FmuState): Int

    external fun serializedFMUstateSize(c: Long, state: Long, size: IntByReference): Int

    external fun serializeFMUstate(c: Long, state: Long, serializedState: ByteArray): Int

    external fun deSerializeFMUstate(c: Long, state: FmuState, serializedState: ByteArray): Int

    /***************************************************
     * Functions for FMI2 for Co-simulation
     */

    external fun step(
            c: Long, currentCommunicationPoint: Double,
            communicationStepSize: Double, noSetFMUStatePriorToCurrentPoint: Boolean): Int

    external fun cancelStep(c: Long): Int

    external fun setRealInputDerivatives(c: Long, vr: IntArray, order: IntArray, value: DoubleArray): Int

    external fun getRealOutputDerivatives(c: Long, vr: IntArray, order: IntArray, value: DoubleArray): Int

    external fun getStatus(c: Long, s: Int, value: IntByReference): Int

    external fun getRealStatus(c: Long, s: Int, value: DoubleByReference): Int

    external fun getIntegerStatus(c: Long, s: Int, value: IntByReference): Int

    external fun getStringStatus(c: Long, s: Int, value: StringByReference): Int

    external fun getBooleanStatus(c: Long, s: Int, value: BooleanByReference): Int

    external fun getMaxStepSize(c: Long, stepSize: DoubleByReference): Int

    /***************************************************
     * Functions for FMI2 for Model Exchange
     */

    external fun enterEventMode(c: Long): Int

    external fun newDiscreteStates(c: Long, ev: EventInfo): Int

    external fun enterContinuousTimeMode(c: Long): Int

    external fun setContinuousStates(c: Long, x: DoubleArray): Int

    external fun completedIntegratorStep(c: Long, noSetFMUStatePriorToCurrentPoint: Boolean,
                                         enterEventMode: BooleanByReference, terminateSimulation: BooleanByReference): Int

    external fun setTime(c: Long, time: Double): Int

    external fun getDerivatives(c: Long, derivatives: DoubleArray): Int

    external fun getEventIndicators(c: Long, eventIndicators: DoubleArray): Int

    external fun getContinuousStates(c: Long, x: DoubleArray): Int

    external fun getNominalsOfContinuousStates(c: Long, x_nominals: DoubleArray): Int

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(FmiLibrary::class.java)

        init {

            val fileName = libPrefix + "fmi." + libExtension
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
