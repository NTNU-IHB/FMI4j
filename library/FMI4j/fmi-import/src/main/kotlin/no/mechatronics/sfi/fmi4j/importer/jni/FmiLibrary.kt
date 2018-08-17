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
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream


interface IFmiLibrary : Closeable {

    override fun close()

    fun getVersion(): String

    fun getTypesPlatform(): String


    fun setDebugLogging(
            c: Long, loggingOn: Boolean, nCategories: Int, categories: Array<String>): Int

    fun setupExperiment(
            c: Long, toleranceDefined: Boolean,
            tolerance: Double, startTime: Double, stopTime: Double): Int

    fun enterInitializationMode(c: Long): Int

    fun exitInitializationMode(c: Long): Int

    fun instantiate(
            instanceName: String, type: Int, guid: String,
            resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long

    fun terminate(c: Long): Int

    fun reset(c: Long): Int

    fun freeInstance(c: Long)

    //read
    fun getInteger(c: Long, vr: IntArray, ref: IntArray): Int

    fun getReal(c: Long, vr: IntArray, ref: DoubleArray): Int

    fun getString(c: Long, vr: IntArray, ref: Array<String>): Int

    fun getBoolean(c: Long, vr: IntArray, ref: BooleanArray): Int

    //write
    fun setInteger(c: Long, vr: IntArray, values: IntArray): Int

    fun setReal(c: Long, vr: IntArray, values: DoubleArray): Int

    fun setString(c: Long, vr: IntArray, values: Array<String>): Int

    fun setBoolean(c: Long, vr: IntArray, values: BooleanArray): Int

    fun getDirectionalDerivative(
            c: Long, vUnknown_ref: IntArray,
            vKnownRef: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray): Int


    fun getFMUstate(c: Long, state: FmuState): Int

    fun setFMUstate(c: Long, state: Long): Int

    fun freeFMUstate(c: Long, state: FmuState): Int

    fun serializedFMUstateSize(c: Long, state: Long, size: IntByReference): Int

    fun serializeFMUstate(c: Long, state: Long, size: IntByReference): Int

    fun deSerializeFMUs(c: Long, serializedState: ByteArray, state: FmuState): Int


    /***************************************************
     * Functions for FMI2 for Co-simulation
     */

    fun step(
            c: Long, currentCommunicationPoint: Double,
            communicationStepSize: Double, noSetFMUStatePriorToCurrentPoint: Boolean): Int

    fun cancelStep(c: Long): Int

    fun setRealInputDerivatives(c: Long, vr: IntArray, order: IntArray, value: DoubleArray): Int

    fun getRealOutputDerivatives(c: Long, vr: IntArray, order: IntArray, value: DoubleArray): Int

    fun getStatus(c: Long, s: Int, value: IntByReference): Int

    fun getRealStatus(c: Long, s: Int, value: DoubleByReference): Int

    fun getIntegerStatus(c: Long, s: Int, value: IntByReference): Int

    fun getStringStatus(c: Long, s: Int, value: StringByReference): Int

    fun getBooleanStatus(c: Long, s: Int, value: BooleanByReference): Int

    fun getMaxStepSize(c: Long, stepSize: DoubleByReference): Int

    /***************************************************
     * Functions for FMI2 for Model Exchange
     */

    fun enterEventMode(c: Long): Int

    fun newDiscreteStates(c: Long, ev: EventInfo): Int

    fun enterContinuousTimeMode(c: Long): Int

    fun setContinuousStates(c: Long, x: DoubleArray): Int

    fun completedIntegratorStep(c: Long, noSetFMUStatePriorToCurrentPoint: Boolean,
                                enterEventMode: BooleanByReference, terminateSimulation: BooleanByReference): Int

    fun setTime(c: Long, time: Double): Int

    fun getDerivatives(c: Long, derivatives: DoubleArray): Int

    fun getEventIndicators(c: Long, eventIndicators: DoubleArray): Int

    fun getContinuousStates(c: Long, x: DoubleArray): Int

    fun getNominalsOfContinuousStates(c: Long, x_nominals: DoubleArray): Int

    companion object {

        fun newInstance(libName: String): IFmiLibrary {

            return SimpleClassLoader().let { cl ->

                cl.findClass("no.mechatronics.sfi.fmi4j.importer.jni.FmiLibrary")!!.let { clazz ->
                    clazz.getDeclaredConstructor(String::class.java).let { constructor ->
                        constructor.isAccessible = true
                        constructor.newInstance(libName) as IFmiLibrary
                    }
                }
            }

        }

    }

}

/**
 * @author Lars Ivar Hatledal
 */
class FmiLibrary private constructor(libName: String) : IFmiLibrary {

