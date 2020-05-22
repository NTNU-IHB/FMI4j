package no.ntnu.ihb.fmi4j.export.fmi2;

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ModelDescription;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ScalarVariable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

class Fmi2Fmi2SlaveModelDescriptionTest {

    private static Fmi2ModelDescription md;

    @BeforeAll
    static void setUp() {
        Map<String, Object> args = new HashMap<String, Object>(){{
            put("instanceName", "instance");
        }};
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JavaTestingFmi2Slave slave = new JavaTestingFmi2Slave(args);
        slave.__define__();
        System.out.println(slave.getModelDescriptionXml());
        JAXB.marshal(slave.getModelDescription(), bos);
        md = JAXB.unmarshal(new ByteArrayInputStream(bos.toByteArray()), Fmi2ModelDescription.class);
    }

    @Test
    void testInfo() {
        Assertions.assertEquals("Test", md.getModelName());
        Assertions.assertEquals("Lars Ivar Hatledal", md.getAuthor());
    }

    @Test
    void testReal() {
        Fmi2ScalarVariable var = md.getModelVariables().getScalarVariable()
                .stream().filter(v -> v.getValueReference() == 0).findFirst().get();

        Assertions.assertNotNull(var);
        Assertions.assertEquals(2.0, (double) var.getReal().getStart());
    }

    @Test
    void testReals() {
        Fmi2ScalarVariable v1 = md.getModelVariables().getScalarVariable()
                .stream().filter(v -> v.getValueReference() == 1).findFirst().get();

        Assertions.assertNotNull(v1);
        Assertions.assertEquals(50.0, (double) v1.getReal().getStart());

        Fmi2ScalarVariable v2 = md.getModelVariables().getScalarVariable()
                .stream().filter(v -> v.getValueReference() == 2).findFirst().get();

        Assertions.assertNotNull(v2);
        Assertions.assertEquals(200.0, (double) v2.getReal().getStart());
    }

    @Test
    void testStrings() {
        Fmi2ScalarVariable v1 = md.getModelVariables().getScalarVariable()
                .stream().filter(v -> v.getName().equals("string[0]")).findFirst().get();

        Assertions.assertNotNull(v1);
        Assertions.assertEquals("Hello", v1.getString().getStart());

        Fmi2ScalarVariable v2 = md.getModelVariables().getScalarVariable()
                .stream().filter(v -> v.getName().equals("string[1]")).findFirst().get();

        Assertions.assertNotNull(v2);
        Assertions.assertEquals("world!", v2.getString().getStart());
    }

}
