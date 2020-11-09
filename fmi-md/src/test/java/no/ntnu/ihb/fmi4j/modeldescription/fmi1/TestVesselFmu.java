package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TestVesselFmu {

    @Test
    public void testVesselFmu() throws IOException {

        File xml = new File(TestVesselFmu.class.getClassLoader()
                .getResource("fmi1.0/VesselFmu/modelDescription.xml").getFile());

        XmlMapper mapper = new XmlMapper();
        FmiModelDescription md = mapper.readValue(xml, FmiModelDescription.class);

        Assertions.assertNotNull(md.getImplementation().getCoSimulationStandAlone().capabilities);

    }

}
