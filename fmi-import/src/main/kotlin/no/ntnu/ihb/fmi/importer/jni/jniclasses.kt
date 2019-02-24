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

package no.ntnu.ihb.fmi.importer.jni

open class IntByReference(
        var value: Int = 0
)

open class DoubleByReference(
        var value: Double = 0.0
)

open class StringByReference(
        var value: String = ""
)

open class BooleanByReference(
        var value: Boolean = false
)

open class LongByReference(
        var value: Long = 0
)

class EventInfo {

    var newDiscreteStatesNeeded: Boolean = false
    var terminateSimulation: Boolean = false
    var nominalsOfContinuousStatesChanged: Boolean = false
    var valuesOfContinuousStatesChanged: Boolean = false
    var nextEventTimeDefined: Boolean = false
    var nextEventTime: Double = 0.0

    override fun toString(): String {
        return "EventInfo{" +
                "newDiscreteStatesNeeded=" + newDiscreteStatesNeeded +
                ", terminateSimulation=" + terminateSimulation +
                ", nominalsOfContinuousStatesChanged=" + nominalsOfContinuousStatesChanged +
                ", valuesOfContinuousStatesChanged=" + valuesOfContinuousStatesChanged +
                ", nextEventTimeDefined=" + nextEventTimeDefined +
                ", nextEventTime=" + nextEventTime +
                '}'.toString()
    }

}