package no.mechatronics.sfi.fmi4j.modeldescription

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute


/**
 * Providing default settings for the integrator, such as stop time and
relative tolerance
 <br>
DefaultExperiment consists of the optional default start time, stop time, relative tolerance, and step size
for the first simulation run. A tool may ignore this information. However, it is convenient for a user that
startTime, stopTime, tolerance and stepSize have already a meaningful default value for the model at
hand. Furthermore, for CoSimulation the stepSize defines the preferred communicationStepSize.
 */
@XmlAccessorType(XmlAccessType.FIELD)
class DefaultExperiment{

    @XmlAttribute
    val startTime: Double = 0.0
    @XmlAttribute
    val stopTime: Double = 0.0
    @XmlAttribute
    val tolerance: Double = 1E-4
    @XmlAttribute
    val stepSize: Double = 1.0/100

    override fun toString(): String {
        return "DefaultExperiment(startTime=$startTime, stopTime=$stopTime, tolerance=$tolerance, stepSize=$stepSize)"
    }

}
