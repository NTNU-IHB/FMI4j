package no.ntnu.ihb.fmi4j.slaves;

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave;
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable;
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariableGetter;
import no.ntnu.ihb.fmi4j.export.fmi2.SlaveInfo;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SlaveInfo(
        modelName = "Test",
        author = "Lars Ivar Hatledal"
)
public class JavaTestFmi2Slave extends Fmi2Slave {

    @ScalarVariable(causality = Fmi2Causality.output)
    protected double realOut = 2.0;

    @ScalarVariable(causality = Fmi2Causality.parameter, variability = Fmi2Variability.constant)
    protected double param = 1.0;

    @ScalarVariable(causality = Fmi2Causality.output, variability = Fmi2Variability.discrete)
    protected double intOut = 1.0;

    public JavaTestFmi2Slave(@NotNull Map<String, Object> args) {
        super(args);
    }

    @Override
    public void doStep(double currentTime, double dt) {
        realOut += dt;
    }

    @ScalarVariableGetter
    public double getSpeed() {
        return 99;
    }

}
