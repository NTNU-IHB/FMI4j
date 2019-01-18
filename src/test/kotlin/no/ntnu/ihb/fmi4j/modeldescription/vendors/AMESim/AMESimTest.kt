package no.ntnu.ihb.fmi4j.modeldescription.vendors.AMESim

import no.ntnu.ihb.fmi4j.TestFMUs
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AMESimTest {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AMESimTest::class.java)
    }

    @Test
    fun test() {

       TestFMUs.fmi20().cs()
                .vendor("AMESim")
                .version("15")
                .fmu("MIS_cs").use {

                   LOG.debug(it.modelDescriptionXml)
                    it.asCoSimulationFmu()
                }

    }

}