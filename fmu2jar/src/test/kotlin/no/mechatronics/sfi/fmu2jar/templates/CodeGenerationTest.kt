package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import org.junit.Assert
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class CodeGenerationTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(CodeGenerationTest::class.java)
    }

    @Test
    fun generateBody() {

        val path = "fmus/cs/win64/20sim/4.6.4.8004/ControlledTemperature/modelDescription.xml"
        val file = File(javaClass.classLoader.getResource(path).file)
        Assert.assertTrue(file.exists())
        val xml = file.readText(Charsets.UTF_8)
        val md = ModelDescriptionParser.parse(xml)
        Assert.assertNotNull(md)

        LOG.info( CodeGeneration.generateWrapper(md) )

    }

}