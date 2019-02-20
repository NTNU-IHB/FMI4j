package no.ntnu.ihb.fmi4j.modeldescription.vendors.fmusdk

import no.ntnu.ihb.fmi4j.modeldescription.TestFMUs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@EnabledOnOs(OS.WINDOWS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VanDerPolTest {

    private companion object {

        val LOG: Logger = LoggerFactory.getLogger(VanDerPolTest::class.java)

        val modelDescription = TestFMUs.fmi20().me()
                .vendor("FMUSDK").version("2.0.4")
                .name("vanDerPol").modelDescription()
                .asModelExchangeModelDescription()
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