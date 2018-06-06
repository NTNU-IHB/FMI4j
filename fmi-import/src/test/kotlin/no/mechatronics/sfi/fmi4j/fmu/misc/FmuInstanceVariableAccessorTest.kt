package no.mechatronics.sfi.fmi4j.fmu.misc

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.fmu.Fmu
import no.mechatronics.sfi.fmi4j.modeldescription.variables.BooleanVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.IntegerVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.StringVariable
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FmuInstanceVariableAccessorTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(FmuInstanceVariableAccessorTest::class.java)
    }

    private val fmu: Fmu

    init {
        val file = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/${TestUtils.getOs()}/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")
        Assertions.assertTrue(file.exists())
        fmu = Fmu.from(file)
    }

    @AfterAll
    fun tearDown() {
        fmu.close()
    }


    @Test
    fun test1() {

        fmu.asCoSimulationFmu().newInstance().use { fmu ->

            fmu.init()
            fmu.modelVariables.forEach { variable ->
                when(variable) {
                    is IntegerVariable -> Assertions.assertEquals(variable.read(), fmu.variableAccessor.readInteger(variable.valueReference))
                    is RealVariable -> Assertions.assertEquals(variable.read(), fmu.variableAccessor.readReal(variable.valueReference))
                    is StringVariable -> Assertions.assertEquals(variable.read(), fmu.variableAccessor.readString(variable.valueReference))
                    is BooleanVariable -> Assertions.assertEquals(variable.read(), fmu.variableAccessor.readBoolean(variable.valueReference))
                }
            }

        }

    }

    @Test
    fun test2() {

        fmu.asCoSimulationFmu().newInstance().use { fmu ->

            fmu.init()
            fmu.modelVariables.forEach { variable ->
                when(variable) {
                    is IntegerVariable -> Assertions.assertEquals(fmu.variableAccessor.readInteger(variable.valueReference), fmu.variableAccessor.readInteger(variable.name))
                    is RealVariable -> Assertions.assertEquals(fmu.variableAccessor.readReal(variable.valueReference), fmu.variableAccessor.readReal(variable.name))
                    is StringVariable -> Assertions.assertEquals(fmu.variableAccessor.readString(variable.valueReference), fmu.variableAccessor.readString(variable.name))
                    is BooleanVariable -> Assertions.assertEquals(fmu.variableAccessor.readBoolean(variable.valueReference), fmu.variableAccessor.readBoolean(variable.name))
                }
            }

        }

    }

}