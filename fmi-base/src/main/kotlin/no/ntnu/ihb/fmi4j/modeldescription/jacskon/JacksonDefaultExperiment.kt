package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.DefaultExperiment

/**
 * @author Lars Ivar Hatledal
 */
data class JacksonDefaultExperiment(

        @JacksonXmlProperty
        override val startTime: Double = 0.0,

        @JacksonXmlProperty
        override val stopTime: Double = 0.0,

        @JacksonXmlProperty
        override val tolerance: Double = 1E-4,

        @JacksonXmlProperty
        override val stepSize: Double = 1E-3

) : DefaultExperiment
