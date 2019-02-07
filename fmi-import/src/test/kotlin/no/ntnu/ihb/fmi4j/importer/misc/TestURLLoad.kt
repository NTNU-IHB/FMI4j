package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.TestFMUs
import no.ntnu.ihb.fmi4j.importer.Fmu
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestURLLoad {

    @Test
    fun test() {
        Fmu.from(TestFMUs.fmi20().me()
                .vendor("FMUSDK").version("2.0.4")
                .name("bouncingball").file().toURI().toURL()).use {
            Assertions.assertEquals("bouncingBall", it.modelDescription.modelName)
        }
    }

}