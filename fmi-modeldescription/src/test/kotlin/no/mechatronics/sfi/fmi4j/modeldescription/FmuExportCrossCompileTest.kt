package no.mechatronics.sfi.fmi4j.modeldescription

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.modeldescription.misc.VariableNamingConvention
import no.mechatronics.sfi.fmi4j.modeldescription.structure.DependenciesKind
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class FmuExportCrossCompileTest {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(FmuExportCrossCompileTest::class.java)
    }

    private val modelDescription: CoSimulationModelDescription

    init {
        val fmu = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/${TestUtils.getOs()}/OpenModelica/v1.11.0/FmuExportCrossCompile/FmuExportCrossCompile.fmu")
        println(fmu)
        Assertions.assertTrue(fmu.exists())
        modelDescription = ModelDescriptionParser.parse(fmu).asCoSimulationModelDescription()
    }

    @Test
    fun testUnknowns() {

        val der = modelDescription.modelStructure.derivatives
        Assertions.assertEquals(der.size, 2)

        val d1 = der[0]
        Assertions.assertEquals(d1.index, 3)
        Assertions.assertEquals(d1.dependencies.size, 1)
        Assertions.assertEquals(DependenciesKind.DEPENDENT, d1.dependenciesKind)

        val d2 = der[1]
        Assertions.assertEquals(d2.index, 4)
        Assertions.assertTrue(d2.dependencies.isEmpty())
        Assertions.assertNull(d2.dependenciesKind)
    }

    @Test
    fun testVariableNamingConvention() {
        val variableNamingConvention = modelDescription.variableNamingConvention
        LOG.info("variableNamingConvention=$variableNamingConvention")
        Assertions.assertTrue(modelDescription.variableNamingConvention == VariableNamingConvention.STRUCTURED)
    }

    @Test
    fun test1() {

        val bos = ByteArrayOutputStream()
        ObjectOutputStream(bos).use {
            it.writeObject(modelDescription)
            it.flush()
        }

        ObjectInputStream(ByteArrayInputStream(bos.toByteArray())).use {
            val md: CommonModelDescription = it.readObject() as CoSimulationModelDescription
            Assertions.assertEquals(modelDescription.modelVariables.size, md.modelVariables.size)
            md.modelVariables.variables.forEach { LOG.info("$it") }
            LOG.info("${md.modelStructure}")
        }

    }

}