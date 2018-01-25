package no.mechatronics.sfi.fmi4j.fmu.me

import no.mechatronics.sfi.fmi4j.FmiSimulation
import no.mechatronics.sfi.fmi4j.fmu.AbstractFmu
import no.mechatronics.sfi.fmi4j.misc.VariableAccessProvider
import no.mechatronics.sfi.fmi4j.proxy.structs.Fmi2EventInfo
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import org.apache.commons.math3.ode.FirstOrderIntegrator


/**
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeFmuWithIntegrator internal constructor(
        private val fmu: ModelExchangeFmu,
        private val integrator: FirstOrderIntegrator
) : FmiSimulation, VariableAccessProvider by fmu {

    private val states: DoubleArray
    private val nominalStates: DoubleArray
    private val derivatives: DoubleArray

    private val preEventIndicators: DoubleArray
    private val eventIndicators: DoubleArray

    private val eventInfo: Fmi2EventInfo = Fmi2EventInfo()

    override val version
        get() = fmu.version

    override var currentTime: Double = 0.0
        private set

    /**
     * @see AbstractFmu.isTerminated
     */
    override val isInitialized
        get() = fmu.isInitialized

    /**
     * @see AbstractFmu.isTerminated
     */
    override val isTerminated
        get() = fmu.isTerminated

    /**
     * @see AbstractFmu.lastStatus
     */
    override val lastStatus
        get() = fmu.lastStatus

    override val modelDescription = fmu.modelDescription
    override val modelVariables = fmu.modelVariables

    init {

        val numberOfContinuousStates = modelDescription.numberOfContinuousStates
        val numberOfEventIndicators = modelDescription.numberOfEventIndicators

        this.states = DoubleArray(numberOfContinuousStates)
        this.nominalStates = DoubleArray(numberOfContinuousStates)
        this.derivatives = DoubleArray(numberOfContinuousStates)

        this.preEventIndicators = DoubleArray(numberOfEventIndicators)
        this.eventIndicators = DoubleArray(numberOfEventIndicators)

    }

    private val ode: FirstOrderDifferentialEquations by lazy {
        object : FirstOrderDifferentialEquations {

            override fun getDimension(): Int = modelDescription.numberOfContinuousStates
            override fun computeDerivatives(time: Double, y: DoubleArray, yDot: DoubleArray) {
                for ((i, d) in derivatives.withIndex()) {
                    yDot[i] = d
                }
            }
        }
    }



    override fun reset() = fmu.reset()
    override fun terminate() = fmu.terminate()

    override fun close() {
        terminate()
    }

    override fun init() = init(0.0)
    override fun init(start: Double) = init(start, -1.0)

    override fun init(start: Double, stop: Double): Boolean {

        if (fmu.init(start, stop)) {
            currentTime = start

            eventInfo.setNewDiscreteStatesNeededTrue()
            eventInfo.setTerminateSimulationFalse()

            while (eventInfo.getNewDiscreteStatesNeeded()) {
                fmu.newDiscreteStates(eventInfo)
                if (eventInfo.getTerminateSimulation()) {
                    terminate()
                    return false
                }
            }
            fmu.enterContinuousTimeMode()
            fmu.getContinuousStates(states)
            fmu.getNominalsOfContinuousStates(nominalStates)

            return true
        }

        return false

    }

    override fun doStep(dt: Double): Boolean {

        if (dt <= 0) {
            throw IllegalArgumentException("dt must be positive and greater than 0! Was: $dt")
        }

        var time = currentTime
        val stopTime = time + dt

        while (time < stopTime) {

            var tNext = Math.min(time + dt, stopTime);

            val timeEvent = eventInfo.getNextEventTimeDefined() && (eventInfo.nextEventTime <= time)
            if (timeEvent) {
                tNext = eventInfo.nextEventTime
            }

            var stateEvent = false
            if (tNext - time > 1E-13) {
                solve(time, tNext).also { result ->
                    stateEvent = result.stateEvent
                    time = result.time
                }
            } else {
                time = tNext
            }

            fmu.setTime(time)

            val completedIntegratorStep = fmu.completedIntegratorStep()
            if (completedIntegratorStep.terminateSimulation) {
                terminate()
                return false
            }

            val stepEvent = completedIntegratorStep.enterEventMode

            if (timeEvent || stateEvent || stepEvent) {
                fmu.enterEventMode()

                eventInfo.setNewDiscreteStatesNeededTrue()
                eventInfo.setTerminateSimulationFalse()

                while (eventInfo.getNewDiscreteStatesNeeded()) {
                    fmu.newDiscreteStates(eventInfo)
                    if (eventInfo.getTerminateSimulation()) {
                        terminate()
                        return false
                    }
                }
                fmu.enterContinuousTimeMode()
            }


        }
        currentTime = time
        return true
    }

    private data class SolveResult(
            val stateEvent: Boolean,
            val time: Double
    )

    private fun solve(t: Double, tNext: Double): SolveResult {

        fmu.getContinuousStates(states)
        fmu.getDerivatives(derivatives)

        val dt = tNext - t
        val integratedTime = integrator.integrate(ode, t, states, currentTime + dt, states)

        fmu.setContinuousStates(states)

        for (i in preEventIndicators.indices) {
            preEventIndicators[i] = eventIndicators[i]
        }

        fmu.getEventIndicators(eventIndicators)

        var stateEvent = false
        for (i in preEventIndicators.indices) {
            stateEvent = preEventIndicators[i] * eventIndicators[i] < 0
            if (stateEvent)  {
                break
            }
        }

        return SolveResult(stateEvent, integratedTime)

    }

}