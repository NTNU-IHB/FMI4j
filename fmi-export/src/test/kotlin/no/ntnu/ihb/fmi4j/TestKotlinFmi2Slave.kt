package no.ntnu.ihb.fmi4j

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class TestKotlinFmi2Slave {

    @Test
    fun testKotlinSlave() {

        val slave = KotlinTestingFmi2Slave(mapOf("instanceName" to "instance")).apply {
            __define__()
        }
        slave.setupExperiment(1.0)

        with(slave.modelDescription) {
            Assertions.assertEquals("str", modelVariables.scalarVariable[0].name)
            Assertions.assertEquals("start", modelVariables.scalarVariable[1].name)
            Assertions.assertEquals("container.value", modelVariables.scalarVariable[3].name)
            Assertions.assertEquals("container.container.value", modelVariables.scalarVariable[4].name)
            Assertions.assertEquals(slave.getString(longArrayOf(slave.getValueRef("str"))).first(), "1.0")
        }

    }

    @Test
    fun testExtendingKotlinSlave() {

        val slave = KotlinTestingExtendingFmi2Slave(mapOf("instanceName" to "instance")).apply {
            __define__()
        }
        slave.setupExperiment(1.0)
        slave.modelDescription.modelVariables.scalarVariable
        slave.modelDescription.apply {
            Assertions.assertEquals("container.value", modelVariables.scalarVariable[3].name)
            Assertions.assertEquals("container.container.value", modelVariables.scalarVariable[4].name)
            Assertions.assertEquals(slave.getString(longArrayOf(slave.getValueRef("str"))).first(), "1.0")
            Assertions.assertEquals(slave.getReal(longArrayOf(slave.getValueRef("subModel.out"))).first(), 99.0)
        }

    }

}
