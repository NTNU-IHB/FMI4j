package no.mechatronics.sfi.fmi4j.fmu

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

class FmuVariableAccessorTest {

    companion object {

        val LOG: Logger = LoggerFactory.getLogger(CoSimulationFmuTest_kt::class.java)

        private lateinit var fmuFile: FmuFile

        @JvmStatic
        @BeforeClass
        fun setUp() {

            val path = "../test/fmi2/cs/win64/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"
            val file = File(path)
            Assert.assertNotNull(file)
            fmuFile = FmuFile.from(file)

        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            fmuFile.close()
        }

    }

    @Test
    fun test1() {

        fmuFile.asCoSimulationFmu().newInstance().use { fmu ->

            Assert.assertTrue(fmu.init())

            fmu.modelVariables.forEach { variable ->

//                println(variable)

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

        fmuFile.asCoSimulationFmu().newInstance().use { fmu ->

            Assert.assertTrue(fmu.init())

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