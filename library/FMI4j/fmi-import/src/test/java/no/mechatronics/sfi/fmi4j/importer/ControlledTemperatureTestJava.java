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

package no.mechatronics.sfi.fmi4j.importer;


import no.mechatronics.sfi.fmi4j.TestUtils;
import no.mechatronics.sfi.fmi4j.common.FmiStatus;
import no.mechatronics.sfi.fmi4j.common.FmuSlave;
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Lars Ivar Hatledal
 */

@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
public class ControlledTemperatureTestJava {

    private final static Logger LOG = LoggerFactory.getLogger(ControlledTemperatureTestJava.class);

    private static Fmu fmuFile;

    @BeforeAll
    public static void setUp() throws IOException {

        final File file = new File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/" + TestUtils.getOs()
                        + "/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu");
        Assertions.assertTrue(file.exists());
        fmuFile = Fmu.from(file);
    }

    @AfterAll
    public static void tearDown() {
        fmuFile.close();
    }

    @Test
    public void test() {

        try (FmuSlave instance = fmuFile.asCoSimulationFmu().newInstance()) {

            Assertions.assertEquals("2.0", instance.getModelDescription().getFmiVersion());

            final double startTemp = instance.getVariableByName("HeatCapacity1.T0")
                    .asRealVariable().getStart();
            Assertions.assertEquals(298.0, startTemp);

            instance.init();
            Assertions.assertSame(instance.getLastStatus(), FmiStatus.OK);

            final RealVariable heatCapacity1_C
                    = instance.getVariableByName("HeatCapacity1.C").asRealVariable();
            Assertions.assertEquals(0.1, (double) heatCapacity1_C.getStart());
            LOG.info("heatCapacity1_C={}", heatCapacity1_C.read().getValue());

            final RealVariable temperature_room
                    = instance.getVariableByName("Temperature_Room").asRealVariable();

            double dt = 1d / 100;
            for (int i = 0; i < 5; i++) {
                instance.doStep(dt);
                Assertions.assertSame(instance.getLastStatus(), FmiStatus.OK);
                double value = temperature_room.read().getValue();
                Assertions.assertSame(instance.getLastStatus(), FmiStatus.OK);

                LOG.info("temperature_room={}", value);

                Assertions.assertEquals(value, (double) instance.getVariableAccessor()
                        .readReal("Temperature_Room").getValue());

            }

        }

    }

}
