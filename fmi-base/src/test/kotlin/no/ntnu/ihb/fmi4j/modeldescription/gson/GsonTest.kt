package no.ntnu.ihb.fmi4j.modeldescription.gson

import com.google.gson.GsonBuilder
import no.ntnu.ihb.fmi4j.modeldescription.TestFMUs
import no.ntnu.ihb.fmi4j.modeldescription.jacskon.JacksonModelDescription
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GsonTest {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(Test::class.java)
    }

    @Test
    fun test() {

        val modelDescription = TestFMUs.fmi20().cs()
                .vendor("MapleSim").version("2017")
                .name("ControlledTemperature").modelDescription()

        GsonBuilder()
                .setPrettyPrinting()
                .create().also { gson ->

                    gson.toJson(modelDescription).also { json ->
                        LOG.info("$json")
                        gson.fromJson(json, JacksonModelDescription::class.java).also { md ->
                            LOG.info("${md.modelVariables}")
                        }

                    }

                }

    }

}