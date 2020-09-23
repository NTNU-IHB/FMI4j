package no.ntnu.ihb.fmi4j.importer.fmi2.cs.vendors.fmi4j

import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS

internal class TestFmi4jFmu {

    @Test
    fun testFmi4jFmu() {

        val fmuFile = TestFMUs.get("2.0/cs/fmi4j/KotlinTestFmi2Slave.fmu")

        Fmu.from(fmuFile).use { fmu ->
            fmu.asCoSimulationFmu().newInstance().use { slave ->
                slave.simpleSetup()
                slave.doStep(0.1)
                slave.terminate()
            }
        }

    }

}
