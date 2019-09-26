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


package no.ntnu.ihb.fmi4j.modeldescription.fmi2

import no.ntnu.ihb.fmi4j.modeldescription.BaseUnit
import no.ntnu.ihb.fmi4j.modeldescription.DisplayUnit
import no.ntnu.ihb.fmi4j.modeldescription.Unit

fun Fmi2Unit.convert(): Unit {
    return Unit(
            name = this@convert.name,
            baseUnit = this@convert.baseUnit?.convert(),
            displayUnits = this@convert.displayUnit?.map { it.convert() }
    )
}

fun Fmi2Unit.BaseUnit.convert(): BaseUnit {
    return BaseUnit(
            kg = this@convert.kg ?: 0,
            m = this@convert.m ?: 0,
            s = this@convert.s ?: 0,
            A = this@convert.a ?: 0,
            K = this@convert.k ?: 0,
            mol = this@convert.mol ?: 0,
            cd = this@convert.cd ?: 0,
            rad = this@convert.rad ?: 0,
            factor = this@convert.factor ?: 1.0,
            offset = this@convert.offset ?: 0.0
    )
}

fun Fmi2Unit.DisplayUnit.convert(): DisplayUnit {
    return DisplayUnit(
            name = this@convert.name,
            factor = this@convert.factor ?: 1.0,
            offset = this@convert.offset ?: 0.0
    )
}
