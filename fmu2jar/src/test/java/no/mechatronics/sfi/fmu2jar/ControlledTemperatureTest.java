//package no.mechatronics.sfi.fmu2jar;
//
//import no.mechatronics.sfi.fmi4j.misc.RealReader;
//import no.mechatronics.sfi.fmu2jar.controlledtemperature.ControlledTemperature;
//
//public class ControlledTemperatureTest {
//
//    public static void main(String[] args) {
//
//       try( ControlledTemperature ct = ControlledTemperature.newInstance()) {
//           ct.init();
//           RealReader reader = ct.getParameters().getHeatCapacity1_CReader();
//           System.out.println(reader.read());
//       }
//
//    }
//
//}
