package no.mechatronics.sfi.fmi4j.modeldescription

import no.mechatronics.sfi.fmi4j.modeldescription.misc.VariableNamingConvention
import no.mechatronics.sfi.fmi4j.modeldescription.structure.DependenciesKind
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*

class FmuExportCrossCompileTest {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(FmuExportCrossCompileTest::class.java)

        private lateinit var modelDescription: CommonModelDescription

        @JvmStatic
        @BeforeClass
        fun setup() {
            val fmu = File(TEST_FMUs, "FMI_2.0/CoSimulation/win64/OpenModelica/v1.11.0/FmuExportCrossCompile/FmuExportCrossCompile.fmu")
            Assert.assertTrue(fmu.exists())
            modelDescription = ModelDescriptionParser.parse(fmu)
        }

    }

    @Test
    fun testUnknowns() {

        val der = modelDescription.modelStructure.derivatives
        Assert.assertEquals(der.size, 2)

        val d1 = der[0]
        Assert.assertEquals(d1.index, 3)
        Assert.assertEquals(d1.dependencies.size, 1)
        Assert.assertEquals(DependenciesKind.DEPENDENT, d1.dependenciesKind)

        val d2 = der[1]
        Assert.assertEquals(d2.index, 4)
        Assert.assertTrue(d2.dependencies.isEmpty())
        Assert.assertNull(d2.dependenciesKind)
    }

    @Test
    fun testVariableNamingConvention() {
        val variableNamingConvention = modelDescription.variableNamingConvention
        LOG.info("variableNamingConvention=$variableNamingConvention")
        Assert.assertTrue(modelDescription.variableNamingConvention == VariableNamingConvention.STRUCTURED)
    }


    @Test
    fun test1() {

        val bos = ByteArrayOutputStream()
        ObjectOutputStream(bos).use {
            it.writeObject(modelDescription)
            it.flush()
        }

        ObjectInputStream(ByteArrayInputStream(bos.toByteArray())).use {
            val md: CommonModelDescription = it.readObject() as CommonModelDescription
            md.modelVariables.variables.forEach { LOG.info("$it") }
            LOG.info("${md.modelStructure}")
        }

    }

}