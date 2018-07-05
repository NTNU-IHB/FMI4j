package no.mechatronics.sfi.fmi4j.importer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS

@EnabledOnOs(OS.WINDOWS)
class TestURLLoad {

    @Test
    fun test() {
        Fmu.from(TestURLLoad::class.java.classLoader.getResource("bouncingBall.fmu")).use {
            Assertions.assertEquals("bouncingBall", it.modelDescription.modelName)
        }
    }

}