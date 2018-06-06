package no.mechatronics.sfi.fmu2jar

//import no.mechatronics.sfi.fmi4j.common.FmuRead
//import no.mechatronics.sfi.fmi4j.common.Real
//import no.mechatronics.sfi.fmu2jar.controlledtemperature.ControlledTemperature
//import org.junit.jupiter.api.Test
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//
//class ControlledTemperatureTest_kt {
//
//    companion object {
//        val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest_kt::class.java)
//    }
//
//    @Test
//    fun test() {
//
//        ControlledTemperature.newInstance().use { ct ->
//
//            ct.init()
//            val temp: FmuRead<Real> = ct.outputs.getTemperature_Reference()
//            LOG.info("Temperature_reference=$temp")
//
//        }
//
//    }
//
//}