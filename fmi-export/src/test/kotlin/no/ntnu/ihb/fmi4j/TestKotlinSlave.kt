package no.ntnu.ihb.fmi4j

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class TestKotlinSlave {

    @Test
    fun testKotlinSlave() {

        val slave = KotlinTestingSlave("instance").definekt()
        val md = KotlinTestingSlave("instance").definekt().modelDescription
        Assertions.assertEquals("container.value", md.modelVariables.scalarVariable[0].name)
        Assertions.assertEquals("container.container2.value2", md.modelVariables.scalarVariable[1].name)

    }

}
