
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import com.fasterxml.jackson.annotation.JsonProperty;


public class FmiImplementation {

    @JsonProperty(value = "CoSimulation_StandAlone")
    protected FmiImplementation.CoSimulationStandAlone coSimulationStandAlone;
    @JsonProperty(value = "CoSimulation_Tool")
    protected FmiImplementation.CoSimulationTool coSimulationTool;

    /**
     * Gets the value of the coSimulationStandAlone property.
     * 
     * @return
     *     possible object is
     *     {@link FmiImplementation.CoSimulationStandAlone }
     *     
     */
    public FmiImplementation.CoSimulationStandAlone getCoSimulationStandAlone() {
        return coSimulationStandAlone;
    }

    /**
     * Sets the value of the coSimulationStandAlone property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiImplementation.CoSimulationStandAlone }
     *     
     */
    public void setCoSimulationStandAlone(FmiImplementation.CoSimulationStandAlone value) {
        this.coSimulationStandAlone = value;
    }

    /**
     * Gets the value of the coSimulationTool property.
     * 
     * @return
     *     possible object is
     *     {@link FmiImplementation.CoSimulationTool }
     *     
     */
    public FmiImplementation.CoSimulationTool getCoSimulationTool() {
        return coSimulationTool;
    }

    /**
     * Sets the value of the coSimulationTool property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiImplementation.CoSimulationTool }
     *     
     */
    public void setCoSimulationTool(FmiImplementation.CoSimulationTool value) {
        this.coSimulationTool = value;
    }


    public static class CoSimulationStandAlone {

        @JsonProperty(value = "Capabilities", required = true)
        protected FmiCoSimulationCapabilities capabilities;

        /**
         * Gets the value of the capabilities property.
         * 
         * @return
         *     possible object is
         *     {@link FmiCoSimulationCapabilities }
         *     
         */
        public FmiCoSimulationCapabilities getCapabilities() {
            return capabilities;
        }

        /**
         * Sets the value of the capabilities property.
         * 
         * @param value
         *     allowed object is
         *     {@link FmiCoSimulationCapabilities }
         *     
         */
        public void setCapabilities(FmiCoSimulationCapabilities value) {
            this.capabilities = value;
        }

    }


    public static class CoSimulationTool {

        @JsonProperty(value = "Capabilities", required = true)
        protected FmiCoSimulationCapabilities capabilities;
        @JsonProperty(value = "Model", required = true)
        protected FmiCoSimulationModel model;

        /**
         * Gets the value of the capabilities property.
         * 
         * @return
         *     possible object is
         *     {@link FmiCoSimulationCapabilities }
         *     
         */
        public FmiCoSimulationCapabilities getCapabilities() {
            return capabilities;
        }

        /**
         * Sets the value of the capabilities property.
         * 
         * @param value
         *     allowed object is
         *     {@link FmiCoSimulationCapabilities }
         *     
         */
        public void setCapabilities(FmiCoSimulationCapabilities value) {
            this.capabilities = value;
        }

        /**
         * Gets the value of the model property.
         * 
         * @return
         *     possible object is
         *     {@link FmiCoSimulationModel }
         *     
         */
        public FmiCoSimulationModel getModel() {
            return model;
        }

        /**
         * Sets the value of the model property.
         * 
         * @param value
         *     allowed object is
         *     {@link FmiCoSimulationModel }
         *     
         */
        public void setModel(FmiCoSimulationModel value) {
            this.model = value;
        }

    }

}
