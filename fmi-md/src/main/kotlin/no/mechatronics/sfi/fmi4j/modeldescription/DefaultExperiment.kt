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

package no.mechatronics.sfi.fmi4j.modeldescription

import java.io.Serializable
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute


/**
 * Providing default settings for the integrator, such as stop time and
 * relative tolerance
 *
 * DefaultExperiment consists of the optional default start time, stop time, relative tolerance, and step size
 * for the first simulation run. A tool may ignore this information. However, it is convenient for a user that
 * startTime, stopTime, tolerance and stepSize have already a meaningful default value for the model at
 * hand. Furthermore, for CoSimulation the stepSize defines the preferred communicationStepSize.
 */
interface DefaultExperiment {

    val startTime: Double
    val stopTime: Double
    val tolerance: Double
    val stepSize: Double
}

/**
 * @inheritDoc
 */
@XmlAccessorType(XmlAccessType.FIELD)
class DefaultExperimentImpl: DefaultExperiment, Serializable {

    @XmlAttribute
    override val startTime: Double = 0.0
    @XmlAttribute
    override val stopTime: Double = 0.0
    @XmlAttribute
    override val tolerance: Double = 1E-4
    @XmlAttribute
    override val stepSize: Double = 1.0/100

    override fun toString(): String {
        return "DefaultExperimentImpl(startTime=$startTime, stopTime=$stopTime, tolerance=$tolerance, stepSize=$stepSize)"
    }

}
