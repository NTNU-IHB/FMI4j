package no.ntnu.ihb.fmi4j.importer;

import no.ntnu.ihb.fmi4j.Fmi4jVariableUtils;
import no.ntnu.ihb.fmi4j.FmiStatus;
import no.ntnu.ihb.fmi4j.SlaveInstance;
import no.ntnu.ihb.fmi4j.TestFMUs;
import no.ntnu.ihb.fmi4j.importer.fmi2.CoSimulationFmu;
import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu;
import no.ntnu.ihb.fmi4j.modeldescription.variables.RealVariable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class ControlledTemperatureTestJava {

    private final static Logger LOG = LoggerFactory.getLogger(ControlledTemperatureTestJava.class);

    private static CoSimulationFmu fmu;

    @BeforeAll
    public static void setUp() throws IOException {
        fmu = Fmu.from(TestFMUs.get("2.0/cs/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")).asCoSimulationFmu();

    }

    @AfterAll
    public static void tearDown() {
        fmu.close();
    }

    @Test
    public void test() throws IOException {

        try (SlaveInstance slave = fmu.newInstance()) {

            Assertions.assertTrue(slave.getInstanceName()
                    .contains(slave.getModelDescription().getAttributes().getModelIdentifier()));

            Assertions.assertEquals("2.0", slave.getModelDescription().getFmiVersion());

            final double startTemp = slave.getModelDescription()
                    .getVariableByName("HeatCapacity1.T0").asRealVariable().getStart();
            Assertions.assertEquals(298.0, startTemp);

            slave.simpleSetup();

            final RealVariable heatCapacity1_C = slave.getModelDescription()
                    .getVariableByName("HeatCapacity1.C").asRealVariable();
            Assertions.assertEquals(0.1, (double) heatCapacity1_C.getStart());
            LOG.info("heatCapacity1_C={}", Fmi4jVariableUtils.read(heatCapacity1_C, slave).getValue());

            final RealVariable temperature_room = slave.getModelDescription()
                    .getVariableByName("Temperature_Room").asRealVariable();

            double t = 0;
            double dt = 1d / 100;
            for (int i = 0; i < 5; i++) {
                Assertions.assertTrue(slave.doStep(t, dt));
                Assertions.assertEquals(slave.getLastStatus(), FmiStatus.OK);
                t += dt;
                double value = Fmi4jVariableUtils.read(temperature_room, slave).getValue();
                Assertions.assertEquals(slave.getLastStatus(), FmiStatus.OK);

                LOG.info("temperature_room={}", value);

                Assertions.assertEquals(value, (double) Fmi4jVariableUtils
                        .readReal(slave, "Temperature_Room").getValue());

            }

        }

    }

}
