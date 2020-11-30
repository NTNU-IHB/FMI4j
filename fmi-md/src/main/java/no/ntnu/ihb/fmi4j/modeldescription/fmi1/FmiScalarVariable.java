
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * Properties of a scalar variable
 *
 * <p>Java class for fmiScalarVariable complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="fmiScalarVariable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="Real">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="quantity" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="unit" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="displayUnit" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="relativeQuantity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                   &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                   &lt;attribute name="nominal" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                   &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                   &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Integer">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="declaredType" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="quantity" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                   &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                   &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                   &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
 *                   &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
 *                   &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
 *                   &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="DirectDependency" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="valueReference" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="variability" default="continuous">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *             &lt;enumeration value="constant"/>
 *             &lt;enumeration value="parameter"/>
 *             &lt;enumeration value="discrete"/>
 *             &lt;enumeration value="continuous"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="causality" default="internal">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *             &lt;enumeration value="input"/>
 *             &lt;enumeration value="output"/>
 *             &lt;enumeration value="internal"/>
 *             &lt;enumeration value="none"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="alias" default="noAlias">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *             &lt;enumeration value="noAlias"/>
 *             &lt;enumeration value="alias"/>
 *             &lt;enumeration value="negatedAlias"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fmiScalarVariable", propOrder = {
        "real",
        "integer",
        "_boolean",
        "string",
        "enumeration",
        "directDependency"
})
public class FmiScalarVariable {

    @XmlElement(name = "Real")
    protected FmiScalarVariable.Real real;
    @XmlElement(name = "Integer")
    protected FmiScalarVariable.Integer integer;
    @XmlElement(name = "Boolean")
    protected FmiScalarVariable.Boolean _boolean;
    @XmlElement(name = "String")
    protected FmiScalarVariable.String string;
    @XmlElement(name = "Enumeration")
    protected FmiScalarVariable.Enumeration enumeration;
    @XmlElement(name = "DirectDependency")
    protected FmiScalarVariable.DirectDependency directDependency;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected java.lang.String name;
    @XmlAttribute(name = "valueReference", required = true)
    @XmlSchemaType(name = "unsignedInt")
    protected long valueReference;
    @XmlAttribute(name = "description")
    protected java.lang.String description;
    @XmlAttribute(name = "variability")
    protected FmiVariability variability;
    @XmlAttribute(name = "causality")
    protected FmiCausality causality;
    @XmlAttribute(name = "alias")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected java.lang.String alias;

    /**
     * Gets the value of the real property.
     *
     * @return
     *     possible object is
     *     {@link FmiScalarVariable.Real }
     *
     */
    public FmiScalarVariable.Real getReal() {
        return real;
    }

    /**
     * Sets the value of the real property.
     *
     * @param value
     *     allowed object is
     *     {@link FmiScalarVariable.Real }
     *
     */
    public void setReal(FmiScalarVariable.Real value) {
        this.real = value;
    }

    /**
     * Gets the value of the integer property.
     *
     * @return
     *     possible object is
     *     {@link FmiScalarVariable.Integer }
     *
     */
    public FmiScalarVariable.Integer getInteger() {
        return integer;
    }

    /**
     * Sets the value of the integer property.
     *
     * @param value
     *     allowed object is
     *     {@link FmiScalarVariable.Integer }
     *
     */
    public void setInteger(FmiScalarVariable.Integer value) {
        this.integer = value;
    }

    /**
     * Gets the value of the boolean property.
     *
     * @return
     *     possible object is
     *     {@link FmiScalarVariable.Boolean }
     *
     */
    public FmiScalarVariable.Boolean getBoolean() {
        return _boolean;
    }

    /**
     * Sets the value of the boolean property.
     *
     * @param value
     *     allowed object is
     *     {@link FmiScalarVariable.Boolean }
     *
     */
    public void setBoolean(FmiScalarVariable.Boolean value) {
        this._boolean = value;
    }

    /**
     * Gets the value of the string property.
     *
     * @return
     *     possible object is
     *     {@link FmiScalarVariable.String }
     *
     */
    public FmiScalarVariable.String getString() {
        return string;
    }

    /**
     * Sets the value of the string property.
     *
     * @param value
     *     allowed object is
     *     {@link FmiScalarVariable.String }
     *
     */
    public void setString(FmiScalarVariable.String value) {
        this.string = value;
    }

    /**
     * Gets the value of the enumeration property.
     *
     * @return
     *     possible object is
     *     {@link FmiScalarVariable.Enumeration }
     *
     */
    public FmiScalarVariable.Enumeration getEnumeration() {
        return enumeration;
    }

    /**
     * Sets the value of the enumeration property.
     *
     * @param value
     *     allowed object is
     *     {@link FmiScalarVariable.Enumeration }
     *
     */
    public void setEnumeration(FmiScalarVariable.Enumeration value) {
        this.enumeration = value;
    }

    /**
     * Gets the value of the directDependency property.
     *
     * @return
     *     possible object is
     *     {@link FmiScalarVariable.DirectDependency }
     *
     */
    public FmiScalarVariable.DirectDependency getDirectDependency() {
        return directDependency;
    }

