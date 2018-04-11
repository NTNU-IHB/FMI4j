package no.mechatronics.sfi.fmu2jar;

import no.mechatronics.sfi.fmu2jar.controlledtemperature.ControlledTemperature;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlledTemperatureTest {

    private Logger LOG = LoggerFactory.getLogger(ControlledTemperatureTest.class);

    @Test
    public void test() {

       try(ControlledTemperature ct = ControlledTemperature.newInstance()) {

           ct.init();
           double temp = ct.getOutputs().getTemperature_Reference().getValue();
           LOG.info("Temperature_reference={}", temp);

       }

    }

}
