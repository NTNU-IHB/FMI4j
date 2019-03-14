
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
 * Unit definition (with respect to SI base units) and default display units
 * 
 * <p>Java class for fmi2Unit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fmi2Unit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BaseUnit" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="kg" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="m" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="s" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="A" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="K" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="mol" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="cd" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="rad" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="factor" type="{http://www.w3.org/2001/XMLSchema}double" default="1" />
 *                 &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="DisplayUnit">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="factor" type="{http://www.w3.org/2001/XMLSchema}double" default="1" />
 *                   &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fmi2Unit", propOrder = {
    "baseUnit",
    "displayUnit"
})
public class Fmi2Unit {

    @XmlElement(name = "BaseUnit")
    protected Fmi2Unit.BaseUnit baseUnit;
    @XmlElement(name = "DisplayUnit")
    protected List<Fmi2Unit.DisplayUnit> displayUnit;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String name;

    /**
     * Gets the value of the baseUnit property.
     * 
     * @return
     *     possible object is
     *     {@link Fmi2Unit.BaseUnit }
     *     
     */
    public Fmi2Unit.BaseUnit getBaseUnit() {
        return baseUnit;
    }

    /**
     * Sets the value of the baseUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2Unit.BaseUnit }
     *     
     */
    public void setBaseUnit(Fmi2Unit.BaseUnit value) {
        this.baseUnit = value;
    }

    /**
     * Gets the value of the displayUnit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the displayUnit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisplayUnit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Fmi2Unit.DisplayUnit }
     * 
     * 
     */
    public List<Fmi2Unit.DisplayUnit> getDisplayUnit() {
        if (displayUnit == null) {
            displayUnit = new ArrayList<Fmi2Unit.DisplayUnit>();
        }
        return this.displayUnit;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="kg" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="m" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="s" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="A" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="K" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="mol" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="cd" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="rad" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="factor" type="{http://www.w3.org/2001/XMLSchema}double" default="1" />
     *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class BaseUnit {

        @XmlAttribute(name = "kg")
        protected Integer kg;
        @XmlAttribute(name = "m")
        protected Integer m;
        @XmlAttribute(name = "s")
        protected Integer s;
        @XmlAttribute(name = "A")
        protected Integer a;
        @XmlAttribute(name = "K")
        protected Integer k;
        @XmlAttribute(name = "mol")
        protected Integer mol;
        @XmlAttribute(name = "cd")
        protected Integer cd;
        @XmlAttribute(name = "rad")
        protected Integer rad;
        @XmlAttribute(name = "factor")
        protected Double factor;
        @XmlAttribute(name = "offset")
        protected Double offset;

        /**
         * Gets the value of the kg property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getKg() {
            if (kg == null) {
                return  0;
            } else {
                return kg;
            }
        }

        /**
         * Sets the value of the kg property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setKg(Integer value) {
            this.kg = value;
        }

        /**
         * Gets the value of the m property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getM() {
            if (m == null) {
                return  0;
            } else {
                return m;
            }
        }

        /**
         * Sets the value of the m property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setM(Integer value) {
            this.m = value;
        }

        /**
         * Gets the value of the s property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getS() {
            if (s == null) {
                return  0;
            } else {
                return s;
            }
        }

        /**
         * Sets the value of the s property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setS(Integer value) {
            this.s = value;
        }

        /**
         * Gets the value of the a property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getA() {
            if (a == null) {
                return  0;
            } else {
                return a;
            }
        }

        /**
         * Sets the value of the a property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setA(Integer value) {
            this.a = value;
        }

        /**
         * Gets the value of the k property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getK() {
            if (k == null) {
                return  0;
            } else {
                return k;
            }
        }

        /**
         * Sets the value of the k property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setK(Integer value) {
            this.k = value;
        }

        /**
         * Gets the value of the mol property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getMol() {
            if (mol == null) {
                return  0;
            } else {
                return mol;
            }
        }

        /**
         * Sets the value of the mol property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setMol(Integer value) {
            this.mol = value;
        }

        /**
         * Gets the value of the cd property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getCd() {
            if (cd == null) {
                return  0;
            } else {
                return cd;
            }
        }

        /**
         * Sets the value of the cd property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCd(Integer value) {
            this.cd = value;
        }

        /**
         * Gets the value of the rad property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getRad() {
            if (rad == null) {
                return  0;
            } else {
                return rad;
            }
        }

        /**
         * Sets the value of the rad property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setRad(Integer value) {
            this.rad = value;
        }

        /**
         * Gets the value of the factor property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public double getFactor() {
            if (factor == null) {
                return  1.0D;
            } else {
                return factor;
            }
        }

        /**
         * Sets the value of the factor property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setFactor(Double value) {
            this.factor = value;
        }

        /**
         * Gets the value of the offset property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public double getOffset() {
            if (offset == null) {
                return  0.0D;
            } else {
                return offset;
            }
        }

        /**
         * Sets the value of the offset property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setOffset(Double value) {
            this.offset = value;
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
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="factor" type="{http://www.w3.org/2001/XMLSchema}double" default="1" />
     *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DisplayUnit {

        @XmlAttribute(name = "name", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String name;
        @XmlAttribute(name = "factor")
        protected Double factor;
        @XmlAttribute(name = "offset")
        protected Double offset;

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
         * Gets the value of the factor property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public double getFactor() {
            if (factor == null) {
                return  1.0D;
            } else {
                return factor;
            }
        }

        /**
         * Sets the value of the factor property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setFactor(Double value) {
            this.factor = value;
        }

        /**
         * Gets the value of the offset property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public double getOffset() {
            if (offset == null) {
                return  0.0D;
            } else {
                return offset;
            }
        }

        /**
         * Sets the value of the offset property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setOffset(Double value) {
            this.offset = value;
        }

    }

}
