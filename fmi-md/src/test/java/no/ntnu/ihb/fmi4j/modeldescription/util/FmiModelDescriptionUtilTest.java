package no.ntnu.ihb.fmi4j.modeldescription.util;

import no.ntnu.ihb.fmi4j.TestFMUs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class FmiModelDescriptionUtilTest {

    @Test
    void extractFmiVersionFromFile() throws IOException {
        {
            File fmu = TestFMUs.get("1.0/cs/VanDerPol.fmu");
            Assertions.assertTrue(fmu.exists(), "No such file: " + fmu.getAbsolutePath());
            Assertions.assertEquals("1.0", FmiModelDescriptionUtil.extractVersion(fmu));
        }
        {
            File fmu = TestFMUs.get("2.0/cs/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu");
            Assertions.assertTrue(fmu.exists(), "No such file: " + fmu.getAbsolutePath());
            Assertions.assertEquals("2.0", FmiModelDescriptionUtil.extractVersion(fmu));
        }
    }

    @Test
    void extractFmiGuidFromFile() throws IOException {
        {
            File fmu = TestFMUs.get("1.0/cs/VanDerPol.fmu");
            Assertions.assertTrue(fmu.exists(), "No such file: " + fmu.getAbsolutePath());
            Assertions.assertEquals("{8c4e810f-3da3-4a00-8276-176fa3c9f000}", FmiModelDescriptionUtil.extractGuid(fmu));
        }
        {
            File fmu = TestFMUs.get("2.0/cs/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu");
            Assertions.assertTrue(fmu.exists(), "No such file: " + fmu.getAbsolutePath());
            Assertions.assertEquals("{06c2700b-b39c-4895-9151-304ddde28443}", FmiModelDescriptionUtil.extractGuid(fmu));
        }
    }
}
