
package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;


public class Fmi2Unit {

    @JsonProperty(value = "BaseUnit")
    protected Fmi2Unit.BaseUnit baseUnit;
    @JsonProperty(value = "DisplayUnit")
    @JacksonXmlElementWrapper(useWrapping = false)
    protected List<Fmi2Unit.DisplayUnit> displayUnit;
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


    public static class BaseUnit {

        protected Integer kg;
        protected Integer m;
        protected Integer s;
        protected Integer a;
        protected Integer k;
        protected Integer mol;
        protected Integer cd;
        protected Integer rad;
        protected Double factor;
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

    public static class DisplayUnit {

        protected String name;
        protected Double factor;
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
