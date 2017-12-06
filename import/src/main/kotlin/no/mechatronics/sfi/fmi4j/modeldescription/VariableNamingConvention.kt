package no.mechatronics.sfi.fmi4j.modeldescription

import org.w3c.dom.Node
import javax.xml.bind.annotation.XmlEnum
import javax.xml.bind.annotation.XmlEnumValue
import javax.xml.bind.annotation.XmlType
import javax.xml.bind.annotation.adapters.XmlAdapter

@XmlType
@XmlEnum(String::class)
enum class VariableNamingConvention {

    @XmlEnumValue("flat")
    FLAT,
    @XmlEnumValue("structured")
    STRUCTURED

}
