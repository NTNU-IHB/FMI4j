
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;


public class FmiCoSimulationCapabilities {

    protected Boolean canHandleVariableCommunicationStepSize;
    protected Boolean canHandleEvents;
    protected Boolean canRejectSteps;
    protected Boolean canInterpolateInputs;
    protected Long maxOutputDerivativeOrder;
    protected Boolean canRunAsynchronuously;
    protected Boolean canSignalEvents;
    protected Boolean canBeInstantiatedOnlyOncePerProcess;
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
