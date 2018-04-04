package no.mechatronics.sfi.fmu2jar;

import no.mechatronics.sfi.fmi4j.common.FmiStatus;
import no.mechatronics.sfi.fmu2jar.controlledtemperature.ControlledTemperature;

public class ControlledTemperatureTest {

    public static void main(String[] args) {

       try( ControlledTemperature ct = ControlledTemperature.newInstance()) {
           if (ct.init() == FmiStatus.OK) {
               System.out.println(ct.getOutputs().getTemperature_Reference());
           }

       }

    }

}
