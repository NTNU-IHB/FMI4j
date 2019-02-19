package no.ntnu.ihb.fmi4j.importer.misc

import no.ntnu.ihb.fmi4j.importer.TestFMUs
import no.ntnu.ihb.fmi4j.importer.Fmu
import org.javafmi.proxy.Status
import org.javafmi.wrapper.Simulation
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompareWithJavaFmi {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(CompareWithJavaFmi::class.java)

        private val file = TestFMUs.fmi20().cs()
                .vendor("20sim").version("4.6.4.8004")
                .name("ControlledTemperature").file()

        private val fmu = Fmu.from(file).asCoSimulationFmu()

    }

    @AfterAll
    fun tearDown() {
        fmu.close()
        Simulation(file.absolutePath).apply {
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

        Simulation(file.absolutePath).apply {

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

        fmu.newInstance().also { slave ->

            val start = Instant.now()
            slave.simpleSetup()
            while (slave.simulationTime < stop) {
                val status = slave.doStep(stepSize)
                slave.modelVariables.forEach {
                    it.read(slave)
                }
                Assertions.assertTrue(status)
            }
            slave.terminate()
            val end = Instant.now()
            duration2 = Duration.between(start, end).toMillis()
        }
        return duration1 to duration2
    }


}