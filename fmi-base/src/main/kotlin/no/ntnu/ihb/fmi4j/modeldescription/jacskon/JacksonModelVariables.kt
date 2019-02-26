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
