package no.mechatronics.sfi.fmi4j;


import no.mechatronics.sfi.fmi4j.fmu.CoSimulationFmu;
import no.mechatronics.sfi.fmi4j.fmu.VariableReader;
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status;
import no.mechatronics.sfi.fmi4j.modeldescription.types.RealVariable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CoSimulationFmuTest {

    Fmi2Simulation fmu;

    @Before
    public void setUp() throws IOException {
        final URL url = getClass().getClassLoader().getResource("v2/cs/ControlledTemperature/ControlledTemperature.fmu");
        Assert.assertNotNull(url);

        fmu = new CoSimulationFmu(url);
        
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

        final RealVariable startTemp = fmu.getModelVariables().getReal("HeatCapacity1.T0");
        
        fmu.init();

        Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
        Assert.assertEquals(0.1, fmu.getModelVariables().getReal("HeatCapacity1.C").getStart(), 0);

        Assert.assertEquals(0.1, fmu.getModelVariables().getReal("HeatCapacity1.C").getStart(), 0);

        VariableReader read = fmu.read("Temperature_Room");

        final RealVariable r = fmu.getModelVariables().getReal("Temperature_Room");
        Assert.assertNotNull(r);

        double first1 = Double.NaN;

        double dt = 1d/100;
        for (int i = 0; i < 5; i++) {
            fmu.doStep(dt);
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
            double value = read.asReal();
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
            if (Double.isNaN(first1)) {
                first1 = value;
            }
            System.out.println(value);

        }

        fmu.reset();
        Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);

        for (int i = 0; i < 5; i++) {
            fmu.doStep(1d / 100);
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
            double value = r.getValue();
            Assert.assertTrue(fmu.getLastStatus() == Fmi2Status.OK);
            if (i == 0) {
                Assert.assertEquals(first1, value, 0);
            }

        }


    }


    void readme() {

        CoSimulationFmu fmu = new CoSimulationFmu(new File("path/to/fmu.fmu"));
        fmu.init();

        double t = 0;
        double dt = 1d/100;

        while (t < 10) {
            fmu.doStep(dt);
        }

        fmu.terminate();

    }

}
