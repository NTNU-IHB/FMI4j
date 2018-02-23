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

package no.mechatronics.sfi.fmi4j.proxy.v2.enums

import java.util.*
import kotlin.streams.toList

/**
 *
 * @author Lars Ivar Hatledal
 */
enum class Fmi2StatusKind(
        val code: Int
) {

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