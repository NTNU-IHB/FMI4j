package no.mechatronics.sfi.fmi4j.modeldescription;

import no.mechatronics.sfi.fmi4j.TestFMUs;
import no.mechatronics.sfi.fmi4j.modeldescription.parser.ModelDescriptionParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.File;

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
public class ModelDescriptionParseTest_java {

    @Test
    public void test1() {

        File fmuFile = TestFMUs.fmi20().cs()
                .vendor("20sim").version("4.6.4.8004")
                .file("ControlledTemperature");

        Assertions.assertTrue(fmuFile.exists());

        Assertions.assertNotNull(ModelDescriptionParser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(ModelDescriptionParser.parse(xml).asCoSimulationModelDescription());

    }

    @Test
    public void test2() {


        File fmuFile = TestFMUs.fmi20().cs()
                .vendor("JModelica.org").version("1.15")
                .file("PID_Controller");

        Assertions.assertTrue(fmuFile.exists());

        Assertions.assertNotNull(ModelDescriptionParser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(ModelDescriptionParser.parse(xml).asCoSimulationModelDescription());

    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void test3() {

        File fmuFile = TestFMUs.fmi20().cs()
                .vendor("EDALab_HIFSuite").version("2017.05_antlia")
                .file("uart");

        Assertions.assertTrue(fmuFile.exists());

        Assertions.assertNotNull(ModelDescriptionParser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(ModelDescriptionParser.parse(xml).asCoSimulationModelDescription());

    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void test4() {

        File fmuFile = TestFMUs.fmi20().cs()
                .vendor("AMESim").version("15")
                .file("fuelrail_cs");

        Assertions.assertTrue(fmuFile.exists());

        Assertions.assertNotNull(ModelDescriptionParser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(ModelDescriptionParser.parse(xml).asCoSimulationModelDescription());

    }

}
