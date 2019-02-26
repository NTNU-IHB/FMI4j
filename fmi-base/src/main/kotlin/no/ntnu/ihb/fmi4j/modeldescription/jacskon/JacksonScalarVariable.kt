package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.variables.*

/**
 * @author Lars Ivar Hatledal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class JacksonScalarVariable(

        @JacksonXmlProperty
        override val name: String,

        @JacksonXmlProperty
        override val valueReference: Long,

        @JacksonXmlProperty
        override val description: String? = null,

        @JacksonXmlProperty
        override val causality: Causality? = null,

        @JacksonXmlProperty
        override val variability: Variability? = null,

        @JacksonXmlProperty
        override val initial: Initial? = null


) : ScalarVariable {

    @JacksonXmlProperty(localName = ScalarVariable.INTEGER_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var integerAttribute: JacksonIntegerAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.REAL_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var realAttribute: JacksonRealAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.STRING_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var stringAttribute: JacksonStringAttributeImpl? = null

    @JacksonXmlProperty(localName = ScalarVariable.BOOLEAN_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var booleanAttribute: JacksonBooleanAttribute? = null

    @JacksonXmlProperty(localName = ScalarVariable.ENUMERATION_TYPE)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    var enumerationAttribute: JacksonEnumerationAttribute? = null

    /**
     * Return a typed version of this variable.
     */
    fun toTyped(): TypedScalarVariable<*> {

        return when {
            integerAttribute != null -> IntegerVariable(this, integerAttribute!!)
            realAttribute != null -> RealVariable(this, realAttribute!!)
            stringAttribute != null -> StringVariable(this, stringAttribute!!)
            booleanAttribute != null -> BooleanVariable(this, booleanAttribute!!)
            enumerationAttribute != null -> EnumerationVariable(this, enumerationAttribute!!)
            else -> throw IllegalStateException("All attributes are null!")
        }

    }

    override fun toString(): String {

        val attribute = when {
            integerAttribute != null -> integerAttribute
            realAttribute != null -> realAttribute
            stringAttribute != null -> stringAttribute
            booleanAttribute != null -> booleanAttribute
            enumerationAttribute != null -> enumerationAttribute
            else -> throw IllegalStateException("All attributes are null!")
        }

        val entries = mutableListOf<String>().apply {
            add("name=$name")
            add("valueReference=$valueReference")
            causality?.also { add("causality=$causality") }
            variability?.also { add("variability=$variability") }
            initial?.also { add("initial=$initial") }
            description?.also { add("description=$description") }
            add("attribute=$attribute")
        }.joinToString(", ")

        return "JacksonScalarVariable($entries)"
    }

}

