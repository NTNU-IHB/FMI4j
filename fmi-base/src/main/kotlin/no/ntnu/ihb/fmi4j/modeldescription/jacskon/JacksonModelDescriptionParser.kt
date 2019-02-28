package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionProvider


/**
 * Parses the modelDescription.xml holding static information about an FMU
 */
object JacksonModelDescriptionParser : ModelDescriptionParser() {

    private val mapper by lazy {
        XmlMapper().apply {
            registerModule(KotlinModule())
            registerModule(JacksonXmlModule())
            enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        }
    }


    override fun parse(xml: String): ModelDescriptionProvider {

        val correctedXml = xml.replace("calculatedParameter", "CALCULATED_PARAMETER")
        return mapper.readValue(correctedXml, JacksonModelDescription::class.java)

    }


}

