package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileFilter

internal class TestBuilder {

    private companion object {
        private val group = "no.ntnu.ihb.fmi4j.slaves"
        private val version = File("../VERSION").readLines().first()
        private val dest = File("build/generated").absolutePath
        private val jar = File("../fmu-slaves/build/libs").listFiles(FileFilter {
            it.name.trim() == "fmu-slaves-$version.jar"
        })!!.first().absolutePath
    }

    @Test
    fun testJavaClass() {

        val testFile = File(javaClass.classLoader.getResource("TestFile.txt")!!.file)

        FmuBuilder.main(
            arrayOf(
                "-m", "$group.JavaTestFmi2Slave",
                "-f", jar,
                "-d", dest,
                "-r", testFile.absolutePath
            )
        )

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
                    Assertions.assertEquals("123", slave.readString(0L).value)

                    slave.close()

                }

            }
        }
    }

    @Test
    fun testKotlinClass() {
        FmuBuilder.main(
            arrayOf(
                "-m", "$group.KotlinTestFmi2Slave",
                "-f", jar,
                "-d", dest
            )
        )

        val fmuFile = File(dest, "KotlinTestFmi2Slave.fmu")
        Assertions.assertTrue(fmuFile.exists())

        Fmu.from(fmuFile).asCoSimulationFmu().use { fmu ->

            val md = fmu.modelDescription
            val modelIdentifier = md.attributes.modelIdentifier
            List(2) { i -> fmu.newInstance("${modelIdentifier}_$i") }.forEach { slave ->
                Assertions.assertTrue(slave.simpleSetup())
                Assertions.assertEquals(10.0, slave.readReal("speed").value)
                slave.doStep(0.1)
                Assertions.assertEquals(-1.0, slave.readReal("speed").value)
                slave.reset()
                Assertions.assertEquals(10.0, slave.readReal("speed").value)

                val ref = DoubleArray(1)
                slave.readAll(
                    null, null,
                    longArrayOf(md.getValueReference("speed")), ref,
                    null, null,
                    null, null
                )
                Assertions.assertEquals(10.0, ref.first())

                println(ref.toList())

            }
        }

    }

    @Test
    fun testParallelInstantiate() {

        FmuBuilder.main(
            arrayOf(
                "-m", "$group.KotlinTestFmi2Slave",
                "-f", jar,
                "-d", dest
            )
        )

        val fmuFile = File(dest, "KotlinTestFmi2Slave.fmu")
        Assertions.assertTrue(fmuFile.exists())

        Fmu.from(fmuFile).asCoSimulationFmu().use { fmu ->
            (0..10).toList().parallelStream().forEach {
                fmu.newInstance().use { slave ->
                    slave.simpleSetup()
                }
            }
        }

    }

}
