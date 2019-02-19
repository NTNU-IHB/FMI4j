package no.ntnu.ihb.fmi4j.modeldescription.vendors.openmodelica

import no.ntnu.ihb.fmi4j.modeldescription.TestFMUs
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescription
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FmuExportCrossCompileTest {

    private companion object {

        val LOG: Logger = LoggerFactory.getLogger(FmuExportCrossCompileTest::class.java)

        val modelDescription = TestFMUs.fmi20().cs()
                .vendor("OpenModelica").version("v1.11.0")
                .name("FmuExportCrossCompile").modelDescription()

    }

    @Test
    fun testUnknowns() {

        val der = modelDescription.modelStructure.derivatives
        Assertions.assertEquals(der.size, 2)

        val d1 = der[0]
        Assertions.assertEquals(d1.index, 3)
        Assertions.assertEquals(d1.dependencies.size, 1)
        Assertions.assertEquals("dependent", d1.dependenciesKind)

        val d2 = der[1]
        Assertions.assertEquals(d2.index, 4)
        Assertions.assertTrue(d2.dependencies.isEmpty())
        Assertions.assertEquals("", d2.dependenciesKind)
    }

    @Test
    fun testVariableNamingConvention() {
        val variableNamingConvention = modelDescription.variableNamingConvention
        LOG.info("variableNamingConvention=$variableNamingConvention")
        Assertions.assertTrue(modelDescription.variableNamingConvention == "structured")
    }

    @Test
    fun testSerialization() {

        val bos = ByteArrayOutputStream()
        ObjectOutputStream(bos).use {
            it.writeObject(modelDescription)
            it.flush()
        }

        ObjectInputStream(ByteArrayInputStream(bos.toByteArray())).use {
            val md: ModelDescription = it.readObject() as ModelDescription
            Assertions.assertEquals(modelDescription.modelVariables.size, md.modelVariables.size)
            md.modelVariables.forEach { LOG.info("$it") }
            LOG.info("${md.modelStructure}")
        }

    }

}
