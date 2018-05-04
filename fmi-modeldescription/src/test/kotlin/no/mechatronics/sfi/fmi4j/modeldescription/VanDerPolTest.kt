package no.mechatronics.sfi.fmi4j.modeldescription

import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class VanDerPolTest {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(VanDerPolTest::class.java)

        private lateinit var modelDescription: ModelExchangeModelDescription

        @JvmStatic
        @BeforeClass
        fun setup() {
            val fmu = File(TEST_FMUs, "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu")
            Assert.assertTrue(fmu.exists())
            modelDescription = ModelDescriptionParser.parse(fmu).asModelExchangeModelDescription()
        }

    }

    @Test
    fun testFmiVersion() {
        val fmiVersion = modelDescription.fmiVersion
        LOG.info("fmiVersion=$fmiVersion")
        Assert.assertEquals("2.0", fmiVersion)
    }

    @Test
    fun testNumberOfEventIndicators() {
        val numberOfEventIndicators = modelDescription.numberOfEventIndicators
        LOG.info("numberOfEventIndicators=$numberOfEventIndicators")
        Assert.assertEquals(0, numberOfEventIndicators)
    }

    @Test
    fun testNumberOfReals() {
        val numberOfReals = modelDescription.modelVariables.reals.size
        LOG.info("numberOfReals=$numberOfReals")
        Assert.assertEquals(5, numberOfReals)
    }

    @Test
    fun testModelName() {
        val modelName = modelDescription.modelName
        LOG.info("modelName=$modelName")
        Assert.assertEquals("van der Pol oscillator", modelName)
    }

    @Test
    fun testModelIdentifier() {
        val modelIdentifier = modelDescription.modelIdentifier
        LOG.info("modelIdentifier=$modelIdentifier")
        Assert.assertEquals("vanDerPol", modelIdentifier)
    }

    @Test
    fun testGuid() {
        val guid = modelDescription.guid
        LOG.info("guid=$guid")
        Assert.assertEquals("{8c4e810f-3da3-4a00-8276-176fa3c9f000}", guid)
    }

    @Test
    fun testLogCategories() {
        val logCategories = modelDescription.logCategories!!.map { it.name }
        LOG.info("$logCategories")
        Assert.assertTrue(logCategories.containsAll(
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