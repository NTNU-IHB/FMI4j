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
 * @author Lars Ivar Hatledal
 */
interface ScalarVariable {

    /**
     * The full, unique name of the variable. Every variable is uniquely identified within an FMU
     * instance by this name or by its ScalarVariable index (the element position in the
     * ModelVariables list; the first list element has index=1).
     */
    val name: String

    /**
     * A handle of the variable to efficiently identify the variable value in the model interface.
     * This handle is a secret of the tool that generated the C functions. It is not required to be
     * unique. The only guarantee is that valueReference is sufficient to identify the respective variable value in the call of the C functions. This implies that it is unique for a
     * particular base data type (Real, Integer/Enumeration, Boolean, String) with
     * exception of categories that have identical values (such categories are also called “alias”
     * categories). This attribute is “required”.
     */
    val valueReference: Long

    /**
     * An optional description string describing the meaning of the variable
     */
    val description: String?

    /**
     * @see Causality
     */
    val causality: Causality?

    /**
     * @see Variability
     */
    val variability: Variability?

    /**
     * @see Initial
     */
    val initial: Initial?

    companion object {
        const val INTEGER_TYPE = "Integer"
        const val REAL_TYPE = "Real"
        const val STRING_TYPE = "String"
        const val BOOLEAN_TYPE = "Boolean"
        const val ENUMERATION_TYPE = "Enumeration"
    }

}
