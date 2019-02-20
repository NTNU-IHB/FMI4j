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

package no.ntnu.ihb.fmi4j.modeldescription.structure

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.io.Serializable

/**
 *
 * Dependency of scalar Unknown from Knowns in continuous-time and event mode (Model Exchange),
 * and at communications points (Co-simulation)
 *
 * @author Lars Ivar Hatledal
 */
interface Unknown {

    /**
     * ScalarVariable index of Unknown
     */
    val index: Int

    /**
     * Defines the dependency of the Unknown (directly or indirectly via auxiliary variables)
     * on the Knowns in Continuous-Time and Event Mode (ModelExchange) and at Communication Points (CoSimulation)
     */
    val dependencies: List<Int>

    /**
     * If present, it must be assumed that the Unknown depends on the Knowns
     * without a particular structure.
     */
    val dependenciesKind: String?

}

/**
 * @author Lars Ivar Hatledal
 */
class UnknownImpl(

        @JacksonXmlProperty
        override var index: Int,

        @JacksonXmlProperty(localName = "dependencies")
        private var _dependencies: String? = null,

        @JacksonXmlProperty
        override val dependenciesKind: String? = null

) : Unknown, Serializable {

    override val dependencies: List<Int>
        get() = _dependencies?.let {
            it.split(" ").mapNotNull { it.toIntOrNull() }
        } ?: emptyList()

    override fun toString(): String {
        return "UnknownImpl(index=$index, dependencies=$dependencies, dependenciesKind=$dependenciesKind)"
    }

}