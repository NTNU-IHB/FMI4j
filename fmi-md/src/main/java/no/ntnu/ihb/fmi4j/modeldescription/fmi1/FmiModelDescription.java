
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class FmiModelDescription {

    @JsonProperty(value = "UnitDefinitions")
    protected FmiModelDescription.UnitDefinitions unitDefinitions;
    @JsonProperty(value = "TypeDefinitions")
    protected FmiModelDescription.TypeDefinitions typeDefinitions;
    @JsonProperty(value = "DefaultExperiment")
    protected FmiModelDescription.DefaultExperiment defaultExperiment;
    @JsonProperty(value = "VendorAnnotations")
    protected FmiModelDescription.VendorAnnotations vendorAnnotations;
    @JsonProperty(value = "ModelVariables")
    protected FmiModelDescription.ModelVariables modelVariables;
    @JsonProperty(value = "Implementation")
    protected FmiImplementation implementation;

    protected String fmiVersion;
    protected String modelName;
    protected String modelIdentifier;
    protected String guid;
    protected String description;
    protected String author;
    protected String version;
    protected String generationTool;
    protected String generationDateAndTime;
    protected String variableNamingConvention;
    protected long numberOfContinuousStates;
    protected long numberOfEventIndicators;

    /**
     * Gets the value of the unitDefinitions property.
     * 
     * @return
     *     possible object is
     *     {@link FmiModelDescription.UnitDefinitions }
     *     
     */
    public FmiModelDescription.UnitDefinitions getUnitDefinitions() {
        return unitDefinitions;
    }

    /**
     * Sets the value of the unitDefinitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiModelDescription.UnitDefinitions }
     *     
     */
    public void setUnitDefinitions(FmiModelDescription.UnitDefinitions value) {
        this.unitDefinitions = value;
    }

    /**
     * Gets the value of the typeDefinitions property.
     * 
     * @return
     *     possible object is
     *     {@link FmiModelDescription.TypeDefinitions }
     *     
     */
    public FmiModelDescription.TypeDefinitions getTypeDefinitions() {
        return typeDefinitions;
    }

    /**
     * Sets the value of the typeDefinitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiModelDescription.TypeDefinitions }
     *     
     */
    public void setTypeDefinitions(FmiModelDescription.TypeDefinitions value) {
        this.typeDefinitions = value;
    }

    /**
     * Gets the value of the defaultExperiment property.
     * 
     * @return
     *     possible object is
     *     {@link FmiModelDescription.DefaultExperiment }
     *     
     */
    public FmiModelDescription.DefaultExperiment getDefaultExperiment() {
        return defaultExperiment;
    }

    /**
     * Sets the value of the defaultExperiment property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiModelDescription.DefaultExperiment }
     *     
     */
    public void setDefaultExperiment(FmiModelDescription.DefaultExperiment value) {
        this.defaultExperiment = value;
    }

    /**
     * Gets the value of the vendorAnnotations property.
     * 
     * @return
     *     possible object is
     *     {@link FmiModelDescription.VendorAnnotations }
     *     
     */
    public FmiModelDescription.VendorAnnotations getVendorAnnotations() {
        return vendorAnnotations;
    }

    /**
     * Sets the value of the vendorAnnotations property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiModelDescription.VendorAnnotations }
     *     
     */
    public void setVendorAnnotations(FmiModelDescription.VendorAnnotations value) {
        this.vendorAnnotations = value;
    }

    /**
     * Gets the value of the modelVariables property.
     * 
     * @return
     *     possible object is
     *     {@link FmiModelDescription.ModelVariables }
     *     
     */
    public FmiModelDescription.ModelVariables getModelVariables() {
        return modelVariables;
    }

    /**
     * Sets the value of the modelVariables property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiModelDescription.ModelVariables }
     *     
     */
    public void setModelVariables(FmiModelDescription.ModelVariables value) {
        this.modelVariables = value;
    }

    /**
     * Gets the value of the implementation property.
     * 
     * @return
     *     possible object is
     *     {@link FmiImplementation }
     *     
     */
    public FmiImplementation getImplementation() {
        return implementation;
    }

    /**
     * Sets the value of the implementation property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiImplementation }
     *     
     */
    public void setImplementation(FmiImplementation value) {
        this.implementation = value;
    }

    /**
     * Gets the value of the fmiVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFmiVersion() {
        if (fmiVersion == null) {
            return "1.0";
        } else {
            return fmiVersion;
        }
    }

    /**
     * Sets the value of the fmiVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFmiVersion(String value) {
        this.fmiVersion = value;
    }

    /**
     * Gets the value of the modelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Sets the value of the modelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelName(String value) {
        this.modelName = value;
    }

    /**
     * Gets the value of the modelIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelIdentifier() {
        return modelIdentifier;
    }

    /**
     * Sets the value of the modelIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelIdentifier(String value) {
        this.modelIdentifier = value;
    }

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
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
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the generationTool property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenerationTool() {
        return generationTool;
    }

    /**
     * Sets the value of the generationTool property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenerationTool(String value) {
        this.generationTool = value;
    }

    public String getGenerationDateAndTime() {
        return generationDateAndTime;
    }

    public void setGenerationDateAndTime(String value) {
        this.generationDateAndTime = value;
    }

    /**
     * Gets the value of the variableNamingConvention property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariableNamingConvention() {
        if (variableNamingConvention == null) {
            return "flat";
        } else {
            return variableNamingConvention;
        }
    }

    /**
     * Sets the value of the variableNamingConvention property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariableNamingConvention(String value) {
        this.variableNamingConvention = value;
    }

    /**
     * Gets the value of the numberOfContinuousStates property.
     * 
     */
    public long getNumberOfContinuousStates() {
        return numberOfContinuousStates;
    }

    /**
     * Sets the value of the numberOfContinuousStates property.
     * 
     */
    public void setNumberOfContinuousStates(long value) {
        this.numberOfContinuousStates = value;
    }

    /**
     * Gets the value of the numberOfEventIndicators property.
     * 
     */
    public long getNumberOfEventIndicators() {
        return numberOfEventIndicators;
    }

    /**
     * Sets the value of the numberOfEventIndicators property.
     * 
     */
    public void setNumberOfEventIndicators(long value) {
        this.numberOfEventIndicators = value;
    }


    public static class DefaultExperiment {

        protected Double startTime;
        protected Double stopTime;
        protected Double tolerance;

        /**
         * Gets the value of the startTime property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getStartTime() {
            return startTime;
        }

        /**
         * Sets the value of the startTime property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setStartTime(Double value) {
            this.startTime = value;
        }

        /**
         * Gets the value of the stopTime property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getStopTime() {
            return stopTime;
        }

        /**
         * Sets the value of the stopTime property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setStopTime(Double value) {
            this.stopTime = value;
        }

        /**
         * Gets the value of the tolerance property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getTolerance() {
            return tolerance;
        }

        /**
         * Sets the value of the tolerance property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setTolerance(Double value) {
            this.tolerance = value;
        }

    }


    public static class ModelVariables {

        @JsonProperty(value = "ScalarVariable")
        @JacksonXmlElementWrapper(useWrapping = false)
        protected List<FmiScalarVariable> scalarVariable;

        /**
         * Gets the value of the scalarVariable property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the scalarVariable property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getScalarVariable().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FmiScalarVariable }
         * 
         * 
         */
        public List<FmiScalarVariable> getScalarVariable() {
            if (scalarVariable == null) {
                scalarVariable = new ArrayList<FmiScalarVariable>();
            }
            return this.scalarVariable;
        }

    }


    public static class TypeDefinitions {

        @JsonProperty(value = "Type")
        @JacksonXmlElementWrapper(useWrapping = false)
        protected List<FmiType> type;

        /**
         * Gets the value of the type property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the type property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FmiType }
         * 
         * 
         */
        public List<FmiType> getType() {
            if (type == null) {
                type = new ArrayList<FmiType>();
            }
            return this.type;
        }

    }


    public static class UnitDefinitions {

        @JsonProperty(value = "BaseUnit")
        @JacksonXmlElementWrapper(useWrapping = false)
        protected List<FmiBaseUnit> baseUnit;

        /**
         * Gets the value of the baseUnit property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the baseUnit property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBaseUnit().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FmiBaseUnit }
         * 
         * 
         */
        public List<FmiBaseUnit> getBaseUnit() {
            if (baseUnit == null) {
                baseUnit = new ArrayList<>();
            }
            return this.baseUnit;
        }

    }


    public static class VendorAnnotations {

        @JsonProperty(value = "Tool")
        @JacksonXmlElementWrapper(useWrapping = false)
        protected List<FmiModelDescription.VendorAnnotations.Tool> tool;

        /**
         * Gets the value of the tool property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the tool property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTool().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FmiModelDescription.VendorAnnotations.Tool }
         * 
         * 
         */
        public List<FmiModelDescription.VendorAnnotations.Tool> getTool() {
            if (tool == null) {
                tool = new ArrayList<FmiModelDescription.VendorAnnotations.Tool>();
            }
            return this.tool;
        }


        public static class Tool {

            @JsonProperty(value = "Annotation")
            protected List<FmiModelDescription.VendorAnnotations.Tool.Annotation> annotation;
            protected String name;

            /**
             * Gets the value of the annotation property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the annotation property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAnnotation().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FmiModelDescription.VendorAnnotations.Tool.Annotation }
             * 
             * 
             */
            public List<FmiModelDescription.VendorAnnotations.Tool.Annotation> getAnnotation() {
                if (annotation == null) {
                    annotation = new ArrayList<>();
                }
                return this.annotation;
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


            public static class Annotation {

                protected String name;
                protected String value;

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
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

            }

        }

    }

}
