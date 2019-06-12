/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import java.io.File

class Fmi1ModelExchangeLibrary(
        lib: File,
        modelIdentifier: String
) : Fmi1Library(lib, modelIdentifier) {

    private external fun instantiateModel(p: Long, instanceName: String, guid: String, loggingOn: Boolean): FmiComponent

    private external fun initialize(p: Long, c: FmiComponent, toleranceControlled: Boolean, relativeTolerance: Double): NativeStatus

    private external fun terminate(p: Long, c: FmiComponent): NativeStatus

    private external fun freeModelInstance(p: Long, c: FmiComponent)

    private external fun setTime(p: Long, c: FmiComponent, time: Double): NativeStatus

    private external fun setContinuousStates(p: Long, c: FmiComponent, x: DoubleArray): NativeStatus

    private external fun completedIntegratorStep(p: Long, c: FmiComponent, callEventUpdate: Boolean): NativeStatus

    private external fun getDerivatives(p: Long, c: FmiComponent, derivatives: DoubleArray): NativeStatus

    private external fun getEventIndicators(p: Long, c: FmiComponent, eventIndicators: DoubleArray): NativeStatus

    private external fun getContinuousStates(p: Long, c: FmiComponent, x: DoubleArray): NativeStatus

    private external fun getNominalContinuousStates(p: Long, c: FmiComponent, x_nominals: DoubleArray): NativeStatus

    private external fun getStateValueReferences(p: Long, c: FmiComponent, vrx: ValueReferences): NativeStatus

    fun instantiateModel(instanceName: String, guid: String, loggingOn: Boolean): FmiComponent {
        return instantiateModel(p, instanceName, guid, loggingOn)
    }

    fun initialize(c: FmiComponent, toleranceControlled: Boolean, relativeTolerance: Double): FmiStatus {
        return initialize(p, c, toleranceControlled, relativeTolerance).transform()
    }

    override fun terminate(c: FmiComponent): FmiStatus {
        return terminate(p, c).transform()
    }

    override fun freeInstance(c: FmiComponent) {
        freeModelInstance(p, c)
    }

    fun setTime(c: FmiComponent, time: Double): FmiStatus {
        return setTime(p, c, time).transform()
    }

    fun setContinuousStates(c: FmiComponent, x: DoubleArray): FmiStatus {
        return setContinuousStates(p, c, x).transform()
    }

    fun completedIntegratorStep(c: FmiComponent, callEventUpdate: Boolean): FmiStatus {
        return completedIntegratorStep(p, c, callEventUpdate).transform()
    }

    fun getDerivatives(c: FmiComponent, derivatives: DoubleArray): FmiStatus {
        return getDerivatives(p, c, derivatives).transform()
    }

    fun getEventIndicators(c: FmiComponent, eventIndicators: DoubleArray): FmiStatus {
        return getEventIndicators(p, c, eventIndicators).transform()
    }

    fun getContinuousStates(c: FmiComponent, x: DoubleArray): FmiStatus {
        return getContinuousStates(p, c, x).transform()
    }

    fun getNominalsOfContinuousStates(c: FmiComponent, x_nominals: DoubleArray): FmiStatus {
        return getNominalContinuousStates(p, c, x_nominals).transform()
    }

    fun getStateValueReferences(c: FmiComponent, vrx: ValueReferences): FmiStatus {
        return getStateValueReferences(p, c, vrx).transform()
    }

}

/**
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeLibraryWrapper(
        c: Long,
        library: Fmi1ModelExchangeLibrary
) : Fmi1LibraryWrapper<Fmi1ModelExchangeLibrary>(c, library) {

    fun instantiateModel(instanceName: String, guid: String, loggingOn: Boolean): FmiComponent {
        return library.instantiateModel(instanceName, guid, loggingOn)
    }

    fun initialize(toleranceControlled: Boolean, relativeTolerance: Double): FmiStatus {
        return updateStatus(library.initialize(c, toleranceControlled, relativeTolerance))
    }

    fun setTime(time: Double): FmiStatus {
        return updateStatus((library.setTime(c, time)))
    }

    fun setContinuousStates(x: DoubleArray): FmiStatus {
        return updateStatus((library.setContinuousStates(c, x)))
    }

    fun completedIntegratorStep(): FmiStatus {
        return updateStatus((library.completedIntegratorStep(c, true)))
    }

    fun getDerivatives(derivatives: DoubleArray): FmiStatus {
        return updateStatus((library.getDerivatives(c, derivatives)))
    }

    fun getEventIndicators(eventIndicators: DoubleArray): FmiStatus {
        return updateStatus((library.getEventIndicators(c, eventIndicators)))
    }

    fun getContinuousStates(x: DoubleArray): FmiStatus {
        return updateStatus((library.getContinuousStates(c, x)))
    }

    fun getNominalsOfContinuousStates(x_nominal: DoubleArray): FmiStatus {
        return updateStatus((library.getNominalsOfContinuousStates(c, x_nominal)))
    }

    fun getStateValueReferences(vrx: ValueReferences): FmiStatus {
        return updateStatus(library.getStateValueReferences(c, vrx))
    }

}

class EventInfo {

    var iterationConverged: Boolean = false
    var terminateSimulation: Boolean = false
    var stateValueReferencesChanged: Boolean = false
    var stateValuesChanged: Boolean = false
    var upcomingTimeEvent: Boolean = false
    var nextEventTime: Double = 0.0

    override fun toString(): String {
        return "EventInfo(iterationConverged=$iterationConverged, terminateSimulation=$terminateSimulation, stateValueReferencesChanged=$stateValueReferencesChanged, stateValuesChanged=$stateValuesChanged, upcomingTimeEvent=$upcomingTimeEvent, nextEventTime=$nextEventTime)"
    }

}
