package no.mechatronics.sfi.fmi4j.modeldescription.jacksonxml

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.module.kotlin.*
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionImpl
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import org.junit.Assert
import org.junit.Test
import java.io.File

class TestJackson {

    @Test
    fun test() {

        val path = "../test/fmi2/cs/win64/FMUSDK/2.0.4/BouncingBall/modelDescription.xml"
        //val path = "../test/fmi2/cs/win64/20sim/4.6.4.8004/ControlledTemperature/modelDescription.xml"
        val file = File(path)
        Assert.assertTrue(file.exists())

        val mapper = XmlMapper().apply {
            registerModule(KotlinModule())
            registerModule(JacksonXmlModule().apply {
                addDeserializer(AbstractTypedScalarVariable::class.java, ScalarVariableAdapter2())
            })
            enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        }
        val md = mapper.readValue<ModelDescriptionImpl>(file)

        println(md.modelVariables.size)
        println(md.modelStructure.derivatives)

    }

//    @Test
//    fun test2() {
//
//        val xml = """
//<fmiModelDescription>
//    <ModelVariables>
//        <ScalarVariable name="h" valueReference="0" description="height, used as state"
//                      causality="local" variability="continuous" initial="exact">
//        <Real start="1"/>
//        </ScalarVariable>
//        <ScalarVariable name="der(h)" valueReference="1" description="velocity of ball"
//                      causality="local" variability="continuous" initial="calculated">
//        <Real derivative="1"/>
//        </ScalarVariable>
//        <ScalarVariable name="v" valueReference="2" description="velocity of ball, used as state"
//                      causality="local" variability="continuous" initial="exact">
//        <Real start="0" reinit="true"/>
//        </ScalarVariable>
//        <ScalarVariable name="der(v)" valueReference="3" description="acceleration of ball"
//                      causality="local" variability="continuous" initial="calculated">
//        <Real derivative="3"/>
//        </ScalarVariable>
//        <ScalarVariable name="g" valueReference="4" description="acceleration of gravity"
//                      causality="parameter" variability="fixed" initial="exact">
//        <Real start="9.81"/>
//        </ScalarVariable>
//        <ScalarVariable name="e" valueReference="5" description="dimensionless parameter"
//                      causality="parameter" variability="tunable" initial="exact">
//        <Real start="0.7" min="0.5" max="1"/>
//        </ScalarVariable>
//    </ModelVariables>
//</fmiModelDescription>
//            """
//
//        val mapper = XmlMapper().apply {
//            registerModule(KotlinModule())
//            registerModule(JacksonXmlModule())
//            enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
//        }
//        val md = mapper.readValue<ModelDescription>(xml)
//
//        println(md.variables)
//
//    }
//
//
//    @JacksonXmlRootElement(localName = "fmiModelDescription")
//    class ModelDescription {
//
//        @JacksonXmlElementWrapper(localName = "ModelVariables")
//        @JacksonXmlProperty(localName = "ScalarVariable")
//        @JsonDeserialize(using = ScalarVariableAdapter2::class)
//        val variables: List<TypedScalarVariable<*>>? = null
//
//    }



//    @JsonIgnoreProperties(ignoreUnknown = true)
//    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
//    @JsonSubTypes(
//            JsonSubTypes.Type(value = RealVariable::class, name= "Real")
//    )
//    abstract class ScalarVariable {
//
//        @JacksonXmlProperty
//        lateinit var name: String
//
//        @JacksonXmlProperty
//        val declaredType: String? = null
//
//        @JacksonXmlProperty
//        val description: String? = null
//
//        @JacksonXmlProperty
//        val causality: Causality? = null
//
//        @JacksonXmlProperty
//        val variability: Variability? = null
//
//        @JacksonXmlProperty
//        val initial: Initial? = null
//
//        @JacksonXmlProperty(localName= "valueReference")
//        private val _valueReference: Int? = null
//
//        val valueReference: Int
//            get() = _valueReference ?: throw IllegalStateException("ValueReference was null!")
//
//        override fun toString(): String {
//            return "ScalarVariable(name='$name', declaredType=$declaredType, description=$description, causality=$causality, variability=$variability, initial=$initial, _valueReference=$_valueReference)"
//        }
//
//
//    }
//
//    class RealVariable: ScalarVariable() {
//
//        @JacksonXmlProperty
//        val min: Double? = null
//
//        @JacksonXmlProperty
//        val max: Double? = null
//
//        @JacksonXmlProperty
//        var start: Double? = null
//
//
//    }

}