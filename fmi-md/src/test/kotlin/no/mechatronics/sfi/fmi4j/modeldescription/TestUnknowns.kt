package no.mechatronics.sfi.fmi4j.modeldescription


import no.mechatronics.sfi.fmi4j.modeldescription.structure.DependenciesKind
import org.apache.commons.io.FileUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.charset.Charset

class TestUnknowns {

    private lateinit var modelDescription: ModelDescriptionProvider

    @Before
    fun setUp() {
        val path = "../test/fmi2/cs/win64/OpenModelica/v1.11.0/FmuExportCrossCompile/modelDescription.xml"
        val file = File(path)
        Assert.assertTrue(file.exists())
        val xml = FileUtils.readFileToString(file, Charset.forName("UTF-8"))
        modelDescription = ModelDescriptionParser.parse(xml)
    }

    @Test
    fun testUnknowns() {

        val der = modelDescription.modelStructure.derivatives
        Assert.assertEquals(der.size, 2)

        val d1 = der[0]
        Assert.assertEquals(d1.index, 3)
        Assert.assertEquals(d1.dependencies!!.size, 1)
        Assert.assertEquals(DependenciesKind.DEPENDENT, d1.dependenciesKind)

        val d2 = der[1]
        Assert.assertEquals(d2.index, 4)
        Assert.assertNull(d2.dependencies)
        Assert.assertNull(d2.dependenciesKind)
    }


}