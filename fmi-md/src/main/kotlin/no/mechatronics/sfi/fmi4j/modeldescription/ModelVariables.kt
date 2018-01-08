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

package no.mechatronics.sfi.fmi4j.modeldescription

import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

interface IModelVariables: Iterable<ScalarVariable<*>> {

    val variables: List<ScalarVariable<*>>

    fun getValueReference(name: String) : Int

    fun getValueReferences(names: Iterable<String>): IntArray

    /**
     * Get the number of model variables held by this structure
     */
    val size: Int

    fun getVariableNames() : List<String>

    /**
     * Get model variable by index
     */
    fun getByIndex(index: Int): ScalarVariable<*>?

    /**
     * Get model variable by valueReference
     */
    fun getByValueReference(vr: Int) : ScalarVariable<*>?

    /**
     * Get model variable by name
     */
    fun getByName(name: String) : ScalarVariable<*>?

    fun getInteger(name: String) : IntegerVariable?

    fun getReal(name: String) : RealVariable?

    fun getString(name: String) : StringVariable?

    fun getBoolean(name: String) : BooleanVariable?

}

@XmlAccessorType(XmlAccessType.FIELD)
class ModelVariables : IModelVariables {

    @XmlElement(name = "ScalarVariable")
    @XmlJavaTypeAdapter(ScalarVariableAdapter::class)
    private val _variables: List<ScalarVariable<*>>? = null

    override val variables: List<ScalarVariable<*>>
    get() {
        return _variables ?: emptyList()
    }

    override fun getValueReference(name: String) : Int
            = variables.firstOrNull({it.name == name})?.valueReference ?: throw IllegalArgumentException("No such variable: $name")

    override fun getValueReferences(names: Iterable<String>): IntArray
            = names.map { getValueReference(it) }.toIntArray()

    override val size = variables.size

    override fun getByIndex(index: Int) = variables.get(index)

    override fun getByValueReference(vr: Int) : ScalarVariable<*>? {
        return variables
                .firstOrNull{it.valueReference == vr}
    }

    override fun getByName(name: String) : ScalarVariable<*>? {
        return variables
                .firstOrNull{it.name.equals(name)}
    }

    @SuppressWarnings("unchecked")
    private fun <T> getType(name: String, type: Class<T>) : T? {
        return variables
                .firstOrNull { type.isInstance(it) && it.name.equals(name) }
                ?.let { it as T }
    }

    override fun getInteger(name: String) : IntegerVariable? {
        return getType(name, IntegerVariable::class.java)
    }

    override fun getReal(name: String) : RealVariable? {
        return getType(name, RealVariable::class.java)
    }

    override fun getString(name: String) : StringVariable? {
        return getType(name, StringVariable::class.java)
    }

    override fun getBoolean(name: String) : BooleanVariable? {
        return getType(name, BooleanVariable::class.java)
    }

    override fun getVariableNames() : List<String> {
        return map { it.name }.toList()
    }

    override fun iterator(): Iterator<ScalarVariable<*>> {
        return variables.iterator()
    }

    override fun toString(): String {
        return "ModelVariables(variables=$variables)"
    }

}

