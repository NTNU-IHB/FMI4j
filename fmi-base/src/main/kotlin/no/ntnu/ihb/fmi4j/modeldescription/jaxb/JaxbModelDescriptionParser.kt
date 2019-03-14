package no.ntnu.ihb.fmi4j.modeldescription.jaxb

import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionProvider
import java.io.StringReader
import javax.xml.bind.JAXBContext

object JaxbModelDescriptionParser: ModelDescriptionParser() {

    override fun parse(xml: String): ModelDescriptionProvider {
        val ctx = JAXBContext.newInstance(FmiModelDescription::class.java)
        return ctx.createUnmarshaller().unmarshal(StringReader(xml)).let {
            JaxbModelDescription(it as FmiModelDescription)
        }
    }

}