package no.mechatronics.sfi.fmi4j.modeldescription;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class ModelDescriptionParseTest_java {

    @Test
    public void test() throws IOException {
        File file1 = new File("../test/fmi2/cs/win64/20sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu");
        Assert.assertTrue(file1.exists());
        ModelDescriptionParser.parse(file1).asCoSimulationModelDescription();

        File file2 = new File("../test/fmi2/cs/win64/20sim/4.6.4.8004/ControlledTemperature/modelDescription.xml");
        Assert.assertTrue(file2.exists());
        String xml = FileUtils.readFileToString(file2, Charset.forName("UTF-8"));
        ModelDescriptionParser.parse(xml).asCoSimulationModelDescription();
    }

}
