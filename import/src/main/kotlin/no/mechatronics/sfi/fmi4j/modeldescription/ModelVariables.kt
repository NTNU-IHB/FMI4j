package no.mechatronics.sfi.fmi4j.modeldescription

import no.mechatronics.sfi.fmi4j.modeldescription.types.*
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
            return _variables ?: throw AssertionError()
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
    fun get(index: Int) = variables.get(index)

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

