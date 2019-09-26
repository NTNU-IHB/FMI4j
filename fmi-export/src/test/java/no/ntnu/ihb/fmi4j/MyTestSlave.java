package no.ntnu.ihb.fmi4j;

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;

@FmiSlaveInfo(
        name = "Test",
        author = "Lars Ivar Hatledal"
)
class MyTestSlave extends FmiSlave {

    @ScalarVariable(causality = Fmi2Causality.output)
    private double realOut = 2.0;

    @ScalarVariables(size = 2, causality = Fmi2Causality.output)
    private double[] realsOut = {50.0, 200.0};

    @Override
    public void setupExperiment(double startTime) {

    }

    @Override
    public void enterInitialisationMode() {

    }

    @Override
    public void exitInitialisationMode() {

    }

    @Override
    public void doStep(double dt) {

    }

    @Override
    public void terminate() {

    }
}

