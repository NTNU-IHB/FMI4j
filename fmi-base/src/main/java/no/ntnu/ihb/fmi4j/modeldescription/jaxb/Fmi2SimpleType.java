
package no.ntnu.ihb.fmi4j.modeldescription.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Type attributes of a scalar variable
 * 
 * <p>Java class for fmi2SimpleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fmi2SimpleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="Real">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attGroup ref="{}fmi2RealAttributes"/>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Integer">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attGroup ref="{}fmi2IntegerAttributes"/>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Boolean" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *           &lt;element name="String" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *           &lt;element name="Enumeration">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence maxOccurs="unbounded">
 *                     &lt;element name="Item">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                             &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                             &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attribute name="quantity" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fmi2SimpleType", propOrder = {
    "real",
    "integer",
    "_boolean",
    "string",
    "enumeration"
})
public class Fmi2SimpleType {

    @XmlElement(name = "Real")
    protected Fmi2SimpleType.Real real;
    @XmlElement(name = "Integer")
    protected Fmi2SimpleType.Integer integer;
    @XmlElement(name = "Boolean")
    protected Object _boolean;
    @XmlElement(name = "String")
    protected Object string;
    @XmlElement(name = "Enumeration")
    protected Fmi2SimpleType.Enumeration enumeration;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;

    /**
     * Gets the value of the real property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2SimpleType.Real }
     *     
     */
    public Fmi2SimpleType.Real getReal() {
        return real;
    }

    /**
     * Sets the value of the real property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2SimpleType.Real }
     *     
     */
    public void setReal(Fmi2SimpleType.Real value) {
        this.real = value;
    }

    /**
     * Gets the value of the integer property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2SimpleType.Integer }
     *     
     */
    public Fmi2SimpleType.Integer getInteger() {
        return integer;
    }

    /**
     * Sets the value of the integer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2SimpleType.Integer }
     *     
     */
    public void setInteger(Fmi2SimpleType.Integer value) {
        this.integer = value;
    }

    /**
     * Gets the value of the boolean property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getBoolean() {
        return _boolean;
    }

    /**
     * Sets the value of the boolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setBoolean(Object value) {
        this._boolean = value;
    }

    /**
     * Gets the value of the string property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getString() {
        return string;
    }

    /**
     * Sets the value of the string property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setString(Object value) {
        this.string = value;
    }

    /**
     * Gets the value of the enumeration property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2SimpleType.Enumeration }
     *     
     */
    public Fmi2SimpleType.Enumeration getEnumeration() {
        return enumeration;
    }

    /**
     * Sets the value of the enumeration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2SimpleType.Enumeration }
     *     
     */
    public void setEnumeration(Fmi2SimpleType.Enumeration value) {
        this.enumeration = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="Item">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                 &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="quantity" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "item"
    })
    public static class Enumeration {

        @XmlElement(name = "Item", required = true)
        protected List<Fmi2SimpleType.Enumeration.Item> item;
        @XmlAttribute(name = "quantity")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String quantity;

        /**
         * Gets the value of the item property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the item property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Fmi2SimpleType.Enumeration.Item }
         * 
         * 
         */
        public List<Fmi2SimpleType.Enumeration.Item> getItem() {
            if (item == null) {
                item = new ArrayList<Fmi2SimpleType.Enumeration.Item>();
            }
            return this.item;
        }

        /**
         * Gets the value of the quantity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQuantity() {
            return quantity;
        }

        /**
         * Sets the value of the quantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQuantity(String value) {
            this.quantity = value;
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
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
         *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
         *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Item {

            @XmlAttribute(name = "name", required = true)
            @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
            @XmlSchemaType(name = "normalizedString")
            protected String name;
            @XmlAttribute(name = "value", required = true)
            protected int value;
            @XmlAttribute(name = "description")
            protected String description;

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the value property.
             * 
             */
            public int getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             */
            public void setValue(int value) {
                this.value = value;
            }

            /**
             * Gets the value of the description property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescription() {
                return description;
            }

            /**
             * Sets the value of the description property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDescription(String value) {
                this.description = value;
            }

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

        @XmlAttribute(name = "quantity")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String quantity;
        @XmlAttribute(name = "min")
        protected java.lang.Integer min;
        @XmlAttribute(name = "max")
        protected java.lang.Integer max;

        /**
         * Gets the value of the quantity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQuantity() {
            return quantity;
        }

        /**
         * Sets the value of the quantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQuantity(String value) {
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

        @XmlAttribute(name = "quantity")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String quantity;
        @XmlAttribute(name = "unit")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String unit;
        @XmlAttribute(name = "displayUnit")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String displayUnit;
        @XmlAttribute(name = "relativeQuantity")
        protected Boolean relativeQuantity;
        @XmlAttribute(name = "min")
        protected Double min;
        @XmlAttribute(name = "max")
        protected Double max;
        @XmlAttribute(name = "nominal")
        protected Double nominal;
        @XmlAttribute(name = "unbounded")
        protected Boolean unbounded;

        /**
         * Gets the value of the quantity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQuantity() {
            return quantity;
        }

        /**
         * Sets the value of the quantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQuantity(String value) {
            this.quantity = value;
        }

        /**
         * Gets the value of the unit property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUnit() {
            return unit;
        }

        /**
         * Sets the value of the unit property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUnit(String value) {
            this.unit = value;
        }

        /**
         * Gets the value of the displayUnit property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDisplayUnit() {
            return displayUnit;
        }

        /**
         * Sets the value of the displayUnit property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDisplayUnit(String value) {
            this.displayUnit = value;
        }

        /**
         * Gets the value of the relativeQuantity property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
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
         *     {@link Boolean }
         *     
         */
        public void setRelativeQuantity(Boolean value) {
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
         *     {@link Boolean }
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
         *     {@link Boolean }
         *     
         */
        public void setUnbounded(Boolean value) {
            this.unbounded = value;
        }

    }

}
