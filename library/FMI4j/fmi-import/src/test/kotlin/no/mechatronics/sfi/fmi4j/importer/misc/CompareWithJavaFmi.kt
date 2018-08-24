package no.mechatronics.sfi.fmi4j.importer.misc


import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.currentOS
import no.mechatronics.sfi.fmi4j.importer.Fmu
import org.javafmi.proxy.Status
import org.javafmi.wrapper.Simulation
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Duration
import java.time.Instant

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class CompareWithJavaFmi {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(CompareWithJavaFmi::class.java)
    }

    private val path = "${TestUtils.getTEST_FMUs()}/FMI_2.0/CoSimulation/$currentOS" +
            "/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"

    private val fmu = Fmu.from(File(path))

    @AfterAll
    fun tearDown() {
        fmu.close()
        Simulation(path).apply {
            fmuFile.deleteTemporalFolder()
        }
    }

    @Test
    fun test() {

        val stop = 10.0
        val stepSize = 1.0 / 100

        var duration1 = 0L
        var duration2 = 0L

        doTest(stepSize, 1.0)

        for (i in 0 until 5) {
            doTest(stepSize, stop).also {
                duration1 += it.first
                duration2 += it.second
            }
        }

        LOG.info("JavaFMI duration=$duration1, FMI4j duration=$duration2")

    }


    private fun doTest(stepSize: Double, stop: Double): Pair<Long, Long> {

        var duration1: Long = 0
        var duration2: Long = 0

        Simulation(path).apply {

            val start = Instant.now()
            init(0.0)
            while (currentTime < stop) {
                val status = doStep(stepSize)
                modelDescription.modelVariables.forEach {
                    read(it.name).asDouble()
                }
                Assertions.assertTrue(status == Status.OK)
            }
            terminate()
            val end = Instant.now()
            duration1 = Duration.between(start, end).toMillis()
        }

        fmu.asCoSimulationFmu().newInstance().apply {

            val start = Instant.now()
            init(0.0)
            while (simulationTime < stop) {
                val status = doStep(stepSize)
                modelVariables.forEach {
                    it.read()
                }
                Assertions.assertTrue(status)
            }
            terminate()
            val end = Instant.now()
            duration2 = Duration.between(start, end).toMillis()
        }
        return duration1 to duration2
    }


}