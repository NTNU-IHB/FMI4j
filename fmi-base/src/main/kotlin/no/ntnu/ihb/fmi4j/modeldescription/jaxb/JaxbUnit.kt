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


package no.ntnu.ihb.fmi4j.modeldescription.jaxb

import no.ntnu.ihb.fmi4j.modeldescription.BaseUnit
import no.ntnu.ihb.fmi4j.modeldescription.DisplayUnit
import no.ntnu.ihb.fmi4j.modeldescription.Unit

class JaxbUnit internal constructor(
        private val unit: Fmi2Unit
): Unit {

    override val name: String
        get() = unit.name
    override val baseUnit: BaseUnit?
        get() = unit.baseUnit?.let { JaxbBaseUnit(it) }
    override val displayUnits: List<DisplayUnit>?
        get() = unit.displayUnit?.map { JaxbDisplayUnit(it) }
}

class JaxbBaseUnit internal constructor(
    private val baseUnit: Fmi2Unit.BaseUnit
): BaseUnit {

    override val kg: Int
        get() = baseUnit.kg
    override val m: Int
        get() = baseUnit.m
    override val s: Int
        get() = baseUnit.s
    override val A: Int
        get() = baseUnit.a
    override val K: Int
        get() = baseUnit.k
    override val mol: Int
        get() = baseUnit.mol
    override val cd: Int
        get() = baseUnit.cd
    override val rad: Int
        get() = baseUnit.rad
    override val factor: Double
        get() = baseUnit.factor
    override val offset: Double
        get() = baseUnit.offset

}

class JaxbDisplayUnit internal constructor(
    private val displayUnit: Fmi2Unit.DisplayUnit
): DisplayUnit {

    override val name: String
        get() = displayUnit.name
    override val factor: Double
        get() = displayUnit.factor
    override val offset: Double
        get() = displayUnit.offset

}
