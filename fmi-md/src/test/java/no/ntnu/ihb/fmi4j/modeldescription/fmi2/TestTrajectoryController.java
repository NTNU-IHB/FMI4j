package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TestTrajectoryController {

    @Test
    public void test() throws IOException {

        String file = TestTrajectoryController.class.getClassLoader()
                .getResource("fmi2.0/TrajectoryController/modelDescription.xml").getFile();
        File xmlFile = new File(file.replace("%20", " "));

        Fmi2ModelDescription md = Fmi2ModelDescription.fromXml(xmlFile);
        Assertions.assertEquals(md.modelName, "TrajectoryController");
    }

}
