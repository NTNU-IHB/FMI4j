package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

import no.ntnu.ihb.fmi4j.TestFMUs;
import no.ntnu.ihb.fmi4j.modeldescription.util.FmiModelDescriptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

public class TestControlledTemperature {

    private static Fmi2ModelDescription md;

    @BeforeAll
    static void setup() throws IOException {
        File fmu = TestFMUs.get("2.0/cs/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu");
        String xml = FmiModelDescriptionUtil.extractModelDescriptionXml(fmu);
        md = JAXB.unmarshal(new StringReader(xml), Fmi2ModelDescription.class);
    }

    @Test
    void testCausality() {
        Fmi2ScalarVariable var1 = md.getModelVariables().getScalarVariable().stream().filter(v -> v.valueReference == 0).findFirst().get();
        Assertions.assertEquals(Fmi2Causality.parameter, var1.causality);
    }

    @Test
    void testVariability() {
        Fmi2ScalarVariable var = md.getModelVariables().getScalarVariable().stream().filter(v -> v.valueReference == 18).findFirst().get();
        Assertions.assertEquals(Fmi2Variability.fixed, var.variability);
    }

}
