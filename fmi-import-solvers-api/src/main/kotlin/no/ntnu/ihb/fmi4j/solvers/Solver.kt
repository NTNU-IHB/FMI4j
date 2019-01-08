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

package no.ntnu.ihb.fmi4j.solvers

/**
 * Interface for solvers
 * A solver is used to solve Model Exchange FMUs
 *
 * @author Lars Ivar Hatledal
 */
interface Solver {

    /**
     * Get the name of the method
     */
    val name: String

    /**
     * differential equations to integrate
     */
    fun setEquations(equations: no.ntnu.ihb.fmi4j.solvers.Equations)

    /**
     * Integrate the differential equations up to the given time.
     * This method solves an Initial Value Problem (IVP).
     *
     * @param t0 – initial time
     * @param x0 – initial value of the state vector at t0
     * @param t – target time for the integration (can be set to a value smaller than t0 for backward integration)
     * @param x – placeholder where to put the state vector at each successful step (and hence at the end of integration), can be the same object as y0
     */
    fun integrate(t0: Double, x0: DoubleArray, t: Double, x: DoubleArray): Double

}

/**
 * @author Lars Ivar Hatledal
 */
interface Equations {

    /**
     * Get the dimension of the problem.
     */
    val dimension: Int

    /**
     * Get the current time derivative of the state vector
     *
     * @param t – current value of the independent time variable
     * @param y – array containing the current value of the state vector
     * @param yDot – placeholder array where to put the time derivative of the state vector
     */
    fun computeDerivatives(time: Double, y: DoubleArray, yDot: DoubleArray)

}