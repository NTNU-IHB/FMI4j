package no.ntnu.ihb.fmi4j.importer.fmi2.cs.vendors.fmuproxy

import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import org.junit.jupiter.api.Test

internal class TestProxyFmu {

    @Test
    fun testProxyFmu() {

        val fmuFile = TestFMUs.get("2.0/cs/fmuproxy/identity-proxy.fmu")

        Fmu.from(fmuFile).use { fmu ->
            fmu.asCoSimulationFmu().newInstance().use { slave ->
                slave.simpleSetup()
                slave.doStep(0.1)
                slave.terminate()
            }
        }

    }

}
