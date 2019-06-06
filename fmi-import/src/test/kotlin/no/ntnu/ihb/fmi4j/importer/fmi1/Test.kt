package no.ntnu.ihb.fmi4j.importer.fmi1

import no.ntnu.ihb.fmi4j.readReal
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class Test {

    @Test
    fun TestFmi1() {

        Fmu.from(File("D:\\Development\\misc\\cse-demos\\dp-ship\\DPController.fmu")).use { fmu ->

            Assertions.assertEquals("7d868e5a-ae40-3592-9c35-0ed5d0b25746", fmu.guid)
            Assertions.assertEquals("DPController", fmu.modelDescription.asCoSimulationModelDescription().attributes.modelIdentifier)

            fmu.asCoSimulationFmu().newInstance().use {

                it.setup()
                Assertions.assertEquals(1.0, it.readReal("ComTimeStep").value)
                it.doStep(1E-3)

            }

        }

    }

}
