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

package no.ntnu.ihb.fmi4j.modeldescription.variables

import no.ntnu.ihb.fmi4j.common.StringArray
import no.ntnu.ihb.fmi4j.common.ValueReference
import no.ntnu.ihb.fmi4j.common.ValueReferences
import java.io.Serializable

/**
 * @author Lars Ivar Hatledal
 */
class ModelVariables(
        private val variables: List<ScalarVariable>
) : List<ScalarVariable> by variables {

    val integers: List<IntegerVariable>
        get() = mapNotNull { (it as? IntegerVariable)?.asIntegerVariable() }

    val reals: List<RealVariable>
        get() = mapNotNull { (it as? RealVariable)?.asRealVariable() }

    val strings: List<StringVariable>
        get() = mapNotNull { (it as? StringVariable)?.asStringVariable() }

    val booleans: List<BooleanVariable>
        get() = mapNotNull { (it as? BooleanVariable)?.asBooleanVariable() }

    val enumerations: List<EnumerationVariable>
        get() = mapNotNull { (it as? EnumerationVariable)?.asEnumerationVariable() }

    /**
     * Does a variable with the provided valueReference exist?
     *
     * @param valueReference
     */
    fun isValidValueReference(valueReference: ValueReference): Boolean {
        return valueReference in map { it.valueReference }
    }

    /**
     * Get the valueReference of the variable named <name>
     *
     * @name name
     * @throws IllegalArgumentException if there is no variable with the provided name
     */
    fun getValueReference(name: String): ValueReference {
        return firstOrNull { it.name == name }?.valueReference
                ?: throw IllegalArgumentException("No variable with name '$name'")
    }

    /**
     * Get a list of value references matching the provided names
     * @throws IllegalArgumentException if a name is provided that does not match a variable
     */
    fun getValueReferences(names: StringArray): ValueReferences {
        return names.map { getValueReference(it) }.toLongArray()
    }

    /**
     * Get all variables with the given valueReference
     *
     * @vr valueReference
     * @throws IllegalArgumentException if there are no variables with the provided value reference
     */
    fun getByValueReference(vr: ValueReference): List<ScalarVariable> {
        return filter { it.valueReference == vr }
    }

    /**
     * Get variable by name
     * @name the variable name
     * @throws IllegalArgumentException if there is no variable with the provided name
     */
    fun getByName(name: String): ScalarVariable {
        return firstOrNull { it.name == name }
                ?: throw IllegalArgumentException("No variable with name '$name'")
    }

    /**
     * Return a list of all variables with the provided causality
     */
    fun getByCausality(causality: Causality): List<ScalarVariable> {
        return filter { it.causality == causality }
    }

}


