/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
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

@XmlAccessorType(XmlAccessType.FIELD)
class ModelVariables : Iterable<ScalarVariable<*>> {

    @XmlElement(name = "ScalarVariable")
    @XmlJavaTypeAdapter(ScalarVariableAdapter::class)
    private var _variables: List<ScalarVariable<*>>? = null

    val variables: List<ScalarVariable<*>>
    get() {

        if (_variables == null) {
             return emptyList()
        } else {
           return _variables!!
        }

    }

    fun getValueReference(name: String) : Int? {
        return variables.firstOrNull({it.name.equals(name)})?.valueReference
    }

    fun getValueReferences(name: Iterable<String>) : IntArray? {
        return variables.filter { it.name.equals(name) }.map { it.valueReference }.toIntArray()
    }

    /**
     * Get the number of model variables held by this structure
     */
    fun size() = variables.size

    /**
     * Get model variable by index
     */
    fun getByIndex(index: Int) = variables.get(index)

    fun getByValueReference(vr: Int) : ScalarVariable<*>? {
        return variables
                .firstOrNull{it.valueReference == vr}
    }

    /**
     * Get model variable by name
     */
    fun get(name: String) : ScalarVariable<*>? {
        return variables
                .firstOrNull{it.name.equals(name)}
    }

    @SuppressWarnings("unchecked")
    private fun <T> getType(name: String, type: Class<T>) : T? {
        return variables
                .firstOrNull { type.isInstance(it) && it.name.equals(name) }
                ?.let { it as T }
    }

    fun getInteger(name: String) : IntegerVariable? {
        return getType(name, IntegerVariable::class.java)
    }

    fun getReal(name: String) : RealVariable? {
        return getType(name, RealVariable::class.java)
    }

    fun getString(name: String) : StringVariable? {
        return getType(name, StringVariable::class.java)
    }

    fun getBoolean(name: String) : BooleanVariable? {
        return getType(name, BooleanVariable::class.java)
    }

    fun getVariableNames() : List<String> {
        return map { it.name }.toList()
    }

    override fun iterator(): Iterator<ScalarVariable<*>> {
        return variables.iterator()
    }

    override fun toString(): String {
        return "ModelVariables(variables=$variables)"
    }

}

