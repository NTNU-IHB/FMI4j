package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelVariables

object CodeGeneration {


    fun generateBody(modelDescription: ModelDescription, fileName: String = modelDescription.modelName): String {

        val modelName: String = modelDescription.modelName
        val modelVariables: ModelVariables = modelDescription.modelVariables

        return """

package no.mechatronics.sfi.fmu2jar.${modelName.toLowerCase()}

import java.net.URL
import no.mechatronics.sfi.fmi4j.FmiSimulation
import no.mechatronics.sfi.fmi4j.fmu.FmuFile
import no.mechatronics.sfi.fmi4j.fmu.FmuBuilder
import no.mechatronics.sfi.fmi4j.fmu.CoSimulationFmu


class $modelName private constructor(
    val fmu: FmiSimulation
) : FmiSimulation by fmu {

    companion object {

        private val builder: FmuBuilder

        init {
            val url: URL = $modelName::class.java.classLoader.getResource("${fileName}.fmu")!!
            FmuFile(url).also {
                this.builder = FmuBuilder(it)
            }
        }

        @JvmStatic
        fun newInstance() : $modelName {
            return $modelName(builder.asCoSimulationFmu().newInstance())
        }

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

}