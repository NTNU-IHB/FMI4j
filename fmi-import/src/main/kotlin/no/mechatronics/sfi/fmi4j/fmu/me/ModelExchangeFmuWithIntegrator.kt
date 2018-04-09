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

package no.mechatronics.sfi.fmi4j.fmu.me

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.fmu.FmiSimulation
import no.mechatronics.sfi.fmi4j.fmu.Fmu
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private const val EPS = 1E-13

/**
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeFmuWithIntegrator internal constructor(
        internal val fmu: ModelExchangeFmu,
        private val integrator: FirstOrderIntegrator
) : FmiSimulation, Fmu by fmu {

    private val x: DoubleArray
    private val nominalStates: DoubleArray
    private val dx: DoubleArray

    private val pz: DoubleArray
    private val z: DoubleArray

    override var currentTime: Double = 0.0
        private set

    override val modelDescription = fmu.modelDescription

    init {

        val numberOfContinuousStates = modelDescription.numberOfContinuousStates
        val numberOfEventIndicators = modelDescription.numberOfEventIndicators

        this.x = DoubleArray(numberOfContinuousStates)
        this.nominalStates = DoubleArray(numberOfContinuousStates)
        this.dx = DoubleArray(numberOfContinuousStates)

        this.pz = DoubleArray(numberOfEventIndicators)
        this.z = DoubleArray(numberOfEventIndicators)

    }

    private val ode: FirstOrderDifferentialEquations by lazy {
        object : FirstOrderDifferentialEquations {

            override fun getDimension(): Int = modelDescription.numberOfContinuousStates
            override fun computeDerivatives(time: Double, y: DoubleArray, yDot: DoubleArray) {
                for ((index, value) in dx.withIndex()) {
                    yDot[index] = value
                }
            }
        }
    }

    private fun FmiStatus.warnOnStatusNotOK(functionName: String) {
        if (this != FmiStatus.OK) {
            LOG.warn("$functionName return status $this")
        }
    }

    private fun eventIteration(): Boolean {

        fmu.eventInfo.setNewDiscreteStatesNeededTrue()
        fmu.eventInfo.setTerminateSimulationFalse()

        while (fmu.eventInfo.getNewDiscreteStatesNeeded()) {
            fmu.newDiscreteStates().also {
                it.warnOnStatusNotOK("fmu.newDiscreteStates()")
            }
            if (fmu.eventInfo.getTerminateSimulation()) {
                LOG.debug("eventInfo.getTerminateSimulation() returned true. Terminating FMU...")
                terminate()
                return true
            }
        }

        fmu.enterContinuousTimeMode().also {
            it.warnOnStatusNotOK("fmu.enterContinuousTimeMode()")
        }

        return false
    }

    override fun init() {
        init(0.0)
    }

    override fun init(start: Double) {
        init(start, 0.0)
    }

    override fun init(start: Double, stop: Double) {

        if (!isInitialized) {
            fmu.init(start, stop)
            currentTime = start
            if (eventIteration()) {
                throw IllegalArgumentException()
            }
        }

    }

    override fun doStep(stepSize: Double): Boolean {

        if (stepSize <= 0) {
            throw IllegalArgumentException("stepSize must be positive and greater than 0! Was: $stepSize")
        }

        var time: Double = currentTime
        val stopTime: Double = time + stepSize

        while (time < stopTime) {

            var tNext = Math.min(time + stepSize, stopTime)

            val timeEvent = fmu.eventInfo.getNextEventTimeDefined() && fmu.eventInfo.getNextEventTime() <= time
            if (timeEvent) {
                tNext = fmu.eventInfo.getNextEventTime()
            }

            var stateEvent = false
            if ((tNext - time) > EPS) {
                solve(time, tNext).also { result ->
                    stateEvent = result.first
                    time = result.second
                }
            } else {
                time = tNext
            }

            fmu.setTime(time).also {
                it.warnOnStatusNotOK("fmu.setTime()")
            }

            var enterEventMode = false
            if (!modelDescription.completedIntegratorStepNotNeeded) {
                val completedIntegratorStep = fmu.completedIntegratorStep()
                if (completedIntegratorStep.terminateSimulation) {
                    LOG.debug("completedIntegratorStep.terminateSimulation returned true. Terminating FMU...")
                    terminate()
                    return false
                }
                enterEventMode = completedIntegratorStep.enterEventMode
            }

            if (timeEvent || stateEvent || enterEventMode) {

                fmu.enterEventMode().also {
                    it.warnOnStatusNotOK("fmu.enterEventMode()")
                }

                if (eventIteration()) {
                    return false
                }

            }

        }

        currentTime = time
        return true

    }

    private fun solve(t: Double, tNext: Double): Pair<Boolean, Double> {

        fmu.getContinuousStates(x)
        fmu.getDerivatives(dx)

        val dt = (tNext - t)
        val integratedTime = integrator.integrate(ode, t, x, (currentTime + dt), x)

        fmu.setContinuousStates(x)

        System.arraycopy(z, 0, pz, 0, z.size)

        fmu.getEventIndicators(z)

        fun stateEvent(): Boolean {

            for (i in pz.indices) {
                if (pz[i] * z[i] < 0) {
                    return true
                }
            }
            return false
        }

        return stateEvent() to integratedTime

    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(ModelExchangeFmuWithIntegrator::class.java)
    }

}