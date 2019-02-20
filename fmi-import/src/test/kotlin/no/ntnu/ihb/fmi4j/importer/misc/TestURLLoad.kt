package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.importer.TestFMUs
import no.ntnu.ihb.fmi4j.importer.Fmu
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestURLLoad {

    @Test
    fun test() {
        Fmu.from(TestFMUs.fmi20().cs()
                .vendor("20sim").version("4.6.4.8004")
                .name("ControlledTemperature").file().toURI().toURL()).use {
            Assertions.assertEquals("ControlledTemperature", it.modelDescription.modelName)
        }
    }

}