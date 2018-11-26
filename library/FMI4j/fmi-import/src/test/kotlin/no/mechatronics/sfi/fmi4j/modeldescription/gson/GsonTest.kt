package no.mechatronics.sfi.fmi4j.modeldescription.gson

import com.google.gson.GsonBuilder
import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.common.currentOS
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionImpl
import no.mechatronics.sfi.fmi4j.modeldescription.parser.ModelDescriptionParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class GsonTest {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(Test::class.java)
    }

    @Test
    fun test() {

        val fmu = File(TestUtils.getTEST_FMUs(),
                "2.0/me/$currentOS" +
                        "/MapleSim/2017/ControlledTemperature/ControlledTemperature.fmu")
        Assertions.assertTrue(fmu.exists())
        val modelDescription = ModelDescriptionParser.parse(fmu)

        GsonBuilder()
                .setPrettyPrinting()
                .create().also { gson ->

                    gson.toJson(modelDescription).also { json ->
                        LOG.info("$json")
                        gson.fromJson(json, ModelDescriptionImpl::class.java).also { md ->
                            LOG.info("${md.modelVariables}")
                        }

                    }

                }

    }

}