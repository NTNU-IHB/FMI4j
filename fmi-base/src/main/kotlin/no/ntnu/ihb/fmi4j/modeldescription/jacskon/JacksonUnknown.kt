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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.Unknown


/**
 * @author Lars Ivar Hatledal
 */
class JacksonUnknown(

        @JacksonXmlProperty
        override var index: Int,

        @JacksonXmlProperty(localName = "dependencies")
        private var _dependencies: String? = null,

        @JacksonXmlProperty(localName = "dependenciesKind")
        private val _dependenciesKind: String? = null

) : Unknown {

    override val dependencies: List<Int>
        get() = _dependencies?.let {
            it.split(" ").mapNotNull { it.toIntOrNull() }
        } ?: emptyList()

    override val dependenciesKind: List<String>
        get() = _dependenciesKind?.trim()?.split(" ")?.mapNotNull { if(it.isEmpty()) null else it } ?: emptyList()

    override fun toString(): String {
        return "JacksonUnknown(index=$index, dependencies=$dependencies, dependenciesKind=$dependenciesKind)"
    }

}