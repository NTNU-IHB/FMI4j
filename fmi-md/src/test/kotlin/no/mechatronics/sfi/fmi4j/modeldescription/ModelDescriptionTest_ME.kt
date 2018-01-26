package no.mechatronics.sfi.fmi4j.modeldescription

import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import org.apache.commons.io.IOUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.nio.charset.Charset

class ModelDescriptionTest_ME {

    private lateinit var modelDescription: ModelExchangeModelDescription

    @Before
    fun setUp() {
        val xml = IOUtils.toString(javaClass.classLoader
                .getResource("v2/me/VanDerPol/modelDescription.xml"), Charset.defaultCharset())
        modelDescription = ModelDescriptionParser.parse(xml).asME()
    }

    @Test
    fun testFmiVersion() {
        val fmiVersion = modelDescription.fmiVersion
        println("fmiVersion=$fmiVersion")
        Assert.assertEquals("2.0", fmiVersion)
    }

    @Test
    fun testNumberOfEventIndicators() {
        val numberOfEventIndicators = modelDescription.numberOfEventIndicators
        println("numberOfEventIndicators=$numberOfEventIndicators")
        Assert.assertEquals(0, numberOfEventIndicators)
    }

    @Test
    fun testModelName() {
        val modelName = modelDescription.modelName
        println("modelName=$modelName")
        Assert.assertEquals("van der Pol oscillator", modelName)
    }

    @Test
    fun testModelIdentifer() {
        val modelIdentifier = modelDescription.modelIdentifier
        println("modelIdentifier=$modelIdentifier")
        Assert.assertEquals("vanDerPol", modelIdentifier)
    }

    @Test
    fun testGuid() {
        val guid = modelDescription.guid
        println("guid=$guid")
        Assert.assertEquals("{8c4e810f-3da3-4a00-8276-176fa3c9f000}", guid)
    }

    @Test
    fun testLogCategories() {
        val logCategories = modelDescription.logCategories?.map { it.name }
        println(logCategories)
        Assert.assertTrue(logCategories!!.containsAll(
                listOf("logAll", "logError", "logFmiCall", "logEvent")
        ))
    }

    @Test
    fun testModelStructure() {
        val modelStructure = modelDescription.modelStructure

        Assert.assertTrue(modelStructure.derivatives.size == 2)
        Assert.assertTrue(modelStructure.derivatives.map { it.index }.containsAll(listOf(2,4)))

        Assert.assertTrue(modelStructure.initialUnknowns.size == 2)
        Assert.assertTrue(modelStructure.initialUnknowns.map { it.index }.containsAll(listOf(2,4)))
    }

}