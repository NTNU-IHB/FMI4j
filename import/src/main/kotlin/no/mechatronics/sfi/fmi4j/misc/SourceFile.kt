package no.mechatronics.sfi.fmi4j.misc

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute

@XmlAccessorType(XmlAccessType.FIELD)
class SourceFile {

    @XmlAttribute
    val name: String = ""

    override fun toString(): String {
        return "SourceFile(name='$name')"
    }


}