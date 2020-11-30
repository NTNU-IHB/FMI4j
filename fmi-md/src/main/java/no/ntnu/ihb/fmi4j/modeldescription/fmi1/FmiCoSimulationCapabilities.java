
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import javax.xml.bind.annotation.*;


/**
 * List of capability flags that an FMI co-simulation interface can provide
 *
 *
 * <p>Java class for fmiCoSimulationCapabilities complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="fmiCoSimulationCapabilities">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="canHandleVariableCommunicationStepSize" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="canHandleEvents" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="canRejectSteps" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="canInterpolateInputs" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="maxOutputDerivativeOrder" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" default="0" />
 *       &lt;attribute name="canRunAsynchronuously" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="canSignalEvents" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="canBeInstantiatedOnlyOncePerProcess" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="canNotUseMemoryManagementFunctions" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fmiCoSimulationCapabilities")
public class FmiCoSimulationCapabilities {

    @XmlAttribute(name = "canHandleVariableCommunicationStepSize")
    protected Boolean canHandleVariableCommunicationStepSize;
    @XmlAttribute(name = "canHandleEvents")
    protected Boolean canHandleEvents;
    @XmlAttribute(name = "canRejectSteps")
    protected Boolean canRejectSteps;
    @XmlAttribute(name = "canInterpolateInputs")
    protected Boolean canInterpolateInputs;
    @XmlAttribute(name = "maxOutputDerivativeOrder")
    @XmlSchemaType(name = "unsignedInt")
    protected Long maxOutputDerivativeOrder;
    @XmlAttribute(name = "canRunAsynchronuously")
    protected Boolean canRunAsynchronuously;
    @XmlAttribute(name = "canSignalEvents")
    protected Boolean canSignalEvents;
    @XmlAttribute(name = "canBeInstantiatedOnlyOncePerProcess")
    protected Boolean canBeInstantiatedOnlyOncePerProcess;
    @XmlAttribute(name = "canNotUseMemoryManagementFunctions")
    protected Boolean canNotUseMemoryManagementFunctions;

    /**
     * Gets the value of the canHandleVariableCommunicationStepSize property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isCanHandleVariableCommunicationStepSize() {
        if (canHandleVariableCommunicationStepSize == null) {
            return false;
        } else {
            return canHandleVariableCommunicationStepSize;
        }
    }

    /**
     * Sets the value of the canHandleVariableCommunicationStepSize property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCanHandleVariableCommunicationStepSize(Boolean value) {
        this.canHandleVariableCommunicationStepSize = value;
    }

    /**
     * Gets the value of the canHandleEvents property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isCanHandleEvents() {
        if (canHandleEvents == null) {
            return false;
        } else {
            return canHandleEvents;
        }
    }

    /**
     * Sets the value of the canHandleEvents property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCanHandleEvents(Boolean value) {
        this.canHandleEvents = value;
    }

    /**
     * Gets the value of the canRejectSteps property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isCanRejectSteps() {
        if (canRejectSteps == null) {
            return false;
        } else {
            return canRejectSteps;
        }
    }

    /**
     * Sets the value of the canRejectSteps property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCanRejectSteps(Boolean value) {
        this.canRejectSteps = value;
    }

    /**
     * Gets the value of the canInterpolateInputs property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isCanInterpolateInputs() {
        if (canInterpolateInputs == null) {
            return false;
        } else {
            return canInterpolateInputs;
        }
    }

    /**
     * Sets the value of the canInterpolateInputs property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCanInterpolateInputs(Boolean value) {
        this.canInterpolateInputs = value;
    }

    /**
     * Gets the value of the maxOutputDerivativeOrder property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public long getMaxOutputDerivativeOrder() {
        if (maxOutputDerivativeOrder == null) {
            return  0L;
        } else {
            return maxOutputDerivativeOrder;
        }
    }

    /**
     * Sets the value of the maxOutputDerivativeOrder property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setMaxOutputDerivativeOrder(Long value) {
        this.maxOutputDerivativeOrder = value;
    }

    /**
     * Gets the value of the canRunAsynchronuously property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isCanRunAsynchronuously() {
        if (canRunAsynchronuously == null) {
            return false;
        } else {
            return canRunAsynchronuously;
        }
    }

    /**
     * Sets the value of the canRunAsynchronuously property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCanRunAsynchronuously(Boolean value) {
        this.canRunAsynchronuously = value;
    }

    /**
     * Gets the value of the canSignalEvents property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isCanSignalEvents() {
        if (canSignalEvents == null) {
            return false;
        } else {
            return canSignalEvents;
        }
    }

    /**
     * Sets the value of the canSignalEvents property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCanSignalEvents(Boolean value) {
        this.canSignalEvents = value;
    }

    /**
     * Gets the value of the canBeInstantiatedOnlyOncePerProcess property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isCanBeInstantiatedOnlyOncePerProcess() {
        if (canBeInstantiatedOnlyOncePerProcess == null) {
            return false;
        } else {
            return canBeInstantiatedOnlyOncePerProcess;
        }
    }

    /**
     * Sets the value of the canBeInstantiatedOnlyOncePerProcess property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCanBeInstantiatedOnlyOncePerProcess(Boolean value) {
        this.canBeInstantiatedOnlyOncePerProcess = value;
    }

    /**
     * Gets the value of the canNotUseMemoryManagementFunctions property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isCanNotUseMemoryManagementFunctions() {
        if (canNotUseMemoryManagementFunctions == null) {
            return false;
        } else {
            return canNotUseMemoryManagementFunctions;
        }
    }

    /**
     * Sets the value of the canNotUseMemoryManagementFunctions property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCanNotUseMemoryManagementFunctions(Boolean value) {
        this.canNotUseMemoryManagementFunctions = value;
    }

}
