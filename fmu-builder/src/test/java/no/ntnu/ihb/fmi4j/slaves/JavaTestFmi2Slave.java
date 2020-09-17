package no.ntnu.ihb.fmi4j.slaves;

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave;
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

    protected double realOut = 2.0;
    protected double param = 1.0;
    protected int intOut = 1;
    protected double speed = 99.0;

    public JavaTestFmi2Slave(@NotNull Map<String, Object> args) {
        super(args);
    }

    @Override
    protected void registerVariables() {
        register(real("realOut", () -> realOut)
        .causality(Fmi2Causality.output));
        register(real("param", () -> param)
        .causality(Fmi2Causality.parameter).variability(Fmi2Variability.constant));
        register(integer("intOut", () -> intOut)
                .causality(Fmi2Causality.output).variability(Fmi2Variability.discrete));
        register(real("speed", () -> speed)
                .causality(Fmi2Causality.output).variability(Fmi2Variability.discrete));
    }

    @Override
    public void doStep(double currentTime, double dt) {
        realOut += dt;
    }

}
