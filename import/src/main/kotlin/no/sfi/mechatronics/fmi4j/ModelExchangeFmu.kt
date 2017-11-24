package no.sfi.mechatronics.fmi4j

import com.sun.jna.Native
import com.sun.jna.platform.unix.solaris.LibKstat
import no.sfi.mechatronics.fmi4j.jna.Fmi2EventInfo
import no.sfi.mechatronics.fmi4j.jna.Fmi2ModelExchangeLibrary
import no.sfi.mechatronics.fmi4j.jna.Fmi2Type
import no.sfi.mechatronics.fmi4j.jna.ModelExchangeLibraryWrapper
import no.sfi.mechatronics.fmi4j.modeldescription.ModelExchangeModelDescription
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import org.apache.commons.math3.ode.FirstOrderIntegrator
import java.io.File
import java.net.URL


open class ModelExchangeFmu(

        fmuFile: FmuFile,
        val integrator: FirstOrderIntegrator,
        visible: Boolean,
        loggingOn: Boolean

) : Fmu<ModelExchangeLibraryWrapper, ModelExchangeModelDescription>(ModelExchangeHelper(fmuFile, visible, loggingOn)) {

    constructor(fmuFile: FmuFile, firstOrderIntegrator: FirstOrderIntegrator) : this(fmuFile, firstOrderIntegrator, false,  false)

    constructor(file: File, firstOrderIntegrator: FirstOrderIntegrator, visible: Boolean, loggingOn: Boolean) : this(FmuFile(file), firstOrderIntegrator, visible, loggingOn)
    constructor(url: URL, firstOrderIntegrator: FirstOrderIntegrator, visible: Boolean, loggingOn: Boolean) : this(FmuFile(url), firstOrderIntegrator, visible, loggingOn)
    constructor(file: File, firstOrderIntegrator: FirstOrderIntegrator) : this(FmuFile(file), firstOrderIntegrator, false, false)
    constructor(url: URL, firstOrderIntegrator: FirstOrderIntegrator) : this(FmuFile(url), firstOrderIntegrator, false, false)

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

        override fun getDimension(): Int {
            return modelDescription.numberOfContinuousStates
        }

        override fun computeDerivatives(t: Double, y: DoubleArray?, yDot: DoubleArray?) {
            return getDerivatives(yDot!!)
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

    fun setContinousStates(x: DoubleArray) {
        wrapper.setContinousStates(x)
    }

    fun enterEventMode() {
        wrapper.enterEventMode()
    }

    fun enterContinuousTimeMode() {
        wrapper.enterContinuousTimeMode()
    }

    fun newDiscreteStates() {
        wrapper.newDiscreteStates(eventInfo)
    }

    fun completedIntegratorStep(): Pair<Boolean, Boolean> {
        return wrapper.completedIntegratorStep(true)
    }

    fun getDerivatives(derivatives: DoubleArray) {
        wrapper.getDerivatives(derivatives)
    }

    fun getEventIndicators(eventIndicators: DoubleArray) {
        wrapper.getEventIndicators(eventIndicators)
    }

    fun getContinuousStates(x: DoubleArray) {
        wrapper.getContinuousStates(x)
    }

    fun getNominalsOfContinuousStates(x_nominal: DoubleArray) {
        wrapper.getNominalsOfContinuousStates(x_nominal)
    }

}


private class ModelExchangeHelper(
        fmuFile: FmuFile,
        visible: Boolean,
        loggingOn: Boolean
) : FmuHelper<ModelExchangeLibraryWrapper, ModelExchangeModelDescription>(fmuFile, Fmi2Type.ModelExchange, visible, loggingOn) {

    override val wrapper: ModelExchangeLibraryWrapper by lazy {
        System.setProperty("jna.library.path", fmuFile.getLibraryFolderPath())
        ModelExchangeLibraryWrapper(Native.loadLibrary(fmuFile.getLibraryName(modelDescription), Fmi2ModelExchangeLibrary::class.java)!!)
    }

    override val modelDescription: ModelExchangeModelDescription by lazy {
        ModelExchangeModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())
    }
}


