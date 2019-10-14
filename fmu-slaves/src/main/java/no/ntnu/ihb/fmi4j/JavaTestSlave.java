package no.ntnu.ihb.fmi4j;

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability;

@SlaveInfo(
        modelName = "Test",
        author = "Lars Ivar Hatledal",
        version = "asdas"
)
public class JavaTestSlave extends Fmi2Slave {

    @ScalarVariable(causality = Fmi2Causality.output, variability = Fmi2Variability.constant)
    protected double realOut = 2.0;

    @ScalarVariable
    Vector3 vector3;



    @Override
    public boolean doStep(double currentTime, double dt) {
        realOut += dt;
        return true;
    }


    static class Vector3 implements RealVector {

        @Override
        public int getSize() {
            return 3;
        }

        @Override
        public double get(int index) {
            return 0;
        }

        @Override
        public void set(int index, double value) {

        }

        @ScalarVariable
        double x;

        @ScalarVariable
        double y;

    }

}
