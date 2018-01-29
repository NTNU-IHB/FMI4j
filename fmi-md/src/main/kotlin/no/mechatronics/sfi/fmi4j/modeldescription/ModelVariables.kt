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

import java.io.Serializable
import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

/**
 * @author Lars Ivar Hatledal
 */
interface ModelVariables: Iterable<ScalarVariable> {

    /**
     * Get the number of model variables held by this structure
     */
    val size: Int
        get() = variables.size

    val variables: List<ScalarVariable>

    override fun iterator() = variables.iterator()

    /**
     * Get the valueReference of the variable named <name>
     * @name name
     */
    fun getValueReference(name: String) : Int
            = variables.firstOrNull({it.name == name})?.valueReference ?: throw IllegalArgumentException("No such variable: $name")

    fun getValueReferences(names: Collection<String>): IntArray
            = names.map { getValueReference(it) }.toIntArray()

    /**
    * Get model variable by valueReference
     * @vr valueReference
    */
    fun getByValueReference(vr: Int) : ScalarVariable {
        return variables
                .firstOrNull{it.valueReference == vr}
                ?: throw IllegalArgumentException("No variable with valueReference '$vr'")
    }

     fun getByName(name: String) : ScalarVariable {
        return variables
                .firstOrNull{it.name == (name)}
                ?: throw IllegalArgumentException("No variable with name '$name'")
    }

    private fun <T: ScalarVariable> getType(name: String, type: Class<T>) : T? {
        return variables
                .filterIsInstance(type)
                .firstOrNull { it.name == (name) }
                ?: throw IllegalArgumentException("No variable of type ${type.simpleName} with name '$name'")
    }

}

/**
 * @author Lars Ivar Hatedal
 */
@XmlAccessorType(XmlAccessType.FIELD)
class ModelVariablesImpl : ModelVariables, Serializable {

    @XmlElement(name = "ScalarVariable")
    @XmlJavaTypeAdapter(ScalarVariableAdapter::class)
    private val _variables: List<TypedScalarVariable<*>>? = null

    override val variables: List<ScalarVariable>
        get() = _variables ?: emptyList()

    override fun toString(): String {
        return "ModelVariables(variables=$variables)"
    }

}

