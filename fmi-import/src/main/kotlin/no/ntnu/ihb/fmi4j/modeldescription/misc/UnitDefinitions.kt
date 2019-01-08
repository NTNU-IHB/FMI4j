/*
 * The MIT License
 *
 * Copyright 2017-2018 Norwegian University of Technology
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

package no.ntnu.ihb.fmi4j.modeldescription.misc

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.io.Serializable

typealias UnitDefinitions = List<Unit>

/**
 * @author Lars Ivar Hatledal
 */
class Unit(

        /**
         * Name of Unit element, e.g. "N.m", "Nm", "%/s".
         * "name" must be unique with respect to other elements of the
         * UnitDefinitions list. The variable values of fmi2SetXXX and fmi2GetXXX
         * are with respect to this unit.
         */
        @JacksonXmlProperty
        val name: String

) : Serializable {

    @JacksonXmlProperty(localName = "BaseUnit")
    val baseUnit: BaseUnit? = null

    @JacksonXmlProperty(localName = "DisplayUnit")
    @JacksonXmlElementWrapper(useWrapping = false)
    val displayUnits: List<DisplayUnit>? = null

    override fun toString(): String {
        return "Unit(name='$name', baseUnit=$baseUnit, displayUnits=$displayUnits)"
    }

}

/**
 * @author Lars Ivar Hatledal
 */
data class BaseUnit(
        /**
         * Exponent of SI base unit "kg"
         */
        @JacksonXmlProperty
        val kg: Int = 0,

        /**
         * Exponent of SI base unit "m"
         */
        @JacksonXmlProperty
        val m: Int = 0,

        /**
         * Exponent of SI base unit "s"
         */
        @JacksonXmlProperty
        val s: Int = 0,

        /**
         * Exponent of SI base unit "A"
         */
        @JacksonXmlProperty
        val A: Int = 0,

        /**
         * Exponent of SI base unit "K"
         */
        @JacksonXmlProperty
        val K: Int = 0,

        /**
         * Exponent of SI base unit "mol"
         */
        @JacksonXmlProperty
        val mol: Int = 0,

        /**
         * Exponent of SI base unit "cd"
         */
        @JacksonXmlProperty
        val cd: Int = 0,

        /**
         * Exponent of SI base unit "rad"
         */
        @JacksonXmlProperty
        val rad: Int = 0,

        @JacksonXmlProperty
        val factor: Double = 1.0,

        @JacksonXmlProperty
        val offset: Double = 0.0
)

/**
 * @author Lars Ivar Hatledal
 */
data class DisplayUnit(

        /**
         * Name of DisplayUnit element
         */
        @JacksonXmlProperty
        val name: String,

        @JacksonXmlProperty
        val factor: Double = 1.0,

        @JacksonXmlProperty
        val offset: Double = 0.0

)
