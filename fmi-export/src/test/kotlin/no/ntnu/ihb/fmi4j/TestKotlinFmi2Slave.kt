package no.ntnu.ihb.fmi4j

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class TestKotlinFmi2Slave {

    @Test
    fun testKotlinSlave() {

        val slave = KotlinTestingFmi2Slave(mapOf("instanceName" to "instance")).apply {
            __define__()
        }
        slave.setupExperiment(1.0, -1.0, -1.0)

        slave.modelDescription.apply {
            Assertions.assertNotNull(modelVariables.scalarVariable.firstOrNull { it.name == "real" })
            Assertions.assertNotNull(modelVariables.scalarVariable.firstOrNull { it.name == "str" })
            Assertions.assertNotNull(modelVariables.scalarVariable.firstOrNull { it.name == "start" })
            Assertions.assertNotNull(modelVariables.scalarVariable.firstOrNull { it.name == "container.value" })
            Assertions.assertNotNull(modelVariables.scalarVariable.firstOrNull { it.name == "container.container.value" })
            Assertions.assertEquals("1.0", slave.getString(longArrayOf(slave.getValueRef("str"))).first())
            Assertions.assertEquals(123.0, slave.getReal(longArrayOf(slave.getValueRef("real"))).first())
        }

    }

    @Test
    fun testExtendingKotlinSlave() {

        val slave = KotlinTestingExtendingFmi2Slave(mapOf("instanceName" to "instance")).apply {
            __define__()
        }
        slave.setupExperiment(1.0, -1.0, -1.0)
        slave.modelDescription.modelVariables.scalarVariable
        slave.modelDescription.apply {
            Assertions.assertNotNull(modelVariables.scalarVariable.firstOrNull { it.name == "container.value" })
            Assertions.assertNotNull(modelVariables.scalarVariable.firstOrNull { it.name == "container.container.value" })
            Assertions.assertEquals(slave.str, slave.getString(longArrayOf(slave.getValueRef("str"))).first())
            Assertions.assertEquals(99.0, slave.getReal(longArrayOf(slave.getValueRef("subModel.out"))).first())
        }

    }

}
