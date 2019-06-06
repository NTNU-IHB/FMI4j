/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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

package no.ntnu.ihb.fmi4j.importer.fmi1

/**
 *
 * @author Lars Ivar Hatledal
 */
enum class FmiStatusKind(
        val code: Int
) {

    /**
     * Can be called when the fmiDoStep function returned fmiPending.
     * The function delivers fmiPending if the computation is not finished.
     * If the computation is finished meanwhile the function returns the result of the asynchronous executed fmiDoStep(...) call.
     */
    DO_STEP_STATUS(0),

    /**
     * Can be called when the fmiDoStep function returned fmiPending.
     * The function delivers a string which informs about the status of the currently running asynchronous fmiDoStep computation
     */
    PENDING_STATUS(1),

    /**
     * Returns the time until the last communication step was computed successfully.
     * Can be called after fmiDoStep(...) returned fmiDiscard.
     */
    LAST_SUCCESSFUL_TIME(2);

    companion object {

        @JvmStatic
        fun valueOf(i: Int): FmiStatusKind {
            for (kind in values()) {
                if (i == kind.code) {
                    return kind
                }
            }
            throw IllegalArgumentException("$i not in range of ${values().map { it.code }.toList()}")
        }
    }

}