package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.IntegerVariable
import no.mechatronics.sfi.fmi4j.modeldescription.ModelVariables
import no.mechatronics.sfi.fmi4j.modeldescription.RealVariable
import no.mechatronics.sfi.fmi4j.modeldescription.ScalarVariable
import no.mechatronics.sfi.fmi4j.modeldescription.enums.Causality

object VariableAccessorsTemplate {


    private fun capitalizeFirstLetterAndReplaceDotsWithSlash(variable: ScalarVariable<*>): String {
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

    fun generateJavaDoc(v: ScalarVariable<*>) : String {

        return StringBuilder().apply {

            val tab = "\t\t\t"

            append("/**\n")

            if (v.description.isNotEmpty()) {
                append("$tab * ").append(v.description).append('\n')
            }

            if (v.start != null) {
                append("$tab * Start=").append(v.start).append('\n')
            }

            if (v.causality != null) {
                append("$tab * Causality=").append(v.causality).append('\n')
            }

            if (v.variability != null) {
                append("$tab * Variability=").append(v.variability).append('\n')
            }

            if (v.initial != null) {
                append("$tab * Initial=").append(v.initial).append('\n')
            }

            if (v is IntegerVariable) {

                if (v.min != null) {
                    append("$tab * max=").append(v.min!!).append('\n')
                }
                if (v.max != null) {
                    append("$tab * max=").append(v.max!!).append('\n')
                }

            }

            if (v is RealVariable) {

                if (v.min != null) {
                   append("$tab * max=").append(v.min!!).append('\n')
                }
                if (v.max != null) {
                   append("$tab * max=").append(v.max!!).append('\n')
                }

                if (v.nominal != null) {
                    append("$tab * nominal=").append(v.nominal!!).append('\n')
                }

            }

            append("$tab */")

        }.let {
            it.toString()
        }

    }


    private fun generateGet(variable: ScalarVariable<*>, sb: StringBuilder) {

        sb.append("""

        ${generateJavaDoc(variable)}
        fun get${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}(): ${fmiTypeToKotlinType(variable)} {
            return fmu.getReader(${variable.valueReference}).read${variable.typeName}()
        }
            """)

    }

    private fun generateSet(variable: ScalarVariable<*>, sb :StringBuilder) {

        sb.append("""

        ${generateJavaDoc(variable)}
        fun set${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}(value: ${fmiTypeToKotlinType(variable)}) {
            fmu.getWriter(${variable.valueReference}).write(value)
        }
            """)

    }


    fun generateInputsBody(modelVariables: ModelVariables) : String {
        val sb = StringBuilder()
        modelVariables.variables.filter {
            it.causality == Causality.INPUT
        }.forEach({
            generateSet(it, sb)
        })
        return sb.toString()
    }

    fun generateOutputsBody(modelVariables: ModelVariables) : String {
        val sb = StringBuilder()
        modelVariables.variables.filter {
            it.causality == Causality.OUTPUT
        }.forEach({
            generateGet(it, sb)
        })
        return sb.toString()
    }

    fun generateCalculatedParametersBody(modelVariables: ModelVariables) : String {
        val sb = StringBuilder()
        modelVariables.variables.filter {
            it.causality == Causality.CALCULATED_PARAMETER
        }.forEach({
            generateGet(it, sb)
        })
        return sb.toString()
    }


    fun generateParametersBody(modelVariables: ModelVariables) : String {
        val sb = StringBuilder()
        modelVariables.variables.filter {
            it.causality == Causality.PARAMETER
        }.forEach({
            generateGet(it, sb)
            generateSet(it, sb)
        })
        return sb.toString()
    }

    fun generateLocalsBody(modelVariables: ModelVariables) : String {
        val sb = StringBuilder()
        modelVariables.variables.filter {
            it.causality == Causality.LOCAL
        }.forEach({
            generateGet(it, sb)
            generateSet(it, sb)
        })
        return sb.toString()
    }

}