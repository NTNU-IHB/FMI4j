package no.mechatronics.sfi.fmi4j.modeldescription

import javax.xml.bind.annotation.XmlEnum
import javax.xml.bind.annotation.XmlEnumValue
import javax.xml.bind.annotation.XmlType

@XmlType
@XmlEnum(String::class)
enum class VariableNamingConvention {

    @XmlEnumValue("flat")
    FLAT,
    @XmlEnumValue("structured")
    STRUCTURED

}
