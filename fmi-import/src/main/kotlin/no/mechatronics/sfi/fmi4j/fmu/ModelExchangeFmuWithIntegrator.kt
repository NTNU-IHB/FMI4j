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

package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.FmiSimulation
import no.mechatronics.sfi.fmi4j.proxy.v2.structs.Fmi2EventInfo
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import org.apache.commons.math3.ode.FirstOrderIntegrator


/**
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeFmuWithIntegrator internal constructor(
        internal val fmu: ModelExchangeFmu,
        private val integrator: FirstOrderIntegrator
) : FmiSimulation {

    private val states: DoubleArray
    private val nominalStates: DoubleArray
    private val derivatives: DoubleArray

    private val preEventIndicators: DoubleArray
    private val eventIndicators: DoubleArray

    private val eventInfo: Fmi2EventInfo = Fmi2EventInfo()

    override var currentTime: Double = 0.0
        private set

    /**
     * @see AbstractFmuInstance.isTerminated
     */
    override val isInitialized
        get() = fmu.isInitialized

    /**
     * @see AbstractFmuInstance.isTerminated
     */
    override val isTerminated
        get() = fmu.isTerminated

    /**
     * @see AbstractFmuInstance.lastStatus
     */
    override val lastStatus
        get() = fmu.lastStatus

    override val modelDescription = fmu.modelDescription
    override val modelVariables = fmu.modelVariables

    override val variableAccessor = fmu.variableAccessor

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
                for ((index, value) in derivatives.withIndex()) {
                    yDot[index] = value
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

            val timeEvent = eventInfo.getNextEventTimeDefined() && (eventInfo.getNextEventTime() <= time)
            if (timeEvent) {
                tNext = eventInfo.getNextEventTime()
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

        fun stateEvent() : Boolean {
            for (i in preEventIndicators.indices) {
                if (preEventIndicators[i] * eventIndicators[i] < 0) {
                    return true
                }
            }
            return false
        }

        return SolveResult(stateEvent(), integratedTime)

    }

}