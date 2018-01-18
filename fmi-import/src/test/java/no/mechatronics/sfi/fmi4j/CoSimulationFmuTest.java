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


import no.mechatronics.sfi.fmi4j.fmu.AbstractFmu;
import no.mechatronics.sfi.fmi4j.fmu.CoSimulationFmu;
import no.mechatronics.sfi.fmi4j.fmu.FmuBuilder;
import no.mechatronics.sfi.fmi4j.misc.RealReader;
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status;
import no.mechatronics.sfi.fmi4j.modeldescription.RealVariable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class CoSimulationFmuTest {

    private FmuBuilder builder;
    private FmiSimulation fmu;

    @Before
    public void setUp() throws IOException {
        final URL url = CoSimulationFmuTest.class.getClassLoader()
                .getResource("v2/cs/ControlledTemperature/ControlledTemperature.fmu");
        Assert.assertNotNull(url);

        builder =  new FmuBuilder(url);
        fmu = builder.asCoSimulationFmu().newInstance();

    }

    @After
    public void tearDown() {
        if (fmu != null) {
            fmu.terminate();
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
        }
    }

    @org.junit.Test
    public void test() throws Exception {

        final RealVariable startTemp = fmu.getModelVariables().getByName("HeatCapacity1.T0").asRealVariable();
        
        fmu.init();
        Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);

        RealVariable heatCapacity1_C = fmu.getModelVariables().getByName("HeatCapacity1.C").asRealVariable();
        Assert.assertEquals(0.1, heatCapacity1_C.getStart(), 0);

        RealReader read = fmu.getReader("Temperature_Room").asRealReader();

        double first1 = Double.NaN;

        double dt = 1d/100;
        for (int i = 0; i < 5; i++) {
            fmu.doStep(dt);
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
            double value = read.read();
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
            if (Double.isNaN(first1)) {
                first1 = value;
            }
            System.out.println(value);

        }

        ((AbstractFmu) fmu).reset(false);

        Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);

        AtomicBoolean first = new AtomicBoolean(true);
        while (fmu.getCurrentTime() < 5) {
            fmu.doStep(1d / 100);
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
            double value = read.read();
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
            if (first.getAndSet(false)) {
                Assert.assertEquals(first1, value, 0);
            }
            System.out.println(value);

        }

        try (FmiSimulation fmu2 = builder.asCoSimulationFmu().newInstance()) {
            fmu2.init();
            System.out.println(fmu2.getReader("Temperature_Room").asRealReader().read());
        }

    }
}
