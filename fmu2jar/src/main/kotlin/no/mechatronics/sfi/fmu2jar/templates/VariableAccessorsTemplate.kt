package no.mechatronics.sfi.fmu2jar.templates

import no.mechatronics.sfi.fmi4j.modeldescription.enums.Causality
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*

object VariableAccessorsTemplate {

    private fun capitalizeFirstLetterAndReplaceDotsWithSlash(variable: ScalarVariable): String {
        return variable.name.let {
            it.substring(0, 1).capitalize() + it.substring(1, it.length).replace(".", "_")
        }
    }

    private fun generateJavaDoc(v: TypedScalarVariable<*>) : String {

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

        }.toString()

    }

    private fun generateGet(variable: TypedScalarVariable<*>, sb: StringBuilder) {
        sb.append("""
        ${generateJavaDoc(variable)}
        fun get${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}() = fmu.variableAccessor.get${variable.typeName}(${variable.valueReference})
            """)
    }

    private fun generateSet(variable: TypedScalarVariable<*>, sb :StringBuilder) {
        sb.append("""
        ${generateJavaDoc(variable)}
        fun set${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}(value: ${variable.typeName}) = fmu.variableAccessor.set${variable.typeName}(${variable.valueReference}, value)
            """)
    }

    fun generateInputsBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.variables.filter {
                it.causality == Causality.INPUT
            }.forEach({
                generateSet(it, this)
            })
        }.toString()
    }

    fun generateOutputsBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.variables.filter {
                it.causality == Causality.OUTPUT
            }.forEach({
                generateGet(it, this)
            })
        }.toString()
    }

    fun generateCalculatedParametersBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.variables.filter {
                it.causality == Causality.CALCULATED_PARAMETER
            }.forEach({
                generateGet(it, this)
            })
        }.toString()
    }

    fun generateParametersBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.variables.filter {
                it.causality == Causality.PARAMETER
            }.forEach({
                generateGet(it, this)
                generateSet(it, this)
            })
        }.toString()
    }

    fun generateLocalsBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.variables.filter {
                it.causality == Causality.LOCAL
            }.forEach({
                generateGet(it, this)
                generateSet(it, this)
            })
        }.toString()
    }

}