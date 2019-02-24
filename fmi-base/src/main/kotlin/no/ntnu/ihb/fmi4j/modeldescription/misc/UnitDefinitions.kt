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

import java.io.Serializable

typealias UnitDefinitions = List<Unit>

/**
 * @author Lars Ivar Hatledal
 */
data class Unit(

        /**
         * Name of Unit element, e.g. "N.m", "Nm", "%/s".
         * "name" must be unique with respect to other elements of the
         * UnitDefinitions list. The variable values of fmi2SetXXX and fmi2GetXXX
         * are with respect to this unit.
         */
        val name: String,

        val baseUnit: BaseUnit? = null,
        
        val displayUnits: List<DisplayUnit>? = null

) : Serializable

/**
 * @author Lars Ivar Hatledal
 */
data class BaseUnit(

        /**
         * Exponent of SI base unit "kg"
         */
        val kg: Int? = null,

        /**
         * Exponent of SI base unit "m"
         */
        val m: Int? = null,

        /**
         * Exponent of SI base unit "s"
         */
        val s: Int? = null,

        /**
         * Exponent of SI base unit "A"
         */
        val A: Int? = null,

        /**
         * Exponent of SI base unit "K"
         */
        val K: Int? = null,

        /**
         * Exponent of SI base unit "mol"
         */
        
        val mol: Int? = null,

        /**
         * Exponent of SI base unit "cd"
         */
        val cd: Int? = null,

        /**
         * Exponent of SI base unit "rad"
         */
        val rad: Int? = null,

        val factor: Double = 1.0,

        val offset: Double = 0.0
)

/**
 * @author Lars Ivar Hatledal
 */
data class DisplayUnit(

        /**
         * Name of DisplayUnit element
         */
        val name: String,
        
        val factor: Double = 1.0,

        val offset: Double = 0.0

)
