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

package no.mechatronics.sfi.fmi4j.modeldescription.variables

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import no.mechatronics.sfi.fmi4j.common.StringArray
import java.io.Serializable

/**
 * @author Lars Ivar Hatledal
 */
interface ModelVariables: Iterable<TypedScalarVariable<*>> {

    /**
     * Get the number of model categories held by this structure
     */
    val size: Int
        get() = variables.size

    val variables: List<TypedScalarVariable<*>>

    val integers: List<IntegerVariable>
        get() = variables.mapNotNull { if (it is IntegerVariable) it.asIntegerVariable() else null }

    val reals: List<RealVariable>
        get() = variables.mapNotNull { if (it is RealVariable) it.asRealVariable() else null }

    val strings: List<StringVariable>
        get() = variables.mapNotNull { if (it is StringVariable) it.asStringVariable() else null }

    val booleans: List<BooleanVariable>
        get() = variables.mapNotNull { if (it is BooleanVariable) it.asBooleanVariable() else null }

    val enumerations: List<EnumerationVariable>
        get() = variables.mapNotNull { if (it is EnumerationVariable) it.asEnumerationVariable() else null }

    operator fun get(index: Int): TypedScalarVariable<*>
            = variables[index]

    override fun iterator(): Iterator<TypedScalarVariable<*>>
            = variables.iterator()

    /**
     * Get the valueReference of the variable named <name>
     * @name name
     * @throws IllegalArgumentException if there is no variable with the provided name
     */
    fun getValueReference(name: String) : Int
            = variables.firstOrNull({it.name == name})?.valueReference ?: throw IllegalArgumentException("No variable with name '$name'")

    fun getValueReferences(names: StringArray): IntArray
            = names.map { getValueReference(it) }.toIntArray()

    /**
    * Get all variables with the given valueReference
    * @vr valueReference
    * @throws IllegalArgumentException if there are no variables with the provided value reference
    */
    fun getByValueReference(vr: Int): List<TypedScalarVariable<*>> {
        val filter = variables.filter { it.valueReference == vr }
        if (filter.isEmpty()) {
            throw IllegalArgumentException("No variable with value reference '$vr'")
        }
        return filter
    }

    /**
     * Get variable by name
     * @name the variable name
     * @throws IllegalArgumentException if there is no variable with the provided name
     */
    fun getByName(name: String) : TypedScalarVariable<*> {
        return variables.firstOrNull({it.name == name}) ?: throw IllegalArgumentException("No variable with name '$name'")
    }

    fun isValidValueReference(valueReference: Int): Boolean {
        return valueReference in variables.map { it.valueReference }
    }

}

/**
 * @author Lars Ivar Hatledal
 */
@JacksonXmlRootElement(localName = "ModelVariables")
class ModelVariablesImpl : ModelVariables, Serializable {

    @JacksonXmlProperty(localName = "ScalarVariable")
    @JacksonXmlElementWrapper(useWrapping = false)
    private val _variables: List<ScalarVariableImpl>? = null

    override val variables: List<TypedScalarVariable<*>> by lazy {
        _variables?.map { it.toTyped() } ?: emptyList()
    }

    override fun toString(): String {
        return "ModelVariablesImpl(variables=$variables)"
    }

}

