package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class TestBuilder {

    companion object {
        val group = "no.ntnu.ihb.fmi4j"
        val dest = File("build/generated").absolutePath
        val jar = File("../fmu-slaves/build/libs/fmu-slaves.jar").absolutePath
    }

    @Test
    fun testJavaClass() {

        val modifiedClasspath = StringBuilder()
                .append("C:\\Users\\LarsIvar\\Documents\\IdeaProjects\\FMI4j\\java\\fmi4j\\fmu-slaves\\build\\libs\\fmu-slaves.jar;")
                .append(System.getProperty("java.class.path"))

        FmuBuilder.main(arrayOf("-f", jar, "-m", "$group.JavaTestSlave", "-d", dest))
//        testFmu(File(dest, "Test.fmu"))
    }

    @Test
    fun testKotlinClass() {
        FmuBuilder.main(arrayOf("-f", jar, "-m", "$group.KotlinTestSlave", "-d", dest))
//        testFmu(File(dest, "KotlinTestSlave.fmu"))
    }

    private fun testFmu(fmuFile: File) {
        Assertions.assertTrue(fmuFile.exists())

        Fmu.from(fmuFile).use { fmu ->
            fmu.asCoSimulationFmu().newInstance().use { slave ->

                slave.doStep(0.1)

            }
        }

    }

}
