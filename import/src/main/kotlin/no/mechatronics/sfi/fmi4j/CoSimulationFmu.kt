package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import no.mechatronics.sfi.fmi4j.proxy.CoSimulationLibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2StatusKind
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CoSimulationFmu internal constructor(
        fmuFile: FmuFile,
        modelDescription: CoSimulationModelDescription,
        wrapper: CoSimulationLibraryWrapper
) : AbstractFmu<CoSimulationModelDescription, CoSimulationLibraryWrapper>(fmuFile, modelDescription, wrapper), FmiSimulation {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(CoSimulationFmu::class.java)
    }

    override var currentTime: Double = 0.0
        private set

    override fun init(start: Double, stop: Double): Boolean {
        return super.init(start, stop).also {
            currentTime = start
        }

    }

    override fun doStep(dt: Double) : Boolean {

        if (!isInitialized) {
            LOG.warn("Calling doStep without having called init(), " +
                    "remember that you have to call init() again after a call to reset()!")
            return false
        }

        val status = wrapper.doStep(currentTime, dt, true)
        currentTime += dt

        return status == Fmi2Status.OK
    }

    fun cancelStep() = wrapper.cancelStep()

    fun setRealInputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.setRealInputDerivatives(vr, order, value)

    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.getRealOutputDerivatives(vr, order, value)

    fun getStatus(s: Fmi2StatusKind) = wrapper.getStatus(s)
    fun getRealStatus(s: Fmi2StatusKind) = wrapper.getRealStatus(s)
    fun getIntegerStatus(s: Fmi2StatusKind) = wrapper.getIntegerStatus(s)
    fun getBooleanStatus(s: Fmi2StatusKind) = wrapper.getBooleanStatus(s)
    fun getStringStatus(s: Fmi2StatusKind) = wrapper.getStringStatus(s)

}