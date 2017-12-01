package no.mechatronics.sfi.fmi4j;

import no.mechatronics.sfi.fmi4j.fmu.ModelExchangeFmu;
import no.mechatronics.sfi.fmi4j.fmu.ModelExchangeFmuWithIntegrator;
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status;
import no.mechatronics.sfi.fmi4j.modeldescription.types.RealVariable;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.net.URL;

public class ModelExchangeTest {

    ModelExchangeFmuWithIntegrator fmu;

    @Before
    public void setUp() throws IOException {
        final URL url = getClass().getClassLoader().getResource("v2/me/bouncingBall/bouncingBall.fmu");
        Assert.assertNotNull(url);

        FirstOrderIntegrator integrator;
        //integrator= new DormandPrince853Integrator(1E-8, 1.0, 1E-10, 1E-10);
//        integrator = new AdamsBashforthIntegrator(100, 1E-10, 1.0, 1E-10, 1E-10);
        // integrator = new ClassicalRungeKuttaIntegrator(1E-3);
         integrator = new EulerIntegrator(1E-3);

       fmu = new ModelExchangeFmuWithIntegrator(new ModelExchangeFmu(url,false, false), integrator);

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


        RealVariable h = fmu.getModelVariables().getReal("h");
        h.setStart(5.0);

        fmu.init();
        Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);

        double macroStep = 1E-1;

        while (fmu.getCurrentTime() < 5) {

            System.out.println("t=" + fmu.getCurrentTime() + ", height=" + h.getValue());

            fmu.doStep( macroStep);

       }


    }

    void readme() {
/*
        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(1E-3);
        ModelExchangeFmu fmu = new ModelExchangeFmu(new File("path/to/fmu.fmu"), integrator);
        fmu.init();

        double microStep = 1E-3;
        double macroStep = 1E-2;
        while (fmu.getCurrentTime() < 5) {
            fmu.doStep(macroStep);
        }

        fmu.terminate();
*/
    }

}
