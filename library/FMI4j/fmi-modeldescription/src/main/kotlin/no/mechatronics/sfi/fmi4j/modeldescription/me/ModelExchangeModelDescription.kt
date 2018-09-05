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

package no.mechatronics.sfi.fmi4j.modeldescription.me

import no.mechatronics.sfi.fmi4j.modeldescription.SpecificModelDescription


/**
 * @author Lars Ivar Hatledal
 */
interface ModelExchangeModelDescription : SpecificModelDescription {

    /**
     * The (fixed) number of event indicators for an FMU based on FMI for
     * Model Exchange.
     */
    val numberOfEventIndicators: Int

    /**
     * If true, function
     * fmi2CompletedIntegratorStep need not to
     * be called (which gives a slightly more efficient
     * integration). If it is called, it has no effect.
     * If false (the default), the function must be called
     * after every completed integrator step, see
     * section 3.2.2.
     */
    val completedIntegratorStepNotNeeded: Boolean
}
