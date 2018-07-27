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

/**
 * @author Lar Ivar Hatledal
 */
object VariableAccessorsTemplate {

    private val TypedScalarVariable<*>.kotlinTypename: String
        get() = when (this) {
            is IntegerVariable -> "Int"
            is RealVariable -> "Real"
            is StringVariable -> "String"
            is BooleanVariable -> "Boolean"
            is EnumerationVariable -> "Int"
            else -> throw IllegalStateException("$this is not a valid variable type..")
        }

    private fun capitalizeFirstLetterAndReplaceDotsWithSlash(variable: ScalarVariable): String {
        return variable.name.replace(".", "_").decapitalize()
    }

    private fun generateJavaDoc(v: TypedScalarVariable<*>) : String {

        val tab = "\t\t"
        val tabStar = "$tab *"
        val newLine = "\n$tabStar\n"
        return StringBuilder().apply {

            append("/**\n")
            append("$tabStar ").append("Name:").append(v.name)

            v.start?.also { append(newLine).append("$tabStar Start=$it") }
            v.causality?.also { append(newLine).append("$tabStar Causality=$it") }
            v.variability?.also { append(newLine).append("$tabStar Variability=$it") }
            v.initial?.also { append(newLine).append("$tabStar Initial=$it") }

            when (v) {
                is IntegerVariable -> {
                    v.min?.also { append(newLine).append("$tabStar min=$it") }
                    v.max?.also { append(newLine).append("$tabStar max=$it") }
                }
                is RealVariable -> {
                    v.min?.also { append(newLine).append("$tabStar min=$it") }
                    v.max?.also { append(newLine).append("$tabStar max=$it") }
                    v.nominal?.also { append(newLine).append("$tabStar nominal=$it") }
                    v.unbounded?.also { append(newLine).append("$tabStar unbounded=$it") }
                }
                is EnumerationVariable -> {
                    v.min?.also { append("$tabStar min=") }
                    v.max?.also { append("$tabStar max=") }
                    v.quantity?.also { append("$tabStar quantity=") }
                }
            }

            v.description?.also { append(newLine).append("$tabStar ").append("Description: $it") }

            append("\n$tab */")

        }.toString()

    }

    private fun generateGet(variable: TypedScalarVariable<*>, sb: StringBuilder) {
        sb.append("""
        ${generateJavaDoc(variable)}
        fun get${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}(): FmuRead<${variable.kotlinTypename}> {
            return fmu.variableAccessor.read${variable.typeName}(${variable.valueReference})
        }
            """)
    }

    private fun generateSet(variable: TypedScalarVariable<*>, sb :StringBuilder) {
        sb.append("""
        ${generateJavaDoc(variable)}
        fun set${capitalizeFirstLetterAndReplaceDotsWithSlash(variable)}(value: ${variable.typeName}): FmiStatus {
            return fmu.variableAccessor.write${variable.typeName}(${variable.valueReference}, value)
        }
            """)
    }

    fun generateInputsBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.getByCausality(Causality.INPUT).forEach {
                generateGet(it, this)
                generateSet(it, this)
            }
        }.toString()
    }

    fun generateOutputsBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.getByCausality(Causality.OUTPUT).forEach {
                generateGet(it, this)
            }
        }.toString()
    }

    fun generateCalculatedParametersBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.getByCausality(Causality.CALCULATED_PARAMETER).forEach {
                generateGet(it, this)
            }
        }.toString()
    }

    fun generateParametersBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.getByCausality(Causality.PARAMETER).forEach {
                generateGet(it, this)
                generateSet(it, this)
            }
        }.toString()
    }

    fun generateLocalsBody(modelVariables: ModelVariables) : String {
        return StringBuilder().apply {
            modelVariables.getByCausality(Causality.LOCAL).forEach {
                generateGet(it, this)
                generateSet(it, this)
            }
        }.toString()
    }

}