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

import no.mechatronics.sfi.fmi4j.TestUtils
import no.mechatronics.sfi.fmi4j.modeldescription.misc.DefaultExperiment
import no.mechatronics.sfi.fmi4j.modeldescription.variables.ModelVariables
import no.mechatronics.sfi.fmi4j.modeldescription.variables.RealVariable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class ControlledTemperatureTest {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(ControlledTemperatureTest::class.java)

    }

    private val modelDescription: CoSimulationModelDescription

    init {
        val fmu = File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/${TestUtils.getOs()}/20Sim/4.6.4.8004/ControlledTemperature/ControlledTemperature.fmu")
        Assertions.assertTrue(fmu.exists())
        modelDescription = ModelDescriptionParser.parse(fmu).asCoSimulationModelDescription()
    }

    @Test
    fun testFmiVersion() {
        val fmiVersion = modelDescription.fmiVersion
        LOG.info("fmiVersion=$fmiVersion")
        Assertions.assertEquals("2.0", fmiVersion)
    }

    @Test
    fun testModelName() {
        val modelName = modelDescription.modelName
        LOG.info("modelName=$modelName")
        Assertions.assertEquals("ControlledTemperature", modelName)
    }

    @Test
    fun testGetByValueReference() {
        val result = modelDescription.modelVariables.getByValueReference(19)
        Assertions.assertEquals(5, result.size)
    }

    @Test
    fun testGetByName() {
        val result = modelDescription.modelVariables.getByName("Thermistor.p_el_low.i")
        Assertions.assertEquals(40, result.valueReference)
    }

    @Test
    fun testModelIdentifier() {
        val modelIdentifier = modelDescription.modelIdentifier
        LOG.info("modelIdentifier=$modelIdentifier")
        Assertions.assertEquals("ControlledTemperature", modelIdentifier)
    }

    @Test
    fun testGuid() {
        val guid = modelDescription.guid
        LOG.info("guid=$guid")
        Assertions.assertEquals("{06c2700b-b39c-4895-9151-304ddde28443}", guid)
    }

    @Test
    fun testLicense() {
        val license = modelDescription.license
        LOG.info("licence=$license")
        Assertions.assertEquals("-", license)
    }

    @Test
    fun testOutputs() {
        val outputs = modelDescription.modelStructure.outputs
        LOG.info("outputs=$outputs")
        Assertions.assertTrue(outputs.map { it.index }.containsAll(listOf(115, 116)))
    }

    @Test
    fun testDefaultExperiment() {
        val ex: DefaultExperiment = modelDescription.defaultExperiment!!
        Assertions.assertEquals(0.0, ex.startTime)
        Assertions.assertEquals(20.0, ex.stopTime)
        Assertions.assertEquals(1.0e-4, ex.stepSize)
    }

    @Test
    fun testNumVariables() {
        val variables: ModelVariables =  modelDescription.modelVariables
        Assertions.assertEquals(120, variables.size)
        LOG.info(variables.joinToString("\n"))
    }

    @Test
    fun testStartVariables() {
        val variables: ModelVariables =  modelDescription.modelVariables
        val variable: RealVariable = variables.getByName("HeatCapacity1.T0").asRealVariable()
        LOG.info("HeatCapacity1.T0=$variable")
        Assertions.assertEquals(298.0, variable.start!!)
    }

//    @Test
//    fun testMinMax() {
//        val variables =  modelDescription.modelVariables
//        val variable = variables.getByName("Temperature_Room") as RealVariable
//        LOG.info("Temperature_Room=$variable")
//        Assertions.assertEquals(2.0, variable.min)
//        Assertions.assertEquals(4.0, variable.max)
//    }

    @Test
    fun needsExecutionTool(){
        Assertions.assertTrue(!modelDescription.needsExecutionTool)
    }

    @Test
    fun canNotUseMemoryManagementFunctions(){
        Assertions.assertTrue(modelDescription.canNotUseMemoryManagementFunctions)
    }


    @Test
    fun testSourceFiles() {

        val sourceFiles = modelDescription.sourceFiles
        LOG.info("$sourceFiles")
        Assertions.assertTrue(sourceFiles.map { it.name }.containsAll(
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