package no.mechatronics.sfi.modeldescription.log

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType


@XmlAccessorType(XmlAccessType.FIELD)
class Category(
        val name:String? = null
){
    override fun toString(): String {
        return "Category(name=$name)"
    }
}

