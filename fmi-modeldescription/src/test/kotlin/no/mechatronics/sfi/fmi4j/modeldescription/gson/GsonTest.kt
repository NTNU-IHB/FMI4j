package no.mechatronics.sfi.fmi4j.modeldescription.gson

import com.google.gson.GsonBuilder
import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.modeldescription.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class GsonTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(Test::class.java)
    }

    @Test
    fun test() {

        val fmu = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/ModelExchange/${TestUtils.getOs()}/MapleSim/2017/ControlledTemperature/ControlledTemperature.fmu")
        Assertions.assertTrue(fmu.exists())
        val modelDescription = ModelDescriptionParser.parse(fmu).asModelExchangeModelDescription()

        GsonBuilder()
                .setPrettyPrinting()
                .create().also { gson ->

                    val json = gson.toJson(modelDescription)
                    LOG.info("$json")

                    val md = gson.fromJson(json, ModelExchangeModelDescriptionImpl::class.java)
                    LOG.info("${md.modelVariables}")

                }

    }

}