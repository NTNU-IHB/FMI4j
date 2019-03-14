/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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

package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import no.ntnu.ihb.fmi4j.modeldescription.variables.ModelVariables
import no.ntnu.ihb.fmi4j.modeldescription.variables.TypedScalarVariable

/**
 * @author Lars Ivar Hatledal
 */
@JacksonXmlRootElement(localName = "ModelVariables")
class JacksonModelVariables : ModelVariables {

    @JacksonXmlProperty(localName = "ScalarVariable")
    @JacksonXmlElementWrapper(useWrapping = false)
    private val variables: List<JacksonScalarVariable>? = null

    @Transient
    private var _variables: List<TypedScalarVariable<*>>? = null

    override fun getVariables(): List<TypedScalarVariable<*>> {
        if (_variables == null) {
            _variables = variables!!.map { it.toTyped() }
        }
        return _variables!!
    }

    override fun toString(): String {
        return "JacksonModelVariables(variables=$variables)"
    }

}
