package no.ntnu.ihb.fmi4j.export.fmi2;

import no.ntnu.ihb.fmi4j.export.RealVector;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

@SlaveInfo(
        modelName = "Test",
        author = "Lars Ivar Hatledal",
        license = "MIT"
)
class JavaTestingFmi2Slave extends Fmi2Slave {

    private static final Logger LOG = Logger.getLogger(JavaTestingFmi2Slave.class.getName());

    @ScalarVariable(causality = Fmi2Causality.output)
    protected double realOut = 2.0;

    @ScalarVariable(causality = Fmi2Causality.output)
    protected double[] realsOut = {50.0, 200.0};

    @ScalarVariable(causality = Fmi2Causality.local)
    protected String[] string = {"Hello", "world!"};

    @ScalarVariable
    protected Vector3 vector3 = new Vector3();

    @VariableContainer
    protected Container container = new Container();

    public JavaTestingFmi2Slave(@NotNull String instanceName) {
        super(instanceName);
    }

    @Override
    public void doStep(double currentTime, double dt) {
        LOG.log(Level.INFO, "currentTime=" + dt + ", dt=" + dt);
    }

    static class Container {

        @ScalarVariable
        double speed = 0;

    }

    static class Vector3 implements RealVector {

        @ScalarVariable
        double x;
        @ScalarVariable
        double y;
        @ScalarVariable
        double z;

        @Override
        public int getSize() {
            return 3;
        }

        @Override
        public double get(int index) {
            if (index == 0) {
                return x;
            } else if (index == 1) {
                return y;
            } else if (index == 2) {
                return z;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public void set(int index, double value) {
            if (index == 0) {
                x = value;
            } else if (index == 1) {
                y = value;
            } else if (index == 2) {
                z = value;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }
    }

}
