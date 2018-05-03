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

import no.mechatronics.sfi.fmi4j.modeldescription.misc.DefaultExperiment
import no.mechatronics.sfi.fmi4j.modeldescription.misc.VariableNamingConvention
import no.mechatronics.sfi.fmi4j.modeldescription.variables.ModelVariables
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

class ModelDescriptionTest_CS {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(ModelDescriptionTest_CS::class.java)

        private lateinit var modelDescription: CoSimulationModelDescription

        @JvmStatic
        @BeforeClass
        fun setUp() {
            val path = "../test/fmi2/cs/win64/20sim/4.6.4.8004/ControlledTemperature/modelDescription.xml"
            val file = File(path)
            Assert.assertTrue(file.exists())
            val xml = file.readText(Charsets.UTF_8)
            modelDescription = ModelDescriptionParser.parse(xml).asCoSimulationModelDescription()
        }
    }

    @Test
    fun testFmiVersion() {
        val fmiVersion = modelDescription.fmiVersion
        LOG.info("fmiVersion=$fmiVersion")
        Assert.assertEquals("2.0", fmiVersion)
    }

    @Test
    fun testModelName() {
        val modelName = modelDescription.modelName
        LOG.info("modelName=$modelName")
        Assert.assertEquals("ControlledTemperature", modelName)
    }

    @Test
    fun testGetByValueReference() {
        val result = modelDescription.modelVariables.getByValueReference(19)
        Assert.assertEquals(5, result.size)
    }

    @Test
    fun testGetByName() {
        val result = modelDescription.modelVariables.getByName("Thermistor.p_el_low.i")
        Assert.assertEquals(40, result.valueReference)
    }

    @Test
    fun testModelIdentifier() {
        val modelIdentifier = modelDescription.modelIdentifier
        LOG.info("modelIdentifier=$modelIdentifier")
        Assert.assertEquals("ControlledTemperature", modelIdentifier)
    }

    @Test
    fun testGuid() {
        val guid = modelDescription.guid
        LOG.info("guid=$guid")
        Assert.assertEquals("{06c2700b-b39c-4895-9151-304ddde28443}", guid)
    }

    @Test
    fun testLicense() {
        val license = modelDescription.license
        LOG.info("licence=$license")
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
        LOG.info(variables.joinToString("\n"))
    }

    @Test
    fun testStartVariables() {
        val variables: ModelVariables =  modelDescription.modelVariables
        val variable: RealVariable = variables.getByName("HeatCapacity1.T0").asRealVariable()
        LOG.info("HeatCapacity1.T0=$variable")
        Assert.assertEquals(298.0, variable.start!!, 0.0)
    }

    @Test
    fun testMinMax() {
        val variables =  modelDescription.modelVariables
        val variable = variables.getByName("Temperature_Room") as RealVariable
        LOG.info("Temperature_Room=$variable")
        Assert.assertEquals(2.0, variable.min)
        Assert.assertEquals(4.0, variable.max)
    }

    @Test
    fun needsExecutionTool(){
        Assert.assertTrue(!modelDescription.needsExecutionTool)
    }

    @Test
    fun canNotUseMemoryManagementFunctions(){
        Assert.assertTrue(modelDescription.canNotUseMemoryManagementFunctions)
    }

    @Test
    fun testVariableNamingConvention()  {
        val variableNamingConvention = modelDescription.variableNamingConvention
        LOG.info("variableNamingConvention=$variableNamingConvention")
        Assert.assertTrue(modelDescription.variableNamingConvention == VariableNamingConvention.STRUCTURED)
    }
    
    @Test
    fun testSourceFiles() {
        
        val sourceFiles = modelDescription.sourceFiles
        LOG.info("$sourceFiles")
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