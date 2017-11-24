package no.sfi.mechatronics.fmi4j.jna

import java.util.*
import kotlin.streams.toList


enum class Fmi2StatusKind private constructor(val code: Int) {

    /**
     * Can be called when the fmi2DoStep function returned fmi2Pending. The
     * function delivers fmi2Pending if the computation is not finished.
     * Otherwise the function returns the result of the asynchronously executed
     * fmi2DoStep call.
     */
    doStepStatus(0),
    /**
     * Can be called when the fmi2DoStep function returned fmi2Pending. The
     * function delivers a string which informs about the status of the
     * currently running asynchronous fmi2DoStep computation.
     */
    pendingStatus(1),
    /**
     * Returns the end time of the last successfully completed communication
     * step. Can be called after fmi2DoStep(...) returned fmi2Discard.
     */
    lastSuccessfulTime(2),
    /**
     * Returns true, if the slave wants to terminate the simulation. Can be
     * called after fmi2DoStep(...) returned fmi2Discard. Use
     * fmi2LastSuccessfulTime to determine the time instant at which the slave
     * terminated.
     */
    terminated(3);


    companion object {

        @JvmStatic
        fun valueOf(i: Int): Fmi2StatusKind {

            for (kind in values()) {
                if (i == kind.code) {
                    return kind
                }
            }
            throw IllegalArgumentException("$i not in range of ${Arrays.stream(values()).map { it.code }.toList()}")
        }
    }
}