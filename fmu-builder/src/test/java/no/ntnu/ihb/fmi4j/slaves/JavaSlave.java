package no.ntnu.ihb.fmi4j.slaves;

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave;
import no.ntnu.ihb.fmi4j.export.fmi2.SlaveInfo;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;

import java.util.Map;

@SlaveInfo(
        modelName = "MyJavaSlave",
        author = "John Doe"
)
public class JavaSlave extends Fmi2Slave {

    private final int intOut = 99;
    private double realOut = 2.0;
    private final double[] realsOut = {50.0, 200.0};
    private final String[] string = {"Hello", "world!"};

    public JavaSlave(Map<String, Object> args) {
        super(args);
    }

    @Override
    protected void registerVariables() {
        register(integer("intOut", () -> intOut)
                .causality(Fmi2Causality.output));
        register(real("realOut", () -> realOut)
                .causality(Fmi2Causality.output));
        register(real("realsOut", realsOut)
                .causality(Fmi2Causality.output));
        register(string("string", string)
                .causality(Fmi2Causality.local));
    }

    @Override
    public void doStep(double currentTime, double dt) {
        realOut += dt;
    }

}
