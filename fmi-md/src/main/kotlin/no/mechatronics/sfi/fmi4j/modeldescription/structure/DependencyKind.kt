package no.mechatronics.sfi.fmi4j.modeldescription.structure

import javax.xml.bind.annotation.XmlEnum
import javax.xml.bind.annotation.XmlEnumValue
import javax.xml.bind.annotation.XmlType

/**
 *
 * @author Lars Ivar Hatledal
 */
@XmlType
@XmlEnum(String::class)
enum class DependenciesKind {

    /**
     * No particular structure, f(v)
     */
    @XmlEnumValue("dependent")
    DEPENDENT,

    /**
     * Constant factor, p*v (only for Real variables)
     */
    @XmlEnumValue("constant")
    CONSTANT,

    /**
     * tunable factor, p*v (only for Real variables)
     */
    @XmlEnumValue("tunable")
    TUNABLE,

    /**
     * Discrete factor, d*v (only for Real variables
     */
    @XmlEnumValue("discrete")
    DISCRETE
}

/**
 *
 * @author Lars Ivar Hatledal
 */
@XmlType
@XmlEnum(String::class)
enum class DependenciesKindForInitialUnknowns {

    /**
     * No particular structure, f(v)
     */
    @XmlEnumValue("dependent")
    DEPENDENT,

    /**
     * Constant factor, p*v (only for Real variables)
     */
    @XmlEnumValue("constant")
    CONSTANT,
}