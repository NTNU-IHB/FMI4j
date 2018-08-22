package no.mechatronics.sfi.fmi4j.modeldescription.vendors.fmusdk

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.parser.ModelDescriptionParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@EnabledOnOs(OS.WINDOWS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class VanDerPolTest {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(VanDerPolTest::class.java)
    }

    private val modelDescription: ModelExchangeModelDescription

    init {

        val fmu = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu")

        Assertions.assertTrue(fmu.exists())
        modelDescription = ModelDescriptionParser.parse(fmu).asModelExchangeModelDescription()

    }

    @Test
    fun testFmiVersion() {
        val fmiVersion = modelDescription.fmiVersion
        LOG.info("fmiVersion=$fmiVersion")
        Assertions.assertEquals("2.0", fmiVersion)
    }

    @Test
    fun testNumberOfEventIndicators() {
        val numberOfEventIndicators = modelDescription.numberOfEventIndicators
        LOG.info("numberOfEventIndicators=$numberOfEventIndicators")
        Assertions.assertEquals(0, numberOfEventIndicators)
    }

    @Test
    fun testNumberOfReals() {
        val numberOfReals = modelDescription.modelVariables.reals.size
        LOG.info("numberOfReals=$numberOfReals")
        Assertions.assertEquals(5, numberOfReals)
    }

    @Test
    fun testModelName() {
        val modelName = modelDescription.modelName
        LOG.info("modelName=$modelName")
        Assertions.assertEquals("van der Pol oscillator", modelName)
    }

    @Test
    fun testModelIdentifier() {
        val modelIdentifier = modelDescription.modelIdentifier
        LOG.info("modelIdentifier=$modelIdentifier")
        Assertions.assertEquals("vanDerPol", modelIdentifier)
    }

    @Test
    fun testGuid() {
        val guid = modelDescription.guid
        LOG.info("guid=$guid")
        Assertions.assertEquals("{8c4e810f-3da3-4a00-8276-176fa3c9f000}", guid)
    }

    @Test
    fun testLogCategories() {
        val logCategories = modelDescription.logCategories!!.map { it.name }
        LOG.info("$logCategories")
        Assertions.assertTrue(logCategories.containsAll(
                listOf("logAll", "logError", "logFmiCall", "logEvent")
        ))
    }

    @Test
    fun testModelStructure() {
        val modelStructure = modelDescription.modelStructure

        Assertions.assertTrue(modelStructure.derivatives.size == 2)
        Assertions.assertTrue(modelStructure.derivatives.map { it.index }.containsAll(listOf(2, 4)))

        Assertions.assertTrue(modelStructure.initialUnknowns.size == 2)
        Assertions.assertTrue(modelStructure.initialUnknowns.map { it.index }.containsAll(listOf(2, 4)))
    }

}