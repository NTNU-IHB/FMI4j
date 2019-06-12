
package no.ntnu.ihb.fmi4j.modeldescription.fmi1;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
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
 *         &lt;element name="UnitDefinitions" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="BaseUnit" type="{}fmiBaseUnit"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="TypeDefinitions" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="Type" type="{}fmiType"/>
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
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="VendorAnnotations" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="Tool">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                             &lt;element name="Annotation">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ModelVariables" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="ScalarVariable" type="{}fmiScalarVariable"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Implementation" type="{}fmiImplementation" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="fmiVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" fixed="1.0" />
 *       &lt;attribute name="modelName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="modelIdentifier" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="guid" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="author" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}decimal" />
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
 *       &lt;attribute name="numberOfContinuousStates" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *       &lt;attribute name="numberOfEventIndicators" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "unitDefinitions",
    "typeDefinitions",
    "defaultExperiment",
    "vendorAnnotations",
    "modelVariables",
    "implementation"
})
@XmlRootElement(name = "fmiModelDescription")
public class FmiModelDescription {

    @XmlElement(name = "UnitDefinitions")
    protected FmiModelDescription.UnitDefinitions unitDefinitions;
    @XmlElement(name = "TypeDefinitions")
    protected FmiModelDescription.TypeDefinitions typeDefinitions;
    @XmlElement(name = "DefaultExperiment")
    protected FmiModelDescription.DefaultExperiment defaultExperiment;
    @XmlElement(name = "VendorAnnotations")
    protected FmiModelDescription.VendorAnnotations vendorAnnotations;
    @XmlElement(name = "ModelVariables")
    protected FmiModelDescription.ModelVariables modelVariables;
    @XmlElement(name = "Implementation")
    protected FmiImplementation implementation;
    @XmlAttribute(name = "fmiVersion", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String fmiVersion;
    @XmlAttribute(name = "modelName", required = true)
    protected String modelName;
    @XmlAttribute(name = "modelIdentifier", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelIdentifier;
    @XmlAttribute(name = "guid", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String guid;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "author")
    protected String author;
    @XmlAttribute(name = "version")
    protected String version;
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
    @XmlAttribute(name = "numberOfContinuousStates", required = true)
    @XmlSchemaType(name = "unsignedInt")
    protected long numberOfContinuousStates;
    @XmlAttribute(name = "numberOfEventIndicators", required = true)
    @XmlSchemaType(name = "unsignedInt")
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

    /**
     * Gets the value of the generationDateAndTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="ScalarVariable" type="{}fmiScalarVariable"/>
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

        @XmlElement(name = "ScalarVariable")
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="Type" type="{}fmiType"/>
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
        "type"
    })
    public static class TypeDefinitions {

        @XmlElement(name = "Type")
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="BaseUnit" type="{}fmiBaseUnit"/>
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
        "baseUnit"
    })
    public static class UnitDefinitions {

        @XmlElement(name = "BaseUnit")
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
                baseUnit = new ArrayList<FmiBaseUnit>();
            }
            return this.baseUnit;
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
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="Tool">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *                   &lt;element name="Annotation">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "tool"
    })
    public static class VendorAnnotations {

        @XmlElement(name = "Tool")
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


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
         *         &lt;element name="Annotation">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "annotation"
        })
        public static class Tool {

            @XmlElement(name = "Annotation")
            protected List<FmiModelDescription.VendorAnnotations.Tool.Annotation> annotation;
            @XmlAttribute(name = "name", required = true)
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
                    annotation = new ArrayList<FmiModelDescription.VendorAnnotations.Tool.Annotation>();
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


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Annotation {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "value", required = true)
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
