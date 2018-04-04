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

import no.mechatronics.sfi.fmi4j.modeldescription.variables.*


private val TypedScalarVariable<*>.typeName: String
    get() = when(this) {
        is IntegerVariable -> INTEGER_TYPE
        is RealVariable -> REAL_TYPE
        is StringVariable -> STRING_TYPE
        is BooleanVariable -> BOOLEAN_TYPE
        is EnumerationVariable -> ENUMERATION_TYPE
        else -> throw IllegalStateException("$this is not a valid variable type..")
    }


object VariableAccessorsTemplate {

    private fun capitalizeFirstLetterAndReplaceDotsWithSlash(variable: ScalarVariable): String {
        return variable.name.let {
            it.substring(0, 1).capitalize() + it.substring(1, it.length).replace(".", "_")
        }
    }

    private fun generateJavaDoc(v: TypedScalarVariable<*>) : String {

        val tab = "\t\t"
        val tabStar = "$tab *"
        return StringBuilder().apply {

            append("/**\n")
            append("$tabStar ").append("Name: ").append(v.name).append('\n')

            v.description?.also {append("$tabStar ").append("Description: ").append(it).append('\n') }
            v.start?.also { append("$tabStar Start=").append(it).append('\n') }
            v.causality?.also {  append("$tabStar Causality=").append(it).append('\n') }
            v.variability?.also { append("$tabStar Variability=").append(it).append('\n') }
            v.initial?.also { append("$tabStar Initial=").append(it).append('\n') }

            when (v) {
                is IntegerVariable -> {
                    v.min?.also { append("$tabStar min=").append(it).append('\n') }
                    v.max?.also { append("$tabStar max=").append(it).append('\n') }
                }
                is RealVariable -> {
                    v.min?.also { append("$tabStar min=").append(it).append('\n') }
                    v.max?.also { append("$tabStar max=").append(it).append('\n') }
                    v.nominal?.also {  append("$tabStar nominal=").append(it).append('\n') }
                    v.unbounded?.also { append("$tabStar unbounded=").append(it).append('\n') }
                }
                is EnumerationVariable -> {
                    v.min?.also { append("$tabStar min=").append(it).append('\n') }
                    v.max?.also { append("$tabStar max=").append(it).append('\n') }
                }
            }

            append("$tab */")

        }.toString()

    }

    private fun generateGet(variable: TypedScalarVariable<*>, sb: StringBuilder) {
        sb.append("""
        ${generateJavaDoc(variable)}
        fun get${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}() = fmu.variableAccessor.read${variable.typeName}(${variable.valueReference})
            """)
    }

    private fun generateSet(variable: TypedScalarVariable<*>, sb :StringBuilder) {
        sb.append("""
        ${generateJavaDoc(variable)}
        fun set${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}(value: ${variable.typeName}) = fmu.variableAccessor.write${variable.typeName}(${variable.valueReference}, value)
            """)
    }

    fun generateInputsBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.variables.filter {
                it.causality == Causality.INPUT
            }.forEach({
                generateGet(it, this)
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