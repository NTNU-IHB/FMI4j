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

package no.ntnu.ihb.fmi4j.importer.me

import no.ntnu.ihb.fmi4j.common.FmiStatus
import no.ntnu.ihb.fmi4j.common.FmuSlave
import no.ntnu.ihb.fmi4j.common.SimpleFmuInstance
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationAttributes
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.CommonModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.ModelExchangeModelDescription
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.min

/**
 * Wraps a Model Exchange instance, turning it into a FmuSlave
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeFmuStepper internal constructor(
        private val fmuInstance: ModelExchangeInstance,
        private val solver: no.ntnu.ihb.fmi4j.solvers.Solver
) : FmuSlave, SimpleFmuInstance by fmuInstance {

    private val x: DoubleArray

    private val z: DoubleArray
    private val pz: DoubleArray

    init {

        val numberOfContinuousStates = fmuInstance.modelDescription.numberOfContinuousStates
        val numberOfEventIndicators = fmuInstance.modelDescription.numberOfEventIndicators

        this.x = DoubleArray(numberOfContinuousStates)

        this.z = DoubleArray(numberOfEventIndicators)
        this.pz = DoubleArray(numberOfEventIndicators)

        this.solver.setEquations(object : no.ntnu.ihb.fmi4j.solvers.Equations {
            override val dimension: Int = numberOfContinuousStates
            override fun computeDerivatives(time: Double, y: DoubleArray, yDot: DoubleArray) {
                fmuInstance.setTime(time)
                fmuInstance.setContinuousStates(y)
                fmuInstance.getDerivatives(yDot)
            }
        })

    }

    override val modelDescription: CoSimulationModelDescription by lazy {
        CoSimulationModelDescriptionWrapper(fmuInstance.modelDescription)
    }

    override fun simpleSetup(): Boolean {
        return simpleSetup(0.0,0.0,0.0)
    }

    override fun simpleSetup(start: Double, stop: Double, tolerance: Double): Boolean {
        return fmuInstance.setup(start, stop, tolerance) && fmuInstance.enterInitializationMode() && exitInitializationMode()
    }

    override fun exitInitializationMode(): Boolean {

        if (!fmuInstance.exitInitializationMode()) {
            return false
        }

        if (eventIteration()) {
            LOG.error("EventIteration returned false during initialization!")
            return false
        }
        return true
    }

    override fun doStep(stepSize: Double): Boolean {

        if (stepSize <= 0) {
            throw IllegalArgumentException("stepSize must be positive and greater than 0! Was: $stepSize")
        }

        var time = simulationTime
        val stopTime = (time + stepSize)

        while (time < stopTime) {

            var tNext = min(time + stepSize, stopTime)

            val timeEvent = fmuInstance.eventInfo.nextEventTimeDefined && (fmuInstance.eventInfo.nextEventTime <= time)
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

        val integratedTime = solver.integrate(t, x, tNext, x)

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

    private fun eventIteration(): Boolean {

        fmuInstance.eventInfo.newDiscreteStatesNeeded = true
        fmuInstance.eventInfo.terminateSimulation = false

        while (fmuInstance.eventInfo.newDiscreteStatesNeeded) {
            if (!fmuInstance.newDiscreteStates().isOK()) {
                LOG.warn("fmuInstance.newDiscreteStates() returned status $lastStatus")
                return true
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


    private companion object {

        const val EPS = 1E-13

        val LOG: Logger = LoggerFactory.getLogger(ModelExchangeFmuStepper::class.java)

        fun FmiStatus.warnOnStatusNotOK(functionName: String) {
            if (this != FmiStatus.OK) {
                LOG.warn("$functionName returned status: $this")
            }
        }

    }

}

class CoSimulationModelDescriptionWrapper(
        md: ModelExchangeModelDescription
): CommonModelDescription by md, CoSimulationModelDescription {

    override val maxOutputDerivativeOrder: Int
        get() = 0
    override val canHandleVariableCommunicationStepSize: Boolean
        get() = true
    override val canInterpolateInputs: Boolean
        get() = false
    override val canRunAsynchronuously: Boolean
        get() = false
    override val canProvideMaxStepSize: Boolean
        get() = false
    override val attributes: CoSimulationAttributes
        get() = super<CoSimulationModelDescription>.attributes
}