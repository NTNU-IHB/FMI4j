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

interface ModelVariables: Iterable<AbstractScalarVariable<*>> {

    /**
     * Get the number of model variables held by this structure
     */
    val size: Int

    val variables: List<AbstractScalarVariable<*>>

    /**
     * Get the valueReference of the variable named <name>
     * @name name
     */
    fun getValueReference(name: String) : Int

    fun getValueReferences(names: Iterable<String>): IntArray

    /**
     * Get model variable by index
     */
    fun getByIndex(index: Int): AbstractScalarVariable<*>?

    /**
     * Get model variable by valueReference
     */
    fun getByValueReference(vr: Int) : AbstractScalarVariable<*>?

    /**
     * Get model variable by name
     */
    fun getByName(name: String) : AbstractScalarVariable<*>?


}

@XmlAccessorType(XmlAccessType.FIELD)
class ModelVariablesImpl : ModelVariables {

    @XmlElement(name = "ScalarVariable")
    @XmlJavaTypeAdapter(ScalarVariableAdapter::class)
    private val _variables: List<AbstractScalarVariable<*>>? = null

    override val variables: List<AbstractScalarVariable<*>>
    get() {
        return _variables ?: emptyList()
    }

    override fun getValueReference(name: String) : Int
            = variables.firstOrNull({it.name == name})?.valueReference ?: throw IllegalArgumentException("No such variable: $name")

    override fun getValueReferences(names: Iterable<String>): IntArray
            = names.map { getValueReference(it) }.toIntArray()

    override val size = variables.size

    override fun getByIndex(index: Int) = variables.get(index)

    override fun getByValueReference(vr: Int) : AbstractScalarVariable<*>? {
        return variables
                .firstOrNull{it.valueReference == vr}
    }

    override fun getByName(name: String) : AbstractScalarVariable<*>? {
        return variables
                .firstOrNull{it.name == (name)}
    }

    private fun <T: ScalarVariable> getType(name: String, type: Class<T>) : T? {
        return variables
                .filterIsInstance(type)
                .firstOrNull { it.name == (name) }
                ?: throw IllegalArgumentException("No variable of type ${type.simpleName} with name '$name'")
    }

//    override fun getInteger(name: String) : IntegerVariable? {
//        return getType(name, IntegerVariable::class.java)
//    }
//
//    override fun getReal(name: String) : RealVariable? {
//        return getType(name, RealVariable::class.java)
//    }
//
//    override fun getString(name: String) : StringVariable? {
//        return getType(name, StringVariable::class.java)
//    }
//
//    override fun getBoolean(name: String) : BooleanVariable? {
//        return getType(name, BooleanVariable::class.java)
//    }

    override fun iterator(): Iterator<AbstractScalarVariable<*>> {
        return variables.iterator()
    }

    override fun toString(): String {
        return "ModelVariables(variables=$variables)"
    }

}

