package no.ntnu.ihb.fmi4j;

import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable;
import no.ntnu.ihb.fmi4j.export.fmi2.SlaveInfo;
import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability;
import org.jetbrains.annotations.NotNull;

@SlaveInfo(
        modelName = "Test",
        author = "Lars Ivar Hatledal"
)
public class JavaTestFmi2Slave extends Fmi2Slave {

    @ScalarVariable(causality = Fmi2Causality.output, variability = Fmi2Variability.constant)
    protected double realOut = 2.0;

    public JavaTestFmi2Slave(@NotNull String instanceName) {
        super(instanceName);
    }

    @Override
    public void doStep(double currentTime, double dt) {
        realOut += dt;
    }

}
