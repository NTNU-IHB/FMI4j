package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.importer.TestFMUs
import no.ntnu.ihb.fmi4j.modeldescription.variables.BooleanVariable
import no.ntnu.ihb.fmi4j.modeldescription.variables.IntegerVariable
import no.ntnu.ihb.fmi4j.modeldescription.variables.RealVariable
import no.ntnu.ihb.fmi4j.modeldescription.variables.StringVariable
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FmuInstanceVariableAccessorTest {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(FmuInstanceVariableAccessorTest::class.java)
    }

    private val fmu = TestFMUs.fmi20().cs()
            .vendor("20sim").version("4.6.4.8004")
            .name("ControlledTemperature").fmu().asCoSimulationFmu()

    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    @Test
    fun test1() {

        fmu.newInstance().use { slave ->

            Assertions.assertTrue(slave.simpleSetup())

            slave.modelVariables.forEach { variable ->
                when (variable) {
                    is IntegerVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.variableAccessor.readInteger(variable.valueReference))
                    is RealVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.variableAccessor.readReal(variable.valueReference))
                    is StringVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.variableAccessor.readString(variable.valueReference))
                    is BooleanVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.variableAccessor.readBoolean(variable.valueReference))
                }
            }

        }

    }

    @Test
    fun test2() {

        fmu.newInstance().use { slave ->

            Assertions.assertTrue(slave.simpleSetup())

            slave.modelVariables.forEach { variable ->
                when (variable) {
                    is IntegerVariable -> Assertions.assertEquals(
                            slave.variableAccessor.readInteger(variable.valueReference),
                            slave.variableAccessor.readInteger(variable.name))
                    is RealVariable -> Assertions.assertEquals(
                            slave.variableAccessor.readReal(variable.valueReference),
                            slave.variableAccessor.readReal(variable.name))
                    is StringVariable -> Assertions.assertEquals(
                            slave.variableAccessor.readString(variable.valueReference),
                            slave.variableAccessor.readString(variable.name))
                    is BooleanVariable -> Assertions.assertEquals(
                            slave.variableAccessor.readBoolean(variable.valueReference),
                            slave.variableAccessor.readBoolean(variable.name))
                }
            }

        }

    }

}