package no.ntnu.ihb.fmi4j;

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SlaveInfo(
        modelName = "Test",
        author = "Lars Ivar Hatledal"
)
public class TestSlave extends Fmi2Slave {

    @ScalarVariable(causality = Fmi2Causality.output)
    protected double realOut = 2.0;

    @ScalarVariable(causality = Fmi2Causality.output)
    protected int intOut = 99;

    @ScalarVariable(causality = Fmi2Causality.output)
    protected double[] realsOut = {50.0, 200.0};

    @ScalarVariable(causality = Fmi2Causality.local)
    protected String[] string = {"Hello", "world!"};

    @Override
    public boolean doStep(double currentTime, double dt) {
        realOut += dt;
        System.out.println("doStep: currentTime=" + currentTime + ", stepSize=" + dt);
        return true;
    }

}
