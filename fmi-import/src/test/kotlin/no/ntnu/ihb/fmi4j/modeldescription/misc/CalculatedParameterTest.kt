package no.ntnu.ihb.fmi4j.modeldescription.misc

import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import no.ntnu.ihb.fmi4j.modeldescription.variables.Causality
import no.ntnu.ihb.fmi4j.modeldescription.variables.VariableType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CalculatedParameterTest {

    val xml = """
<?xml version="1.0" encoding="UTF-8"?>
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

        ModelDescriptionParser.parse(xml).also {
            Assertions.assertEquals("1234", it.guid)
            Assertions.assertEquals(1, it.modelVariables.reals.size)
            Assertions.assertEquals("test", it.asCoSimulationModelDescription().attributes.modelIdentifier)
            Assertions.assertEquals("myvar", it.modelVariables.getByValueReference(1, VariableType.REAL).first().name)
            it.asCoSimulationModelDescription().modelVariables.forEach {
                Assertions.assertEquals(Causality.CALCULATED_PARAMETER, it.causality)
            }
        }

    }

}
