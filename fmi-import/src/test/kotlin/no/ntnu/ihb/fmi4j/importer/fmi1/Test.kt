package no.ntnu.ihb.fmi4j.importer.fmi1

import no.ntnu.ihb.fmi4j.importer.AbstractFmu
import no.ntnu.ihb.fmi4j.importer.ControlledTemperatureTestJava
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import no.ntnu.ihb.fmi4j.readReal
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import java.io.File

class Test {

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun TestFmi1() {

        val file = File(Test::class.java.classLoader.getResource("fmus/1.0/cs/BouncingBall.fmu")!!.file)
        Assertions.assertEquals("1.0", ModelDescriptionParser.extractVersion(file))

        Fmu.from(file).asCoSimulationFmu().use { fmu ->

            Assertions.assertEquals("{8c4e810f-3df3-4a00-8276-176fa3c9f003}", fmu.modelDescription.guid)
            Assertions.assertEquals("BouncingBall", fmu.modelDescription.attributes.modelIdentifier)

            fmu.newInstance(fmu.modelDescription.attributes.modelIdentifier).use { slave ->

                slave.simpleSetup()
                Assertions.assertEquals(1.0, slave.readReal("h").value)

                while (slave.simulationTime < 3.0) {
                    slave.doStep(1E-2)
                }

                Assertions.assertEquals(0.014, slave.readReal("h").value, 1e-3)

                slave.terminate()

            }

        }

    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testAbstractFmuLoad() {

        val file = File(Test::class.java.classLoader.getResource("fmus/1.0/cs/BouncingBall.fmu")!!.file)
        AbstractFmu.from(file).asCoSimulationFmu().use { fmu ->

            fmu.newInstance(fmu.modelDescription.attributes.modelIdentifier).use { slave ->
                Assertions.assertTrue(slave.simpleSetup())
            }

        }

    }

}
