package no.ntnu.ihb.fmi4j.modeldescription.vendors.AMESim

import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AMESimTest {

    companion object {

        val md = TestFMUs.get("2.0/cs/AMESim/15/MIS_cs/MIS_cs.fmu").let {
            ModelDescriptionParser.parse(it).asCoSimulationModelDescription()
        }

    }

    @Test
    fun test() {

        Assertions.assertEquals("2069003513", md.guid)
        Assertions.assertEquals(true, md.attributes.canBeInstantiatedOnlyOncePerProcess)
        Assertions.assertEquals(true, md.attributes.canNotUseMemoryManagementFunctions)

        md.modelVariables.getByName("maxTimeStep").also {
            Assertions.assertEquals(536870912L, it.valueReference)
            Assertions.assertEquals("maximum internal time step", it.description)
        }

    }

    @Test
    fun testUnitDefinitions() {
        Assertions.assertEquals(md.unitDefinitions?.map { it.name }, listOf("L", "L/min", "s"))
    }

}
