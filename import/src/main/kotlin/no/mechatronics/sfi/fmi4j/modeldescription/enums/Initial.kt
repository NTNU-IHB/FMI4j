/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
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

package no.mechatronics.sfi.fmi4j.modeldescription.enums

import javax.xml.bind.annotation.adapters.XmlAdapter

enum class Initial {
    /**
     * The variable is initialized with the start value (provided under Real,
     * Integer, Boolean, String or Enumeration).
     */
    exact,
    /**
     * The variable is an iteration variable of an algebraic loop and the
     * iteration at initialization starts with the start value.
     */
    approx,
    /**
     * The variable is calculated from other variables during initialization. It
     * is not allowed to provide a “start” value.
     */
    calculated;
}


class InitialAdapter : XmlAdapter<String, Initial>() {

    @Override
    override fun unmarshal(v: String) : Initial {
        return Initial.valueOf(v);
    }

    @Override
    override fun marshal(v: Initial) : String {
        TODO("not implemented")
    }

}
