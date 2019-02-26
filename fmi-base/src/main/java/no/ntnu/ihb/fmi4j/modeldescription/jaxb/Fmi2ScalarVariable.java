
package no.ntnu.ihb.fmi4j.modeldescription.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Properties of a scalar variable
 * 
 * <p>Java class for fmi2ScalarVariable complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fmi2ScalarVariable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="Real">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attGroup ref="{}fmi2RealAttributes"/>
 *                   &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                   &lt;attribute name="derivative" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                   &lt;attribute name="reinit" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Integer">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attGroup ref="{}fmi2IntegerAttributes"/>
 *                   &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Boolean">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="String">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Enumeration">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="declaredType" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="quantity" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                   &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                   &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="Annotations" type="{}fmi2Annotation" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="valueReference" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="causality" default="local">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *             &lt;enumeration value="parameter"/>
 *             &lt;enumeration value="calculatedParameter"/>
 *             &lt;enumeration value="input"/>
 *             &lt;enumeration value="output"/>
 *             &lt;enumeration value="local"/>
 *             &lt;enumeration value="independent"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="variability" default="continuous">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *             &lt;enumeration value="constant"/>
 *             &lt;enumeration value="fixed"/>
 *             &lt;enumeration value="tunable"/>
 *             &lt;enumeration value="discrete"/>
 *             &lt;enumeration value="continuous"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="initial">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *             &lt;enumeration value="exact"/>
 *             &lt;enumeration value="approx"/>
 *             &lt;enumeration value="calculated"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="canHandleMultipleSetPerTimeInstant" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fmi2ScalarVariable", propOrder = {
    "real",
    "integer",
    "_boolean",
    "string",
    "enumeration",
    "annotations"
})
public class Fmi2ScalarVariable {

    @XmlElement(name = "Real")
    protected Fmi2ScalarVariable.Real real;
    @XmlElement(name = "Integer")
    protected Fmi2ScalarVariable.Integer integer;
    @XmlElement(name = "Boolean")
    protected Fmi2ScalarVariable.Boolean _boolean;
    @XmlElement(name = "String")
    protected Fmi2ScalarVariable.String string;
    @XmlElement(name = "Enumeration")
    protected Fmi2ScalarVariable.Enumeration enumeration;
    @XmlElement(name = "Annotations")
    protected Fmi2Annotation annotations;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected java.lang.String name;
    @XmlAttribute(name = "valueReference", required = true)
    @XmlSchemaType(name = "unsignedInt")
    protected long valueReference;
    @XmlAttribute(name = "description")
    protected java.lang.String description;
    @XmlAttribute(name = "causality")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected java.lang.String causality;
    @XmlAttribute(name = "variability")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected java.lang.String variability;
    @XmlAttribute(name = "initial")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected java.lang.String initial;
    @XmlAttribute(name = "canHandleMultipleSetPerTimeInstant")
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
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getCausality() {
        if (causality == null) {
            return "local";
        } else {
            return causality;
        }
    }

    /**
     * Sets the value of the causality property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setCausality(java.lang.String value) {
        this.causality = value;
    }

    /**
     * Gets the value of the variability property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getVariability() {
        if (variability == null) {
            return "continuous";
        } else {
            return variability;
        }
    }

    /**
     * Sets the value of the variability property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setVariability(java.lang.String value) {
        this.variability = value;
    }

    /**
     * Gets the value of the initial property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getInitial() {
        return initial;
    }

    /**
     * Sets the value of the initial property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setInitial(java.lang.String value) {
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
    public void setCanHandleMultipleSetPerTimeInstant(java.lang.Boolean value) {
        this.canHandleMultipleSetPerTimeInstant = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Boolean {

        @XmlAttribute(name = "declaredType")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String declaredType;
        @XmlAttribute(name = "start")
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="declaredType" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="quantity" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}int" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Enumeration {

        @XmlAttribute(name = "declaredType", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String declaredType;
        @XmlAttribute(name = "quantity")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String quantity;
        @XmlAttribute(name = "min")
        protected java.lang.Integer min;
        @XmlAttribute(name = "max")
        protected java.lang.Integer max;
        @XmlAttribute(name = "start")
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attGroup ref="{}fmi2IntegerAttributes"/>
     *       &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}int" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Integer {

        @XmlAttribute(name = "declaredType")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String declaredType;
        @XmlAttribute(name = "start")
        protected java.lang.Integer start;
        @XmlAttribute(name = "quantity")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String quantity;
        @XmlAttribute(name = "min")
        protected java.lang.Integer min;
        @XmlAttribute(name = "max")
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attGroup ref="{}fmi2RealAttributes"/>
     *       &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="derivative" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="reinit" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Real {

        @XmlAttribute(name = "declaredType")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String declaredType;
        @XmlAttribute(name = "start")
        protected Double start;
        @XmlAttribute(name = "derivative")
        @XmlSchemaType(name = "unsignedInt")
        protected Long derivative;
        @XmlAttribute(name = "reinit")
        protected java.lang.Boolean reinit;
        @XmlAttribute(name = "quantity")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String quantity;
        @XmlAttribute(name = "unit")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String unit;
        @XmlAttribute(name = "displayUnit")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String displayUnit;
        @XmlAttribute(name = "relativeQuantity")
        protected java.lang.Boolean relativeQuantity;
        @XmlAttribute(name = "min")
        protected Double min;
        @XmlAttribute(name = "max")
        protected Double max;
        @XmlAttribute(name = "nominal")
        protected Double nominal;
        @XmlAttribute(name = "unbounded")
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class String {

        @XmlAttribute(name = "declaredType")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String declaredType;
        @XmlAttribute(name = "start")
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
