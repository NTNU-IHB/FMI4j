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

package no.mechatronics.sfi.fmi4j.modeldescription.me

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import java.io.File
import java.io.FileInputStream
import java.net.URL
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement


/**
 *
 * @author Lars Ivar Hatledal laht@ntnu.no.
 */
@XmlRootElement(name = "fmiModelDescription")
class ModelExchangeModelDescription : ModelDescription() {

    companion object {
        @JvmStatic
        fun parseModelDescription(xml: String) : ModelExchangeModelDescription = ModelDescription.parseModelDescription(xml, ModelExchangeModelDescription::class.java)
        @JvmStatic
        fun parseModelDescription(url: URL): ModelExchangeModelDescription = ModelDescription.parseModelDescription(url.openStream(), ModelExchangeModelDescription::class.java)
        @JvmStatic
        fun parseModelDescription(file: File): ModelExchangeModelDescription = ModelDescription.parseModelDescription(FileInputStream(file), ModelExchangeModelDescription::class.java)
    }

    /**
     * The (fixed) number of event indicators for an FMU based on FMI for
     * Model Exchange.
     */
    @XmlAttribute
    val numberOfEventIndicators: Int = 0

    fun needsExecutionTool(): Boolean {
        return me!!.needsExecutionTool
    }

    fun completedIntegratorStepNotNeeded(): Boolean {
        return me!!.completedIntegratorStepNotNeeded
    }

    fun canBeInstantiatedOnlyOncePerProcess(): Boolean {
        return me!!.canBeInstantiatedOnlyOncePerProcess
    }

    fun canNotUseMemoryManagementFunctions(): Boolean {
        return me!!.canNotUseMemoryManagementFunctions
    }

    fun canGetAndSetFMUstate(): Boolean {
        return me!!.canGetAndSetFMUstate
    }

    fun canSerializeFMUstate(): Boolean {
        return me!!.canSerializeFMUstate
    }

    fun providesDirectionalDerivative(): Boolean {
        return me!!.providesDirectionalDerivative
    }

}
