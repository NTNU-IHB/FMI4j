package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.ScalarVariable

object CodeGeneration {

    private fun convertVariableName1(variable: ScalarVariable<*>): String {
        return variable.name.let {
            it.substring(0, 1).capitalize() + it.substring(1, it.length).replace(".", "_")
        }
    }


    private fun fmiTypeToKotlinType(variable: ScalarVariable<*>) : String {
        when(variable.typeName) {
            "Integer" -> return "Int"
            "Real" -> return "Double"
            "String" -> return "String"
            "Boolean" -> return "Boolean"
        }
        throw IllegalArgumentException()
    }


    fun generateGet(variable: ScalarVariable<*>, sb: StringBuilder) {

        sb.append("""
            fun get${convertVariableName1(variable)}(): ${fmiTypeToKotlinType(variable)} {
                return fmu.read(${variable.valueReference}).as${variable.typeName}()
            }
            """)

    }

     fun generateSet(variable: ScalarVariable<*>, sb :StringBuilder) {

        sb.append("""
            fun set${convertVariableName1(variable)}(value: ${fmiTypeToKotlinType(variable)}) {
                fmu.write(${variable.valueReference}).with(value)
            }
            """)

    }


    fun generateBody(modelName: String, fileName: String, instanceMethods: String): String {

        return """

package no.mechatronics.sfi.fmu2jar

import no.mechatronics.sfi.fmi4j.FmuFile
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
            return $modelName(CoSimulationFmu(fmuFile))
        }

    }

    $instanceMethods

}

            """

    }

}