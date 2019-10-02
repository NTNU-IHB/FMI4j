package no.ntnu.ihb.fmi4j.modeldescription.misc

import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import no.ntnu.ihb.fmi4j.modeldescription.variables.Causality
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CalculatedParameterTest {

    val xml = """
<?xml version="1.0" encoding="ISO-8859-1"?>
<fmiModelDescription fmiVersion="2.0" modelName="Test" guid="1234">
    <CoSimulation modelIdentifier="test"></CoSimulation>
    <ModelVariables>
        <ScalarVariable name="myvar" valueReference="1" causality="calculatedParameter">
            <Real />
        </ScalarVariable>
    </ModelVariables>
    <ModelStructure>
    </ModelStructure>
</fmiModelDescription>

    """.trimIndent()

    @Test
    fun test() {

        ModelDescriptionParser.parseModelDescription(xml).also {
            it.asCoSimulationModelDescription().modelVariables.forEach {
                Assertions.assertEquals(Causality.CALCULATED_PARAMETER, it.causality)
            }
        }

    }

}