package no.mechatronics.sfi

import no.mechatronics.sfi.modeldescription.ModelDescription
import org.apache.commons.io.IOUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.nio.charset.Charset

class ModelDescriptionTest {

    private lateinit var modelDescription: ModelDescription

    @Before
    fun setUp() {

        val resourceAsStream = javaClass.classLoader.getResource("v2/cs/ControlledTemperature/ControlledTemperature.fmu")
        Assert.assertNotNull(resourceAsStream)
      //  val xml = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"))
        modelDescription = ModelDescription.parseModelDescription(resourceAsStream)
    }


    @Test
    fun getFmiVersion() {

        val fmiVersion = modelDescription.fmiVersion
        Assert.assertEquals("2.0", fmiVersion)
        println("fmiVersion=$fmiVersion")

    }

    @Test
    fun getModelName() {

        val value = modelDescription.modelName
        Assert.assertEquals("ControlledTemperature", value)
        println("modelName=$value")

    }

    @Test
    fun getGuid() {

        val value = modelDescription.guid
        Assert.assertEquals("{06c2700b-b39c-4895-9151-304ddde28443}", value)
        println("guid=$value")

    }

    @Test
    fun getLicense() {

        val value = modelDescription.license
        Assert.assertEquals("-", value)
        println("licence=$value")

    }

}