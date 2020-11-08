
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;


public class FmiType {

    @JsonProperty(value = "RealType")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected FmiType.RealType realType;
    @JsonProperty(value = "IntegerType")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected FmiType.IntegerType integerType;
    @JsonProperty(value = "BooleanType")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected Object booleanType;
    @JsonProperty(value = "StringType")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected Object stringType;
    @JsonProperty(value = "EnumerationType")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    protected FmiType.EnumerationType enumerationType;

    protected String name;
    protected String description;

    /**
     * Gets the value of the realType property.
     * 
     * @return
     *     possible object is
     *     {@link FmiType.RealType }
     *     
     */
    public FmiType.RealType getRealType() {
        return realType;
    }

    /**
     * Sets the value of the realType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiType.RealType }
     *     
     */
    public void setRealType(FmiType.RealType value) {
        this.realType = value;
    }

    /**
     * Gets the value of the integerType property.
     * 
     * @return
     *     possible object is
     *     {@link FmiType.IntegerType }
     *     
     */
    public FmiType.IntegerType getIntegerType() {
        return integerType;
    }

    /**
     * Sets the value of the integerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiType.IntegerType }
     *     
     */
    public void setIntegerType(FmiType.IntegerType value) {
        this.integerType = value;
    }

    /**
     * Gets the value of the booleanType property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getBooleanType() {
        return booleanType;
    }

    /**
     * Sets the value of the booleanType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setBooleanType(Object value) {
        this.booleanType = value;
    }

    /**
     * Gets the value of the stringType property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getStringType() {
        return stringType;
    }

    /**
     * Sets the value of the stringType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setStringType(Object value) {
        this.stringType = value;
    }

    /**
     * Gets the value of the enumerationType property.
     * 
     * @return
     *     possible object is
     *     {@link FmiType.EnumerationType }
     *     
     */
    public FmiType.EnumerationType getEnumerationType() {
        return enumerationType;
    }

    /**
     * Sets the value of the enumerationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiType.EnumerationType }
     *     
     */
    public void setEnumerationType(FmiType.EnumerationType value) {
        this.enumerationType = value;
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


    public static class EnumerationType {

        @JsonProperty(value = "Item")
        @JacksonXmlElementWrapper(useWrapping = false)
        protected List<FmiType.EnumerationType.Item> item;
        protected String quantity;
        protected Integer min;
        protected Integer max;

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
         * {@link FmiType.EnumerationType.Item }
         * 
         * 
         */
        public List<FmiType.EnumerationType.Item> getItem() {
            if (item == null) {
                item = new ArrayList<FmiType.EnumerationType.Item>();
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
         * Gets the value of the min property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getMin() {
            return min;
        }

        /**
         * Sets the value of the min property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setMin(Integer value) {
            this.min = value;
        }

        /**
         * Gets the value of the max property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getMax() {
            return max;
        }

        /**
         * Sets the value of the max property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setMax(Integer value) {
            this.max = value;
        }


        public static class Item {

            protected String name;
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


    public static class IntegerType {

        protected String quantity;
        protected Integer min;
        protected Integer max;

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
         *     {@link Integer }
         *     
         */
        public Integer getMin() {
            return min;
        }

        /**
         * Sets the value of the min property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setMin(Integer value) {
            this.min = value;
        }

        /**
         * Gets the value of the max property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getMax() {
            return max;
        }

        /**
         * Sets the value of the max property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setMax(Integer value) {
            this.max = value;
        }

    }

    public static class RealType {

        protected String quantity;
        protected String unit;
        protected String displayUnit;
        protected Boolean relativeQuantity;
        protected Double min;
        protected Double max;
        protected Double nominal;

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

    }

}
