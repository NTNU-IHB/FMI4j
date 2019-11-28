package no.ntnu.ihb.fmi4j

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class TestKotlinSlave {

    @Test
    fun testKotlinSlave() {

        val slave = KotlinTestingSlave("instance").definekt()
        with(slave.modelDescription) {
            Assertions.assertEquals("container.value", modelVariables.scalarVariable[0].name)
            Assertions.assertEquals("container.container2.value2", modelVariables.scalarVariable[1].name)
        }

    }

}
