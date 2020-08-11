package no.ntnu.ihb.fmi4j.modeldescription;

import no.ntnu.ihb.fmi4j.TestFMUs;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.JaxbModelDescriptionParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.File;

public class ModelDescriptionParseTest {

    private final ModelDescriptionParser parser = new JaxbModelDescriptionParser();

    @Test
    public void test1() {

        File fmuFile = TestFMUs.get("2.0/cs/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu");

        Assertions.assertTrue(fmuFile.exists());
        Assertions.assertNotNull(parser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(parser.parse(xml).asCoSimulationModelDescription());

    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void test2() {

        File fmuFile = TestFMUs.get("2.0/cs/JModelica.org/1.15/PID_Controller/PID_Controller.fmu");

        Assertions.assertTrue(fmuFile.exists());
        Assertions.assertNotNull(parser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(parser.parse(xml).asCoSimulationModelDescription());

    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void test3() {

        File fmuFile = TestFMUs.get("2.0/cs/EDALab_HIFSuite/2017.05_antlia/uart/uart.fmu");

        Assertions.assertTrue(fmuFile.exists());
        Assertions.assertNotNull(parser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(parser.parse(xml).asCoSimulationModelDescription());

    }

    @Test
    public void test4() {

        File fmuFile = TestFMUs.get("2.0/cs/AMESim/15/fuelrail_cs/fuelrail_cs.fmu");

        Assertions.assertTrue(fmuFile.exists());
        Assertions.assertNotNull(parser.parse(fmuFile).asCoSimulationModelDescription());

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        Assertions.assertNotNull(parser.parse(xml).asCoSimulationModelDescription());

    }

}
