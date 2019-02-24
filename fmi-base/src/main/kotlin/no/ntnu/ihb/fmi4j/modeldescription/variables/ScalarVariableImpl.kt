package no.ntnu.ihb.fmi4j.modeldescription.variables

import no.ntnu.ihb.fmi.fmi2.xml.Fmi2ScalarVariable
import no.ntnu.ihb.fmi4j.common.Real

class ScalarVariableImpl(
        private val v: Fmi2ScalarVariable
): ScalarVariable {

    override val name: String
        get() = v.name

    override val valueReference: Long
        get() = v.valueReference

    override val description: String?
        get() = v.description

    override val causality: Causality?
        get() = v.causality?.let { Causality.valueOf(it) }

    override val variability: Variability?
        get() = v.variability?.let { Variability.valueOf(it) }

    override val initial: Initial?
        get() = v.initial?.let { Initial.valueOf(it) }

    override val annotations: Annotations? =
        v.annotations?.tool?.map {
            ToolAnnotation(
                    name = it.name,
                    `object` = it.any
            )
        }

    override val canHandleMultipleSetPerTimeInstant: Boolean
        get() = v.isCanHandleMultipleSetPerTimeInstant

    override fun isIntegerVariable(): Boolean {
        return v.integer != null
    }

    override fun isRealVariable(): Boolean {
        return v.real != null
    }

    override fun isStringVariable(): Boolean {
        return v.string != null
    }

    override fun isBooleanVariable(): Boolean {
        return v.boolean != null
    }

    override fun isEnumerationVariable(): Boolean {
        return v.enumeration != null
    }

    override fun asIntegerVariable(): IntegerVariable {
        return IntegerVariableImpl()
    }

    override fun asRealVariable(): RealVariable {
        return RealVariableImpl()
    }

    override fun asStringVariable(): StringVariable {
        return StringVariableImpl()
    }

    override fun asBooleanVariable(): BooleanVariable {
        return BooleanVariableImpl()
    }

    override fun asEnumerationVariable(): EnumerationVariable {
        return EnumerationVariableImpl()
    }

    override fun toString(): String {
        return "ScalarVariableImpl(name=$name, description=$description, valueReference=$valueReference)"
    }

    inner class IntegerVariableImpl: ScalarVariable by this, IntegerVariable {

        private val attribute: Fmi2ScalarVariable.Integer 
                = v.integer ?: throw IllegalStateException("ScalarVariable is not of type Integer!")
        
        override val start: Int?
            get() = attribute.start
        override val min: Int?
            get() = attribute.min
        override val max: Int?
            get() = attribute.max
        override val quantity: String?
            get() = attribute.quantity
        override val declaredType: String?
            get() = attribute.declaredType
    }

    inner class RealVariableImpl: ScalarVariable by this, RealVariable {

        private val attribute: Fmi2ScalarVariable.Real 
                = v.real ?: throw IllegalStateException("ScalarVariable is not of type Real!")

        override val start: Real?
            get() = attribute.start
        override val min: Real?
            get() = attribute.min
        override val max: Real?
            get() = attribute.max
        override val quantity: String?
            get() = attribute.quantity
        override val declaredType: String?
            get() = attribute.declaredType

        override val nominal: Double?
            get() = attribute.nominal
        override val derivative: Int?
            get() = attribute.derivative?.toInt()
        override val unbounded: Boolean?
            get() = attribute.isUnbounded
        override val reinit: Boolean
            get() = attribute.isReinit
        override val unit: String?
            get() = attribute.unit
        override val displayUnit: String?
            get() = attribute.displayUnit
        override val relativeQuantity: Boolean?
            get() = attribute.isRelativeQuantity
    }
    
    inner class StringVariableImpl: ScalarVariable by this, StringVariable {
        
        private val attribute: Fmi2ScalarVariable.String
                = v.string ?: throw IllegalStateException("ScalarVariable is not of type String!")
        
        override val start: String?
            get() = attribute.start
        override val declaredType: String?
            get() = attribute.declaredType
    }

    inner class BooleanVariableImpl: ScalarVariable by this, BooleanVariable {

        private val attribute: Fmi2ScalarVariable.Boolean
                = v.boolean ?: throw IllegalStateException("ScalarVariable is not of type Boolean!")

        override val start: Boolean?
            get() = attribute.isStart
        override val declaredType: String?
            get() = attribute.declaredType
    }

    inner class EnumerationVariableImpl: ScalarVariable by this, EnumerationVariable {

        private val attribute: Fmi2ScalarVariable.Enumeration
                = v.enumeration ?: throw IllegalStateException("ScalarVariable is not of type Enumeration!")

        override val start: Int?
            get() = attribute.start
        override val min: Int?
            get() = attribute.min
        override val max: Int?
            get() = attribute.max
        override val quantity: String?
            get() = attribute.quantity
        override val declaredType: String?
            get() = attribute.declaredType
    }






}