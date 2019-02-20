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

package no.ntnu.ihb.fmi4j.modeldescription.variables


/**
 * Enumeration that defines how the variable is initialized. It is not allowed to provide a
 * value for initial if causality = "input" or "independent":
 * • = "exact": The variable is initialized with the start value (provided under Real,
 * Integer, Boolean, String or Enumeration).
 * • = "approx": The variable is an iteration variable of an algebraic loop and the
 * iteration at initialization starts with the start value.
 * • = "calculated": The variable is calculated from other categories during initialization.
 * It is not allowed to provide a “start” value.
 * If initial is not present, it is defined by the table below based on causality and
 * variability. If initial = exact or approx, or causality = ″input″ a start
 * value must be provided. If initial = calculated, or causality = ″independent″ it is
 * not allowed to provide a start value.
 * [The environment decides when to use the start value of a variable with causality =
 * ″input″. Examples: (a) automatic tests of FMUs are performed, and the FMU is tested
 * by providing the start value as constant input. (b) For a ModelExchange FMU, the
 * FMU might be part of an algebraic loop. If the input variable is iteration variable of this
 * algebraic loop, then initialization starts with its start value.].
 * If fmiSetXXX is not called on a variable with causality = ″input″ then the FMU must
 * use the start value as value of this input.
 *
 * @author Lars Ivar Hatledal
 */
enum class Initial {

    /**
     * The variable is initialized with the start value (provided under Real,
     * Integer, Boolean, String or Enumeration).
     */
    EXACT,

    /**
     * The variable is an iteration variable of an algebraic loop and the
     * iteration at initialization starts with the start value.
     */
    APPROX,

    /**
     * The variable is calculated from other categories during initialization. It
     * is not allowed to provide a “start” value.
     */
    CALCULATED,

    /**
     * Unknown initial
     */
    UNKNOWN;
}

