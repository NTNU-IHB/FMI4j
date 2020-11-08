
package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Fmi2ScalarVariable {

    @JsonProperty(value = "Real")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected Fmi2ScalarVariable.Real real;
    @JsonProperty(value = "Integer")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected Fmi2ScalarVariable.Integer integer;
    @JsonProperty(value = "Boolean")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected Fmi2ScalarVariable.Boolean _boolean;
    @JsonProperty(value = "String")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected Fmi2ScalarVariable.String string;
    @JsonProperty(value = "Enumeration")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected Fmi2ScalarVariable.Enumeration enumeration;
    @JsonProperty(value = "Annotations")
    protected Fmi2Annotation annotations;
    @JacksonXmlProperty(isAttribute = true)
    protected java.lang.String name;
    @JacksonXmlProperty(isAttribute = true)
    protected long valueReference;
    @JacksonXmlProperty(isAttribute = true)
    protected java.lang.String description;
    @JacksonXmlProperty(isAttribute = true)
    protected Fmi2Causality causality;
    @JacksonXmlProperty(isAttribute = true)
    protected Fmi2Variability variability;
    @JacksonXmlProperty(isAttribute = true)
    protected Fmi2Initial initial;
    @JacksonXmlProperty(isAttribute = true)
    protected java.lang.Boolean canHandleMultipleSetPerTimeInstant;

    /**
     * Gets the value of the real property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2ScalarVariable.Real }
     *     
     */
    public Fmi2ScalarVariable.Real getReal() {
        return real;
    }

    /**
     * Sets the value of the real property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2ScalarVariable.Real }
     *     
     */
    public void setReal(Fmi2ScalarVariable.Real value) {
        this.real = value;
    }

    /**
     * Gets the value of the integer property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2ScalarVariable.Integer }
     *     
     */
    public Fmi2ScalarVariable.Integer getInteger() {
        return integer;
    }

    /**
     * Sets the value of the integer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2ScalarVariable.Integer }
     *     
     */
    public void setInteger(Fmi2ScalarVariable.Integer value) {
        this.integer = value;
    }

    /**
     * Gets the value of the boolean property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2ScalarVariable.Boolean }
     *     
     */
    public Fmi2ScalarVariable.Boolean getBoolean() {
        return _boolean;
    }

    /**
     * Sets the value of the boolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2ScalarVariable.Boolean }
     *     
     */
    public void setBoolean(Fmi2ScalarVariable.Boolean value) {
        this._boolean = value;
    }

    /**
     * Gets the value of the string property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2ScalarVariable.String }
     *     
     */
    public Fmi2ScalarVariable.String getString() {
        return string;
    }

    /**
     * Sets the value of the string property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2ScalarVariable.String }
     *     
     */
    public void setString(Fmi2ScalarVariable.String value) {
        this.string = value;
    }

    /**
     * Gets the value of the enumeration property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2ScalarVariable.Enumeration }
     *     
     */
    public Fmi2ScalarVariable.Enumeration getEnumeration() {
        return enumeration;
    }

    /**
     * Sets the value of the enumeration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2ScalarVariable.Enumeration }
     *     
     */
    public void setEnumeration(Fmi2ScalarVariable.Enumeration value) {
        this.enumeration = value;
    }

    /**
     * Gets the value of the annotations property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2Annotation }
     *     
     */
    public Fmi2Annotation getAnnotations() {
        return annotations;
    }

    /**
     * Sets the value of the annotations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2Annotation }
     *     
     */
    public void setAnnotations(Fmi2Annotation value) {
        this.annotations = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setName(java.lang.String value) {
        this.name = value;
    }

    /**
     * Gets the value of the valueReference property.
     * 
     */
    public long getValueReference() {
        return valueReference;
    }

    /**
     * Sets the value of the valueReference property.
     * 
     */
    public void setValueReference(long value) {
        this.valueReference = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setDescription(java.lang.String value) {
        this.description = value;
    }

    /**
     * Gets the value of the causality property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2Causality }
     *     
     */
    @JsonIgnore
    public Fmi2Causality getCausality() {
        if (causality == null) {
            return Fmi2Causality.local;
        } else {
            return causality;
        }
    }

    /**
     * Sets the value of the causality property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2Causality }
     *     
     */
    @JsonProperty
    public void setCausality(Fmi2Causality value) {
        this.causality = value;
    }

    /**
     * Gets the value of the variability property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2Variability }
     *     
     */
    @JsonIgnore
    public Fmi2Variability getVariability() {
        if (variability == null) {
            return Fmi2Variability.continuous;
        } else {
            return variability;
        }
    }

    /**
     * Sets the value of the variability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2Variability }
     *     
     */
    @JsonProperty
    public void setVariability(Fmi2Variability value) {
        this.variability = value;
    }

    /**
     * Gets the value of the initial property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2Initial }
     *     
     */
    @JsonIgnore
    public Fmi2Initial getInitial() {
        if (initial == null) {
            return Fmi2Initial.undefined;
        } else {
            return initial;
        }
    }

    /**
     * Sets the value of the initial property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2Initial }
     *     
     */
    @JsonProperty
    public void setInitial(Fmi2Initial value) {
        this.initial = value;
    }

    /**
     * Gets the value of the canHandleMultipleSetPerTimeInstant property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.Boolean }
     *     
     */
    public java.lang.Boolean isCanHandleMultipleSetPerTimeInstant() {
        return canHandleMultipleSetPerTimeInstant;
    }

    /**
     * Sets the value of the canHandleMultipleSetPerTimeInstant property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.Boolean }
     *     
     */
    @JsonProperty
    public void setCanHandleMultipleSetPerTimeInstant(java.lang.Boolean value) {
        this.canHandleMultipleSetPerTimeInstant = value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Boolean {

        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String declaredType;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Boolean start;

        /**
         * Gets the value of the declaredType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getDeclaredType() {
            return declaredType;
        }

        /**
         * Sets the value of the declaredType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setDeclaredType(java.lang.String value) {
            this.declaredType = value;
        }

        /**
         * Gets the value of the start property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.Boolean }
         *     
         */
        public java.lang.Boolean isStart() {
            return start;
        }

        /**
         * Sets the value of the start property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Boolean }
         *     
         */
        public void setStart(java.lang.Boolean value) {
            this.start = value;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Enumeration {

        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String declaredType;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String quantity;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Integer min;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Integer max;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Integer start;

        /**
         * Gets the value of the declaredType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getDeclaredType() {
            return declaredType;
        }

        /**
         * Sets the value of the declaredType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setDeclaredType(java.lang.String value) {
            this.declaredType = value;
        }

        /**
         * Gets the value of the quantity property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getQuantity() {
            return quantity;
        }

        /**
         * Sets the value of the quantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setQuantity(java.lang.String value) {
            this.quantity = value;
        }

        /**
         * Gets the value of the min property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.Integer }
         *     
         */
        public java.lang.Integer getMin() {
            return min;
        }

        /**
         * Sets the value of the min property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Integer }
         *     
         */
        public void setMin(java.lang.Integer value) {
            this.min = value;
        }

        /**
         * Gets the value of the max property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.Integer }
         *     
         */
        public java.lang.Integer getMax() {
            return max;
        }

        /**
         * Sets the value of the max property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Integer }
         *     
         */
        public void setMax(java.lang.Integer value) {
            this.max = value;
        }

        /**
         * Gets the value of the start property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.Integer }
         *     
         */
        public java.lang.Integer getStart() {
            return start;
        }

        /**
         * Sets the value of the start property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Integer }
         *     
         */
        public void setStart(java.lang.Integer value) {
            this.start = value;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Integer {

        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String declaredType;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Integer start;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String quantity;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Integer min;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Integer max;

        /**
         * Gets the value of the declaredType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getDeclaredType() {
            return declaredType;
        }

        /**
         * Sets the value of the declaredType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setDeclaredType(java.lang.String value) {
            this.declaredType = value;
        }

        /**
         * Gets the value of the start property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.Integer }
         *     
         */
        public java.lang.Integer getStart() {
            return start;
        }

        /**
         * Sets the value of the start property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Integer }
         *     
         */
        public void setStart(java.lang.Integer value) {
            this.start = value;
        }

        /**
         * Gets the value of the quantity property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getQuantity() {
            return quantity;
        }

        /**
         * Sets the value of the quantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setQuantity(java.lang.String value) {
            this.quantity = value;
        }

        /**
         * Gets the value of the min property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.Integer }
         *     
         */
        public java.lang.Integer getMin() {
            return min;
        }

        /**
         * Sets the value of the min property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Integer }
         *     
         */
        public void setMin(java.lang.Integer value) {
            this.min = value;
        }

        /**
         * Gets the value of the max property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.Integer }
         *     
         */
        public java.lang.Integer getMax() {
            return max;
        }

        /**
         * Sets the value of the max property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Integer }
         *     
         */
        public void setMax(java.lang.Integer value) {
            this.max = value;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Real {
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String declaredType;
        @JacksonXmlProperty(isAttribute = true)
        protected Double start;
        @JacksonXmlProperty(isAttribute = true)
        protected Long derivative;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Boolean reinit;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String quantity;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String unit;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String displayUnit;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Boolean relativeQuantity;
        @JacksonXmlProperty(isAttribute = true)
        protected Double min;
        @JacksonXmlProperty(isAttribute = true)
        protected Double max;
        @JacksonXmlProperty(isAttribute = true)
        protected Double nominal;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.Boolean unbounded;

        /**
         * Gets the value of the declaredType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getDeclaredType() {
            return declaredType;
        }

        /**
         * Sets the value of the declaredType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setDeclaredType(java.lang.String value) {
            this.declaredType = value;
        }

        /**
         * Gets the value of the start property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getStart() {
            return start;
        }

        /**
         * Sets the value of the start property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setStart(Double value) {
            this.start = value;
        }

        /**
         * Gets the value of the derivative property.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        public Long getDerivative() {
            return derivative;
        }

        /**
         * Sets the value of the derivative property.
         * 
         * @param value
         *     allowed object is
         *     {@link Long }
         *     
         */
        public void setDerivative(Long value) {
            this.derivative = value;
        }

        /**
         * Gets the value of the reinit property.
         *
         * @return
         *     possible object is
         *     {@link java.lang.Boolean }
         *
         */
        @JsonIgnore
        public boolean isReinit() {
            if (reinit == null) {
                return false;
            } else {
                return reinit;
            }
        }

        /**
         * Sets the value of the reinit property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Boolean }
         *     
         */
        public void setReinit(java.lang.Boolean value) {
            this.reinit = value;
        }

        /**
         * Gets the value of the quantity property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getQuantity() {
            return quantity;
        }

        /**
         * Sets the value of the quantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setQuantity(java.lang.String value) {
            this.quantity = value;
        }

        /**
         * Gets the value of the unit property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getUnit() {
            return unit;
        }

        /**
         * Sets the value of the unit property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setUnit(java.lang.String value) {
            this.unit = value;
        }

        /**
         * Gets the value of the displayUnit property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getDisplayUnit() {
            return displayUnit;
        }

        /**
         * Sets the value of the displayUnit property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setDisplayUnit(java.lang.String value) {
            this.displayUnit = value;
        }

        /**
         * Gets the value of the relativeQuantity property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.Boolean }
         *     
         */
        @JsonIgnore
        public boolean isRelativeQuantity() {
            if (relativeQuantity == null) {
                return false;
            } else {
                return relativeQuantity;
            }
        }

        /**
         * Sets the value of the relativeQuantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Boolean }
         *     
         */
        public void setRelativeQuantity(java.lang.Boolean value) {
            this.relativeQuantity = value;
        }

        /**
         * Gets the value of the min property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getMin() {
            return min;
        }

        /**
         * Sets the value of the min property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setMin(Double value) {
            this.min = value;
        }

        /**
         * Gets the value of the max property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getMax() {
            return max;
        }

        /**
         * Sets the value of the max property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setMax(Double value) {
            this.max = value;
        }

        /**
         * Gets the value of the nominal property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getNominal() {
            return nominal;
        }

        /**
         * Sets the value of the nominal property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setNominal(Double value) {
            this.nominal = value;
        }

        /**
         * Gets the value of the unbounded property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.Boolean }
         *     
         */
        @JsonIgnore
        public boolean isUnbounded() {
            if (unbounded == null) {
                return false;
            } else {
                return unbounded;
            }
        }

        /**
         * Sets the value of the unbounded property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.Boolean }
         *     
         */
        public void setUnbounded(java.lang.Boolean value) {
            this.unbounded = value;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class String {

        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String declaredType;
        @JacksonXmlProperty(isAttribute = true)
        protected java.lang.String start;

        /**
         * Gets the value of the declaredType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getDeclaredType() {
            return declaredType;
        }

        /**
         * Sets the value of the declaredType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setDeclaredType(java.lang.String value) {
            this.declaredType = value;
        }

        /**
         * Gets the value of the start property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String }
         *     
         */
        public java.lang.String getStart() {
            return start;
        }

        /**
         * Sets the value of the start property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String }
         *     
         */
        public void setStart(java.lang.String value) {
            this.start = value;
        }

    }

}
