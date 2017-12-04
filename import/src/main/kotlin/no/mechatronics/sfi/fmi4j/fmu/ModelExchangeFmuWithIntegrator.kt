/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
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

import no.mechatronics.sfi.fmi4j.Fmi2Simulation
import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2EventInfo
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL


class ModelExchangeFmuWithIntegrator : ModelExchangeFmu, Fmi2Simulation {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ModelExchangeFmu::class.java)

        @JvmStatic
        fun newBuilder(fmuFile: FmuFile) = Builder(fmuFile)
        @JvmStatic
        fun newBuilder(url: URL) = Builder(FmuFile(url))
        @JvmStatic
        inline fun newBuilder(file: File) = Builder(FmuFile(file))
        fun build(fmuFile: FmuFile, block: Builder.() -> Unit) = Builder(fmuFile).apply(block).build()
        fun build(url: URL, block: Builder.() -> Unit) = Builder(FmuFile(url)).apply(block).build()
        fun build(file: File, block: Builder.() -> Unit) = Builder(FmuFile(file)).apply(block).build()

    }

    class Builder(
             fmuFile: FmuFile
    ) : ModelExchangeFmu.Builder(fmuFile) {

        internal lateinit var integrator: FirstOrderIntegrator

        fun integrator(integrator: FirstOrderIntegrator) = apply { this.integrator = integrator }

        override fun visible(value: Boolean): Builder {
            super.visible(value)
            return this
        }

        override fun loggingOn(value: Boolean): Builder {
            super.loggingOn(value)
            return this
        }

        override fun build() : ModelExchangeFmuWithIntegrator{
             if (integrator == null) {
                 integrator = EulerIntegrator(1E-3)
             }
             return ModelExchangeFmuWithIntegrator(this)
        }

    }

    private val integrator: FirstOrderIntegrator
    private val states: DoubleArray
    private val derivatives: DoubleArray

    private val preEventIndicators: DoubleArray
    private val eventIndicators: DoubleArray

    private val eventInfo: Fmi2EventInfo = Fmi2EventInfo()

    private constructor(builder: Builder) : super(builder) {

        integrator = builder.integrator

        val numberOfContinuousStates = modelDescription.numberOfContinuousStates
        val numberOfEventIndicators = modelDescription.numberOfEventIndicators

        states = DoubleArray(numberOfContinuousStates)
        derivatives = DoubleArray(numberOfContinuousStates)

        preEventIndicators = DoubleArray(numberOfEventIndicators)
        eventIndicators = DoubleArray(numberOfEventIndicators)
    }

     override fun init(start: Double, stop: Double) : Boolean {

        if (super.init(start, stop)) {
            eventInfo.setNewDiscreteStatesNeededTrue()
            eventInfo.setTerminateSimulationFalse()

            while (eventInfo.getNewDiscreteStatesNeeded()) {
                newDiscreteStates(eventInfo)
                if (eventInfo.getTerminateSimulation()) {
                    terminate()
                    return false
                }
            }
            enterContinuousTimeMode()
           // fmu.getContinuousStates(states)
            getEventIndicators(eventIndicators)

            return true
        }

        return false

    }

     override fun doStep(dt: Double): Boolean {

        assert(dt > 0)

        var time  = currentTime
        val stopTime =  time + dt

        var tNext: Double
        while ( time < stopTime) {

            tNext = Math.min( time +  dt, stopTime);

            val timeEvent = eventInfo.getNextEventTimeDefined() != false && eventInfo.nextEventTime <= time
            if (timeEvent) {
                tNext = eventInfo.nextEventTime
            }

            var stateEvent = false
            if (tNext -  time > 1E-12) {
                val solve = solve(tNext)
                stateEvent = solve.stateEvent
                time = solve.time
            } else {
                time = tNext
            }

            setTime( time )

            val completedIntegratorStep =  completedIntegratorStep()
            if (completedIntegratorStep.terminateSimulation) {

                terminate()
                return false
            }

            val stepEvent = completedIntegratorStep.enterEventMode

            if (timeEvent || stateEvent || stepEvent) {
                enterEventMode()

                eventInfo.setNewDiscreteStatesNeededTrue()
                eventInfo.setTerminateSimulationFalse()

                while (eventInfo.getNewDiscreteStatesNeeded() && !eventInfo.getTerminateSimulation()) {
                    newDiscreteStates(eventInfo)
                }
                enterContinuousTimeMode()
            }

        }
         return true
    }

    private data class SolveResult(
            val stateEvent: Boolean,
            val time: Double
    )

    private fun solve(tNext:Double) : SolveResult {

        getContinuousStates(states)
        getDerivatives(derivatives)

        val dt = tNext - currentTime
        val t = integrator.integrate(ode, currentTime, states, currentTime + dt, states)

        setContinousStates(states)

        for (i in preEventIndicators.indices) {
            preEventIndicators[i] = eventIndicators[i]
        }

        getEventIndicators(eventIndicators)

        var stateEvent = false
        for (i in preEventIndicators.indices) {
            stateEvent = preEventIndicators[i] * eventIndicators[i] < 0
            if (stateEvent) break
        }

        return SolveResult(stateEvent, t)

    }

}