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

package no.mechatronics.sfi.fmi4j.fmu

import no.mechatronics.sfi.fmi4j.jna.structs.Fmi2EventInfo
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.wrapper.Fmi2ModelExchangeWrapper
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import java.io.File
import java.net.URL

open class ModelExchangeFmu : Fmu<Fmi2ModelExchangeWrapper, ModelExchangeModelDescription> {

    open class Builder(
            val fmuFile: FmuFile
    ) {

        internal var visible: Boolean = false
        internal var loggingOn: Boolean = false

        open fun visible(value: Boolean) = apply { this.visible = value }
        open fun loggingOn(value: Boolean)= apply { this.loggingOn = value }

        open fun build() = ModelExchangeFmu(this)

    }

    val ode: FirstOrderDifferentialEquations by lazy {
        object : FirstOrderDifferentialEquations {
            override fun getDimension(): Int =  modelDescription.numberOfContinuousStates

            override fun computeDerivatives(t: Double, y: DoubleArray?, yDot: DoubleArray?) {
                getDerivatives(yDot!!)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newBuilder(fmuFile: FmuFile) = Builder(fmuFile)
        @JvmStatic
        fun newBuilder(url: URL) = Builder(FmuFile(url))
        @JvmStatic
        fun newBuilder(file: File) = Builder(FmuFile(file))
        fun build(fmuFile: FmuFile, block: Builder.() -> Unit) = Builder(fmuFile).apply(block).build()
        fun build(url: URL, block: Builder.() -> Unit) = Builder(FmuFile(url)).apply(block).build()
        fun build(file: File, block: Builder.() -> Unit) = Builder(FmuFile(file)).apply(block).build()
    }

    override val wrapper: Fmi2ModelExchangeWrapper by lazy {
        Fmi2ModelExchangeWrapper(fmuFile.getLibraryFolderPath(), fmuFile.getLibraryName(modelDescription))
    }

    override val modelDescription: ModelExchangeModelDescription by lazy {
        ModelExchangeModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())
    }

    protected constructor(builder: Builder) : super(builder.fmuFile) {
        super.instantiate(Fmi2Type.ModelExchange, builder.visible, builder.loggingOn)
    }

    fun setTime(time: Double) {
        currentTime = time
        wrapper.setTime(currentTime)
    }

    fun setContinousStates(x: DoubleArray) = wrapper.setContinousStates(x)

    fun enterEventMode() = wrapper.enterEventMode()

    fun enterContinuousTimeMode() = wrapper.enterContinuousTimeMode()

    fun newDiscreteStates(eventInfo: Fmi2EventInfo) = wrapper.newDiscreteStates(eventInfo)

    fun completedIntegratorStep() = wrapper.completedIntegratorStep()

    fun getDerivatives(derivatives: DoubleArray) = wrapper.getDerivatives(derivatives)

    fun getEventIndicators(eventIndicators: DoubleArray) = wrapper.getEventIndicators(eventIndicators)

    fun getContinuousStates(x: DoubleArray) = wrapper.getContinuousStates(x)

    fun getNominalsOfContinuousStates(x_nominal: DoubleArray) = wrapper.getNominalsOfContinuousStates(x_nominal)

}


