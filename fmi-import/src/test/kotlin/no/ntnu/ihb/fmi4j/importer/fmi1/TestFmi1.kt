package no.ntnu.ihb.fmi4j.importer.fmi1

import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.importer.AbstractFmu
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import no.ntnu.ihb.fmi4j.readReal
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TestFmi1 {

    private companion object {
        private val file = TestFMUs.get("1.0/cs/BouncingBall.fmu")
    }

    @Test
    fun TestFmi1() {

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
    fun testAbstractFmuLoad() {

        AbstractFmu.from(file).asCoSimulationFmu().use { fmu ->

            fmu.newInstance(fmu.modelDescription.attributes.modelIdentifier).use { slave ->
                Assertions.assertTrue(slave.simpleSetup())
            }

        }

    }

}
