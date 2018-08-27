package no.mechatronics.sfi.fmu2jar.codegen

import no.mechatronics.sfi.fmi4j.modeldescription.parser.ModelDescriptionParser
import no.mechatronics.sfi.fmu2jar.util.TEST_FMUs
import no.mechatronics.sfi.fmu2jar.util.currentOS
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class CodeGenerationTest {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(CodeGenerationTest::class.java)
    }

    @Test
    fun generateBody() {

        val file = File(TEST_FMUs,
                "FMI_2.0/CoSimulation/$currentOS/20sim/4.6.4.8004/" +
                        "ControlledTemperature/ControlledTemperature.fmu")
        Assertions.assertTrue(file.exists())
        val md = ModelDescriptionParser.parse(file)
        LOG.info( CodeGenerator(md).generateBody() )

    }

}