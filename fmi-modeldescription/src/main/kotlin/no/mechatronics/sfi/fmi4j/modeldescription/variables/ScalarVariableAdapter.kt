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

import org.w3c.dom.Node
import javax.xml.bind.JAXBContext
import javax.xml.bind.annotation.adapters.XmlAdapter


/**
 * @author Lars Ivar Hatledal
 */
class ScalarVariableAdapter : XmlAdapter<Any, AbstractTypedScalarVariable<*>>() {

    @Throws(Exception::class)
    override fun unmarshal(v: Any): AbstractTypedScalarVariable<*> {

        val node = v as Node
        val unmarshal by lazy {
            JAXBContext.newInstance(ScalarVariableImpl::class.java).let {
                it.createUnmarshaller().unmarshal(node, ScalarVariableImpl::class.java).value
            }
        }

        val child = node.childNodes.item(0)
        return when (child.nodeName) {
            INTEGER_TYPE -> IntegerVariable(unmarshal)
            REAL_TYPE -> RealVariable(unmarshal)
            STRING_TYPE -> StringVariable(unmarshal)
            BOOLEAN_TYPE -> BooleanVariable(unmarshal)
            ENUMERATION_TYPE -> EnumerationVariable(unmarshal)
            else -> throw RuntimeException("Error parsing XML. Don't know what to do with '${child.nodeName}'")
        }

    }

    override fun marshal(v: AbstractTypedScalarVariable<*>?): Any {
        TODO("not required")
    }

}
