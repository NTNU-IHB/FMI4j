package no.ntnu.ihb.fmi4j.modeldescription.vendors.openmodelica

import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FmuExportCrossCompileTest {

    private companion object {

        val LOG: Logger = LoggerFactory.getLogger(FmuExportCrossCompileTest::class.java)

        val modelDescription = TestFMUs.get("2.0/cs/OpenModelica/v1.11.0/FmuExportCrossCompile/FmuExportCrossCompile.fmu").let {
            ModelDescriptionParser.parseModelDescription(it)
        }

    }

    @Test
    fun testUnknowns() {

        val der = modelDescription.modelStructure.derivatives
        Assertions.assertEquals(der.size, 2)

        val d1 = der[0]
        Assertions.assertEquals(d1.index, 3)
        Assertions.assertEquals(d1.dependencies.size, 1)
        Assertions.assertEquals(listOf("dependent"), d1.dependenciesKind)

        val d2 = der[1]
        Assertions.assertEquals(d2.index, 4)
        Assertions.assertTrue(d2.dependencies.isEmpty())
        Assertions.assertEquals(emptyList<String>(), d2.dependenciesKind)
    }

    @Test
    fun testVariableNamingConvention() {
        val variableNamingConvention = modelDescription.variableNamingConvention
        LOG.info("variableNamingConvention=$variableNamingConvention")
        Assertions.assertEquals(modelDescription.variableNamingConvention, "structured")
    }

}
