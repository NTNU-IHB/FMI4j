package no.mechatronics.sfi.fmi4j.modeldescription;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ModelDescriptionParseTest_java {

    @Test
    public void test() {

        File fmu = new File(TEST_FMUsKt.getTEST_FMUs(), "FMI_2.0/CoSimulation/win64/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu");
        Assertions.assertTrue(fmu.exists());
        ModelDescriptionParser.parse(fmu).asCoSimulationModelDescription();

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmu);
        ModelDescriptionParser.parse(xml).asCoSimulationModelDescription();

    }

}
