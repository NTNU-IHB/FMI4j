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

package no.ntnu.ihb.fmi4j.solvers.apache

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import org.apache.commons.math3.ode.FirstOrderIntegrator

/**
 * Wraps solvers from apache commons math3 to be used by FMI4j
 * to solve Model Exchange FMUs
 *
 * @author Lars Ivar Hatledal
 */
class ApacheSolver(
        private val solver: FirstOrderIntegrator
) : no.ntnu.ihb.fmi4j.solvers.Solver {

    override val name: String
        get() = solver.name

    lateinit var equations: FirstOrderDifferentialEquations

    override fun setEquations(equations: no.ntnu.ihb.fmi4j.solvers.Equations) {
        this.equations = object : FirstOrderDifferentialEquations {
            override fun computeDerivatives(t: Double, y: DoubleArray, yDot: DoubleArray) {
                return equations.computeDerivatives(t, y, yDot)
            }

            override fun getDimension(): Int {
                return equations.dimension
            }
        }
    }

    override fun integrate(t0: Double, x0: DoubleArray, t: Double, x: DoubleArray): Double {
        return solver.integrate(equations, t0, x0, t, x)
    }

}
