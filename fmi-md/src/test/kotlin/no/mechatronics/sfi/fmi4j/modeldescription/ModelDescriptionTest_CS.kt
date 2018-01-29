/*
 * The MIT License
 *
 * Copyright 2017-2018 Norwegian University of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package no.mechatronics.sfi.fmi4j.modeldescription

import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.misc.DefaultExperiment
import no.mechatronics.sfi.fmi4j.modeldescription.misc.VariableNamingConvention
import org.apache.commons.io.IOUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.nio.charset.Charset
import java.util.*

class ModelDescriptionTest_CS {

    private lateinit var modelDescription: CoSimulationModelDescription

    @Before
    fun setUp() {
        val xml = IOUtils.toString(javaClass.classLoader
                .getResource("v2/cs/ControlledTemperature/modelDescription.xml"), Charset.forName("UTF-8"))
        modelDescription = ModelDescriptionParser.parse(xml).asCS()
    }

    @Test
    fun testFmiVersion() {
        val fmiVersion = modelDescription.fmiVersion
        println("fmiVersion=$fmiVersion")
        Assert.assertEquals("2.0", fmiVersion)
    }

    @Test
    fun testModelName() {
        val modelName = modelDescription.modelName
        println("modelName=$modelName")
        Assert.assertEquals("ControlledTemperature", modelName)
    }

    @Test
    fun testModelIdentifier() {
        val modelIdentifier = modelDescription.modelIdentifier
        println("modelIdentifier=$modelIdentifier")
        Assert.assertEquals("ControlledTemperature", modelIdentifier)
    }

    @Test
    fun testGuid() {
        val guid = modelDescription.guid
        println("guid=$guid")
        Assert.assertEquals("{06c2700b-b39c-4895-9151-304ddde28443}", guid)
    }

    @Test
    fun testLicense() {
        val license = modelDescription.license
        println("licence=$license")
        Assert.assertEquals("-", license)
    }

    @Test
    fun testDefaultExperiment() {
        val ex: DefaultExperiment = modelDescription.defaultExperiment!!
        Assert.assertEquals(0.0, ex.startTime, 0.0)
        Assert.assertEquals(20.0, ex.stopTime, 0.0)
        Assert.assertEquals(1.0e-4, ex.stepSize, 0.0)
    }

    @Test
    fun testNumVariables() {
        val variables: ModelVariables =  modelDescription.modelVariables
        Assert.assertEquals(120, variables.size)
    }

    @Test
    fun testStartVariables() {
        val variables: ModelVariables =  modelDescription.modelVariables
        val variable: RealVariable = variables.getByName("Temperature_Room").asRealVariable()
        println(variable)
        Assert.assertEquals(298.0, variable.start!!, 0.0)
    }

    @Test
    fun testTypeName() {
        val variables: ModelVariables =  modelDescription.modelVariables
        val variable: RealVariable = variables.getByName("Temperature_Room").asRealVariable()
        println(variable)
        Assert.assertEquals("Real", ScalarVariable.getTypeName(variable))
    }

    @Test
    fun testMinMax() {
        val variables =  modelDescription.modelVariables
        val variable = variables.getByName("Temperature_Room").asRealVariable()
        println(variable)
        Assert.assertEquals(2.0, variable.min)
        Assert.assertEquals(4.0, variable.max)
    }


    @Test
    fun testVariableNamingConvention()  {
        val variableNamingConvention = modelDescription.variableNamingConvention
        println("variableNamingConvention=$variableNamingConvention")
        Assert.assertTrue(modelDescription.variableNamingConvention == VariableNamingConvention.STRUCTURED)
    }
    
    @Test
    fun testSourceFiles() {
        
        val sourceFiles = modelDescription.sourceFiles
        println(sourceFiles)
        Assert.assertTrue(sourceFiles.map { it.name }.containsAll(
                Arrays.asList("EulerAngles.c",
                        "fmi2Functions.c" ,
                        "MotionProfiles.c",
                        "xxfuncs.c",
                        "xxinteg.c",
                        "xxinverse.c",
                        "xxmatrix.c",
                        "xxmodel.c",
                        "xxsubmod.c",
                        "xxTable2D.c")
            ))
        
    }

}