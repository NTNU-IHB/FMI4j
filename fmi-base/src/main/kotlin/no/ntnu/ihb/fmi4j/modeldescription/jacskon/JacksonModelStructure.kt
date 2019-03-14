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

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import no.ntnu.ihb.fmi4j.modeldescription.ModelStructure
import no.ntnu.ihb.fmi4j.modeldescription.Unknown

/**
 *
 * @author Lars Ivar Hatledal
 */
class JacksonModelStructure(): ModelStructure {

    @JacksonXmlProperty(localName = "Outputs")
    private val _outputs: Outputs? = null

    @JacksonXmlProperty(localName = "Derivatives")
    private val _derivatives: Derivatives? = null

    @JacksonXmlProperty(localName = "InitialUnknowns")
    private val _initialUnknowns: InitialUnknowns? = null

    private constructor(@Suppress("UNUSED_PARAMETER") dummy: String): this()

    override val outputs: List<Unknown>
        get() = _outputs?.unknowns ?: emptyList()

    override val derivatives: List<Unknown>
        get() = _derivatives?.unknowns ?: emptyList()

    override val initialUnknowns: List<Unknown>
        get() = _initialUnknowns?.unknowns ?: emptyList()

    override fun toString(): String {
        return "JacksonModelStructure(outputs=$outputs, derivatives=$derivatives, initialUnknowns=$initialUnknowns)"
    }

}

/**
 *
 * @author Lars Ivar Hatledal
 */
private class Outputs(

        @JacksonXmlProperty(localName = "Unknown")
        @JacksonXmlElementWrapper(useWrapping = false)
        val unknowns: List<JacksonUnknown>? = null

)

/**
 *
 * @author Lars Ivar Hatledal
 */
private class Derivatives(

        @JsonProperty("Unknown")
        @JacksonXmlElementWrapper(useWrapping = false)
        val unknowns: List<JacksonUnknown>? = null

)

/**
 *
 * @author Lars Ivar Hatledal
 */
private class InitialUnknowns(

        @JsonProperty("Unknown")
        @JacksonXmlElementWrapper(useWrapping = false)
        val unknowns: List<JacksonUnknown>? = null

)
