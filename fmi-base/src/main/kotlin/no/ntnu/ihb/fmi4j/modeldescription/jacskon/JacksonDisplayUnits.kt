package no.ntnu.ihb.fmi4j.modeldescription.jacskon

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.BaseUnit
import no.ntnu.ihb.fmi4j.modeldescription.DisplayUnit
import no.ntnu.ihb.fmi4j.modeldescription.Unit


typealias JacksonUnitDefinitions = List<JacksonUnit>

/**
 * @author Lars Ivar Hatledal
 */
class JacksonUnit(

        /**
         * Name of Unit element, e.g. "N.m", "Nm", "%/s".
         * "name" must be unique with respect to other elements of the
         * UnitDefinitions list. The variable values of fmi2SetXXX and fmi2GetXXX
         * are with respect to this unit.
         */
        @JacksonXmlProperty
        override val name: String

): Unit {

    @JacksonXmlProperty(localName = "BaseUnit")
    override val baseUnit: JacksonBaseUnit? = null

    @JacksonXmlProperty(localName = "DisplayUnit")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val displayUnits: List<JacksonDisplayUnit>? = null

    override fun toString(): String {
        return "JacksonUnit(name='$name', baseUnit=$baseUnit, displayUnits=$displayUnits)"
    }

}


/**
 * @author Lars Ivar Hatledal
 */
data class JacksonDisplayUnit(

        /**
         * Name of DisplayUnit element
         */
        @JacksonXmlProperty
        override val name: String,

        @JacksonXmlProperty
        override val factor: Double = 1.0,

        @JacksonXmlProperty
        override val offset: Double = 0.0

): DisplayUnit


/**
 * @author Lars Ivar Hatledal
 */
data class JacksonBaseUnit(

        /**
         * Exponent of SI base unit "kg"
         */
        @JacksonXmlProperty
        override val kg: Int = 0,

        /**
         * Exponent of SI base unit "m"
         */
        @JacksonXmlProperty
        override val m: Int = 0,

        /**
         * Exponent of SI base unit "s"
         */
        @JacksonXmlProperty
        override val s: Int = 0,

        /**
         * Exponent of SI base unit "A"
         */
        @JacksonXmlProperty
        override val A: Int = 0,

        /**
         * Exponent of SI base unit "K"
         */
        @JacksonXmlProperty
        override val K: Int = 0,

        /**
         * Exponent of SI base unit "mol"
         */
        @JacksonXmlProperty
        override val mol: Int = 0,

        /**
         * Exponent of SI base unit "cd"
         */
        @JacksonXmlProperty
        override val cd: Int = 0,

        /**
         * Exponent of SI base unit "rad"
         */
        @JacksonXmlProperty
        override val rad: Int = 0,

        @JacksonXmlProperty
        override val factor: Double = 1.0,

        @JacksonXmlProperty
        override val offset: Double = 0.0

): BaseUnit
