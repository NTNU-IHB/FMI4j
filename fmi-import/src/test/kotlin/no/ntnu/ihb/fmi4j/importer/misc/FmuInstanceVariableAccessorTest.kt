package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.common.read
import no.ntnu.ihb.fmi4j.common.readBoolean
import no.ntnu.ihb.fmi4j.common.readInteger
import no.ntnu.ihb.fmi4j.common.readReal
import no.ntnu.ihb.fmi4j.importer.TestFMUs
import no.ntnu.ihb.fmi4j.modeldescription.variables.*
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
                            variable.read(slave), slave.readInteger(variable.valueReference))
                    is RealVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.readReal(variable.valueReference))
                    is StringVariable -> Assertions.assertEquals(
                            variable.read(slave), slave.read(variable.valueReference))
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

        fmu.newInstance().use { slave ->

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
                            slave.read(variable.valueReference),
                            slave.read(variable.name))
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