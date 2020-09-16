package no.ntnu.ihb.fmi4j.export.fmi2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class Fmi2Fmi2SlaveInstanceTest {

    private static JavaTestingFmi2Slave slave;

    @BeforeAll
    static void setUp() {
        Map<String, Object> args = new HashMap<String, Object>(){{
            put("instanceName", "instance");
        }};
        slave = new JavaTestingFmi2Slave(args);
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
        long startIndex = slave.getValueReference("vector3[0]");
        long[] vr = new long[]{startIndex, startIndex + 1, startIndex + 2};

        double[] write = {1, 5, 99};
        slave.setReal(vr, write);
        Assertions.assertArrayEquals(write, slave.getReal(vr));
    }

    @Test
    void testContainer() {
        long vr = slave.getValueReference("container.speed");
        double[] write = new double[]{123.0};
        slave.setReal(new long[]{vr}, write);
        Assertions.assertEquals(write[0], slave.getReal(vr));
    }

    @Test
    void testMethodVariable1() {
        long vr = slave.getValueReference("someParameter");
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(30, slave.getInteger(vr));
        }
    }

    @Test
    void testMethodVariable2() {
        long vr = slave.getValueReference("aParameter");
        Assertions.assertEquals(123, slave.getReal(vr));
        double[] write = new double[]{-99};
        slave.setReal(new long[]{vr}, write);
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(write[0], slave.getReal(vr));
        }
    }

    @Test
    void testMethodVariable3() {
        long startIndex = slave.getValueReference("vector3[0]");
        long[] vr = new long[]{startIndex, startIndex + 1, startIndex + 2};
        Assertions.assertArrayEquals(new double[]{1, 2, 3}, slave.getReal(vr));

        double[] write = {1, 5, 99};
        slave.setReal(vr, write);
        Assertions.assertArrayEquals(write, slave.getReal(vr));
    }

}
