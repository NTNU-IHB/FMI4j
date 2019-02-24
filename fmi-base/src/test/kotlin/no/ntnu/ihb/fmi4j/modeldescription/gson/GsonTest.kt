package no.ntnu.ihb.fmi4j.modeldescription.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import no.ntnu.ihb.fmi.fmi2.xml.Fmi2ModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionImpl
import no.ntnu.ihb.fmi4j.modeldescription.TestFMUs
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.xml.datatype.XMLGregorianCalendar

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