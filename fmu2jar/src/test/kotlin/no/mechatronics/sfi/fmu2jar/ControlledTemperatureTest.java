package no.mechatronics.sfi.fmu2jar;

import no.mechatronics.sfi.fmu2jar.controlledtemperature.ControlledTemperature;

public class ControlledTemperatureTest {

    public static void main(String[] args) {

       try( ControlledTemperature ct = ControlledTemperature.newInstance()) {
           double heatCapacity1_c = ct.getParameters().getHeatCapacity1_C();
           System.out.println(heatCapacity1_c);
       }

    }

}
