package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.stringArrayOf
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
                slave.close()
            }
        }

    }

    @Test
    fun testIdentity() {
        FmuBuilder.main(
            arrayOf(
                "-m", "$group.Identity",
                "-f", jar,
                "-d", dest
            )
        )

        val fmuFile = File(dest, "Identity.fmu")
        Assertions.assertTrue(fmuFile.exists())

        val vrs = longArrayOf(0)

        val intValue = intArrayOf(99)
        val realValue = doubleArrayOf(12.3)
        val boolValue = booleanArrayOf(true)
        val strValue = stringArrayOf("Hello identity")

        val intRef = IntArray(1)
        val realRef = DoubleArray(1)
        val boolRef = BooleanArray(1)
        val strRef = StringArray(1)

        Fmu.from(fmuFile).asCoSimulationFmu().use { fmu ->

            val md = fmu.modelDescription
            Assertions.assertEquals("Identity", md.modelName)

            fmu.newInstance().use { slave ->

                Assertions.assertTrue(slave.simpleSetup())

                slave.writeInteger(vrs, intValue)
                slave.writeReal(vrs, realValue)
                slave.writeBoolean(vrs, boolValue)
                slave.writeString(vrs, strValue)

                Assertions.assertTrue(slave.doStep(0.1))

                slave.readInteger(vrs, intRef)
                slave.readReal(vrs, realRef)
                slave.readBoolean(vrs, boolRef)
                slave.readString(vrs, strRef)

                Assertions.assertEquals(intRef.first(), intValue.first())
                Assertions.assertEquals(realRef.first(), realValue.first())
                Assertions.assertEquals(boolRef.first(), boolValue.first())
                Assertions.assertEquals(strRef.first(), strValue.first())

                Assertions.assertTrue(slave.terminate())

            }

        }
    }

    @Test
    fun testGetAndSetAll() {
        FmuBuilder.main(
            arrayOf(
                "-m", "$group.Identity",
                "-f", jar,
                "-d", dest
            )
        )

        val fmuFile = File(dest, "Identity.fmu")
        Assertions.assertTrue(fmuFile.exists())

        val vrs = longArrayOf(0)

        val intValue = intArrayOf(99)
        val realValue = doubleArrayOf(12.3)
        val boolValue = booleanArrayOf(true)
        val strValue = stringArrayOf("Hello identity")

        val intRef = IntArray(1)
        val realRef = DoubleArray(1)
        val boolRef = BooleanArray(1)
        val strRef = StringArray(1)

        Fmu.from(fmuFile).asCoSimulationFmu().use { fmu ->

            fmu.newInstance().use { slave ->

                Assertions.assertTrue(slave.simpleSetup())

                slave.writeAll(
                    vrs, intValue,
                    vrs, realValue,
                    vrs, boolValue,
                    vrs, strValue
                )

                Assertions.assertEquals(true, slave.readBoolean("setAllInvoked").value)

                slave.readAll(
                    vrs, intRef,
                    vrs, realRef,
                    vrs, boolRef,
                    vrs, strRef
                )
                Assertions.assertEquals(intRef.first(), intValue.first())
                Assertions.assertEquals(realRef.first(), realValue.first())
                Assertions.assertEquals(boolRef.first(), boolValue.first())
                Assertions.assertEquals(strRef.first(), strValue.first())

                Assertions.assertEquals(true, slave.readBoolean("getAllInvoked").value)

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
