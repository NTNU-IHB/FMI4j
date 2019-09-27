package no.ntnu.ihb.fmi4j;

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;

@SlaveInfo(
        modelName = "Test",
        author = "Lars Ivar Hatledal",
        license = "MIT"
)
public class MyTestSlave extends Fmi2Slave {

    @ScalarVariable(causality = Fmi2Causality.output)
    protected double realOut = 2.0;

    @ScalarVariables(size = 2, causality = Fmi2Causality.output)
    protected double[] realsOut = {50.0, 200.0};

    @ScalarVariables(size = 2, causality = Fmi2Causality.local)
    protected String[] string = {"Hello", "world!"};


    @Override
    public boolean doStep(double currentTime, double dt) {
        System.out.println("per " + dt);
        return true;
    }

}
