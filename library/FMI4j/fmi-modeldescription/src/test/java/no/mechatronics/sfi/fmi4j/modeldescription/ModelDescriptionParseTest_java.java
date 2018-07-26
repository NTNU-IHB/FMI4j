package no.mechatronics.sfi.fmi4j.modeldescription;


import no.mechatronics.sfi.fmi4j.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.File;

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
public class ModelDescriptionParseTest_java {

    @Test
    public void test() {

        File fmu = new File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/" + TestUtils.getOs() + "/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu");
        Assertions.assertTrue(fmu.exists());
        ModelDescriptionParser.parse(fmu).asCoSimulationModelDescription();

        String xml = ModelDescriptionParser.extractModelDescriptionXml(fmu);
        ModelDescriptionParser.parse(xml).asCoSimulationModelDescription();

    }

}
