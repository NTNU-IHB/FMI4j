package no.mechatronics.sfi.fmi4j.importer.cs.vendors.fmusdk

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.importer.Fmu
import no.mechatronics.sfi.fmi4j.importer.me.vendors.fmusdk.VanDerPolTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.LoggerFactory
import java.io.File

@EnabledOnOs(OS.WINDOWS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class VanDerPolTest {

    private companion object {
        private val LOG = LoggerFactory.getLogger(VanDerPolTest::class.java)
    }

    private val fmu = Fmu.from(File(TestUtils.getTEST_FMUs(),
            "FMI_2.0/CoSimulation/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu"))

    @AfterAll
    fun tearDown() {
        fmu.close()
    }

    @Test
    fun testVersion() {
        Assertions.assertEquals("2.0", fmu.modelDescription.fmiVersion)
    }

    @Test
    fun testInstance() {

        fmu.asCoSimulationFmu().newInstance().use { instance ->

            instance.init()

            val stepSize = 1E-3
            while (instance.currentTime < 5.0) {
                val status = instance.doStep(stepSize)
                Assertions.assertTrue(status)
            }

        }

    }

}