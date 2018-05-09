package no.mechatronics.sfi.fmi4j.fmu.misc


import no.mechatronics.sfi.fmi4j.fmu.Fmu
import no.mechatronics.sfi.fmi4j.fmu.TEST_FMUs
import org.javafmi.proxy.Status
import org.javafmi.wrapper.Simulation
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Duration
import java.time.Instant


class CompareWithJavaFmi {

    companion object {

        val LOG: Logger = LoggerFactory.getLogger(CompareWithJavaFmi::class.java)

        val path: String = "$TEST_FMUs/FMI_2.0/CoSimulation/win64/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu"

        lateinit var fmu: Fmu

        @JvmStatic
        @BeforeClass
        fun setup() {
            fmu = Fmu.from(File(path))
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            fmu.close()
            Simulation(path).apply {
                fmuFile.deleteTemporalFolder()
            }
        }

    }

    private fun doTest(stepSize: Double, stop: Double): Pair<Long, Long> {

        var duration1: Long? = null
        var duration2: Long? = null

        Simulation(path).apply {

            val start = Instant.now()
            init(0.0)
            while (currentTime < stop) {
                val status = doStep(stepSize)
                modelDescription.modelVariables.forEach {
                    read(it.name).asDouble()
                }
                Assert.assertTrue(status == Status.OK)
            }
            terminate()
            val end = Instant.now()
            duration1 = Duration.between(start, end).toMillis()
        }

        fmu.asCoSimulationFmu().newInstance().apply {

            val start = Instant.now()
            init(0.0)
            while (currentTime < stop) {
                val status = doStep(stepSize)
                modelVariables.forEach {
                    it.read()
                }
                Assert.assertTrue(status)
            }
            terminate(true)
            val end = Instant.now()
            duration2 = Duration.between(start, end).toMillis()
        }
        return duration1!! to duration2!!
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

}