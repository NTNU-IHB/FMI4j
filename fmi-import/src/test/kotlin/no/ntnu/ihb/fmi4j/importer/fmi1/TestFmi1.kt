package no.ntnu.ihb.fmi4j.importer.fmi1

import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.importer.AbstractFmu
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import no.ntnu.ihb.fmi4j.readReal
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS

internal class TestFmi1 {

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun TestFmi1() {

        val file = TestFMUs.get("1.0/cs/BouncingBall.fmu")
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

        val file = TestFMUs.get("1.0/cs/BouncingBall.fmu")
        AbstractFmu.from(file).asCoSimulationFmu().use { fmu ->

            fmu.newInstance(fmu.modelDescription.attributes.modelIdentifier).use { slave ->
                Assertions.assertTrue(slave.simpleSetup())
            }

        }

    }

    @Test
    fun testIdentity() {
        val file = TestFMUs.get("1.0/cs/identity.fmu")
        AbstractFmu.from(file).asCoSimulationFmu().use { fmu ->

            fmu.newInstance().use { slave ->
                Assertions.assertTrue(slave.simpleSetup())
                Assertions.assertTrue(slave.doStep(0.1))
                Assertions.assertTrue(slave.terminate())
            }

        }
    }

}
