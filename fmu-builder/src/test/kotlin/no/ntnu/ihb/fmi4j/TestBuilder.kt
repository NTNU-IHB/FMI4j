package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import java.io.File

@EnabledOnOs(OS.WINDOWS)
class TestBuilder {

    companion object {
        val group = "no.ntnu.ihb.fmi4j"
        val dest = File("build/generated").absolutePath
        val jar = File("../fmu-slaves/build/libs/fmu-slaves.jar").absolutePath
    }

    @Test
    fun testJavaClass() {
        FmuBuilder.main(arrayOf("-f", jar, "-m", "$group.JavaTestFmi2Slave", "-d", dest))
        for (i in 0..2) {

            val fmuFile = File(dest, "Test.fmu")
            Assertions.assertTrue(fmuFile.exists())

            Fmu.from(fmuFile).asCoSimulationFmu().use { fmu ->

                val dt = 0.1
                val modelIdentifier = fmu.modelDescription.attributes.modelIdentifier
                List(2) { i -> fmu.newInstance("${modelIdentifier}_$i") }.forEach { slave ->

                    Assertions.assertTrue(slave.simpleSetup())
                    Assertions.assertEquals(2.0, slave.readReal("realOut").value)
                    Assertions.assertTrue(slave.doStep(dt))
                    Assertions.assertEquals(2.0 + dt, slave.readReal("realOut").value)
                    Assertions.assertEquals(99.0, slave.readReal("speed").value)
                    slave.reset()
                    Assertions.assertEquals(2.0, slave.readReal("realOut").value)
                    slave.close()

                }

            }
        }
    }

    @Test
    fun testKotlinClass() {
        FmuBuilder.main(arrayOf("-f", jar, "-m", "$group.KotlinTestFmi2Slave", "-d", dest))

        val fmuFile = File(dest, "KotlinTestFmi2Slave.fmu")
        Assertions.assertTrue(fmuFile.exists())

        Fmu.from(fmuFile).asCoSimulationFmu().use { fmu ->

            val modelIdentifier = fmu.modelDescription.attributes.modelIdentifier
            List(2) { i -> fmu.newInstance("${modelIdentifier}_$i") }.forEach { slave ->
                Assertions.assertTrue(slave.simpleSetup())
                Assertions.assertEquals(10.0, slave.readReal("speed").value)
                slave.doStep(0.1)
                Assertions.assertEquals(-1.0, slave.readReal("speed").value)
                slave.reset()
                Assertions.assertEquals(10.0, slave.readReal("speed").value)
            }
        }
    }

}
