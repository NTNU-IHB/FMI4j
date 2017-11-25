package no.mechatronics.sfi;

import no.mechatronics.sfi.jna.Fmi2Status;
import no.mechatronics.sfi.modeldescription.RealVariable;
import no.mechatronics.sfi.FmuFile;
import no.mechatronics.sfi.ModelExchangeFmu;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ModelExchangeTest {

    ModelExchangeFmu fmu;

    @Before
    public void setUp() throws IOException {
        final URL url = getClass().getClassLoader().getResource("v2/me/bouncingBall/bouncingBall.fmu");
        Assert.assertNotNull(url);

        FirstOrderIntegrator integrator;
       // integrator= new DormandPrince853Integrator(1E-12, 1.0, 1E-10, 1E-10);
        //integrator = new AdamsBashforthIntegrator(100, 1E-10, 1.0, 1E-10, 1E-10);
         integrator = new ClassicalRungeKuttaIntegrator(1E-3);
        // integrator = new EulerIntegrator(1E-3);

        fmu = new ModelExchangeFmu(new FmuFile(url), integrator);
    }

    @After
    public void tearDown() {
        if (fmu != null) {
            fmu.terminate();
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
        }
    }

    @org.junit.Test
    public void test() throws IOException {


        RealVariable h = fmu.getModelVariables().getReal("h");
        h.setStart(5.0);

        fmu.init();
        Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);

        double microStep = 1E-3;
        double macroStep = 1E-2;

        while (fmu.getCurrentTime() < 5) {
            fmu.step(microStep, macroStep);
            System.out.println("t=" + fmu.getCurrentTime() + "height=" + h.getValue());
        }

        fmu.terminate();



    }

    void readme() {

        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(1E-3);
        ModelExchangeFmu fmu = new ModelExchangeFmu(new File("path/to/fmu.fmu"), integrator);
        fmu.init();

        double microStep = 1E-3;
        double macroStep = 1E-2;
        while (fmu.getCurrentTime() < 5) {
            fmu.step(microStep, macroStep);
        }

        fmu.terminate();

    }

}
