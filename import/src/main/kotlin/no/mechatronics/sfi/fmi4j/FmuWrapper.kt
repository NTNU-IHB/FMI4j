package no.mechatronics.sfi.fmi4j

import com.sun.jna.Pointer
import no.mechatronics.sfi.fmi4j.fmu.CoSimulationFmu
import no.mechatronics.sfi.fmi4j.fmu.Fmu
import no.mechatronics.sfi.fmi4j.fmu.IFmu
import no.mechatronics.sfi.fmi4j.fmu.ModelExchangeFmu
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2EventInfo
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator



class MEWrapper(
        val fmu: ModelExchangeFmu,
        val integrator: FirstOrderIntegrator
) {

    val modelDescription = fmu.modelDescription
    val modelVariables = fmu.modelVariables
    val currentTime: Double
    get() {
        return fmu.currentTime
    }

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

     fun init() {

         fmu.init()

        eventInfo.setNewDiscreteStatesNeededTrue()
        eventInfo.setTerminateSimulationFalse()

        while (eventInfo.getNewDiscreteStatesNeeded() && !eventInfo.getTerminateSimulation()) {
            fmu.newDiscreteStates(eventInfo)
        }
        fmu.enterContinuousTimeMode()
        fmu.getEventIndicators(eventIndicators)
    }

     fun doStep(dt: Double) {

         var t  = currentTime

        val stopTime =  t + dt

        if (stopTime <  t) {
            throw IllegalArgumentException("stopTime < currentTime")
        }

        var tNext = dt
        while ( t < stopTime) {

          //  println("microStep= " + integrator.currentSignedStepsize)

            tNext = Math.min( t +  dt, stopTime);

            val timeEvent = eventInfo.getNextEventTimeDefined() != false && eventInfo.nextEventTime <= t
            if (timeEvent) {
                tNext = eventInfo.nextEventTime
            }

            var stateEvent = false
            if (tNext -  t > 1E-12) {
                val solve = solve(tNext)
                stateEvent = solve.first
                t = solve.second
            } else {
                t = tNext
            }

            fmu.setTime( t)

            val completedIntegratorStep =  fmu.completedIntegratorStep()
            if (completedIntegratorStep.second) {
                terminate()
                throw RuntimeException("FMU needed to terminate!")
            }

            val stepEvent = completedIntegratorStep.first

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
    }

    private fun solve(tNext:Double) : Pair<Boolean, Double> {

        fmu.getContinuousStates(states)
        fmu.getDerivatives(derivatives)

        val dt = tNext - currentTime

        val t = integrator.integrate(ode, currentTime, states, currentTime + dt, states)

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

        return Pair(stateEvent, t)

    }

    fun terminate() = fmu.terminate()

}