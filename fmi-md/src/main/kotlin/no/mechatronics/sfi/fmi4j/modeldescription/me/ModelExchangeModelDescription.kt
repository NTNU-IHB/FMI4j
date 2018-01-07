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

import no.mechatronics.sfi.fmi4j.modeldescription.IModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionXmlTemplate
import java.io.File
import java.io.FileInputStream
import java.net.URL
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

object ModelExchangeModelDescriptionParser {
    @JvmStatic
    fun parse(xml: String): IModelExchangeModelDescription = ModelDescriptionParser.parse(xml, ModelExchangeModelDescriptionXmlTemplate::class.java).generate()

    @JvmStatic
    fun parse(url: URL): IModelExchangeModelDescription = ModelDescriptionParser.parse(url.openStream(), ModelExchangeModelDescriptionXmlTemplate::class.java).generate()

    @JvmStatic
    fun parse(file: File): IModelExchangeModelDescription = ModelDescriptionParser.parse(FileInputStream(file), ModelExchangeModelDescriptionXmlTemplate::class.java).generate()
}


interface IModelExchangeModelDescription : IModelDescription {

    /**
     * The (fixed) number of event indicators for an FMU based on FMI for
     * Model Exchange.
     */
    val numberOfEventIndicators: Int

    val completedIntegratorStepNotNeeded: Boolean

}

/**
 *
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
@XmlRootElement(name = "fmiModelDescription")
internal class ModelExchangeModelDescriptionXmlTemplate : ModelDescriptionXmlTemplate() {

    override fun generate() = ModelExchangenModelDescriptionImpl(super.generate())

    inner class ModelExchangenModelDescriptionImpl(
            val modelDescription: IModelDescription
    ) : IModelDescription by modelDescription, IModelExchangeModelDescription {

        @XmlAttribute
        override val numberOfEventIndicators: Int = 0

        override val completedIntegratorStepNotNeeded: Boolean
            get() = me!!.completedIntegratorStepNotNeeded

    }

}

