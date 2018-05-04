package no.mechatronics.sfi.fmi4j.fmu.misc

import no.mechatronics.sfi.fmi4j.fmu.Fmu
import no.mechatronics.sfi.fmi4j.fmu.TEST_FMUs
import no.mechatronics.sfi.fmi4j.modeldescription.variables.BooleanVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.IntegerVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable
import no.mechatronics.sfi.fmi4j.modeldescription.variables.StringVariable
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class FmuInstanceVariableAccessorTest {

    companion object {

        val LOG: Logger = LoggerFactory.getLogger(FmuInstanceVariableAccessorTest::class.java)

        private lateinit var fmu: Fmu

        @JvmStatic
        @BeforeClass
        fun setUp() {
            val file = File(TEST_FMUs, "FMI_2.0/CoSimulation/win64/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")
            Assert.assertTrue(file.exists())
            fmu = Fmu.from(file)
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            fmu.close()
        }

    }

    @Test
    fun test1() {

        fmu.asCoSimulationFmu().newInstance().use { fmu ->

            fmu.init()

            fmu.modelVariables.forEach { variable ->

                when(variable) {
                    is IntegerVariable -> Assert.assertEquals(variable.read(), fmu.variableAccessor.readInteger(variable.valueReference))
                    is RealVariable ->  Assert.assertEquals(variable.read(), fmu.variableAccessor.readReal(variable.valueReference))
                    is StringVariable -> Assert.assertEquals(variable.read(), fmu.variableAccessor.readString(variable.valueReference))
                    is BooleanVariable -> Assert.assertEquals(variable.read(), fmu.variableAccessor.readBoolean(variable.valueReference))
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
                    is IntegerVariable -> Assert.assertEquals(fmu.variableAccessor.readInteger(variable.valueReference), fmu.variableAccessor.readInteger(variable.name))
                    is RealVariable -> Assert.assertEquals(fmu.variableAccessor.readReal(variable.valueReference), fmu.variableAccessor.readReal(variable.name))
                    is StringVariable -> Assert.assertEquals(fmu.variableAccessor.readString(variable.valueReference), fmu.variableAccessor.readString(variable.name))
                    is BooleanVariable -> Assert.assertEquals(fmu.variableAccessor.readBoolean(variable.valueReference), fmu.variableAccessor.readBoolean(variable.name))
                }

            }

        }

    }

}