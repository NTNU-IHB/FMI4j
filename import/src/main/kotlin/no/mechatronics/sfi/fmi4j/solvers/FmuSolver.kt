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

package no.mechatronics.sfi.fmi4j.solvers

import no.mechatronics.sfi.fmi4j.fmu.ModelExchangeFmu
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator

interface FmuSolver {

    fun solve(time:Double, tNext:Double) : Pair<Boolean, Double>

}

abstract class AbstractSolver(val fmu: ModelExchangeFmu) : FmuSolver {


    private val states: DoubleArray
    private val derivatives: DoubleArray

    private val preEventIndicators: DoubleArray
    private val eventIndicators: DoubleArray


    init {

        val numberOfContinuousStates = fmu.modelDescription.numberOfContinuousStates
        val numberOfEventIndicators = fmu.modelDescription.numberOfEventIndicators

        states = DoubleArray(numberOfContinuousStates)
        derivatives = DoubleArray(numberOfContinuousStates)

        preEventIndicators = DoubleArray(numberOfEventIndicators)
        eventIndicators = DoubleArray(numberOfEventIndicators)

        fmu.getEventIndicators(eventIndicators)
    }

    abstract fun integrate(t0: Double, states: DoubleArray, dt: Double, derivatives: DoubleArray)

    override fun solve(time: Double, tNext:Double) : Pair<Boolean, Double> {

        fmu.getContinuousStates(states)
        fmu.getDerivatives(derivatives)

        val dt = tNext - time

        integrate(time, states, dt, derivatives)

        fmu.setContinousStates(states)

        for (i in preEventIndicators.indices) {
            preEventIndicators[i] = eventIndicators[i]
        }

        fmu.getEventIndicators(eventIndicators)

        var stateEvent: Boolean = false
        for (i in preEventIndicators.indices) {
            stateEvent = preEventIndicators[i] * eventIndicators[i] < 0
            if (stateEvent) break
        }

        return Pair(stateEvent, tNext)

    }

}

class ForwardEulerSolver(fmu: ModelExchangeFmu): AbstractSolver(fmu) {

    override fun integrate(t0: Double, states: DoubleArray, dt: Double, derivatives: DoubleArray) {
        for (i in states.indices) {
            states[i] += dt * derivatives[i]
        }
    }
}

class DormandPrince853Solver(fmu: ModelExchangeFmu) : AbstractSolver(fmu) {

    val integrator = DormandPrince853Integrator(1E-12, 1.0, 1E-10, 1E-10)

    override fun integrate(t0: Double, states: DoubleArray, dt: Double, derivatives: DoubleArray) {
        integrator.integrate(fmu.ode, t0, states, t0 + dt, states)
    }
}