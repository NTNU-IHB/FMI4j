package no.ntnu.ihb.fmi4j;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Fmi2SlaveInstanceTest {

    private static TestSlave slave;

    @BeforeAll
    static void setUp() {
        slave = new TestSlave();
        slave.define();
    }

    @Test
    void testReal() {
        Assertions.assertEquals(slave.realOut, slave.getReal(new long[]{0})[0]);
        double newValue = 10;
        slave.setReal(new long[]{0}, new double[]{newValue});
        Assertions.assertEquals(newValue, slave.getReal(new long[]{0})[0]);
    }

    @Test
    void testVector() {
        double x = 1;
        double y = 5;
        double z = 99;

        int startIndex = 5;
        long[] vr = new long[]{startIndex, startIndex + 1, startIndex + 2};
        slave.setReal(vr, new double[]{x, y, z});

        double[] result = slave.getReal(vr);

        Assertions.assertArrayEquals(new double[]{x, y, z}, result);
    }

    @Test
    void testContainer() {

        long vr = slave.getModelDescription().getModelVariables().getScalarVariable().stream()
                .filter(fmi2ScalarVariable -> fmi2ScalarVariable.getName().equals("container.speed")).findFirst().get().getValueReference();

        double[] write = new double[]{123.0};
        slave.setReal(new long[]{vr}, write);

        Assertions.assertArrayEquals(write, slave.getReal(new long[]{vr}));

    }

}
