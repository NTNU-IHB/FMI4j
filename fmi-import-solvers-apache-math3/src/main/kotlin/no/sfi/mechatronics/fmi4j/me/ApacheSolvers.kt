package no.sfi.mechatronics.fmi4j.me

import org.apache.commons.math3.ode.nonstiff.*

/**
 *
 * Factory for creating instances of ApacheSolver
 *
 * @author Lars Ivar Hatledal
 */
object ApacheSolvers {

    @JvmStatic
    fun euler(stepSize: Double): ApacheSolver {
        return ApacheSolver(EulerIntegrator(stepSize))
    }

    @JvmStatic
    fun rk4(stepSize: Double): ApacheSolver {
        return ApacheSolver(ClassicalRungeKuttaIntegrator(stepSize))
    }

    @JvmStatic
    fun luther(stepSize: Double): ApacheSolver {
        return ApacheSolver(LutherIntegrator(stepSize))
    }

    @JvmStatic
    fun gill(stepSize: Double): ApacheSolver {
        return ApacheSolver(GillIntegrator(stepSize))
    }

    @JvmStatic
    fun midpoint(stepSize: Double): ApacheSolver {
        return ApacheSolver(MidpointIntegrator(stepSize))
    }

}