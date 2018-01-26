package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.ModelVariables
import no.mechatronics.sfi.fmi4j.modeldescription.SimpleModelDescription

object CodeGeneration {


     fun generateWrapper(modelDescription: SimpleModelDescription): String {

        val modelName: String = modelDescription.modelName
        val modelVariables: ModelVariables = modelDescription.modelVariables

        return """

package no.mechatronics.sfi.fmu2jar.${modelName.toLowerCase()}

import java.net.URL
import no.mechatronics.sfi.fmi4j.FmiSimulation
import no.mechatronics.sfi.fmi4j.fmu.FmuFile
import no.mechatronics.sfi.fmi4j.fmu.FmuBuilder


class $modelName private constructor(
    val fmu: FmiSimulation
) : FmiSimulation by fmu {

    companion object {

        private val builder: FmuBuilder by lazy {
            val url: URL = $modelName::class.java.classLoader.getResource("${modelName}.fmu")!!
            val file = FmuFile(url)
            FmuBuilder(file)
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

    private fun generateNewInstanceMethod(modelDescription: SimpleModelDescription):String {

        val modelName = modelDescription.modelName
        return StringBuilder().apply {

            if (modelDescription.supportsCoSimulation) {
                append(
                        """
        @JvmStatic
        fun newInstance(): $modelName {
            return $modelName(builder.asCoSimulationFmu().newInstance())
        }
            """
                )
            }
            if (modelDescription.supportsModelExchange) {
                append(
                        """
        @JvmStatic
        fun newInstance(integrator: FirstOrderIntegrator): $modelName {
            return $modelName(builder.asMeSimulationFmuWithIntegrator(integrator).newInstance())
        }
            """
                )
            }

        }.toString()

    }


}