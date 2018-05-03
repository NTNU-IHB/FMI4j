package no.mechatronics.sfi.fmi4j.modeldescription

import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*

class SerializeTest {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(SerializeTest::class.java)

        private lateinit var modelDescription: CommonModelDescription

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

        val bos = ByteArrayOutputStream()
        ObjectOutputStream(bos).use {
            it.writeObject(modelDescription)
            it.flush()
        }

        ObjectInputStream(ByteArrayInputStream(bos.toByteArray())).use {
            val md: CommonModelDescription = it.readObject() as CommonModelDescription
            md.modelVariables.variables.forEach { LOG.info("$it") }
            LOG.info("${md.modelStructure}")
        }

    }

}