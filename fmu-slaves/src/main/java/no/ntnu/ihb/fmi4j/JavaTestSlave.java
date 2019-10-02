package no.ntnu.ihb.fmi4j;

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;

@SlaveInfo(
        modelName = "Test",
        author = "Lars Ivar Hatledal"
)
public class JavaTestSlave extends Fmi2Slave {

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
        return true;
    }

}
