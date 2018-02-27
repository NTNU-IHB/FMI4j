package no.mechatronics.sfi.fmi4j.modeldescription

import com.google.gson.*
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import no.mechatronics.sfi.fmi4j.modeldescription.variables.attributes.BooleanAttribute
import no.mechatronics.sfi.fmi4j.modeldescription.variables.attributes.IntegerAttribute
import org.apache.commons.io.FileUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.reflect.Type
import java.nio.charset.Charset

class SerializeTest {

    private lateinit var  modelDescription: SimpleModelDescription

    @Before
    fun setup() {
        val path = "../test/fmi2/cs/win64/OpenModelica/v1.11.0/FmuExportCrossCompile/modelDescription.xml"
        val file = File(path)
        Assert.assertTrue(file.exists())
        val xml = FileUtils.readFileToString(file, Charset.forName("UTF-8"))
        modelDescription = ModelDescriptionParser.parse(xml)
    }

    @Test
    fun test1() {

        val bos = ByteOutputStream()
        ObjectOutputStream(bos).use {

            it.writeObject(modelDescription)
            it.flush()

        }

        ObjectInputStream(ByteArrayInputStream(bos.bytes)).use {
            val md: SimpleModelDescription = it.readObject() as SimpleModelDescription

            md.modelVariables.variables.forEach { println(it) }
            println(md.modelStructure)

        }


    }

//    class VariableAdapter: JsonDeserializer<AbstractTypedScalarVariable<*>>, JsonSerializer<AbstractTypedScalarVariable<*>> {
//        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): AbstractTypedScalarVariable<*> {
//            println(json)
//            println(typeOfT)
//            println(context)
//            return IntegerVariable(ScalarVariableImpl())
//        }
//
//        override fun serialize(src: AbstractTypedScalarVariable<*>?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
//            val gson = Gson()
//            return when(src) {
//                is IntegerVariable -> gson.toJsonTree(src)
//                is RealVariable -> gson.toJsonTree(src)
//                is StringVariable -> gson.toJsonTree(src)
//                is BooleanVariable -> gson.toJsonTree(src)
//                is EnumerationVariable -> gson.toJsonTree(src)
//                else -> JsonObject()
//            }
//        }
//    }
//
//    @Test
//    fun testGson() {
//
//        val gson = GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeAdapter(AbstractTypedScalarVariable::class.java, VariableAdapter())
//                .create()
//
//
//
//        val json = gson.toJson(modelDescription)
//        println(json)
//        val md = gson.fromJson(json, ModelDescriptionImpl::class.java)
//
//        println(md)
//
//    }

}