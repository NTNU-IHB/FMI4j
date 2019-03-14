package no.ntnu.ihb.fmi4j.modeldescription.jaxb

import no.ntnu.ihb.fmi4j.modeldescription.variables.ModelVariables
import no.ntnu.ihb.fmi4j.modeldescription.variables.TypedScalarVariable

class JaxbModelVariables internal constructor(
        private val mv: FmiModelDescription.ModelVariables
): ModelVariables {

    @delegate:Transient
    private val _variables: List<TypedScalarVariable<*>> by lazy {
        mv.scalarVariable.map { JaxbScalarVariable(it).toTyped() }
    }

    override fun getVariables(): List<TypedScalarVariable<*>> {
        return _variables
    }
}
