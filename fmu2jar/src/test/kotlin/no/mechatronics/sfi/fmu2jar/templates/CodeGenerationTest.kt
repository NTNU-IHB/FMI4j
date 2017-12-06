package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import org.apache.commons.io.FileUtils
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.nio.charset.Charset

class CodeGenerationTest {

    @Test
    fun generateBody() {

        val xml= FileUtils.readFileToString(File("C:\\Users\\laht\\IdeaProjects\\FMI4j\\import\\src\\test\\resources\\v2\\cs\\ControlledTemperature\\modelDescription.xml"), Charset.forName("UTF-8"))
        val md = ModelDescription.parseModelDescription(xml)
        Assert.assertNotNull(md)

        println( CodeGeneration.generateBody(md) )


    }

}