package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2EventInfo
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.jna.lib.wrapper.Fmi2ModelExchangeLibraryWrapper
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import org.apache.commons.math3.ode.FirstOrderIntegrator
import java.io.File
import java.net.URL


private class ModelExchangeHelper(
        fmuFile: FmuFile,
        visible: Boolean,
        loggingOn: Boolean
) : FmuHelper<Fmi2ModelExchangeLibraryWrapper, ModelExchangeModelDescription>(fmuFile, Fmi2Type.ModelExchange, visible, loggingOn) {

    override val wrapper: Fmi2ModelExchangeLibraryWrapper by lazy {
        Fmi2ModelExchangeLibraryWrapper(fmuFile.getLibraryFolderPath(), fmuFile.getLibraryName(modelDescription))
    }

    override val modelDescription: ModelExchangeModelDescription by lazy {
        ModelExchangeModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())
    }
}



open class ModelExchangeFmu @JvmOverloads constructor(

        fmuFile: FmuFile,
        val integrator: FirstOrderIntegrator,
        visible: Boolean = false,
        loggingOn: Boolean = false

) : Fmu<Fmi2ModelExchangeLibraryWrapper, ModelExchangeModelDescription>(ModelExchangeHelper(fmuFile, visible, loggingOn)) {

    @JvmOverloads
    constructor(file: File, firstOrderIntegrator: FirstOrderIntegrator, visible: Boolean = false, loggingOn: Boolean = false) : this(FmuFile(file), firstOrderIntegrator, visible, loggingOn)

    @JvmOverloads
    constructor(url: URL, firstOrderIntegrator: FirstOrderIntegrator, visible: Boolean = false, loggingOn: Boolean = false) : this(FmuFile(url), firstOrderIntegrator, visible, loggingOn)

    private val eventInfo: Fmi2EventInfo

    private val states: DoubleArray
    private val derivatives: DoubleArray

    private val preEventIndicators: DoubleArray
    private val eventIndicators: DoubleArray

    val ode: FirstOrderDifferentialEquations by lazy {
        Ode()
    }

    init {

        eventInfo = Fmi2EventInfo()

        val numberOfContinuousStates = modelDescription.numberOfContinuousStates
        val numberOfEventIndicators = modelDescription.numberOfEventIndicators

        states = DoubleArray(numberOfContinuousStates)
        derivatives = DoubleArray(numberOfContinuousStates)

        preEventIndicators = DoubleArray(numberOfEventIndicators)
        eventIndicators = DoubleArray(numberOfEventIndicators)

    }


    inner class Ode internal constructor() : FirstOrderDifferentialEquations {

        override fun getDimension(): Int =  modelDescription.numberOfContinuousStates

        override fun computeDerivatives(t: Double, y: DoubleArray?, yDot: DoubleArray?) {
             getDerivatives(yDot!!)
        }

    }

    override fun init(setup: ExperimentSetup): Boolean {
        if (!super.init(setup)) {
            return false
        } else {
            eventInfo.setNewDiscreteStatesNeededTrue()
            eventInfo.setTerminateSimulationFalse()

            while (eventInfo.getNewDiscreteStatesNeeded() && !eventInfo.getTerminateSimulation()) {
                newDiscreteStates()
            }
            enterContinuousTimeMode()
            getEventIndicators(eventIndicators)
            return true
        }
    }

    @JvmOverloads
    fun step(microStep: Double, macroStep: Double, callback: Runnable? = null) {

        val stopTime = currentTime + macroStep

        if (stopTime < currentTime) {
            throw IllegalArgumentException("stopTime < currentTime")
        }

        var tNext: Double
        while (currentTime < stopTime) {

            callback?.run()

            tNext = Math.min(currentTime + microStep, stopTime);

            val timeEvent = eventInfo.getNextEventTimeDefined() != false && eventInfo.nextEventTime <= currentTime
            if (timeEvent) {
                tNext = eventInfo.nextEventTime
            }

            var stateEvent = false
            if (tNext - currentTime > 1E-12) {
                val solve = solve(tNext)
                stateEvent = solve.first
                currentTime = solve.second
            } else {
                currentTime = tNext
            }

            setTime(currentTime)

            val completedIntegratorStep = completedIntegratorStep()
            if (completedIntegratorStep.second) {
                terminate()
                throw RuntimeException("FMU needed to terminate!")
            }

            val stepEvent = completedIntegratorStep.first

            if (timeEvent || stateEvent || stepEvent) {
                enterEventMode()

                eventInfo.setNewDiscreteStatesNeededTrue()
                eventInfo.setTerminateSimulationFalse()

                while (eventInfo.getNewDiscreteStatesNeeded() && !eventInfo.getTerminateSimulation()) {
                    newDiscreteStates()
                }
                enterContinuousTimeMode()
            }

        }
    }


    private fun solve(tNext:Double) : Pair<Boolean, Double> {

        getContinuousStates(states)
        getDerivatives(derivatives)

        val dt = tNext - currentTime

        integrator.integrate(ode, currentTime, states, currentTime+dt, states)

        setContinousStates(states)

        for (i in preEventIndicators.indices) {
            preEventIndicators[i] = eventIndicators[i]
        }

        getEventIndicators(eventIndicators)

        var stateEvent: Boolean = false
        for (i in preEventIndicators.indices) {
            stateEvent = preEventIndicators[i] * eventIndicators[i] < 0
            if (stateEvent) break
        }

        return Pair(stateEvent, tNext)

    }


    fun setTime(time: Double) {
        currentTime = time
        wrapper.setTime(currentTime)
    }

    fun setContinousStates(x: DoubleArray) = wrapper.setContinousStates(x)


    fun enterEventMode() = wrapper.enterEventMode()


    fun enterContinuousTimeMode() = wrapper.enterContinuousTimeMode()


    fun newDiscreteStates() = wrapper.newDiscreteStates(eventInfo)


    fun completedIntegratorStep(): Pair<Boolean, Boolean> = wrapper.completedIntegratorStep(true)


    fun getDerivatives(derivatives: DoubleArray) = wrapper.getDerivatives(derivatives)


    fun getEventIndicators(eventIndicators: DoubleArray) = wrapper.getEventIndicators(eventIndicators)


    fun getContinuousStates(x: DoubleArray) = wrapper.getContinuousStates(x)


    fun getNominalsOfContinuousStates(x_nominal: DoubleArray) = wrapper.getNominalsOfContinuousStates(x_nominal)


}


