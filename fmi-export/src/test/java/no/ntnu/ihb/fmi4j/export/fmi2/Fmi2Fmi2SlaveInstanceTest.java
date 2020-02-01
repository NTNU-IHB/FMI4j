package no.ntnu.ihb.fmi4j.export.fmi2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Fmi2Fmi2SlaveInstanceTest {

    private static JavaTestingFmi2Slave slave;

    @BeforeAll
    static void setUp() {
        slave = new JavaTestingFmi2Slave("");
        slave.__define__();
    }

    @Test
    void testReal() {
        long vr = slave.getValueReference("realIn");
        Assertions.assertEquals(slave.realIn, slave.getReal(new long[]{vr}[0]));
        double newValue = 10;
        slave.setReal(new long[]{vr}, new double[]{newValue});
        Assertions.assertEquals(newValue, slave.getReal(new long[]{vr}[0]));
    }

    @Test
    void testVector() {
        double x = 1;
        double y = 5;
        double z = 99;

        long startIndex = slave.getValueReference("vector3[0]");
        long[] vr = new long[]{startIndex, startIndex + 1, startIndex + 2};
        slave.setReal(vr, new double[]{x, y, z});

        double[] result = slave.getReal(vr);

        Assertions.assertArrayEquals(new double[]{x, y, z}, result);
    }

    @Test
    void testContainer() {

        long vr = slave.getValueReference("container.speed1");

        double[] write = new double[]{123.0};
        slave.setReal(new long[]{vr}, write);

        Assertions.assertArrayEquals(write, slave.getReal(new long[]{vr}));

    }

}
