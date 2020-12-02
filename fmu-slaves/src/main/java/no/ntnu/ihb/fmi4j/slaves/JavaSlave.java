package no.ntnu.ihb.fmi4j.slaves;

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave;
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable;
import no.ntnu.ihb.fmi4j.export.fmi2.SlaveInfo;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;

import java.util.Map;

@SlaveInfo(
        modelName = "MyJavaSlave",
        author = "John Doe"
)
public class JavaSlave extends Fmi2Slave {

    @ScalarVariable(causality = Fmi2Causality.output)
    private final int intOut = 99;
    @ScalarVariable(causality = Fmi2Causality.output)
    private final double[] realsOut = {50.0, 200.0};
    @ScalarVariable(causality = Fmi2Causality.local)
    private final String[] string = {"Hello", "world!"};
    @ScalarVariable(causality = Fmi2Causality.output)
    private double realOut = 2.0;

    public JavaSlave(Map<String, Object> args) {
        super(args);
    }


    @Override
    public void doStep(double currentTime, double dt) {
        realOut += dt;
    }

}
