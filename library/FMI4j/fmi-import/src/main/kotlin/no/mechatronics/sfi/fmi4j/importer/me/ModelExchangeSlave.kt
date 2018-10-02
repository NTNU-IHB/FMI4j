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

package no.mechatronics.sfi.fmi4j.importer.me

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.common.FmuSlave
import no.mechatronics.sfi.fmi4j.common.SimpleFmuInstance
import no.mechatronics.sfi.fmi4j.modeldescription.SpecificModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import no.mechatronics.sfi.fmi4j.solvers.Equations
import no.mechatronics.sfi.fmi4j.solvers.Solver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.min

private const val EPS = 1E-13

/**
 * Wraps a Model Exchange instance, turning it into a FmuSlave
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeFmuStepper internal constructor(
        private val fmuInstance: ModelExchangeInstance,
        private val solver: Solver
) : FmuSlave<CoSimulationModelDescription>, SimpleFmuInstance by fmuInstance {

    private val x: DoubleArray
    private val dx: DoubleArray
    private val nominalStates: DoubleArray

    private val z: DoubleArray
    private val pz: DoubleArray

    override val modelDescription: CoSimulationModelDescription by lazy {
        CoSimulationModelDescriptionWrapper(fmuInstance.modelDescription)
    }

    init {

        val numberOfContinuousStates = modelDescription.numberOfContinuousStates
        val numberOfEventIndicators = fmuInstance.modelDescription.numberOfEventIndicators

        this.x = DoubleArray(numberOfContinuousStates)
        this.nominalStates = DoubleArray(numberOfContinuousStates)
        this.dx = DoubleArray(numberOfContinuousStates)

        this.pz = DoubleArray(numberOfEventIndicators)
        this.z = DoubleArray(numberOfEventIndicators)

        this.solver.setEquations(object : Equations {
            override val dimension: Int = modelDescription.numberOfContinuousStates
            override fun computeDerivatives(time: Double, y: DoubleArray, yDot: DoubleArray) {
                for ((index, value) in dx.withIndex()) {
                    yDot[index] = value
                }
            }
        })

    }

    private fun FmiStatus.warnOnStatusNotOK(functionName: String) {
        if (this != FmiStatus.OK) {
            LOG.warn("$functionName returned status: $this")
        }
    }

    private fun eventIteration(): Boolean {

        fmuInstance.eventInfo.newDiscreteStatesNeeded = true
        fmuInstance.eventInfo.terminateSimulation = false

        while (fmuInstance.eventInfo.newDiscreteStatesNeeded) {
            fmuInstance.newDiscreteStates().also {
                it.warnOnStatusNotOK("fmuInstance.newDiscreteStates()")
            }
            if (fmuInstance.eventInfo.terminateSimulation) {
                LOG.debug("eventInfo.getTerminateSimulation() returned true. Terminating FMU...")
                terminate()
                return true
            }
        }

        fmuInstance.enterContinuousTimeMode().also {
            it.warnOnStatusNotOK("fmuInstance.enterContinuousTimeMode()")
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
            fmuInstance.init(start, stop)
            if (eventIteration()) {
                throw IllegalStateException("EventIteration returned false during initialization!")
            }
        } else {
            LOG.warn("Init has already been invoked..")
        }
    }

    override fun doStep(stepSize: Double): Boolean {

        if (!isInitialized) {
            throw IllegalStateException("Init has not been invoked!")
        }

        if (stepSize <= 0) {
            throw IllegalArgumentException("stepSize must be positive and greater than 0! Was: $stepSize")
        }

        var time = simulationTime
        val stopTime = time + stepSize

        while (time < stopTime) {

            var tNext = min(time + stepSize, stopTime)

            val timeEvent = fmuInstance.eventInfo.nextEventTimeDefined && fmuInstance.eventInfo.nextEventTime <= time
            if (timeEvent) {
                tNext = fmuInstance.eventInfo.nextEventTime
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

            fmuInstance.setTime(time).also {
                it.warnOnStatusNotOK("fmuInstance.setTime()")
            }

            var enterEventMode = false
            if (!fmuInstance.modelDescription.completedIntegratorStepNotNeeded) {
                val completedIntegratorStep = fmuInstance.completedIntegratorStep()
                if (completedIntegratorStep.terminateSimulation) {
                    LOG.debug("completedIntegratorStep.terminateSimulation returned true. Terminating FMU...")
                    return false.also {
                        terminate()
                    }
                }
                enterEventMode = completedIntegratorStep.enterEventMode
            }

            if (timeEvent || stateEvent || enterEventMode) {

                fmuInstance.enterEventMode().also {
                    it.warnOnStatusNotOK("fmuInstance.enterEventMode()")
                }

                if (eventIteration()) {
                    return false
                }

            }

        }

        return true

    }

    override fun cancelStep(): Boolean {
        return false
    }

    private fun solve(t: Double, tNext: Double): Pair<Boolean, Double> {

        fmuInstance.getContinuousStates(x)
        fmuInstance.getDerivatives(dx)

        val stepSize = (tNext - t)
        val integratedTime = solver.integrate(t, x, (simulationTime + stepSize), x)

        fmuInstance.setContinuousStates(x)

        System.arraycopy(z, 0, pz, 0, z.size)

        fmuInstance.getEventIndicators(z)

        fun stateEvent(): Boolean {

            for (i in pz.indices) {
                if ((pz[i] * z[i]) < 0) {
                    return true
                }
            }
            return false
        }

        return (stateEvent() to integratedTime)

    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(ModelExchangeFmuStepper::class.java)
    }

}

class CoSimulationModelDescriptionWrapper(
        md: ModelExchangeModelDescription
): SpecificModelDescription by md, CoSimulationModelDescription {

    override val maxOutputDerivativeOrder: Int
        get() = 0
    override val canHandleVariableCommunicationStepSize: Boolean
        get() = true
    override val canInterpolateInputs: Boolean
        get() = false
    override val canRunAsynchronuously: Boolean
        get() = false
}