    init {
        if (!load(libName)) {
            throw RuntimeException("Unable to load native library '$libName' !")
        }
    }

    private external fun load(libName: String): Boolean

    external override fun close()

    external override fun getVersion(): String

    external override fun getTypesPlatform(): String


    external override fun setDebugLogging(
            c: Long, loggingOn: Boolean, nCategories: Int, categories: Array<String>): Int

    external override fun setupExperiment(
            c: Long, toleranceDefined: Boolean,
            tolerance: Double, startTime: Double, stopTime: Double): Int

    external override fun enterInitializationMode(c: Long): Int

    external override fun exitInitializationMode(c: Long): Int

    external override fun instantiate(
            instanceName: String, type: Int, guid: String,
            resourceLocation: String, visible: Boolean, loggingOn: Boolean): Long

    external override fun terminate(c: Long): Int

    external override fun reset(c: Long): Int

    external override fun freeInstance(c: Long)

    //read
    external override fun getInteger(c: Long, vr: IntArray, ref: IntArray): Int

    external override fun getReal(c: Long, vr: IntArray, ref: DoubleArray): Int

    external override fun getString(c: Long, vr: IntArray, ref: Array<String>): Int

    external override fun getBoolean(c: Long, vr: IntArray, ref: BooleanArray): Int

    //write
    external override fun setInteger(c: Long, vr: IntArray, values: IntArray): Int

    external override fun setReal(c: Long, vr: IntArray, values: DoubleArray): Int

    external override fun setString(c: Long, vr: IntArray, values: Array<String>): Int

    external override fun setBoolean(c: Long, vr: IntArray, values: BooleanArray): Int

    external override fun getDirectionalDerivative(
            c: Long, vUnknown_ref: IntArray,
            vKnownRef: IntArray, dvKnown: DoubleArray, dvUnknown: DoubleArray): Int


    external override fun getFMUstate(c: Long, state: FmuState): Int

    external override fun setFMUstate(c: Long, state: Long): Int

    external override fun freeFMUstate(c: Long, state: FmuState): Int

    external override fun serializedFMUstateSize(c: Long, state: Long, size: IntByReference): Int

    external override fun serializeFMUstate(c: Long, state: Long, size: IntByReference): Int

    external override fun deSerializeFMUs(c: Long, serializedState: ByteArray, state: FmuState): Int


    /***************************************************
     * Functions for FMI2 for Co-simulation
     */

    external override fun step(
            c: Long, currentCommunicationPoint: Double,
            communicationStepSize: Double, noSetFMUStatePriorToCurrentPoint: Boolean): Int

    external override fun cancelStep(c: Long): Int

    external override fun setRealInputDerivatives(c: Long, vr: IntArray, order: IntArray, value: DoubleArray): Int

    external override fun getRealOutputDerivatives(c: Long, vr: IntArray, order: IntArray, value: DoubleArray): Int

    external override fun getStatus(c: Long, s: Int, value: IntByReference): Int

    external override fun getRealStatus(c: Long, s: Int, value: DoubleByReference): Int

    external override fun getIntegerStatus(c: Long, s: Int, value: IntByReference): Int

    external override fun getStringStatus(c: Long, s: Int, value: StringByReference): Int

    external override fun getBooleanStatus(c: Long, s: Int, value: BooleanByReference): Int

    external override fun getMaxStepSize(c: Long, stepSize: DoubleByReference): Int

    /***************************************************
     * Functions for FMI2 for Model Exchange
     */

    external override fun enterEventMode(c: Long): Int

    external override fun newDiscreteStates(c: Long, ev: EventInfo): Int

    external override fun enterContinuousTimeMode(c: Long): Int

    external override fun setContinuousStates(c: Long, x: DoubleArray): Int

    external override fun completedIntegratorStep(c: Long, noSetFMUStatePriorToCurrentPoint: Boolean,
                                         enterEventMode: BooleanByReference, terminateSimulation: BooleanByReference): Int

    external override fun setTime(c: Long, time: Double): Int

    external override fun getDerivatives(c: Long, derivatives: DoubleArray): Int

    external override fun getEventIndicators(c: Long, eventIndicators: DoubleArray): Int

    external override fun getContinuousStates(c: Long, x: DoubleArray): Int

    external override fun getNominalsOfContinuousStates(c: Long, x_nominals: DoubleArray): Int

    companion object {

        init {

            val fileName = libPrefix + "fmi." + libExtension
            val copy = File(fileName).apply {
                deleteOnExit()
            }
            try {
                IFmiLibrary::class.java.classLoader
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
