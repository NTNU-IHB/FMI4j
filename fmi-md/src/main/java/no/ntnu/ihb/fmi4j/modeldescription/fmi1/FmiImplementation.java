
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * XML structures related to the FMI co-simulation interface.
 *
 * <p>Java class for fmiImplementation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="fmiImplementation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="CoSimulation_StandAlone">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Capabilities" type="{}fmiCoSimulationCapabilities"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CoSimulation_Tool">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Capabilities" type="{}fmiCoSimulationCapabilities"/>
 *                   &lt;element name="Model" type="{}fmiCoSimulationModel"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fmiImplementation", propOrder = {
        "coSimulationStandAlone",
        "coSimulationTool"
})
public class FmiImplementation {

    @XmlElement(name = "CoSimulation_StandAlone")
    protected FmiImplementation.CoSimulationStandAlone coSimulationStandAlone;
    @XmlElement(name = "CoSimulation_Tool")
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


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Capabilities" type="{}fmiCoSimulationCapabilities"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "capabilities"
    })
    public static class CoSimulationStandAlone {

        @XmlElement(name = "Capabilities", required = true)
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


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Capabilities" type="{}fmiCoSimulationCapabilities"/>
     *         &lt;element name="Model" type="{}fmiCoSimulationModel"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "capabilities",
            "model"
    })
    public static class CoSimulationTool {

        @XmlElement(name = "Capabilities", required = true)
        protected FmiCoSimulationCapabilities capabilities;
        @XmlElement(name = "Model", required = true)
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
