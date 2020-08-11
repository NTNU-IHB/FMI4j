package no.ntnu.ihb.fmi4j.importer.fmi2

import no.ntnu.ihb.fmi4j.*
import no.ntnu.ihb.fmi4j.modeldescription.variables.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FmuVariableAccessorTest {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(FmuVariableAccessorTest::class.java)
    }

    private val fmu = TestFMUs.get("2.0/cs/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu").let {
        Fmu.from(it).asCoSimulationFmu()
    }

    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    @Test
    fun test1() {

        fmu.newInstance(fmu.modelDescription.attributes.modelIdentifier).use { slave ->

            Assertions.assertTrue(slave.simpleSetup())

            slave.modelVariables.forEach { variable ->
                when (variable) {
                    is IntegerVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.readInteger(variable.valueReference))
                    is RealVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.readReal(variable.valueReference))
                    is StringVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.readString(variable.valueReference))
                    is BooleanVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.readBoolean(variable.valueReference))
                    is EnumerationVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.readInteger(variable.valueReference))
                }
            }

        }

    }

    @Test
    fun test2() {

        fmu.newInstance(fmu.modelDescription.attributes.modelIdentifier).use { slave ->

            Assertions.assertTrue(slave.simpleSetup())

            slave.modelVariables.forEach { variable ->
                when (variable) {
                    is IntegerVariable -> Assertions.assertEquals(
                            slave.readInteger(variable.valueReference),
                            slave.readInteger(variable.name))
                    is RealVariable -> Assertions.assertEquals(
                            slave.readReal(variable.valueReference),
                            slave.readReal(variable.name))
                    is StringVariable -> Assertions.assertEquals(
                            slave.readString(variable.valueReference),
                            slave.readString(variable.name))
                    is BooleanVariable -> Assertions.assertEquals(
                            slave.readBoolean(variable.valueReference),
                            slave.readBoolean(variable.name))
                    is EnumerationVariable -> Assertions.assertEquals(
                            slave.readInteger(variable.valueReference),
                            slave.readInteger(variable.name))
                }
            }

        }

    }

}
