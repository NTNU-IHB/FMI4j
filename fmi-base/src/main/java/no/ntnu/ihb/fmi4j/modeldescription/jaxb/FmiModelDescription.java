
package no.ntnu.ihb.fmi4j.modeldescription.jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;sequence maxOccurs="2">
 *           &lt;element name="ModelExchange" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="SourceFiles" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence maxOccurs="unbounded">
 *                               &lt;element name="File">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attribute name="modelIdentifier" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="needsExecutionTool" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="completedIntegratorStepNotNeeded" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canBeInstantiatedOnlyOncePerProcess" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canNotUseMemoryManagementFunctions" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canGetAndSetFMUstate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canSerializeFMUstate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="providesDirectionalDerivative" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="CoSimulation" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="SourceFiles" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence maxOccurs="unbounded">
 *                               &lt;element name="File">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attribute name="modelIdentifier" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                   &lt;attribute name="needsExecutionTool" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canHandleVariableCommunicationStepSize" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canInterpolateInputs" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="maxOutputDerivativeOrder" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" default="0" />
 *                   &lt;attribute name="canRunAsynchronuously" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canBeInstantiatedOnlyOncePerProcess" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canNotUseMemoryManagementFunctions" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canGetAndSetFMUstate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="canSerializeFMUstate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="providesDirectionalDerivative" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *         &lt;element name="UnitDefinitions" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element name="Unit" type="{}fmi2Unit"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="TypeDefinitions" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element name="SimpleType" type="{}fmi2SimpleType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="LogCategories" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element name="Category">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                           &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="DefaultExperiment" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="startTime" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="stopTime" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="tolerance" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="stepSize" type="{http://www.w3.org/2001/XMLSchema}double" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="VendorAnnotations" type="{}fmi2Annotation" minOccurs="0"/>
 *         &lt;element name="ModelVariables">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element name="ScalarVariable" type="{}fmi2ScalarVariable"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ModelStructure">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Outputs" type="{}fmi2VariableDependency" minOccurs="0"/>
 *                   &lt;element name="Derivatives" type="{}fmi2VariableDependency" minOccurs="0"/>
 *                   &lt;element name="InitialUnknowns" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence maxOccurs="unbounded">
 *                             &lt;element name="Unknown">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                                     &lt;attribute name="dependencies">
 *                                       &lt;simpleType>
 *                                         &lt;list itemType="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                                       &lt;/simpleType>
 *                                     &lt;/attribute>
 *                                     &lt;attribute name="dependenciesKind">
 *                                       &lt;simpleType>
 *                                         &lt;list>
 *                                           &lt;simpleType>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *                                               &lt;enumeration value="dependent"/>
 *                                               &lt;enumeration value="constant"/>
 *                                               &lt;enumeration value="fixed"/>
 *                                               &lt;enumeration value="tunable"/>
 *                                               &lt;enumeration value="discrete"/>
 *                                             &lt;/restriction>
 *                                           &lt;/simpleType>
 *                                         &lt;/list>
 *                                       &lt;/simpleType>
 *                                     &lt;/attribute>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="fmiVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" fixed="2.0" />
 *       &lt;attribute name="modelName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="guid" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="author" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="copyright" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="license" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="generationTool" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="generationDateAndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="variableNamingConvention" default="flat">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *             &lt;enumeration value="flat"/>
 *             &lt;enumeration value="structured"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="numberOfEventIndicators" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "modelExchange",
    "coSimulation",
    "unitDefinitions",
    "typeDefinitions",
    "logCategories",
    "defaultExperiment",
    "vendorAnnotations",
    "modelVariables",
    "modelStructure"
})
@XmlRootElement(name = "fmiModelDescription")
public class FmiModelDescription {


