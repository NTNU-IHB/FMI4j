
package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonRootName("fmiModelDescription")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fmi2ModelDescription {

    @JsonProperty(value = "ModelExchange")
    protected Fmi2ModelDescription.ModelExchange modelExchange;
    @JsonProperty(value = "CoSimulation")
    protected Fmi2ModelDescription.CoSimulation coSimulation;
    @JsonProperty(value = "UnitDefinitions")
    protected Fmi2ModelDescription.UnitDefinitions unitDefinitions;
    @JsonProperty(value = "TypeDefinitions")
    protected Fmi2ModelDescription.TypeDefinitions typeDefinitions;
    @JsonProperty(value = "LogCategories")
    protected Fmi2ModelDescription.LogCategories logCategories;
    @JsonProperty(value = "DefaultExperiment")
    protected Fmi2ModelDescription.DefaultExperiment defaultExperiment;
    @JsonProperty(value = "VendorAnnotations")
    protected Fmi2Annotation vendorAnnotations;
    @JsonProperty(value = "ModelVariables", required = true)
    protected Fmi2ModelDescription.ModelVariables modelVariables;
    @JsonProperty(value = "ModelStructure", required = true)
    protected Fmi2ModelDescription.ModelStructure modelStructure;

    @JacksonXmlProperty(isAttribute = true)
    protected String fmiVersion;
    @JacksonXmlProperty(isAttribute = true)
    protected String modelName;
    @JacksonXmlProperty(isAttribute = true)
    protected String guid;
    @JacksonXmlProperty(isAttribute = true)
    protected String description;
    @JacksonXmlProperty(isAttribute = true)
    protected String author;
    @JacksonXmlProperty(isAttribute = true)
    protected String version;
    @JacksonXmlProperty(isAttribute = true)
    protected String copyright;
    @JacksonXmlProperty(isAttribute = true)
    protected String license;
    @JacksonXmlProperty(isAttribute = true)
    protected String generationTool;
    @JacksonXmlProperty(isAttribute = true)
    protected String generationDateAndTime;
    @JacksonXmlProperty(isAttribute = true)
    protected String variableNamingConvention;
    @JacksonXmlProperty(isAttribute = true)
    protected Long numberOfEventIndicators;

    public void toXML(OutputStream out) throws IOException {
        new XmlMapper()
                .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .writeValue(out, this);
    }


    public ModelExchange getModelExchange() {
        return this.modelExchange;
    }

    public CoSimulation getCoSimulation() {
        return this.coSimulation;
    }

    public void setCoSimulation(CoSimulation coSimulation) {
        this.coSimulation = coSimulation;
    }

    /**
     * Gets the value of the unitDefinitions property.
     *
     * @return possible object is
     * {@link Fmi2ModelDescription.UnitDefinitions }
     */
    public Fmi2ModelDescription.UnitDefinitions getUnitDefinitions() {
        return unitDefinitions;
    }

    /**
     * Sets the value of the unitDefinitions property.
     *
     * @param value allowed object is
     *              {@link Fmi2ModelDescription.UnitDefinitions }
     */
    public void setUnitDefinitions(Fmi2ModelDescription.UnitDefinitions value) {
        this.unitDefinitions = value;
    }

    /**
     * Gets the value of the typeDefinitions property.
     *
     * @return possible object is
     * {@link Fmi2ModelDescription.TypeDefinitions }
     */
    public Fmi2ModelDescription.TypeDefinitions getTypeDefinitions() {
        return typeDefinitions;
    }

    /**
     * Sets the value of the typeDefinitions property.
     *
     * @param value allowed object is
     *              {@link Fmi2ModelDescription.TypeDefinitions }
     */
    public void setTypeDefinitions(Fmi2ModelDescription.TypeDefinitions value) {
        this.typeDefinitions = value;
    }

    /**
     * Gets the value of the logCategories property.
     *
     * @return possible object is
     * {@link Fmi2ModelDescription.LogCategories }
     */
    public Fmi2ModelDescription.LogCategories getLogCategories() {
        return logCategories;
    }

    /**
     * Sets the value of the logCategories property.
     *
     * @param value allowed object is
     *              {@link Fmi2ModelDescription.LogCategories }
     */
    public void setLogCategories(Fmi2ModelDescription.LogCategories value) {
        this.logCategories = value;
    }

    /**
     * Gets the value of the defaultExperiment property.
     *
     * @return possible object is
     * {@link Fmi2ModelDescription.DefaultExperiment }
     */
    public Fmi2ModelDescription.DefaultExperiment getDefaultExperiment() {
        return defaultExperiment;
    }

    /**
     * Sets the value of the defaultExperiment property.
     *
     * @param value allowed object is
     *              {@link Fmi2ModelDescription.DefaultExperiment }
     */
    public void setDefaultExperiment(Fmi2ModelDescription.DefaultExperiment value) {
        this.defaultExperiment = value;
    }

    /**
     * Gets the value of the vendorAnnotations property.
     *
     * @return possible object is
     * {@link Fmi2Annotation }
     */
    public Fmi2Annotation getVendorAnnotations() {
        return vendorAnnotations;
    }

    /**
     * Sets the value of the vendorAnnotations property.
     *
     * @param value allowed object is
     *              {@link Fmi2Annotation }
     */
    public void setVendorAnnotations(Fmi2Annotation value) {
        this.vendorAnnotations = value;
    }

    /**
     * Gets the value of the modelVariables property.
     *
     * @return possible object is
     * {@link Fmi2ModelDescription.ModelVariables }
     */
    public Fmi2ModelDescription.ModelVariables getModelVariables() {
        return modelVariables;
    }

    /**
     * Sets the value of the modelVariables property.
     *
     * @param value allowed object is
     *              {@link Fmi2ModelDescription.ModelVariables }
     */
    public void setModelVariables(Fmi2ModelDescription.ModelVariables value) {
        this.modelVariables = value;
    }

    /**
     * Gets the value of the modelStructure property.
     *
     * @return possible object is
     * {@link Fmi2ModelDescription.ModelStructure }
     */
    public Fmi2ModelDescription.ModelStructure getModelStructure() {
        return modelStructure;
    }

    /**
     * Sets the value of the modelStructure property.
     *
     * @param value allowed object is
     *              {@link Fmi2ModelDescription.ModelStructure }
     */
    public void setModelStructure(Fmi2ModelDescription.ModelStructure value) {
        this.modelStructure = value;
    }

    /**
     * Gets the value of the fmiVersion property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFmiVersion() {
        if (fmiVersion == null) {
            return "2.0";
        } else {
            return fmiVersion;
        }
    }

    /**
     * Sets the value of the fmiVersion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFmiVersion(String value) {
        this.fmiVersion = value;
    }

    /**
     * Gets the value of the modelName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Sets the value of the modelName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setModelName(String value) {
        this.modelName = value;
    }

    /**
     * Gets the value of the guid property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGuid(String value) {
        this.guid = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the author property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    /**
     * Gets the value of the version property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the copyright property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the value of the copyright property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCopyright(String value) {
        this.copyright = value;
    }

    /**
     * Gets the value of the license property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets the value of the license property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLicense(String value) {
        this.license = value;
    }

    /**
     * Gets the value of the generationTool property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGenerationTool() {
        return generationTool;
    }

    /**
     * Sets the value of the generationTool property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGenerationTool(String value) {
        this.generationTool = value;
    }

    /**
     * Gets the value of the generationDateAndTime property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGenerationDateAndTime() {
        return generationDateAndTime;
    }

    /**
     * Sets the value of the generationDateAndTime property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGenerationDateAndTime(String value) {
        this.generationDateAndTime = value;
    }

    /**
     * Gets the value of the variableNamingConvention property.
     *
     * @return possible object is
     * {@link String }
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
     * @param value allowed object is
     *              {@link String }
     */
    public void setVariableNamingConvention(String value) {
        this.variableNamingConvention = value;
    }

    /**
     * Gets the value of the numberOfEventIndicators property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getNumberOfEventIndicators() {
        return numberOfEventIndicators;
    }

    /**
     * Sets the value of the numberOfEventIndicators property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setNumberOfEventIndicators(Long value) {
        this.numberOfEventIndicators = value;
    }


    public static class CoSimulation {

        @JsonProperty(value = "SourceFiles")
        protected Fmi2ModelDescription.CoSimulation.SourceFiles sourceFiles;
        @JacksonXmlProperty(isAttribute = true)
        protected String modelIdentifier;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean needsExecutionTool;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canHandleVariableCommunicationStepSize;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canInterpolateInputs;
        @JacksonXmlProperty(isAttribute = true)
        protected Integer maxOutputDerivativeOrder;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canRunAsynchronuously;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canBeInstantiatedOnlyOncePerProcess;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canNotUseMemoryManagementFunctions;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canGetAndSetFMUstate;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canSerializeFMUstate;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean providesDirectionalDerivative;

        /**
         * Gets the value of the sourceFiles property.
         *
         * @return possible object is
         * {@link Fmi2ModelDescription.CoSimulation.SourceFiles }
         */
        public Fmi2ModelDescription.CoSimulation.SourceFiles getSourceFiles() {
            return sourceFiles;
        }

        /**
         * Sets the value of the sourceFiles property.
         *
         * @param value allowed object is
         *              {@link Fmi2ModelDescription.CoSimulation.SourceFiles }
         */
        public void setSourceFiles(Fmi2ModelDescription.CoSimulation.SourceFiles value) {
            this.sourceFiles = value;
        }

        /**
         * Gets the value of the modelIdentifier property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getModelIdentifier() {
            return modelIdentifier;
        }

        /**
         * Sets the value of the modelIdentifier property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setModelIdentifier(String value) {
            this.modelIdentifier = value;
        }

        /**
         * Gets the value of the needsExecutionTool property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        @JsonIgnore
        public boolean isNeedsExecutionTool() {
            if (needsExecutionTool == null) {
                return false;
            } else {
                return needsExecutionTool;
            }
        }

        /**
         * Sets the value of the needsExecutionTool property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        @JsonProperty
        public void setNeedsExecutionTool(Boolean value) {
            this.needsExecutionTool = value;
        }

        /**
         * Gets the value of the canHandleVariableCommunicationStepSize property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        @JsonIgnore
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
         * @param value allowed object is
         *              {@link Boolean }
         */
        @JsonProperty
        public void setCanHandleVariableCommunicationStepSize(Boolean value) {
            this.canHandleVariableCommunicationStepSize = value;
        }

        /**
         * Gets the value of the canInterpolateInputs property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        @JsonIgnore
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
         * @param value allowed object is
         *              {@link Boolean }
         */
        @JsonProperty
        public void setCanInterpolateInputs(Boolean value) {
            this.canInterpolateInputs = value;
        }

        @JsonIgnore
        public int getMaxOutputDerivativeOrder() {
            if (maxOutputDerivativeOrder == null) {
                return 0;
            } else {
                return maxOutputDerivativeOrder;
            }
        }

        /**
         * Sets the value of the maxOutputDerivativeOrder property.
         *
         * @param value allowed object is
         *              {@link Long }
         */
        @JsonProperty
        public void setMaxOutputDerivativeOrder(Integer value) {
            this.maxOutputDerivativeOrder = value;
        }

        /**
         * Gets the value of the canRunAsynchronuously property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        @JsonIgnore
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
         * @param value allowed object is
         *              {@link Boolean }
         */
        @JsonProperty
        public void setCanRunAsynchronuously(Boolean value) {
            this.canRunAsynchronuously = value;
        }

        /**
         * Gets the value of the canBeInstantiatedOnlyOncePerProcess property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        @JsonIgnore
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
         * @param value allowed object is
         *              {@link Boolean }
         */
        @JsonProperty
        public void setCanBeInstantiatedOnlyOncePerProcess(Boolean value) {
            this.canBeInstantiatedOnlyOncePerProcess = value;
        }

        /**
         * Gets the value of the canNotUseMemoryManagementFunctions property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        @JsonIgnore
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
         * @param value allowed object is
         *              {@link Boolean }
         */
        @JsonProperty
        public void setCanNotUseMemoryManagementFunctions(Boolean value) {
            this.canNotUseMemoryManagementFunctions = value;
        }

        /**
         * Gets the value of the canGetAndSetFMUstate property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        @JsonIgnore
        public boolean isCanGetAndSetFMUstate() {
            if (canGetAndSetFMUstate == null) {
                return false;
            } else {
                return canGetAndSetFMUstate;
            }
        }

        /**
         * Sets the value of the canGetAndSetFMUstate property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        @JsonProperty
        public void setCanGetAndSetFMUstate(Boolean value) {
            this.canGetAndSetFMUstate = value;
        }

        /**
         * Gets the value of the canSerializeFMUstate property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        @JsonIgnore
        public boolean isCanSerializeFMUstate() {
            if (canSerializeFMUstate == null) {
                return false;
            } else {
                return canSerializeFMUstate;
            }
        }

        /**
         * Sets the value of the canSerializeFMUstate property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        @JsonProperty
        public void setCanSerializeFMUstate(Boolean value) {
            this.canSerializeFMUstate = value;
        }

        /**
         * Gets the value of the providesDirectionalDerivative property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        @JsonIgnore
        public boolean isProvidesDirectionalDerivative() {
            if (providesDirectionalDerivative == null) {
                return false;
            } else {
                return providesDirectionalDerivative;
            }
        }

        /**
         * Sets the value of the providesDirectionalDerivative property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        @JsonProperty
        public void setProvidesDirectionalDerivative(Boolean value) {
            this.providesDirectionalDerivative = value;
        }


        public static class SourceFiles {

            @JsonProperty(value = "File", required = true)
            @JacksonXmlElementWrapper(useWrapping = false)
            protected List<Fmi2ModelDescription.CoSimulation.SourceFiles.File> file;

            public List<Fmi2ModelDescription.CoSimulation.SourceFiles.File> getFile() {
                if (file == null) {
                    file = new ArrayList<>();
                }
                return this.file;
            }

            public static class File {

                protected String name;

                /**
                 * Gets the value of the name property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setName(String value) {
                    this.name = value;
                }

            }

        }

    }


    public static class DefaultExperiment {

        @JacksonXmlProperty(isAttribute = true)
        protected Double startTime;
        @JacksonXmlProperty(isAttribute = true)
        protected Double stopTime;
        @JacksonXmlProperty(isAttribute = true)
        protected Double tolerance;
        @JacksonXmlProperty(isAttribute = true)
        protected Double stepSize;

        /**
         * Gets the value of the startTime property.
         *
         * @return possible object is
         * {@link Double }
         */
        public Double getStartTime() {
            return startTime;
        }

        /**
         * Sets the value of the startTime property.
         *
         * @param value allowed object is
         *              {@link Double }
         */
        public void setStartTime(Double value) {
            this.startTime = value;
        }

        /**
         * Gets the value of the stopTime property.
         *
         * @return possible object is
         * {@link Double }
         */
        public Double getStopTime() {
            return stopTime;
        }

        /**
         * Sets the value of the stopTime property.
         *
         * @param value allowed object is
         *              {@link Double }
         */
        public void setStopTime(Double value) {
            this.stopTime = value;
        }

        /**
         * Gets the value of the tolerance property.
         *
         * @return possible object is
         * {@link Double }
         */
        public Double getTolerance() {
            return tolerance;
        }

        /**
         * Sets the value of the tolerance property.
         *
         * @param value allowed object is
         *              {@link Double }
         */
        public void setTolerance(Double value) {
            this.tolerance = value;
        }

        /**
         * Gets the value of the stepSize property.
         *
         * @return possible object is
         * {@link Double }
         */
        public Double getStepSize() {
            return stepSize;
        }

        /**
         * Sets the value of the stepSize property.
         *
         * @param value allowed object is
         *              {@link Double }
         */
        public void setStepSize(Double value) {
            this.stepSize = value;
        }

    }


    public static class LogCategories {

        @JsonProperty(value = "Category", required = true)
        @JacksonXmlElementWrapper(useWrapping = false)
        protected List<Fmi2ModelDescription.LogCategories.Category> category;

        /**
         * Gets the value of the category property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the category property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCategory().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Fmi2ModelDescription.LogCategories.Category }
         */
        public List<Fmi2ModelDescription.LogCategories.Category> getCategory() {
            if (category == null) {
                category = new ArrayList<>();
            }
            return this.category;
        }

        public static class Category {

            protected String name;
            protected String description;

            /**
             * Gets the value of the name property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the description property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getDescription() {
                return description;
            }

            /**
             * Sets the value of the description property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setDescription(String value) {
                this.description = value;
            }

        }

    }


    public static class ModelExchange {

        @JsonProperty(value = "SourceFiles")
        protected Fmi2ModelDescription.ModelExchange.SourceFiles sourceFiles;
        @JacksonXmlProperty(isAttribute = true)
        protected String modelIdentifier;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean needsExecutionTool;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean completedIntegratorStepNotNeeded;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canBeInstantiatedOnlyOncePerProcess;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canNotUseMemoryManagementFunctions;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canGetAndSetFMUstate;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean canSerializeFMUstate;
        @JacksonXmlProperty(isAttribute = true)
        protected Boolean providesDirectionalDerivative;

        /**
         * Gets the value of the sourceFiles property.
         *
         * @return possible object is
         * {@link Fmi2ModelDescription.ModelExchange.SourceFiles }
         */
        public Fmi2ModelDescription.ModelExchange.SourceFiles getSourceFiles() {
            return sourceFiles;
        }

        /**
         * Sets the value of the sourceFiles property.
         *
         * @param value allowed object is
         *              {@link Fmi2ModelDescription.ModelExchange.SourceFiles }
         */
        public void setSourceFiles(Fmi2ModelDescription.ModelExchange.SourceFiles value) {
            this.sourceFiles = value;
        }

        /**
         * Gets the value of the modelIdentifier property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getModelIdentifier() {
            return modelIdentifier;
        }

        /**
         * Sets the value of the modelIdentifier property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setModelIdentifier(String value) {
            this.modelIdentifier = value;
        }

        /**
         * Gets the value of the needsExecutionTool property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        public boolean isNeedsExecutionTool() {
            if (needsExecutionTool == null) {
                return false;
            } else {
                return needsExecutionTool;
            }
        }

        /**
         * Sets the value of the needsExecutionTool property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setNeedsExecutionTool(Boolean value) {
            this.needsExecutionTool = value;
        }

        /**
         * Gets the value of the completedIntegratorStepNotNeeded property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        public boolean isCompletedIntegratorStepNotNeeded() {
            if (completedIntegratorStepNotNeeded == null) {
                return false;
            } else {
                return completedIntegratorStepNotNeeded;
            }
        }

        /**
         * Sets the value of the completedIntegratorStepNotNeeded property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setCompletedIntegratorStepNotNeeded(Boolean value) {
            this.completedIntegratorStepNotNeeded = value;
        }

        /**
         * Gets the value of the canBeInstantiatedOnlyOncePerProcess property.
         *
         * @return possible object is
         * {@link Boolean }
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
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setCanBeInstantiatedOnlyOncePerProcess(Boolean value) {
            this.canBeInstantiatedOnlyOncePerProcess = value;
        }

        /**
         * Gets the value of the canNotUseMemoryManagementFunctions property.
         *
         * @return possible object is
         * {@link Boolean }
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
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setCanNotUseMemoryManagementFunctions(Boolean value) {
            this.canNotUseMemoryManagementFunctions = value;
        }

        /**
         * Gets the value of the canGetAndSetFMUstate property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        public boolean isCanGetAndSetFMUstate() {
            if (canGetAndSetFMUstate == null) {
                return false;
            } else {
                return canGetAndSetFMUstate;
            }
        }

        /**
         * Sets the value of the canGetAndSetFMUstate property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setCanGetAndSetFMUstate(Boolean value) {
            this.canGetAndSetFMUstate = value;
        }

        /**
         * Gets the value of the canSerializeFMUstate property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        public boolean isCanSerializeFMUstate() {
            if (canSerializeFMUstate == null) {
                return false;
            } else {
                return canSerializeFMUstate;
            }
        }

        /**
         * Sets the value of the canSerializeFMUstate property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setCanSerializeFMUstate(Boolean value) {
            this.canSerializeFMUstate = value;
        }

        /**
         * Gets the value of the providesDirectionalDerivative property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        public boolean isProvidesDirectionalDerivative() {
            if (providesDirectionalDerivative == null) {
                return false;
            } else {
                return providesDirectionalDerivative;
            }
        }

        /**
         * Sets the value of the providesDirectionalDerivative property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setProvidesDirectionalDerivative(Boolean value) {
            this.providesDirectionalDerivative = value;
        }

        public static class SourceFiles {

            @JsonProperty(value = "File", required = true)
            @JacksonXmlElementWrapper(useWrapping = false)
            protected List<Fmi2ModelDescription.ModelExchange.SourceFiles.File> file;

            public List<Fmi2ModelDescription.ModelExchange.SourceFiles.File> getFile() {
                if (file == null) {
                    file = new ArrayList<Fmi2ModelDescription.ModelExchange.SourceFiles.File>();
                }
                return this.file;
            }

            public static class File {

                protected String name;

                /**
                 * Gets the value of the name property.
                 *
                 * @return possible object is
                 * {@link String }
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 *
                 * @param value allowed object is
                 *              {@link String }
                 */
                public void setName(String value) {
                    this.name = value;
                }

            }

        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModelStructure {

        @JsonProperty(value = "Outputs")
        protected Fmi2VariableDependency outputs;
        @JsonProperty(value = "Derivatives")
        protected Fmi2VariableDependency derivatives;
        @JsonProperty(value = "InitialUnknowns")
        protected Fmi2ModelDescription.ModelStructure.InitialUnknowns initialUnknowns;

        public ModelStructure() {
        }

        protected ModelStructure(String dummy) {
        }

        public Fmi2VariableDependency getOutputs() {
            return outputs;
        }

        public void setOutputs(Fmi2VariableDependency value) {
            this.outputs = value;
        }

        public Fmi2VariableDependency getDerivatives() {
            return derivatives;
        }

        public void setDerivatives(Fmi2VariableDependency value) {
            this.derivatives = value;
        }

        /**
         * Gets the value of the initialUnknowns property.
         *
         * @return possible object is
         * {@link Fmi2ModelDescription.ModelStructure.InitialUnknowns }
         */
        public Fmi2ModelDescription.ModelStructure.InitialUnknowns getInitialUnknowns() {
            return initialUnknowns;
        }

        public void setInitialUnknowns(Fmi2ModelDescription.ModelStructure.InitialUnknowns value) {
            this.initialUnknowns = value;
        }

        public static class InitialUnknowns {

            @JsonProperty(value = "Unknown", required = true)
            @JacksonXmlElementWrapper(useWrapping = false)
            protected List<Fmi2ModelDescription.ModelStructure.InitialUnknowns.Unknown> unknown;

            public List<Fmi2ModelDescription.ModelStructure.InitialUnknowns.Unknown> getUnknown() {
                if (unknown == null) {
                    unknown = new ArrayList<>();
                }
                return this.unknown;
            }

            public static class Unknown {

                @JacksonXmlProperty(isAttribute = true)
                private int index;
                @JacksonXmlProperty(isAttribute = true)
                private String dependencies;
                @JacksonXmlProperty(isAttribute = true)
                private String dependenciesKind;

                public int getIndex() {
                    return index;
                }

                public void setIndex(int value) {
                    this.index = value;
                }

                public List<Integer> getDependencies() {
                    if (dependencies == null || dependencies.isEmpty()) {
                        return new ArrayList<>();
                    } else {
                        String[] split = dependencies.trim().split(" ");
                        return Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList());
                    }
                }

                public List<String> getDependenciesKind() {
                    if (dependenciesKind == null || dependencies.isEmpty()) {
                        return new ArrayList<>();
                    } else {
                        return Arrays.asList(dependenciesKind.split(" "));
                    }
                }

            }

        }

    }


    public static class ModelVariables {

        @JsonProperty(value = "ScalarVariable", required = true)
        @JacksonXmlElementWrapper(useWrapping = false)
        protected List<Fmi2ScalarVariable> scalarVariable;

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
         * {@link Fmi2ScalarVariable }
         */
        public List<Fmi2ScalarVariable> getScalarVariable() {
            if (scalarVariable == null) {
                scalarVariable = new ArrayList<>();
            }
            return this.scalarVariable;
        }

    }

    public static class TypeDefinitions {

        @JsonProperty(value = "SimpleType", required = true)
        @JacksonXmlElementWrapper(useWrapping = false)
        protected List<Fmi2SimpleType> simpleType;

        /**
         * Gets the value of the simpleType property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the simpleType property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSimpleType().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Fmi2SimpleType }
         */
        public List<Fmi2SimpleType> getSimpleType() {
            if (simpleType == null) {
                simpleType = new ArrayList<>();
            }
            return this.simpleType;
        }

    }

    public static class UnitDefinitions {

        @JsonProperty(value = "Unit", required = true)
        @JacksonXmlElementWrapper(useWrapping = false)
        protected List<Fmi2Unit> unit;

        /**
         * Gets the value of the unit property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the unit property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUnit().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Fmi2Unit }
         */
        public List<Fmi2Unit> getUnit() {
            if (unit == null) {
                unit = new ArrayList<>();
            }
            return this.unit;
        }

    }

}
