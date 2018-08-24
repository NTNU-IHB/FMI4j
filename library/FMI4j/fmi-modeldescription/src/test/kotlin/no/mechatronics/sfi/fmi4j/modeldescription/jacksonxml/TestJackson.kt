package no.mechatronics.sfi.fmi4j.modeldescription.jacksonxml

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionImpl
import no.mechatronics.sfi.fmi4j.modeldescription.currentOS
import no.mechatronics.sfi.fmi4j.modeldescription.parser.ModelDescriptionParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class TestJackson {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(TestJackson::class.java)
    }

    @Test
    fun test() {

        val file = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/$currentOS" +
                        "/MapleSim/2017/ControlledTemperature/ControlledTemperature.fmu")
        Assertions.assertTrue(file.exists())

        val mapper = XmlMapper().apply {
            registerModule(KotlinModule())
            registerModule(JacksonXmlModule())
            enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        }

        val md = ModelDescriptionParser.extractModelDescriptionXml(file).let {
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
        val variable = mapper.readValue<ScalarVariable>(xml)

        Assertions.assertNotNull(variable.real)

    }

}