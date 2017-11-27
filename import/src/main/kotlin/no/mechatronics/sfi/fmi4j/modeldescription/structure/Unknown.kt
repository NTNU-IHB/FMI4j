package no.mechatronics.sfi.fmi4j.modeldescription.structure

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

@XmlAccessorType(XmlAccessType.FIELD)
class Unknown(
        val index: Int = 0
) {
    override fun toString(): String {
        return "Unknown(index=$index)"
    }
}
