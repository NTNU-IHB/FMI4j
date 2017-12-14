package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription

object CodeGeneration {


    fun generateBody(modelDescription: ModelDescription, fileName: String = modelDescription.modelName): String {

        val modelName = modelDescription.modelName
        val modelVariables = modelDescription.modelVariables

        return """

package no.mechatronics.sfi.fmu2jar.${modelName.toLowerCase()}

import no.mechatronics.sfi.fmi4j.FmuFile
import no.mechatronics.sfi.fmi4j.FmuBuilder
import no.mechatronics.sfi.fmi4j.FmiSimulation
import no.mechatronics.sfi.fmi4j.CoSimulationFmu
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelVariables
import no.mechatronics.sfi.fmi4j.modeldescription.ScalarVariable



class $modelName private constructor(
    val fmu: FmiSimulation
) : FmiSimulation by fmu {

    companion object {

        val fmuFile: FmuFile

        init {
            fmuFile = FmuFile($modelName::class.java.classLoader.getResource("$fileName"))
        }

        @JvmStatic
        fun build() : $modelName {
            val builder = FmuBuilder(fmuFile)
            return $modelName(builder.asCoSimulationFmu().newInstance())
        }

    }

    val inputs = Inputs()
    val outputs = Outputs()
    val parameters = Parameters()
    val calculatedParameters = CalculatedParameters()
    val locals = Locals()

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