package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestFmiModeldescription {

    private static Fmi2ModelDescription md;

    @BeforeAll
    static void setup() throws IOException {
        md = new XmlMapper().readValue(TestFmiModeldescription.class.getClassLoader()
                .getResource("fmi2/ControlledTemperature/modelDescription.xml"), Fmi2ModelDescription.class);
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
