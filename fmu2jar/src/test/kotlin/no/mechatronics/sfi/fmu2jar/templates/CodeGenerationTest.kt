package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import org.apache.commons.io.IOUtils
import org.junit.Assert
import org.junit.Test
import java.nio.charset.Charset

class CodeGenerationTest {

    @Test
    fun generateBody() {

        val url = javaClass.classLoader.getResource("modelDescription.xml")
        Assert.assertNotNull(url)
        val xml = IOUtils.toString(url, Charset.forName("UTF-8"))
        val md = ModelDescriptionParser.parse(xml)
        Assert.assertNotNull(md)

        println( CodeGeneration.generateBody(md) )


    }

}