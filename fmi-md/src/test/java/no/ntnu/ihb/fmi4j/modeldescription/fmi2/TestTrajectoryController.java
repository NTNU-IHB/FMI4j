package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import no.ntnu.ihb.fmi4j.modeldescription.fmi1.TestVesselFmu;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TestTrajectoryController {

    @Test
    public void test() throws IOException {
        File xml = new File(TestVesselFmu.class.getClassLoader()
                .getResource("fmi2.0/TrajectoryController/modelDescription.xml").getFile());

        XmlMapper mapper = new XmlMapper();
        Fmi2ModelDescription md = mapper.readValue(xml, Fmi2ModelDescription.class);

    }

}
