package no.ntnu.ihb.fmi4j.modeldescription.jacksonxml

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionImpl
import no.ntnu.ihb.fmi4j.modeldescription.TestFMUs
import no.ntnu.ihb.fmi4j.modeldescription.parser.ModelDescriptionParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class TestJackson {

    private companion object {

        val LOG: Logger = LoggerFactory.getLogger(TestJackson::class.java)

        val fmuFile = TestFMUs.fmi20().cs()
                .vendor("MapleSim").version("2017")
                .name("ControlledTemperature").file()

    }

    @Test
    fun test() {

        val mapper = XmlMapper().apply {
            registerModule(KotlinModule())
            registerModule(JacksonXmlModule())
            enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        }

        val md = ModelDescriptionParser.extractModelDescriptionXml(fmuFile).let {
            LOG.info(it)
            mapper.readValue<ModelDescriptionImpl>(it)
        }

        LOG.info("${md.modelVariables.size}")
        LOG.info("${md.modelStructure.outputs}")
        LOG.info("${md.modelStructure.derivatives}")

        LOG.info("${md.unitDefinitions}")

    }

    class Real

    class ScalarVariable {

        @JsonSetter(nulls = Nulls.AS_EMPTY)
        @JacksonXmlProperty(localName = "Real")
        var real: Real? = null

    }

    @Test
    fun test2() {

        val xml = """
            <ScalarVariable >
                <Real />
             </ScalarVariable >
            """

        val mapper = XmlMapper().apply {
            registerModule(KotlinModule())
        }
        mapper.readValue<ScalarVariable>(xml).also {
            Assertions.assertNotNull(it.real)
        }

    }

}