package no.mechatronics.sfi.fmu2jar.templates


import org.junit.Assert
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser

class CodeGenerationTest {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(CodeGenerationTest::class.java)
    }

    @Test
    fun generateBody() {

        val file = File(System.getenv("TEST_FMUs"), "FMI_2.0/CoSimulation/win64/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")
        Assert.assertTrue(file.exists())
        val md = ModelDescriptionParser.parse(file)
        LOG.info(CodeGeneration.generateWrapper(md) )

    }

}