    @XmlElement(name = "ModelExchange", type = FmiModelDescription.ModelExchange.class)
    protected FmiModelDescription.ModelExchange modelExchange;
    @XmlElement(name = "CoSimulation", type = FmiModelDescription.CoSimulation.class)
    protected FmiModelDescription.CoSimulation coSimulation;
    @XmlElement(name = "UnitDefinitions")
    protected FmiModelDescription.UnitDefinitions unitDefinitions;
    @XmlElement(name = "TypeDefinitions")
    protected FmiModelDescription.TypeDefinitions typeDefinitions;
    @XmlElement(name = "LogCategories")
    protected FmiModelDescription.LogCategories logCategories;
    @XmlElement(name = "DefaultExperiment")
    protected FmiModelDescription.DefaultExperiment defaultExperiment;
    @XmlElement(name = "VendorAnnotations")
    protected Fmi2Annotation vendorAnnotations;
    @XmlElement(name = "ModelVariables", required = true)
    protected FmiModelDescription.ModelVariables modelVariables;
    @XmlElement(name = "ModelStructure", required = true)
    protected FmiModelDescription.ModelStructure modelStructure;
    @XmlAttribute(name = "fmiVersion", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String fmiVersion;
    @XmlAttribute(name = "modelName", required = true)
    protected String modelName;
    @XmlAttribute(name = "guid", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String guid;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "author")
    protected String author;
    @XmlAttribute(name = "version")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String version;
    @XmlAttribute(name = "copyright")
    protected String copyright;
    @XmlAttribute(name = "license")
    protected String license;
    @XmlAttribute(name = "generationTool")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String generationTool;
    @XmlAttribute(name = "generationDateAndTime")
    @XmlSchemaType(name = "dateTime")
    protected String generationDateAndTime;
    @XmlAttribute(name = "variableNamingConvention")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String variableNamingConvention;
    @XmlAttribute(name = "numberOfEventIndicators")
    @XmlSchemaType(name = "unsignedInt")
    protected Long numberOfEventIndicators;


    public ModelExchange getModelExchange() {
        return this.modelExchange;
    }

    public CoSimulation getCoSimulation() {
        return this.coSimulation;
    }

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
     * Gets the value of the logCategories property.
     * 
     * @return
     *     possible object is
     *     {@link FmiModelDescription.LogCategories }
     *     
     */
    public FmiModelDescription.LogCategories getLogCategories() {
        return logCategories;
    }

    /**
     * Sets the value of the logCategories property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiModelDescription.LogCategories }
     *     
     */
    public void setLogCategories(FmiModelDescription.LogCategories value) {
        this.logCategories = value;
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
     *     {@link Fmi2Annotation }
     *     
     */
    public Fmi2Annotation getVendorAnnotations() {
        return vendorAnnotations;
    }

    /**
     * Sets the value of the vendorAnnotations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fmi2Annotation }
     *     
     */
    public void setVendorAnnotations(Fmi2Annotation value) {
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
     * Gets the value of the modelStructure property.
     * 
     * @return
     *     possible object is
     *     {@link FmiModelDescription.ModelStructure }
     *     
     */
    public FmiModelDescription.ModelStructure getModelStructure() {
        return modelStructure;
    }

    /**
     * Sets the value of the modelStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link FmiModelDescription.ModelStructure }
     *     
     */
    public void setModelStructure(FmiModelDescription.ModelStructure value) {
        this.modelStructure = value;
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
            return "2.0";
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
     * Gets the value of the copyright property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the value of the copyright property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCopyright(String value) {
        this.copyright = value;
    }

    /**
     * Gets the value of the license property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets the value of the license property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicense(String value) {
        this.license = value;
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

    /**
     * Gets the value of the generationDateAndTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenerationDateAndTime() {
        return generationDateAndTime;
    }

    /**
     * Sets the value of the generationDateAndTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
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
     * Gets the value of the numberOfEventIndicators property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumberOfEventIndicators() {
        return numberOfEventIndicators;
    }

    /**
     * Sets the value of the numberOfEventIndicators property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumberOfEventIndicators(Long value) {
        this.numberOfEventIndicators = value;
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
     *         &lt;element name="SourceFiles" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence maxOccurs="unbounded">
     *                   &lt;element name="File">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="modelIdentifier" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="needsExecutionTool" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canHandleVariableCommunicationStepSize" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canInterpolateInputs" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="maxOutputDerivativeOrder" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" default="0" />
     *       &lt;attribute name="canRunAsynchronuously" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canBeInstantiatedOnlyOncePerProcess" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canNotUseMemoryManagementFunctions" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canGetAndSetFMUstate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canSerializeFMUstate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="providesDirectionalDerivative" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "sourceFiles"
    })
    public static class CoSimulation {

        @XmlElement(name = "SourceFiles")
        protected FmiModelDescription.CoSimulation.SourceFiles sourceFiles;
        @XmlAttribute(name = "modelIdentifier", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String modelIdentifier;
        @XmlAttribute(name = "needsExecutionTool")
        protected Boolean needsExecutionTool;
        @XmlAttribute(name = "canHandleVariableCommunicationStepSize")
        protected Boolean canHandleVariableCommunicationStepSize;
        @XmlAttribute(name = "canInterpolateInputs")
        protected Boolean canInterpolateInputs;
        @XmlAttribute(name = "maxOutputDerivativeOrder")
        @XmlSchemaType(name = "unsignedInt")
        protected Long maxOutputDerivativeOrder;
        @XmlAttribute(name = "canRunAsynchronuously")
        protected Boolean canRunAsynchronuously;
        @XmlAttribute(name = "canBeInstantiatedOnlyOncePerProcess")
        protected Boolean canBeInstantiatedOnlyOncePerProcess;
        @XmlAttribute(name = "canNotUseMemoryManagementFunctions")
        protected Boolean canNotUseMemoryManagementFunctions;
        @XmlAttribute(name = "canGetAndSetFMUstate")
        protected Boolean canGetAndSetFMUstate;
        @XmlAttribute(name = "canSerializeFMUstate")
        protected Boolean canSerializeFMUstate;
        @XmlAttribute(name = "providesDirectionalDerivative")
        protected Boolean providesDirectionalDerivative;

        /**
         * Gets the value of the sourceFiles property.
         * 
         * @return
         *     possible object is
         *     {@link FmiModelDescription.CoSimulation.SourceFiles }
         *     
         */
        public FmiModelDescription.CoSimulation.SourceFiles getSourceFiles() {
            return sourceFiles;
        }

        /**
         * Sets the value of the sourceFiles property.
         * 
         * @param value
         *     allowed object is
         *     {@link FmiModelDescription.CoSimulation.SourceFiles }
         *     
         */
        public void setSourceFiles(FmiModelDescription.CoSimulation.SourceFiles value) {
            this.sourceFiles = value;
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
         * Gets the value of the needsExecutionTool property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
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
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setNeedsExecutionTool(Boolean value) {
            this.needsExecutionTool = value;
        }

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

        /**
         * Gets the value of the canGetAndSetFMUstate property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
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
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setCanGetAndSetFMUstate(Boolean value) {
            this.canGetAndSetFMUstate = value;
        }

        /**
         * Gets the value of the canSerializeFMUstate property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
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
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setCanSerializeFMUstate(Boolean value) {
            this.canSerializeFMUstate = value;
        }

        /**
         * Gets the value of the providesDirectionalDerivative property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
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
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setProvidesDirectionalDerivative(Boolean value) {
            this.providesDirectionalDerivative = value;
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
         *       &lt;sequence maxOccurs="unbounded">
         *         &lt;element name="File">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "file"
        })
        public static class SourceFiles {

            @XmlElement(name = "File", required = true)
            protected List<FmiModelDescription.CoSimulation.SourceFiles.File> file;

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
             * {@link FmiModelDescription.CoSimulation.SourceFiles.File }
             * 
             * 
             */
            public List<FmiModelDescription.CoSimulation.SourceFiles.File> getFile() {
                if (file == null) {
                    file = new ArrayList<FmiModelDescription.CoSimulation.SourceFiles.File>();
                }
                return this.file;
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
             *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class File {

                @XmlAttribute(name = "name", required = true)
                @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
                @XmlSchemaType(name = "normalizedString")
                protected String name;

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

            }

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
     *       &lt;attribute name="startTime" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="stopTime" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="tolerance" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="stepSize" type="{http://www.w3.org/2001/XMLSchema}double" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DefaultExperiment {

        @XmlAttribute(name = "startTime")
        protected Double startTime;
        @XmlAttribute(name = "stopTime")
        protected Double stopTime;
        @XmlAttribute(name = "tolerance")
        protected Double tolerance;
        @XmlAttribute(name = "stepSize")
        protected Double stepSize;

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

        /**
         * Gets the value of the stepSize property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getStepSize() {
            return stepSize;
        }

        /**
         * Sets the value of the stepSize property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setStepSize(Double value) {
            this.stepSize = value;
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
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="Category">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *                 &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "category"
    })
    public static class LogCategories {

        @XmlElement(name = "Category", required = true)
        protected List<FmiModelDescription.LogCategories.Category> category;

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
         * {@link FmiModelDescription.LogCategories.Category }
         * 
         * 
         */
        public List<FmiModelDescription.LogCategories.Category> getCategory() {
            if (category == null) {
                category = new ArrayList<FmiModelDescription.LogCategories.Category>();
            }
            return this.category;
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
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
         *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Category {

            @XmlAttribute(name = "name", required = true)
            @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
            @XmlSchemaType(name = "normalizedString")
            protected String name;
            @XmlAttribute(name = "description")
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


    /**
     * List of capability flags that an FMI2 Model Exchange interface can provide
     * 
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="SourceFiles" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence maxOccurs="unbounded">
     *                   &lt;element name="File">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="modelIdentifier" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *       &lt;attribute name="needsExecutionTool" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="completedIntegratorStepNotNeeded" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canBeInstantiatedOnlyOncePerProcess" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canNotUseMemoryManagementFunctions" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canGetAndSetFMUstate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="canSerializeFMUstate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="providesDirectionalDerivative" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "sourceFiles"
    })
    public static class ModelExchange {

        @XmlElement(name = "SourceFiles")
        protected FmiModelDescription.ModelExchange.SourceFiles sourceFiles;
        @XmlAttribute(name = "modelIdentifier", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String modelIdentifier;
        @XmlAttribute(name = "needsExecutionTool")
        protected Boolean needsExecutionTool;
        @XmlAttribute(name = "completedIntegratorStepNotNeeded")
        protected Boolean completedIntegratorStepNotNeeded;
        @XmlAttribute(name = "canBeInstantiatedOnlyOncePerProcess")
        protected Boolean canBeInstantiatedOnlyOncePerProcess;
        @XmlAttribute(name = "canNotUseMemoryManagementFunctions")
        protected Boolean canNotUseMemoryManagementFunctions;
        @XmlAttribute(name = "canGetAndSetFMUstate")
        protected Boolean canGetAndSetFMUstate;
        @XmlAttribute(name = "canSerializeFMUstate")
        protected Boolean canSerializeFMUstate;
        @XmlAttribute(name = "providesDirectionalDerivative")
        protected Boolean providesDirectionalDerivative;

        /**
         * Gets the value of the sourceFiles property.
         * 
         * @return
         *     possible object is
         *     {@link FmiModelDescription.ModelExchange.SourceFiles }
         *     
         */
        public FmiModelDescription.ModelExchange.SourceFiles getSourceFiles() {
            return sourceFiles;
        }

        /**
         * Sets the value of the sourceFiles property.
         * 
         * @param value
         *     allowed object is
         *     {@link FmiModelDescription.ModelExchange.SourceFiles }
         *     
         */
        public void setSourceFiles(FmiModelDescription.ModelExchange.SourceFiles value) {
            this.sourceFiles = value;
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
         * Gets the value of the needsExecutionTool property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
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
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setNeedsExecutionTool(Boolean value) {
            this.needsExecutionTool = value;
        }

        /**
         * Gets the value of the completedIntegratorStepNotNeeded property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
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
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setCompletedIntegratorStepNotNeeded(Boolean value) {
            this.completedIntegratorStepNotNeeded = value;
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

        /**
         * Gets the value of the canGetAndSetFMUstate property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
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
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setCanGetAndSetFMUstate(Boolean value) {
            this.canGetAndSetFMUstate = value;
        }

        /**
         * Gets the value of the canSerializeFMUstate property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
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
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setCanSerializeFMUstate(Boolean value) {
            this.canSerializeFMUstate = value;
        }

        /**
         * Gets the value of the providesDirectionalDerivative property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
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
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setProvidesDirectionalDerivative(Boolean value) {
            this.providesDirectionalDerivative = value;
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
         *       &lt;sequence maxOccurs="unbounded">
         *         &lt;element name="File">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "file"
        })
        public static class SourceFiles {

            @XmlElement(name = "File", required = true)
            protected List<FmiModelDescription.ModelExchange.SourceFiles.File> file;

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
             * {@link FmiModelDescription.ModelExchange.SourceFiles.File }
             * 
             * 
             */
            public List<FmiModelDescription.ModelExchange.SourceFiles.File> getFile() {
                if (file == null) {
                    file = new ArrayList<FmiModelDescription.ModelExchange.SourceFiles.File>();
                }
                return this.file;
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
             *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class File {

                @XmlAttribute(name = "name", required = true)
                @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
                @XmlSchemaType(name = "normalizedString")
                protected String name;

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

            }

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
     *         &lt;element name="Outputs" type="{}fmi2VariableDependency" minOccurs="0"/>
     *         &lt;element name="Derivatives" type="{}fmi2VariableDependency" minOccurs="0"/>
     *         &lt;element name="InitialUnknowns" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence maxOccurs="unbounded">
     *                   &lt;element name="Unknown">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *                           &lt;attribute name="dependencies">
     *                             &lt;simpleType>
     *                               &lt;list itemType="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *                             &lt;/simpleType>
     *                           &lt;/attribute>
     *                           &lt;attribute name="dependenciesKind">
     *                             &lt;simpleType>
     *                               &lt;list>
     *                                 &lt;simpleType>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
     *                                     &lt;enumeration value="dependent"/>
     *                                     &lt;enumeration value="constant"/>
     *                                     &lt;enumeration value="fixed"/>
     *                                     &lt;enumeration value="tunable"/>
     *                                     &lt;enumeration value="discrete"/>
     *                                   &lt;/restriction>
     *                                 &lt;/simpleType>
     *                               &lt;/list>
     *                             &lt;/simpleType>
     *                           &lt;/attribute>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "outputs",
        "derivatives",
        "initialUnknowns"
    })
    public static class ModelStructure {

        @XmlElement(name = "Outputs")
        protected Fmi2VariableDependency outputs;
        @XmlElement(name = "Derivatives")
        protected Fmi2VariableDependency derivatives;
        @XmlElement(name = "InitialUnknowns")
        protected FmiModelDescription.ModelStructure.InitialUnknowns initialUnknowns;

        /**
         * Gets the value of the outputs property.
         * 
         * @return
         *     possible object is
         *     {@link Fmi2VariableDependency }
         *     
         */
        public Fmi2VariableDependency getOutputs() {
            return outputs;
        }

        /**
         * Sets the value of the outputs property.
         * 
         * @param value
         *     allowed object is
         *     {@link Fmi2VariableDependency }
         *     
         */
        public void setOutputs(Fmi2VariableDependency value) {
            this.outputs = value;
        }

        /**
         * Gets the value of the derivatives property.
         * 
         * @return
         *     possible object is
         *     {@link Fmi2VariableDependency }
         *     
         */
        public Fmi2VariableDependency getDerivatives() {
            return derivatives;
        }

        /**
         * Sets the value of the derivatives property.
         * 
         * @param value
         *     allowed object is
         *     {@link Fmi2VariableDependency }
         *     
         */
        public void setDerivatives(Fmi2VariableDependency value) {
            this.derivatives = value;
        }

        /**
         * Gets the value of the initialUnknowns property.
         * 
         * @return
         *     possible object is
         *     {@link FmiModelDescription.ModelStructure.InitialUnknowns }
         *     
         */
        public FmiModelDescription.ModelStructure.InitialUnknowns getInitialUnknowns() {
            return initialUnknowns;
        }

        /**
         * Sets the value of the initialUnknowns property.
         * 
         * @param value
         *     allowed object is
         *     {@link FmiModelDescription.ModelStructure.InitialUnknowns }
         *     
         */
        public void setInitialUnknowns(FmiModelDescription.ModelStructure.InitialUnknowns value) {
            this.initialUnknowns = value;
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
         *       &lt;sequence maxOccurs="unbounded">
         *         &lt;element name="Unknown">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *                 &lt;attribute name="dependencies">
         *                   &lt;simpleType>
         *                     &lt;list itemType="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *                   &lt;/simpleType>
         *                 &lt;/attribute>
         *                 &lt;attribute name="dependenciesKind">
         *                   &lt;simpleType>
         *                     &lt;list>
         *                       &lt;simpleType>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
         *                           &lt;enumeration value="dependent"/>
         *                           &lt;enumeration value="constant"/>
         *                           &lt;enumeration value="fixed"/>
         *                           &lt;enumeration value="tunable"/>
         *                           &lt;enumeration value="discrete"/>
         *                         &lt;/restriction>
         *                       &lt;/simpleType>
         *                     &lt;/list>
         *                   &lt;/simpleType>
         *                 &lt;/attribute>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "unknown"
        })
        public static class InitialUnknowns {

            @XmlElement(name = "Unknown", required = true)
            protected List<FmiModelDescription.ModelStructure.InitialUnknowns.Unknown> unknown;

            /**
             * Gets the value of the unknown property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the unknown property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getUnknown().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FmiModelDescription.ModelStructure.InitialUnknowns.Unknown }
             * 
             * 
             */
            public List<FmiModelDescription.ModelStructure.InitialUnknowns.Unknown> getUnknown() {
                if (unknown == null) {
                    unknown = new ArrayList<FmiModelDescription.ModelStructure.InitialUnknowns.Unknown>();
                }
                return this.unknown;
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
             *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
             *       &lt;attribute name="dependencies">
             *         &lt;simpleType>
             *           &lt;list itemType="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
             *         &lt;/simpleType>
             *       &lt;/attribute>
             *       &lt;attribute name="dependenciesKind">
             *         &lt;simpleType>
             *           &lt;list>
             *             &lt;simpleType>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
             *                 &lt;enumeration value="dependent"/>
             *                 &lt;enumeration value="constant"/>
             *                 &lt;enumeration value="fixed"/>
             *                 &lt;enumeration value="tunable"/>
             *                 &lt;enumeration value="discrete"/>
             *               &lt;/restriction>
             *             &lt;/simpleType>
             *           &lt;/list>
             *         &lt;/simpleType>
             *       &lt;/attribute>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Unknown {

                @XmlAttribute(name = "index", required = true)
                @XmlSchemaType(name = "unsignedInt")
                protected long index;
                @XmlAttribute(name = "dependencies")
                protected List<Long> dependencies;
                @XmlAttribute(name = "dependenciesKind")
                protected List<String> dependenciesKind;

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public long getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(long value) {
                    this.index = value;
                }

                /**
                 * Gets the value of the dependencies property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the dependencies property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getDependencies().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Long }
                 * 
                 * 
                 */
                public List<Long> getDependencies() {
                    if (dependencies == null) {
                        dependencies = new ArrayList<Long>();
                    }
                    return this.dependencies;
                }

                /**
                 * Gets the value of the dependenciesKind property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the dependenciesKind property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getDependenciesKind().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link String }
                 * 
                 * 
                 */
                public List<String> getDependenciesKind() {
                    if (dependenciesKind == null) {
                        dependenciesKind = new ArrayList<String>();
                    }
                    return this.dependenciesKind;
                }

            }

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
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="ScalarVariable" type="{}fmi2ScalarVariable"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "scalarVariable"
    })
    public static class ModelVariables {

        @XmlElement(name = "ScalarVariable", required = true)
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
         * 
         * 
         */
        public List<Fmi2ScalarVariable> getScalarVariable() {
            if (scalarVariable == null) {
                scalarVariable = new ArrayList<Fmi2ScalarVariable>();
            }
            return this.scalarVariable;
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
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="SimpleType" type="{}fmi2SimpleType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "simpleType"
    })
    public static class TypeDefinitions {

        @XmlElement(name = "SimpleType", required = true)
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
         * 
         * 
         */
        public List<Fmi2SimpleType> getSimpleType() {
            if (simpleType == null) {
                simpleType = new ArrayList<Fmi2SimpleType>();
            }
            return this.simpleType;
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
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="Unit" type="{}fmi2Unit"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "unit"
    })
    public static class UnitDefinitions {

        @XmlElement(name = "Unit", required = true)
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
         * 
         * 
         */
        public List<Fmi2Unit> getUnit() {
            if (unit == null) {
                unit = new ArrayList<Fmi2Unit>();
            }
            return this.unit;
        }

    }

}
