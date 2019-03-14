/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
