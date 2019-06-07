package no.ntnu.ihb.fmi4j.importer.fmi1

import no.ntnu.ihb.fmi4j.readReal
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import java.io.File

class Test {

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun TestFmi1() {

        Fmu.from(File(Test::class.java.classLoader.getResource("fmus/1.0/cs/BouncingBall.fmu").file)).use { fmu ->

            Assertions.assertEquals("{8c4e810f-3df3-4a00-8276-176fa3c9f003}", fmu.guid)
            Assertions.assertEquals("BouncingBall", fmu.modelDescription.attributes.modelIdentifier)

            fmu.newInstance().use { slave ->

                slave.setup()
                Assertions.assertEquals(1.0, slave.readReal("h").value)

                for (i in 0..10) {
                    slave.doStep(1E-3)
                }

                Assertions.assertTrue(slave.readReal("h").value < 1.0)

            }

        }

    }

}
