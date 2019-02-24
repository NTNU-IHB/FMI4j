package no.ntnu.ihb.fmi.xml.vendors.AMESim

import no.ntnu.ihb.fmi.xml.TestFMUs
import no.ntnu.ihb.fmi.xml.misc.Unit
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AMESimTest {

    companion object {

        val md = TestFMUs.fmi20().cs()
                .vendor("AMESim").version("15")
                .name("MIS_cs").modelDescription().let {
                    it.asCoSimulationModelDescription()
                }

    }

    @Test
    fun test() {

        Assertions.assertEquals("2069003513", md.guid)
        Assertions.assertEquals(true, md.canBeInstantiatedOnlyOncePerProcess)
        Assertions.assertEquals(true, md.canNotUseMemoryManagementFunctions)

        md.modelVariables.getByName("maxTimeStep").also {
            Assertions.assertEquals(536870912L, it.valueReference)
            Assertions.assertEquals("maximum internal time step", it.description)
        }


    }

    @Test
    fun testUnitDefinitions() {
        Assertions.assertEquals(md.unitDefinitions, listOf("L", "L/min", "s").map { Unit(it) })
    }

}