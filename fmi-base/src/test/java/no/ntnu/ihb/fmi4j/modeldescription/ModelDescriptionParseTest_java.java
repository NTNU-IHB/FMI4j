package no.ntnu.ihb.fmi4j.modeldescription;

import no.ntnu.ihb.fmi4j.modeldescription.parser.ModelDescriptionParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.File;

public class ModelDescriptionParseTest_java {

    @Test
    public void test1() {

        File fmuFile = TestFMUs.fmi20().cs()
                .vendor("20sim").version("4.6.4.8004")
                .name("ControlledTemperature").file();

        Assertions.assertTrue(fmuFile.exists());
        Assertions.assertNotNull(ModelDescriptionParser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(ModelDescriptionParser.parse(xml).asCoSimulationModelDescription());

    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void test2() {

        File fmuFile = TestFMUs.fmi20().cs()
                .vendor("JModelica.org").version("1.15")
                .name("PID_Controller").file();

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
                .name("uart").file();

        Assertions.assertTrue(fmuFile.exists());
        Assertions.assertNotNull(ModelDescriptionParser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(ModelDescriptionParser.parse(xml).asCoSimulationModelDescription());

    }

    @Test
    public void test4() {

        File fmuFile = TestFMUs.fmi20().cs()
                .vendor("AMESim").version("15")
                .name("fuelrail_cs").file();

        Assertions.assertTrue(fmuFile.exists());
        Assertions.assertNotNull(ModelDescriptionParser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(ModelDescriptionParser.parse(xml).asCoSimulationModelDescription());

    }

}
