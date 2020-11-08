package no.ntnu.ihb.fmi4j.jackson

import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class TestJackson {

    @Test
    fun test() {
        TestFMUs.get("2.0/me/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu").also {
            val md = ModelDescriptionParser.parseModelDescription(it)
            Assertions.assertEquals(5, md.modelVariables.size)
        }
    }

}
