package no.mechatronics.sfi.fmi4j.modeldescription

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class SerializeTest {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(SerializeTest::class.java)

        private lateinit var modelDescription: SimpleModelDescription

        @JvmStatic
        @BeforeClass
        fun setup() {
            val path = "../test/fmi2/cs/win64/OpenModelica/v1.11.0/FmuExportCrossCompile/modelDescription.xml"
            val file = File(path)
            Assert.assertTrue(file.exists())
            val xml = file.readText(Charsets.UTF_8)
            modelDescription = ModelDescriptionParser.parse(xml)
        }

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
            md.modelVariables.variables.forEach { LOG.info("$it") }
            LOG.info("${md.modelStructure}")
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