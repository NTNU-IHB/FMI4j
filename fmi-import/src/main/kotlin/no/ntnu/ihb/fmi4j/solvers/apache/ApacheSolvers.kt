package no.ntnu.ihb.fmi4j.solvers.apache

import org.apache.commons.math3.ode.nonstiff.*

/**
 *
 * Factory for creating instances of ApacheSolver
 *
 * @author Lars Ivar Hatledal
 */
object ApacheSolvers {

    /**
     * Build an Euler integrator with the given step.
     * @param stepSize integration step
     */
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

    /**
     * Build an eighth order Dormand-Prince integrator with the given step bounds
     * @param minStep minimal step (sign is irrelevant, regardless of
     * integration direction, forward or backward), the last step can
     * be smaller than this
     * @param maxStep maximal step (sign is irrelevant, regardless of
     * integration direction, forward or backward), the last step can
     * be smaller than this
     * @param scalAbsoluteTolerance allowed absolute error
     * @param scalRelativeTolerance allowed relative error
     */
    @JvmStatic
    fun dormandPrince853(minStep: Double, maxStep: Double, scalAbsoluteTolerance: Double, scalRelativeTolerance: Double): ApacheSolver {
        return ApacheSolver(DormandPrince853Integrator(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance))
    }

    /**
     * Build an Adams-Bashforth integrator with the given order and step control parameters.
     * @param nSteps number of steps of the method excluding the one being computed
     * @param minStep minimal step (sign is irrelevant, regardless of
     * integration direction, forward or backward), the last step can
     * be smaller than this
     * @param maxStep maximal step (sign is irrelevant, regardless of
     * integration direction, forward or backward), the last step can
     * be smaller than this
     * @param scalAbsoluteTolerance allowed absolute error
     * @param scalRelativeTolerance allowed relative error
     */
    @JvmStatic
    fun adamsBashforthIntegrator(nSteps: Int, minStep: Double, maxStep: Double, scalAbsoluteTolerance: Double, scalRelativeTolerance: Double): ApacheSolver {
        return ApacheSolver(AdamsBashforthIntegrator(nSteps, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance))
    }

}
