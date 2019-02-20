package no.ntnu.ihb.fmi4j.modeldescription.vendors.AMESim

import no.ntnu.ihb.fmi4j.modeldescription.TestFMUs
import org.junit.jupiter.api.Test


class AMESimTest {

    @Test
    fun test() {
       TestFMUs.fmi20().cs()
                .vendor("AMESim").version("15")
                .name("MIS_cs").modelDescription().also {
                    it.asCoSimulationModelDescription()
                }
    }

}