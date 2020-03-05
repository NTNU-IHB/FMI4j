package no.ntnu.ihb.fmi4j.export.fmi2;

import no.ntnu.ihb.fmi4j.export.RealVector;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Initial;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SlaveInfo(
        modelName = "Test",
        author = "Lars Ivar Hatledal",
        license = "MIT"
)
class JavaTestingFmi2Slave extends Fmi2Slave {

    private static final Logger LOG = Logger.getLogger(JavaTestingFmi2Slave.class.getName());

    @ScalarVariable(causality = Fmi2Causality.input)
    protected double realIn = 2.0;

    @ScalarVariable(causality = Fmi2Causality.parameter)
    protected double[] realsParams = {50.0, 200.0};

    @ScalarVariable(causality = Fmi2Causality.local, initial = Fmi2Initial.exact)
    protected String[] string = {"Hello", "world!"};

    @ScalarVariable(causality = Fmi2Causality.local)
    protected final Vector3 vector3 = new Vector3(1, 2, 3);

    @VariableContainer
    protected final Container container = new Container();

    private double aParameter = 123;

    public JavaTestingFmi2Slave(@NotNull Map<String, Object> args) {
        super(args);
    }

    @Override
    public void doStep(double currentTime, double dt) {
        LOG.log(Level.INFO, "currentTime=" + dt + ", dt=" + dt);
    }

    @ScalarVariableGetter(causality = Fmi2Causality.parameter, variability = Fmi2Variability.constant)
    public int getSomeParameter() {
        return 30;
    }

    @ScalarVariableGetter(causality = Fmi2Causality.parameter, variability = Fmi2Variability.tunable)
    public Vector3 getAVector() {
        return vector3;
    }

    @ScalarVariableGetter(causality = Fmi2Causality.parameter, variability = Fmi2Variability.tunable)
    public double getAParameter() {
        return aParameter;
    }

    @ScalarVariableSetter
    public void setAParameter(double value) {
        aParameter = value;
    }

    static class Container {

        @ScalarVariable(name = "speed1")
        double speed = 0;

    }

    static class Vector3 implements RealVector {

        double x;
        double y;
        double z;

        public Vector3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

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
