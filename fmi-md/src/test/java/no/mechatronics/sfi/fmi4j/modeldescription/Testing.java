package no.mechatronics.sfi.fmi4j.modeldescription;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

class Testing {

    @Test
    public void testTypeName() throws IOException {
        String xml = IOUtils.toString(Testing.class.getClassLoader()
                .getResource("v2/cs/ControlledTemperature/modelDescription.xml"), Charset.forName("UTF-8"));
        SimpleModelDescription modelDescription = ModelDescriptionParser.parse(xml).asCS();

        RealVariable temperature_room = modelDescription.getModelVariables().getByName("Temperature_Room").asRealVariable();

        Assert.assertTrue(ScalarVariable.Companion.getTypeName(temperature_room).equals("Real"));

    }

}