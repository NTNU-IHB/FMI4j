package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.*
import no.mechatronics.sfi.fmi4j.modeldescription.enums.Causality

object VariableAccessorsTemplate {

    private fun capitalizeFirstLetterAndReplaceDotsWithSlash(variable: ScalarVariable): String {
        return variable.name.let {
            it.substring(0, 1).capitalize() + it.substring(1, it.length).replace(".", "_")
        }
    }

//    private fun fmiTypeToKotlinType(variable: ScalarVariable) : String {
//        when(variable) {
//            is IntegerVariable -> return "Int"
//            is RealVariable -> return "Double"
//            is StringVariable -> return "String"
//            is BooleanVariable -> return "Boolean"
//        }
//        throw IllegalArgumentException()
//    }

    private fun generateJavaDoc(v: ScalarVariable) : String {

        val tab = "\t\t"
        return StringBuilder().apply {

            append("/**\n")
            append("$tab * ").append("Name: ").append(v.name).append('\n')
            if (v.description.isNotEmpty()) {
                append("$tab * ").append("Description: ").append(v.description).append('\n')
            }

            v.start?.also { append("$tab * Start=").append(it).append('\n') }
            v.causality?.also {  append("$tab * Causality=").append(it).append('\n') }
            v.variability?.also { append("$tab * Variability=").append(it).append('\n') }
            v.initial?.also { append("$tab * Initial=").append(it).append('\n') }

            when (v) {
                is IntegerVariable -> {
                    v.min?.also { append("$tab * min=").append(it).append('\n') }
                    v.max?.also { append("$tab * max=").append(it).append('\n') }
                }
                is RealVariable -> {
                    v.min?.also { append("$tab * min=").append(it).append('\n') }
                    v.max?.also { append("$tab * max=").append(it).append('\n') }
                    v.nominal?.also {  append("$tab * nominal=").append(it).append('\n') }
                    v.unbounded?.also { append("$tab * unbounded=").append(it).append('\n') }
                }
            }

            append("$tab */")

        }.let {
            it.toString()
        }

    }

    private fun generateGet(variable: ScalarVariable, sb: StringBuilder) {
        sb.append("""
        ${generateJavaDoc(variable)}
        fun get${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}Reader() = fmu.getReader(${variable.valueReference}).as${variable.typeName}Reader()
            """)
    }

    private fun generateSet(variable: ScalarVariable, sb :StringBuilder) {
        sb.append("""
        ${generateJavaDoc(variable)}
        fun get${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}Writer() = fmu.getWriter(${variable.valueReference}).as${variable.typeName}Writer()
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