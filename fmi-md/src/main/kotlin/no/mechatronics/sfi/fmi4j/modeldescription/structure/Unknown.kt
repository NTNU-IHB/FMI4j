package no.mechatronics.sfi.fmi4j.modeldescription.structure

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

interface Unknown {
    val index: Int
}

@XmlAccessorType(XmlAccessType.FIELD)
class UnknownImpl(
        override val index: Int = 0
): Unknown {
    override fun toString(): String {
        return "Unknown(index=$index)"
    }
}