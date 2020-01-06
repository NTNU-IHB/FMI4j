package no.ntnu.ihb.fmi4j

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class TestKotlinFmi2Slave {

    @Test
    fun testKotlinSlave() {

        val slave = KotlinTestingFmi2Slave("instance")
        slave.__define__()
        slave.setupExperiment(1.0)
        slave.modelDescription.modelVariables.scalarVariable
        with(slave.modelDescription) {
            Assertions.assertEquals("container.value", modelVariables.scalarVariable[0].name)
            Assertions.assertEquals("container.container2.value2", modelVariables.scalarVariable[1].name)
            Assertions.assertEquals(slave.getString(slave.getValueReference("str")), "1.0")
        }

    }

}
