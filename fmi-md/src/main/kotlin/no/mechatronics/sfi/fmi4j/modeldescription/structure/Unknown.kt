package no.mechatronics.sfi.fmi4j.modeldescription.structure

import java.io.Serializable
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute

/**
 * @author Lars Ivar Hatledal
 */
interface Unknown {
    val index: Int
}

@XmlAccessorType(XmlAccessType.FIELD)
class UnknownImpl: Unknown, Serializable {

    @XmlAttribute(name = "index")
    private var _index: Int? = null

    override val index: Int
        get() = _index ?: throw IllegalStateException("Index was null!")


    override fun toString(): String {
        return "UnknownImpl(index=$index)"
    }

}