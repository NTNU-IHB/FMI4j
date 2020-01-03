package no.ntnu.ihb.fmi4j

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class TestKotlinFmi2Slave {

    @Test
    fun testKotlinSlave() {

        val slave = KotlinTestingFmi2Slave("instance")
        slave.setupExperiment(1.0)
        with(slave.modelDescription) {
            Assertions.assertEquals("container.value", modelVariables.scalarVariable[0].name)
            Assertions.assertEquals("container.container2.value2", modelVariables.scalarVariable[1].name)
            Assertions.assertEquals(slave.getString(2), "1.0")
        }

    }

}