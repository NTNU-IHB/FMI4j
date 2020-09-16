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

    protected double realIn = 2.0;
    protected double[] realsParams = {50.0, 200.0};

    protected String[] stringParams = {"Hello", "world!"};

    protected final Vector3 vector3 = new Vector3(1, 2, 3);

    protected final Container container = new Container();

    private double aParameter = 123;

    public JavaTestingFmi2Slave(@NotNull Map<String, Object> args) {
        super(args);
    }

    @Override
    protected void registerVariables() {
        register(real("realIn")
                .causality(Fmi2Causality.input)
                .getter(() -> realIn));
        register(real("realsParams", realsParams)
                .causality(Fmi2Causality.parameter));
        register(string("realsParams", stringParams)
                .causality(Fmi2Causality.local)
                .initial(Fmi2Initial.exact));
        register(real("vector3", vector3)
                .causality(Fmi2Causality.local));
        register(real("container.speed")
                .getter(() -> container.speed)
                .causality(Fmi2Causality.local));
        register(real("aParameter")
                .getter(() -> aParameter)
                .causality(Fmi2Causality.local));
    }

    @Override
    public void doStep(double currentTime, double dt) {
        LOG.log(Level.INFO, "currentTime=" + dt + ", dt=" + dt);
    }

    static class Container {

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
