
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;


public class FmiCoSimulationModel {

    @JsonProperty(value = "File")
    @JacksonXmlElementWrapper(useWrapping = false)
    protected List<FmiCoSimulationModel.File> file;
    protected String entryPoint;
    protected Boolean manualStart;
    protected String type;

    /**
     * Gets the value of the file property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the file property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FmiCoSimulationModel.File }
     * 
     * 
     */
    public List<FmiCoSimulationModel.File> getFile() {
        if (file == null) {
            file = new ArrayList<FmiCoSimulationModel.File>();
        }
        return this.file;
    }

    /**
     * Gets the value of the entryPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryPoint() {
        return entryPoint;
    }

    /**
     * Sets the value of the entryPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryPoint(String value) {
        this.entryPoint = value;
    }

    /**
     * Gets the value of the manualStart property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isManualStart() {
        if (manualStart == null) {
            return false;
        } else {
            return manualStart;
        }
    }

    /**
     * Sets the value of the manualStart property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setManualStart(Boolean value) {
        this.manualStart = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }


    public static class File {

        protected String file;

        /**
         * Gets the value of the file property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFile() {
            return file;
        }

        /**
         * Sets the value of the file property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFile(String value) {
            this.file = value;
        }

    }

}
