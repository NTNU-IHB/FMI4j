
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;


public class FmiBaseUnit {

    @JsonProperty(value = "DisplayUnitDefinition")
    @JacksonXmlElementWrapper(useWrapping = false)
    protected List<FmiBaseUnit.DisplayUnitDefinition> displayUnitDefinition;
    protected String unit;

    /**
     * Gets the value of the displayUnitDefinition property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the displayUnitDefinition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisplayUnitDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FmiBaseUnit.DisplayUnitDefinition }
     * 
     * 
     */
    public List<FmiBaseUnit.DisplayUnitDefinition> getDisplayUnitDefinition() {
        if (displayUnitDefinition == null) {
            displayUnitDefinition = new ArrayList<FmiBaseUnit.DisplayUnitDefinition>();
        }
        return this.displayUnitDefinition;
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


    public static class DisplayUnitDefinition {

        protected String displayUnit;
        protected Double gain;
        protected Double offset;

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
         * Gets the value of the gain property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public double getGain() {
            if (gain == null) {
                return  1.0D;
            } else {
                return gain;
            }
        }

        /**
         * Sets the value of the gain property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setGain(Double value) {
            this.gain = value;
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
