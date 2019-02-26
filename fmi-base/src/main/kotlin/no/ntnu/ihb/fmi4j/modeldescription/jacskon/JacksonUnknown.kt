package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.Unknown


/**
 * @author Lars Ivar Hatledal
 */
class JacksonUnknown(

        @JacksonXmlProperty
        override var index: Int,

        @JacksonXmlProperty(localName = "dependencies")
        private var _dependencies: String? = null,

        @JacksonXmlProperty(localName = "dependenciesKind")
        private val _dependenciesKind: String? = null

) : Unknown {

    override val dependencies: List<Int>
        get() = _dependencies?.let {
            it.split(" ").mapNotNull { it.toIntOrNull() }
        } ?: emptyList()

    override val dependenciesKind: List<String>
        get() = _dependenciesKind?.trim()?.split(" ")?.mapNotNull { if(it.isEmpty()) null else it } ?: emptyList()

    override fun toString(): String {
        return "JacksonUnknown(index=$index, dependencies=$dependencies, dependenciesKind=$dependenciesKind)"
    }

}