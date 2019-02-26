/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package no.ntnu.ihb.fmi4j.importer;

import no.ntnu.ihb.fmi4j.common.Fmi4jVariableUtils;
import no.ntnu.ihb.fmi4j.common.FmiStatus;
import no.ntnu.ihb.fmi4j.common.FmuSlave;
import no.ntnu.ihb.fmi4j.modeldescription.variables.RealVariable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Lars Ivar Hatledal
 */

public class ControlledTemperatureTestJava {

    private final static Logger LOG = LoggerFactory.getLogger(ControlledTemperatureTestJava.class);

    private static CoSimulationFmu fmu;

    @BeforeAll
    public static void setUp() throws IOException {
        fmu = TestFMUs.fmi20().cs()
                .vendor("20sim").version("4.6.4.8004")
                .name("ControlledTemperature").fmu().asCoSimulationFmu();
    }

    @AfterAll
    public static void tearDown() {
        fmu.close();
    }

    @Test
    public void test() {

        try (FmuSlave slave = fmu.newInstance()) {

            Assertions.assertEquals("2.0", slave.getModelDescription().getFmiVersion());

            final double startTemp = slave.getModelDescription()
                    .getVariableByName("HeatCapacity1.T0").asRealVariable().getStart();
            Assertions.assertEquals(298.0, startTemp);

            slave.simpleSetup();

            final RealVariable heatCapacity1_C = slave.getModelDescription()
                    .getVariableByName("HeatCapacity1.C").asRealVariable();
            Assertions.assertEquals(0.1, (double) heatCapacity1_C.getStart());
            LOG.info("heatCapacity1_C={}", heatCapacity1_C.read(slave).getValue());

            final RealVariable temperature_room = slave.getModelDescription()
                    .getVariableByName("Temperature_Room").asRealVariable();

            double dt = 1d / 100;
            for (int i = 0; i < 5; i++) {
                Assertions.assertTrue(slave.doStep(dt));
                Assertions.assertEquals(slave.getLastStatus(), FmiStatus.OK);
                double value = temperature_room.read(slave).getValue();
                Assertions.assertEquals(slave.getLastStatus(), FmiStatus.OK);

                LOG.info("temperature_room={}", value);

                Assertions.assertEquals(value, (double) Fmi4jVariableUtils
                        .readReal(slave, "Temperature_Room").getValue());

            }

        }

    }

}
