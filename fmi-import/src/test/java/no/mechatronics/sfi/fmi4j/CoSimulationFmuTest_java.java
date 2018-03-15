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

package no.mechatronics.sfi.fmi4j;


import no.mechatronics.sfi.fmi4j.common.FmiStatus;
import no.mechatronics.sfi.fmi4j.fmu.AbstractFmu;
import no.mechatronics.sfi.fmi4j.fmu.CoSimulationFmu;
import no.mechatronics.sfi.fmi4j.fmu.FmuFile;
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Lars Ivar Hatledal
 */
public class CoSimulationFmuTest_java {

    private static FmuFile fmuFile;

    @BeforeClass
    public static void setUp() throws IOException {

        String path = "../test/fmi2/cs/win64/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu";
        final File file = new File(path);
        Assert.assertNotNull(file);
        fmuFile = FmuFile.from(file);

    }

    @AfterClass
    public static void tearDown() throws IOException {
        fmuFile.close();
    }

    @Test
    public void test() throws Exception {

        try(FmiSimulation fmu = fmuFile.asCoSimulationFmu().newInstance()) {

            Assert.assertEquals("2.0", fmu.getModelDescription().getFmiVersion());

            final double startTemp = fmu.getVariableByName("HeatCapacity1.T0").asRealVariable().getStart();
            Assert.assertNotNull(startTemp);
            Assert.assertEquals(298.0, startTemp,0);

            Assert.assertTrue(fmu.init());
            Assert.assertTrue(fmu.getLastStatus() == FmiStatus.OK);

            final RealVariable heatCapacity1_C = fmu.getVariableByName("HeatCapacity1.C").asRealVariable();
            Assert.assertEquals(0.1, heatCapacity1_C.getStart(), 0);
            System.out.println(heatCapacity1_C.read().getValue());

            final RealVariable temperature_room = fmu.getVariableByName("Temperature_Room").asRealVariable();

            double first1 = Double.NaN;

            double dt = 1d/100;
            for (int i = 0; i < 5; i++) {
                fmu.doStep(dt);
                Assert.assertTrue(fmu.getLastStatus() == FmiStatus.OK);
                double value = temperature_room.read().getValue();
                Assert.assertTrue(fmu.getLastStatus() == FmiStatus.OK);
                if (Double.isNaN(first1)) {
                    first1 = value;
                }
                System.out.println(value);

                Assert.assertEquals(value, fmu.getVariableAccessor().readReal("Temperature_Room").getValue(), 0);

            }

            ((AbstractFmu) fmu).reset(false);

            Assert.assertTrue(fmu.getLastStatus() == FmiStatus.OK);

            AtomicBoolean first = new AtomicBoolean(true);
            while (fmu.getCurrentTime() < 5) {
                fmu.doStep(dt);
                Assert.assertTrue(fmu.getLastStatus() == FmiStatus.OK);
                double value = temperature_room.read().getValue();
                Assert.assertTrue(fmu.getLastStatus() == FmiStatus.OK);
                if (first.getAndSet(false)) {
                    Assert.assertEquals(first1, value, 0);
                }
                System.out.println(value);

            }

            try (FmiSimulation fmu2 = fmuFile.asCoSimulationFmu().newInstance()) {
                if (fmu2.init()) {
                    System.out.println(fmu2.getVariableAccessor().readReal(temperature_room.getValueReference()));
                }
            }

        }

    }
}
