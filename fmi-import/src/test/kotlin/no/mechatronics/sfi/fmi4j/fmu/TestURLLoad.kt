package no.mechatronics.sfi.fmi4j.fmu

import org.junit.Assert
import org.junit.Test

class TestURLLoad {


    @Test
    fun test() {

        Fmu.from(TestURLLoad::class.java.classLoader.getResource("bouncingBall.fmu")).use {

            Assert.assertEquals("bouncingBall", it.modelDescription.modelName)

        }

    }


}