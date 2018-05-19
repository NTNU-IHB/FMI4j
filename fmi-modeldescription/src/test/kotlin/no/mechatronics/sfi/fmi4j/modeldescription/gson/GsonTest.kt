package no.mechatronics.sfi.fmi4j.modeldescription.gson


import com.google.gson.GsonBuilder
import no.mechatronics.sfi.fmi4j.modeldescription.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class GsonTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(Test::class.java)
    }

    @Test
    fun test() {

        val fmu = File(TEST_FMUs, "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu")
        Assertions.assertTrue(fmu.exists())
        val md = ModelDescriptionParser.parse(fmu).asModelExchangeModelDescription()

        GsonBuilder()
                .setPrettyPrinting()
                .create().also { gson ->

                    val json = gson.toJson(md)
                    LOG.info("$json")

                    @Suppress("NAME_SHADOWING")
                    val md = gson.fromJson(json, ModelExchangeModelDescriptionImpl::class.java)
                    LOG.info("${md.modelVariables}")

                }

    }

}