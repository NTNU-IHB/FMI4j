package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

import no.ntnu.ihb.fmi4j.modeldescription.fmi1.TestVesselFmu;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;

public class TestTrajectoryController {

    @Test
    public void test() throws IOException {
        File xmlFile = new File(TestVesselFmu.class.getClassLoader()
                .getResource("fmi2.0/TrajectoryController/modelDescription.xml").getFile());

        Fmi2ModelDescription md = JAXB.unmarshal(xmlFile, Fmi2ModelDescription.class);
    }

}
