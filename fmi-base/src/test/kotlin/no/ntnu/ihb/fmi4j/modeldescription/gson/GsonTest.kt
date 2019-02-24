package no.ntnu.ihb.fmi4j.modeldescription.gson

import no.ntnu.ihb.fmi4j.modeldescription.TestFMUs
import no.ntnu.ihb.fmi4j.xml.ModelDescriptionImpl
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

        val json = (modelDescription as ModelDescriptionImpl).toJson().also {
            LOG.info(it)
        }

        ModelDescriptionImpl.fromJson(json).also {
            LOG.info(it.toString())
        }

    }

}