package no.mechatronics.sfi.fmi4j.modeldescription.jacksonxml

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionImpl
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import no.mechatronics.sfi.fmi4j.modeldescription.TEST_FMUs
import no.mechatronics.sfi.fmi4j.modeldescription.variables.AbstractTypedScalarVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.ScalarVariableAdapter2
import org.junit.Assert
import org.junit.Test
import java.io.File

class TestJackson {

    @Test
    fun test() {

        val file = File(TEST_FMUs, "FMI_2.0/CoSimulation/win64/FMUSDK/2.0.4/BouncingBall/bouncingBall.fmu")
        Assert.assertTrue(file.exists())

        val mapper = XmlMapper().apply {
            registerModule(KotlinModule())
            registerModule(JacksonXmlModule().apply {
                addDeserializer(AbstractTypedScalarVariable::class.java, ScalarVariableAdapter2())
            })
            enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        }
        val md = mapper.readValue<ModelDescriptionImpl>(ModelDescriptionParser.extractModelDescriptionXml(file))


        println(md.modelVariables.size)
        println(md.modelStructure.outputs)
        println(md.modelStructure.derivatives)

        md.modelVariables.forEach({
            println(it)
        })

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

        Assert.assertNotNull(variable.real)

    }

}