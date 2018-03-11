package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.common.FmiStatus
import no.mechatronics.sfi.fmi4j.fmu.FmuFile
import no.mechatronics.sfi.fmi4j.modeldescription.variables.VariableAccessor
import org.junit.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class TestFmuState {

    companion object {

        val LOG: Logger = LoggerFactory.getLogger(TestFmuState::class.java)

    }

    lateinit var fmuFile: FmuFile

    @Before
    fun setup() {
        val path = "../test/fmi2/cs/win64/SimulationX/3.7.41138/ControlledTemperature/ControlledTemperature.fmu"
        val file = File(path)
        Assert.assertNotNull(file)
        fmuFile = FmuFile(file)
    }

//    @Test
//    fun test() {
//
//        fmuFile.asCoSimulationFmu().newInstance(loggingOn = true).use { fmu ->
//
//            Assert.assertTrue(fmu.modelDescription.canGetAndSetFMUstate)
//
//            Assert.assertTrue(fmu.init())
//
//            val tRes = fmu.getVariableByName("TRes").asRealVariable()
//
//            LOG.info("tRes_0=${tRes.read()}")
//
//            val dt = 1.0/100
//            while (fmu.currentTime < 1) {
//                fmu.doStep(dt)
//                Assert.assertTrue(fmu.lastStatus == FmiStatus.OK)
//            }
//
//            val state = fmu.getFMUState()
//            Assert.assertTrue(fmu.lastStatus == FmiStatus.OK)
//
//            LOG.info("tRes_1=${tRes.read()}")
//
//            while (fmu.currentTime <  2) {
//                fmu.doStep(dt)
//                Assert.assertTrue(fmu.lastStatus == FmiStatus.OK)
//            }
//
//            LOG.info("tRes_2=${tRes.read()}")
//
////            val status = fmu.setFMUState(state)
////            Assert.assertEquals(FmiStatus.OK, status)
//
//            LOG.info("tResAfterSet=${tRes.read()}")
//
//        }
//
//    }

}