package no.sfi.mechatronics.fmi4j.modeldescription

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper


/**
 * Defines the structure of the model. Especially, the ordered lists of
outputs, continuous-time states and initial unknowns (the unknowns
during Initialization Mode) are defined here. Furthermore, the
dependency of the unkowns from the knowns can be optionally
defined. [This information can be, for example used to compute
efficiently a sparse Jacobian for simulation or to utilize the
input/output dependency in order to detect that in some cases there
are actually no algebraic loops when connecting FMUs together].
 */
@XmlAccessorType(XmlAccessType.FIELD)
class ModelStructure {

    @XmlElementWrapper(name = "Outputs")
    @XmlElement(name = "Unknown")
    private val _outputs: List<Int>? = null

    val outputs: List<Int>
        get() {
            if (_outputs == null) {
                return emptyList()
            } else {
                return _outputs
            }
        }

    @XmlElementWrapper(name = "Derivatives")
    @XmlElement(name = "Unknown")
    private val _derivatives: List<Unknown>? = null

    val derivatives: List<Unknown>
        get() {
            if (_derivatives == null) {
                return emptyList()
            } else {
                return _derivatives
            }
        }

    @XmlElementWrapper(name = "InitialUnknowns")
    @XmlElement(name = "Unknown")
    private val _initialUnknowns: List<Unknown>? = null

    val initialUnknowns: List<Unknown>
        get() {
            if (_initialUnknowns == null) {
                return emptyList()
            } else {
                return _initialUnknowns
            }
        }

    override fun toString(): String {
        return "ModelStructure(outputs=$outputs, derivatives=$derivatives, initialUnknowns=$initialUnknowns)"
    }


}

@XmlAccessorType(XmlAccessType.FIELD)
class Unknown(
        val index: Int = 0
) {
    override fun toString(): String {
        return "Unknown(index=$index)"
    }
}
