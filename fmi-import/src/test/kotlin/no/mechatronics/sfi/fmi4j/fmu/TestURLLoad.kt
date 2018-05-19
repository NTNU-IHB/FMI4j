package no.mechatronics.sfi.fmi4j.fmu

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestURLLoad {

    @Test
    fun test() {
        Fmu.from(TestURLLoad::class.java.classLoader.getResource("bouncingBall.fmu")).use {
            Assertions.assertEquals("bouncingBall", it.modelDescription.modelName)
        }
    }

}