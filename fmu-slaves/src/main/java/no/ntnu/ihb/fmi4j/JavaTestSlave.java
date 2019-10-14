package no.ntnu.ihb.fmi4j;

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability;

@SlaveInfo(
        modelName = "Test",
        author = "Lars Ivar Hatledal"
)
public class JavaTestSlave extends Fmi2Slave {

    @ScalarVariable(causality = Fmi2Causality.output, variability = Fmi2Variability.constant)
    protected double realOut = 2.0;

    @Override
    public boolean doStep(double currentTime, double dt) {
        realOut += dt;
        return true;
    }

}