    /**
     * Sets the value of the directDependency property.
     *
     * @param value
     *     allowed object is
     *     {@link FmiScalarVariable.DirectDependency }
     *
     */
    public void setDirectDependency(FmiScalarVariable.DirectDependency value) {
        this.directDependency = value;
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
     * Gets the value of the variability property.
     *
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *
     */
    public FmiVariability getVariability() {
        if (variability == null) {
            return FmiVariability.continuous;
        } else {
            return variability;
        }
    }

    /**
     * Sets the value of the variability property.
     *
     * @param value
     *     allowed object is
     *     {@link FmiVariability }
     *
     */
    public void setVariability(FmiVariability value) {
        this.variability = value;
    }

    /**
     * Gets the value of the causality property.
     *
     * @return
     *     possible object is
     *     {@link FmiCausality }
     *
     */
    public FmiCausality getCausality() {
        if (causality == null) {
            return FmiCausality.internal;
        } else {
            return causality;
        }
    }

    /**
     * Sets the value of the causality property.
     *
     * @param value
     *     allowed object is
     *     {@link FmiCausality }
     *
     */
    public void setCausality(FmiCausality value) {
        this.causality = value;
    }

    /**
     * Gets the value of the alias property.
     *
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *
     */
    public java.lang.String getAlias() {
        if (alias == null) {
            return "noAlias";
        } else {
            return alias;
        }
    }

    /**
     * Sets the value of the alias property.
     *
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *
     */
    public void setAlias(java.lang.String value) {
        this.alias = value;
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
     *       &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
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
        @XmlAttribute(name = "fixed")
        protected java.lang.Boolean fixed;

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

        /**
         * Gets the value of the fixed property.
         *
         * @return
         *     possible object is
         *     {@link java.lang.Boolean }
         *
         */
        public java.lang.Boolean isFixed() {
            return fixed;
        }

        /**
         * Sets the value of the fixed property.
         *
         * @param value
         *     allowed object is
         *     {@link java.lang.Boolean }
         *
         */
        public void setFixed(java.lang.Boolean value) {
            this.fixed = value;
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
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "name"
    })
    public static class DirectDependency {

        @XmlElement(name = "Name")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected List<java.lang.String> name;

        /**
         * Gets the value of the name property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the name property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getName().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link java.lang.String }
         *
         *
         */
        public List<java.lang.String> getName() {
            if (name == null) {
                name = new ArrayList<java.lang.String>();
            }
            return this.name;
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
     *       &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
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
        @XmlAttribute(name = "fixed")
        protected java.lang.Boolean fixed;

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

        /**
         * Gets the value of the fixed property.
         *
         * @return
         *     possible object is
         *     {@link java.lang.Boolean }
         *
         */
        public java.lang.Boolean isFixed() {
            return fixed;
        }

        /**
         * Sets the value of the fixed property.
         *
         * @param value
         *     allowed object is
         *     {@link java.lang.Boolean }
         *
         */
        public void setFixed(java.lang.Boolean value) {
            this.fixed = value;
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
     *       &lt;attribute name="quantity" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Integer {

        @XmlAttribute(name = "declaredType")
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
        @XmlAttribute(name = "fixed")
        protected java.lang.Boolean fixed;

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

        /**
         * Gets the value of the fixed property.
         *
         * @return
         *     possible object is
         *     {@link java.lang.Boolean }
         *
         */
        public java.lang.Boolean isFixed() {
            return fixed;
        }

        /**
         * Sets the value of the fixed property.
         *
         * @param value
         *     allowed object is
         *     {@link java.lang.Boolean }
         *
         */
        public void setFixed(java.lang.Boolean value) {
            this.fixed = value;
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
     *       &lt;attribute name="quantity" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="unit" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="displayUnit" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="relativeQuantity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="nominal" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Real {

        @XmlAttribute(name = "declaredType")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected java.lang.String declaredType;
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
        @XmlAttribute(name = "start")
        protected Double start;
        @XmlAttribute(name = "fixed")
        protected java.lang.Boolean fixed;

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
         * Gets the value of the fixed property.
         *
         * @return
         *     possible object is
         *     {@link java.lang.Boolean }
         *
         */
        public java.lang.Boolean isFixed() {
            return fixed;
        }

        /**
         * Sets the value of the fixed property.
         *
         * @param value
         *     allowed object is
         *     {@link java.lang.Boolean }
         *
         */
        public void setFixed(java.lang.Boolean value) {
            this.fixed = value;
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
     *       &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
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
        @XmlAttribute(name = "fixed")
        protected java.lang.Boolean fixed;

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

        /**
         * Gets the value of the fixed property.
         *
         * @return
         *     possible object is
         *     {@link java.lang.Boolean }
         *
         */
        public java.lang.Boolean isFixed() {
            return fixed;
        }

        /**
         * Sets the value of the fixed property.
         *
         * @param value
         *     allowed object is
         *     {@link java.lang.Boolean }
         *
         */
        public void setFixed(java.lang.Boolean value) {
            this.fixed = value;
        }

    }

}
