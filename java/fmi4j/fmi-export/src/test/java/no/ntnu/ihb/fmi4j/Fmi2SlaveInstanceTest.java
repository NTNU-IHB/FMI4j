package no.ntnu.ihb.fmi4j;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Fmi2SlaveInstanceTest {

    private static MyTestSlave slave;

    @BeforeAll
    static void setUp() {
        slave = new MyTestSlave();
        slave.getModelDescription();
    }

    @Test
    void testReal() {
        Assertions.assertEquals(slave.realOut, slave.getReal(new long[]{0})[0]);
        double newValue = 10;
        slave.setReal(new long[]{0}, new double[]{newValue});
        Assertions.assertEquals(newValue, slave.getReal(new long[]{0})[0]);
    }

}
