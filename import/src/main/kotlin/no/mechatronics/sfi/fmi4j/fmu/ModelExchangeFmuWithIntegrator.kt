package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.Fmi2Simulation
import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2EventInfo
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator


class ModelExchangeFmuWithIntegrator(
        val fmu: ModelExchangeFmu,
        val integrator: FirstOrderIntegrator = EulerIntegrator(1E-2)
) : Fmi2Simulation {

    override val modelDescription = fmu.modelDescription
    override val modelVariables = fmu.modelVariables
    override val currentTime: Double
    get() {
        return fmu.currentTime
    }
    override val fmuFile: FmuFile = fmu.fmuFile

    private val states: DoubleArray
    private val derivatives: DoubleArray

    private val preEventIndicators: DoubleArray
    private val eventIndicators: DoubleArray

    private val eventInfo: Fmi2EventInfo = Fmi2EventInfo()

    val ode: FirstOrderDifferentialEquations by lazy {
        object : FirstOrderDifferentialEquations {
            override fun getDimension(): Int =  modelDescription.numberOfContinuousStates

            override fun computeDerivatives(t: Double, y: DoubleArray?, yDot: DoubleArray?) {
                fmu.getDerivatives(yDot!!)
            }
        }
    }

    init {

        val numberOfContinuousStates = modelDescription.numberOfContinuousStates
        val numberOfEventIndicators = modelDescription.numberOfEventIndicators

        states = DoubleArray(numberOfContinuousStates)
        derivatives = DoubleArray(numberOfContinuousStates)

        preEventIndicators = DoubleArray(numberOfEventIndicators)
        eventIndicators = DoubleArray(numberOfEventIndicators)

    }


     override fun init() = init(0.0)
     override fun init(start: Double) = init(start, -1.0)
     override fun init(start: Double, stop: Double) : Boolean {

        if (fmu.init(start, stop)) {
            eventInfo.setNewDiscreteStatesNeededTrue()
            eventInfo.setTerminateSimulationFalse()

            while (eventInfo.getNewDiscreteStatesNeeded() && !eventInfo.getTerminateSimulation()) {
                fmu.newDiscreteStates(eventInfo)
            }
            fmu.enterContinuousTimeMode()
            fmu.getEventIndicators(eventIndicators)

            return true
        }

        return false

    }

     override fun doStep(dt: Double): Boolean {

         assert(dt > 0)

        var t  = currentTime
        val stopTime =  t + dt

        var tNext: Double
        while ( t < stopTime) {

            tNext = Math.min( t +  dt, stopTime);

            val timeEvent = eventInfo.getNextEventTimeDefined() != false && eventInfo.nextEventTime <= t
            if (timeEvent) {
                tNext = eventInfo.nextEventTime
            }

            var stateEvent = false
            if (tNext -  t > 1E-12) {
                val solve = solve(tNext)
                stateEvent = solve.stateEvent
                t = solve.time
            } else {
                t = tNext
            }

            fmu.setTime( t )

            val completedIntegratorStep =  fmu.completedIntegratorStep()
            if (completedIntegratorStep.terminateSimulation) {
                terminate()
                return false
            }

            val stepEvent = completedIntegratorStep.enterEventMode

            if (timeEvent || stateEvent || stepEvent) {
                fmu.enterEventMode()

                eventInfo.setNewDiscreteStatesNeededTrue()
                eventInfo.setTerminateSimulationFalse()

                while (eventInfo.getNewDiscreteStatesNeeded() && !eventInfo.getTerminateSimulation()) {
                    fmu.newDiscreteStates(eventInfo)
                }
                fmu.enterContinuousTimeMode()
            }

        }
         return true
    }

    private data class SolveResult(
            val stateEvent: Boolean,
            val time: Double
    )

    private fun solve(tNext:Double) : SolveResult {

        fmu.getContinuousStates(states)
        fmu.getDerivatives(derivatives)

        val dt = tNext - currentTime

        integrator.integrate(ode, currentTime, states, currentTime + dt, states)

        fmu.setContinousStates(states)

        for (i in preEventIndicators.indices) {
            preEventIndicators[i] = eventIndicators[i]
        }

        fmu.getEventIndicators(eventIndicators)

        var stateEvent = false
        for (i in preEventIndicators.indices) {
            stateEvent = preEventIndicators[i] * eventIndicators[i] < 0
            if (stateEvent) break
        }

        return SolveResult(stateEvent, tNext)

    }

    override fun terminate() = fmu.terminate()

    fun getLastStatus()  = fmu.getLastStatus()

}