package no.mechatronics.sfi.fmi4j.importer.misc

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.currentOS
import no.mechatronics.sfi.fmi4j.importer.Fmu
import no.mechatronics.sfi.fmi4j.modeldescription.variables.BooleanVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.IntegerVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.StringVariable
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class FmuInstanceVariableAccessorTest {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(FmuInstanceVariableAccessorTest::class.java)
    }

    private val fmu = Fmu.from(File(TestUtils.getTEST_FMUs(),
            "FMI_2.0/CoSimulation/$currentOS" +
                    "/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"))


    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    @Test
    fun test1() {

        fmu.asCoSimulationFmu().newInstance().use { slave ->

            slave.init()
            slave.modelVariables.forEach { variable ->
                when (variable) {
                    is IntegerVariable -> Assertions.assertEquals(variable.read(slave), slave.readInteger(variable.valueReference))
                    is RealVariable -> Assertions.assertEquals(variable.read(slave), slave.readReal(variable.valueReference))
                    is StringVariable -> Assertions.assertEquals(variable.read(slave), slave.readString(variable.valueReference))
                    is BooleanVariable -> Assertions.assertEquals(variable.read(slave), slave.readBoolean(variable.valueReference))
                }
            }

        }

    }

    @Test
    fun test2() {

        fmu.asCoSimulationFmu().newInstance().use { instance ->

            instance.init()
            instance.modelVariables.forEach { variable ->
                when (variable) {
                    is IntegerVariable -> Assertions.assertEquals(instance.readInteger(variable.valueReference), instance.readInteger(variable.name))
                    is RealVariable -> Assertions.assertEquals(instance.readReal(variable.valueReference), instance.readReal(variable.name))
                    is StringVariable -> Assertions.assertEquals(instance.readString(variable.valueReference), instance.readString(variable.name))
                    is BooleanVariable -> Assertions.assertEquals(instance.readBoolean(variable.valueReference), instance.readBoolean(variable.name))
                }
            }

        }

    }

}