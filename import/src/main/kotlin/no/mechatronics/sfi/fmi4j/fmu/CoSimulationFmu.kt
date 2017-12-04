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

import no.mechatronics.sfi.fmi4j.Fmi2Simulation
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Status
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2StatusKind
import no.mechatronics.sfi.fmi4j.jna.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.wrapper.Fmi2CoSimulationWrapper
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import java.io.File
import java.net.URL

open class CoSimulationFmu: Fmu<Fmi2CoSimulationWrapper, CoSimulationModelDescription>, Fmi2Simulation {

    class Builder(
            val fmuFile: FmuFile
    ) {

        internal var visible: Boolean = false
        internal var loggingOn: Boolean = false

        fun visible(value: Boolean) = apply { this.visible = value }
        fun loggingOn(value: Boolean) = apply { this.loggingOn = value }

        fun build() = CoSimulationFmu(this)

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

    override val wrapper: Fmi2CoSimulationWrapper by lazy {
        Fmi2CoSimulationWrapper(fmuFile.getLibraryFolderPath(), fmuFile.getLibraryName(modelDescription))
    }

    override val modelDescription: CoSimulationModelDescription by lazy {
        CoSimulationModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())
    }

    protected constructor(builder: Builder) : super(builder.fmuFile) {

        super.instantiate(Fmi2Type.CoSimulation, builder.visible, builder.loggingOn)

    }

    override fun doStep(dt: Double) : Boolean {
        val status = wrapper.doStep(currentTime, dt, true)
        currentTime += dt

        return status == Fmi2Status.OK
    }

    fun cancelStep() = wrapper.cancelStep()

    fun setRealInputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.setRealInputDerivatives(vr, order, value)

    fun getRealOutputDerivatives(vr: IntArray, order: IntArray, value: DoubleArray)
            = wrapper.getRealOutputDerivatives(vr, order, value)

    fun getStatus(s: Fmi2StatusKind) = wrapper.getStatus(s)
    fun getRealStatus(s: Fmi2StatusKind) = wrapper.getRealStatus(s)
    fun getIntegerStatus(s: Fmi2StatusKind) = wrapper.getIntegerStatus(s)
    fun getBooleanStatus(s: Fmi2StatusKind) = wrapper.getBooleanStatus(s)
    fun getStringStatus(s: Fmi2StatusKind) = wrapper.getStringStatus(s)

}


