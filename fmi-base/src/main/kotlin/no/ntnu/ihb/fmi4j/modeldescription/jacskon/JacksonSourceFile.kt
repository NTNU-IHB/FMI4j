package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.SourceFile

/**
 * @author Lars Ivar Hatledal
 */
data class JacksonSourceFile(

        @JacksonXmlProperty
        override val name: String

) : SourceFile