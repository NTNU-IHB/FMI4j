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

package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.SimpleModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.variables.ModelVariables
import org.apache.commons.io.IOUtils
import java.nio.charset.Charset

object CodeGeneration {

    private val licenseHeader: String by lazy {
        IOUtils.toString(CodeGeneration::class.java.classLoader.getResource("license_header.txt"), Charset.forName("UTF-8"))
    }

     fun generateWrapper(modelDescription: SimpleModelDescription): String {

        val modelName: String = modelDescription.modelName
        val modelVariables: ModelVariables = modelDescription.modelVariables

        return """
$licenseHeader

package no.mechatronics.sfi.fmu2jar.${modelName.toLowerCase()}

import java.net.URL
import no.mechatronics.sfi.fmi4j.fmu.FmiSimulation
import no.mechatronics.sfi.fmi4j.fmu.FmuFile
import no.mechatronics.sfi.fmi4j.common.Real
${getIntegratorImport(modelDescription)}

/**
 * @author Lars Ivar Hatledal
 */
class $modelName private constructor(
    val fmu: FmiSimulation
) : FmiSimulation by fmu {

    companion object {

        private val fmuFile: FmuFile by lazy {
            val url: URL = $modelName::class.java.classLoader.getResource("$modelName.fmu")!!
            FmuFile.from(url)
        }
        ${generateNewInstanceMethod(modelDescription)}
    }

    val locals = Locals()
    val inputs = Inputs()
    val outputs = Outputs()
    val parameters = Parameters()
    val calculatedParameters = CalculatedParameters()

    inner class Inputs {
        ${VariableAccessorsTemplate.generateInputsBody(modelVariables)}
    }

    inner class Outputs {
        ${VariableAccessorsTemplate.generateOutputsBody(modelVariables)}
    }

    inner class Parameters {
        ${VariableAccessorsTemplate.generateParametersBody(modelVariables)}
    }

    inner class CalculatedParameters {
        ${VariableAccessorsTemplate.generateCalculatedParametersBody(modelVariables)}
    }

    inner class Locals {
        ${VariableAccessorsTemplate.generateLocalsBody(modelVariables)}
    }

}

            """

    }

    private fun getIntegratorImport(modelDescription: SimpleModelDescription): String {
        return modelDescription.supportsModelExchange.let {
            if (it) "import org.apache.commons.math3.ode.FirstOrderIntegrator" else ""
        }
    }

    private fun generateNewInstanceMethod(modelDescription: SimpleModelDescription): String {

        val modelName = modelDescription.modelName
        return StringBuilder().apply {

            if (modelDescription.supportsCoSimulation) {
                append(
                        """
        @JvmStatic
        fun newInstance(): $modelName {
            return $modelName(fmuFile.asCoSimulationFmu().newInstance())
        }
            """
                )
            }
            if (modelDescription.supportsModelExchange) {
                append(
                        """
        @JvmStatic
        fun newInstance(integrator: FirstOrderIntegrator): $modelName {
            return $modelName(fmuFile.asModelExchangeFmu().newInstance(integrator))
        }
            """
                )
            }

        }.toString()

    }

}