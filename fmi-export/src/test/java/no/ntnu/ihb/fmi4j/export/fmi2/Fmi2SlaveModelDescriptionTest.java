package no.ntnu.ihb.fmi4j.export.fmi2;

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ScalarVariable;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ModelDescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

class Fmi2SlaveModelDescriptionTest {

    private static Fmi2ModelDescription md;

    @BeforeAll
    static void setUp() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JAXB.marshal(new JavaTestingSlave("").define().getModelDescription(), bos);
//        System.out.println(new String(bos.toByteArray()));
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
//        Assertions.assertEquals(2.0, (double) var.getReal().getStart());
    }

    @Test
    void testReals() {
        Fmi2ScalarVariable v1 = md.getModelVariables().getScalarVariable()
                .stream().filter(v -> v.getValueReference() == 1).findFirst().get();

        Assertions.assertNotNull(v1);
//        Assertions.assertEquals(50.0, (double) v1.getReal().getStart());

        Fmi2ScalarVariable v2 = md.getModelVariables().getScalarVariable()
                .stream().filter(v -> v.getValueReference() == 2).findFirst().get();

        Assertions.assertNotNull(v2);
//        Assertions.assertEquals(200.0, (double) v2.getReal().getStart());
    }

    @Test
    void testStrings() {
        Fmi2ScalarVariable v1 = md.getModelVariables().getScalarVariable()
                .stream().filter(v -> v.getValueReference() == 3).findFirst().get();

        Assertions.assertNotNull(v1);
//        Assertions.assertEquals("Hello", v1.getString().getStart());

        Fmi2ScalarVariable v2 = md.getModelVariables().getScalarVariable()
                .stream().filter(v -> v.getValueReference() == 4).findFirst().get();

        Assertions.assertNotNull(v2);
//        Assertions.assertEquals("world!", v2.getString().getStart());
    }

}
