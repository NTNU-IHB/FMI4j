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

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
        val child = node.childNodes.item(0)

        val unmarshal by lazy {
            val ctx = JAXBContext.newInstance(ScalarVariableImpl::class.java)
            ctx.createUnmarshaller().unmarshal(node, ScalarVariableImpl::class.java).value
        }

        return when (child.nodeName) {
            "Integer" -> IntegerVariable(unmarshal)
            "Real" -> RealVariable(unmarshal)
            "String" -> StringVariable(unmarshal)
            "Boolean" -> BooleanVariable(unmarshal)
            "Enumeration" -> EnumerationVariable(unmarshal)
            else -> throw RuntimeException("Error parsing XML. Don't know what to do with '${child.nodeName}'")
        }

    }

    override fun marshal(v: AbstractTypedScalarVariable<*>?): Any {
        TODO("not implemented")
    }

}

private fun assignAttributeManually(tree: JsonNode, variable: ScalarVariableImpl) {

//    val tree1 = parser.readValueAs(Map::class.java)
//    println(tree1)
//    val tree2 = parser.readValueAs(Map::class.java)
//    println(tree2)
//    println("################")

    when {
        tree.has(INTEGER_TYPE) -> variable.integerAttribute = IntegerAttribute()
        tree.has(REAL_TYPE) -> variable.realAttribute = RealAttribute()
        tree.has(STRING_TYPE) -> variable.stringAttribute = StringAttribute()
        tree.has(BOOLEAN_TYPE) -> variable.booleanAttribute = BooleanAttribute()
        tree.has(ENUMERATION_TYPE) -> variable.enumerationAttribute = EnumerationAttribute()
    }

}

class ScalarVariableAdapter2: StdDeserializer<AbstractTypedScalarVariable<*>>(null as Class<*>?) {

    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): AbstractTypedScalarVariable<*>? {


        val tree = parser.readValueAsTree<JsonNode>()

        val mapper = jacksonObjectMapper().apply {

            SimpleModule().apply {
                addDeserializer(AbstractTypedScalarVariable::class.java, ScalarVariableAdapter2())
            }.also { registerModule(it) }


            enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)


        }

        println(tree)
        println(tree.toString())

        val variable = mapper.readValue(tree.toString(), ScalarVariableImpl::class.java)
        println(variable)
        variable ?: return null.also { println("faen") }

        if (variable.noAttributes) {
            println("no attribs")
            assignAttributeManually(tree, variable)
        }

        return when {
            variable.integerAttribute != null -> IntegerVariable(variable)
            variable.realAttribute != null -> RealVariable(variable)
            variable.stringAttribute != null -> StringVariable(variable)
            variable.booleanAttribute != null -> BooleanVariable(variable)
            variable.enumerationAttribute != null -> EnumerationVariable(variable)
            else -> null
        }.also { println(it) }

    }
}
