package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.LogCategory


typealias JacksonLogCategories = List<JacksonLogCategory>

/**
 * @author Lars Ivar Hatledal
 */
data class JacksonLogCategory(

        @JacksonXmlProperty
        override val name: String,

        @JacksonXmlProperty
        override val description: String? = null

): LogCategory