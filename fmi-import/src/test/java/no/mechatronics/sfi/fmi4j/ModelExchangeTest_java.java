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

import no.mechatronics.sfi.fmi4j.fmu.FmuBuilder;
import no.mechatronics.sfi.fmi4j.misc.SingleRead;
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*;
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Status;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.net.URL;

/**
 * @author Lars Ivar Hatledal
 */
public class ModelExchangeTest_java {

    private FmiSimulation fmu;

    @Before
    public void setUp() throws IOException {
        final URL url = getClass().getClassLoader().getResource("v2/me/vanDerPol/vanDerPol.fmu");
        Assert.assertNotNull(url);

        FirstOrderIntegrator integrator;
        //integrator= new DormandPrince853Integrator(1E-8, 1.0, 1E-10, 1E-10);
//        integrator = new AdamsBashforthIntegrator(100, 1E-10, 1.0, 1E-10, 1E-10);
        // integrator = new ClassicalRungeKuttaIntegrator(1E-3);
        integrator = new EulerIntegrator(1E-3);

        fmu = new FmuBuilder(url)
                .asModelExchangeFmu()
                .newInstance(integrator);

    }

    @After
    public void tearDown() {
        if (fmu != null) {
            fmu.terminate();
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
        }
    }

    @org.junit.Test
    public void test() {

        Assert.assertEquals("2.0", fmu.getVersion());

        RealVariable x0 = fmu.getModelVariables()
                .getByName("x0").asRealVariable();

        Assert.assertTrue(fmu.init());

        double macroStep = 1.0 / 10;
        while (fmu.getCurrentTime() < 1) {
            System.out.println("t=" + fmu.getCurrentTime() + ", x0=" + x0.getValue());
            fmu.doStep(macroStep);
        }
    }

}


