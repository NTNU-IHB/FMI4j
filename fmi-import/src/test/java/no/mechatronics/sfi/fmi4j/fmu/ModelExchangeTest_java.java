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

package no.mechatronics.sfi.fmi4j.fmu;

import no.mechatronics.sfi.fmi4j.common.FmiStatus;
import no.mechatronics.sfi.fmi4j.common.FmuRead;
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.apache.commons.math3.ode.nonstiff.LutherIntegrator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Lars Ivar Hatledal
 */
public class ModelExchangeTest_java {

    private static final Logger LOG = LoggerFactory.getLogger(ModelExchangeTest_java.class);

    private static Fmu fmu;


    @BeforeClass
    public static void setup() throws IOException {
        final File file = new File(TEST_FMUsKt.getTEST_FMUs(), "FMI_2.0/ModelExchange/win64/FMUSDK/2.0.4/vanDerPol/vanDerPol.fmu");
        Assert.assertTrue(file.exists());
        fmu = Fmu.from(file);
    }

    @AfterClass
    public static void tearDown() {
        fmu.close();
    }

    @Test
    public void testVersion() {
        Assert.assertEquals("2.0", fmu.getModelDescription().getFmiVersion());
    }

    private void runFmu(FirstOrderIntegrator integrator) {

        LOG.info("Using solver: {}", integrator.getClass().getSimpleName());

        FmiSimulation instance = ModelExchangeTest_java.fmu.asModelExchangeFmu()
                .newInstance(integrator, false, true);

        RealVariable x0 = instance.getModelVariables()
                .getByName("x0").asRealVariable();

        instance.init();

        double macroStep = 1.0 / 10;
        while (instance.getCurrentTime() < 1) {
            FmuRead<Double> read = x0.read();
            Assert.assertSame(read.getStatus(), FmiStatus.OK);
            LOG.info("t={}, x0={}", instance.getCurrentTime(), read.getValue() );
            instance.doStep(macroStep);
        }

        instance.terminate();
    }

    @Test
    public void testEuler() {
        runFmu(new EulerIntegrator(1E-3));
    }

    @Test
    public void testRungeKutta() {
        runFmu(new ClassicalRungeKuttaIntegrator(1E-3));
    }

    @Test
    public void testLuther() {
        runFmu(new LutherIntegrator(1E-3));
    }

}


