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

package no.ntnu.ihb.fmi4j.importer.fmi1.jni

import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.importer.fmi1.FmiStatusKind
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.util.BooleanByReference
import no.ntnu.ihb.fmi4j.util.DoubleByReference
import no.ntnu.ihb.fmi4j.util.IntByReference
import no.ntnu.ihb.fmi4j.util.StringByReference

/**
 * @author Lars Ivar Hatledal
 */
class Fmi1CoSimulationLibrary(
        libName: String,
        modelIdentifier: String
) : Fmi1Library(libName, modelIdentifier) {

    private external fun instantiateSlave(p: Long, instanceName: String, guid: String,
                                          fmuLocation: String, visible: Boolean, interactive: Boolean, loggingOn: Boolean): FmiComponent

    private external fun initializeSlave(p: Long, c: FmiComponent, startTime: Double, stopTime: Double): NativeStatus

    private external fun doStep(p: Long, c: FmiComponent, currentCommunicationPoint: Double,
                                communicationStepSize: Double, newStep: Boolean): NativeStatus

    private external fun reset(p: Long, c: FmiComponent): NativeStatus

    private external fun terminateSlave(p: Long, c: FmiComponent): NativeStatus

    private external fun freeSlaveInstance(p: Long, c: FmiComponent)


    private external fun setRealInputDerivatives(p: Long, c: FmiComponent, vr: ValueReferences, order: IntArray,
                                                 value: DoubleArray): NativeStatus

    private external fun getRealOutputDerivatives(p: Long, c: FmiComponent, vr: ValueReferences, order: IntArray,
                                                  value: DoubleArray): NativeStatus

    private external fun getStatus(p: Long, c: FmiComponent, s: Int, value: IntByReference): NativeStatus

    private external fun getIntegerStatus(p: Long, c: FmiComponent, s: Int, value: IntByReference): NativeStatus

    private external fun getRealStatus(p: Long, c: FmiComponent, s: Int, value: DoubleByReference): NativeStatus

    private external fun getStringStatus(p: Long, c: FmiComponent, s: Int, value: StringByReference): NativeStatus

    private external fun getBooleanStatus(p: Long, c: FmiComponent, s: Int, value: BooleanByReference): NativeStatus

    fun instantiateSlave(instanceName: String, guid: String,
                         fmuLocation: String, visible: Boolean, interactive: Boolean, loggingOn: Boolean): Long {
        return instantiateSlave(p, instanceName, guid, fmuLocation, visible, interactive, loggingOn)
    }

    fun initializeSlave(c: FmiComponent, startTime: Double, stopTime: Double): FmiStatus {
        return initializeSlave(p, c, startTime, stopTime).transform()
    }

    fun doStep(c: FmiComponent,
               currentCommunicationPoint: Double,
               communicationStepSize: Double,
               newStep: Boolean): FmiStatus {
        return doStep(p, c, currentCommunicationPoint, communicationStepSize, newStep).transform()
    }

    fun reset(c: FmiComponent): FmiStatus {
        return reset(p, c).transform()
    }


    override fun terminate(c: FmiComponent): FmiStatus {
        return terminateSlave(p, c).transform()
    }

    override fun freeInstance(c: FmiComponent) {
        freeSlaveInstance(p, c)
    }

    fun setRealInputDerivatives(c: FmiComponent,
                                vr: ValueReferences, order: IntArray,
                                value: DoubleArray): FmiStatus {
        return setRealInputDerivatives(p, c, vr, order, value).transform()
    }

    fun getRealOutputDerivatives(c: FmiComponent,
                                 vr: ValueReferences, order: IntArray,
                                 value: DoubleArray): FmiStatus {
        return getRealOutputDerivatives(p, c, vr, order, value).transform()
    }

    fun getStatus(c: FmiComponent, s: Int, value: IntByReference): FmiStatus {
        return getStatus(p, c, s, value).transform()
    }

    fun getIntegerStatus(c: FmiComponent, s: Int, value: IntByReference): FmiStatus {
        return getIntegerStatus(p, c, s, value).transform()
    }

    fun getRealStatus(c: FmiComponent, s: Int, value: DoubleByReference): FmiStatus {
        return getRealStatus(p, c, s, value).transform()
    }

    fun getStringStatus(c: FmiComponent, s: Int, value: StringByReference): FmiStatus {
        return getStringStatus(p, c, s, value).transform()
    }

    fun getBooleanStatus(c: FmiComponent, s: Int, value: BooleanByReference): FmiStatus {
        return getBooleanStatus(p, c, s, value).transform()
    }

}

/**
 *
 * @author Lars Ivar Hatledal
 */
class CoSimulationLibraryWrapper(
        c: Long,
        library: Fmi1CoSimulationLibrary
) : Fmi1LibraryWrapper<Fmi1CoSimulationLibrary>(c, library) {

    fun instantiateSlave(instanceName: String, guid: String,
                         fmuLocation: String, visible: Boolean, interactive: Boolean, loggingOn: Boolean): Long {
        return library.instantiateSlave(instanceName, guid, fmuLocation, visible, interactive, loggingOn)
    }

    fun initializeSlave(startTime: Double, stopTime: Double): FmiStatus {
        return library.initializeSlave(c, startTime, stopTime)
    }

    fun doStep(t: Double, dt: Double, newStep: Boolean): FmiStatus {
        return updateStatus(library.doStep(c, t, dt, newStep))
    }

    fun reset(): FmiStatus {
        return updateStatus(library.reset(c)).also { status ->
            if (status == FmiStatus.OK) {
                isTerminated = false
            }
        }
    }

    fun setRealInputDerivatives(vr: ValueReferences, order: IntArray, value: DoubleArray): FmiStatus {
        return updateStatus(library.setRealInputDerivatives(c, vr, order, value))
    }

    fun getRealOutputDerivatives(vr: ValueReferences, order: IntArray, value: DoubleArray): FmiStatus {
        return updateStatus(library.getRealOutputDerivatives(c, vr, order, value))
    }

    fun getStatus(s: FmiStatusKind): FmiStatus {
        return IntByReference().let {
            updateStatus(library.getStatus(c, s.code, it))
            FmiStatus.valueOf(it.value)
        }
    }

    fun getIntegerStatus(s: FmiStatusKind): Int {
        return IntByReference().let {
            updateStatus(library.getIntegerStatus(c, s.code, it))
            it.value
        }

    }

    fun getRealStatus(s: FmiStatusKind): Double {
        return DoubleByReference().let {
            updateStatus(library.getRealStatus(c, s.code, it))
            it.value
        }

    }

    fun getBooleanStatus(s: FmiStatusKind): Boolean {
        return BooleanByReference().let {
            updateStatus(library.getBooleanStatus(c, s.code, it))
            it.value
        }
    }

    fun getStringStatus(s: FmiStatusKind): String {
        return StringByReference().let {
            updateStatus((library.getStringStatus(c, s.code, it)))
            it.value
        }

    }

}
