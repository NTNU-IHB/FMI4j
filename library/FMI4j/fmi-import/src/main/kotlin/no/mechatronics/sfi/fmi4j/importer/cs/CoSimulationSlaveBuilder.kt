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

package no.mechatronics.sfi.fmi4j.importer.cs

import no.mechatronics.sfi.fmi4j.importer.Fmu
import no.mechatronics.sfi.fmi4j.importer.jni.Fmi2CoSimulationLibrary
import no.mechatronics.sfi.fmi4j.importer.misc.FmiType

class CoSimulationSlaveBuilder internal constructor(
        private val fmu: Fmu
) {

    private val modelDescription
        get() = fmu.modelDescription.asCoSimulationModelDescription()

    private val libraryCache: Fmi2CoSimulationLibrary by lazy {
        loadLibrary()
    }

    private fun loadLibrary(): Fmi2CoSimulationLibrary {
        val libName = fmu.getAbsoluteLibraryPath(modelDescription.modelIdentifier)
        return Fmi2CoSimulationLibrary(libName).also {
            fmu.registerLibrary(it)
        }
    }

    @JvmOverloads
    fun newInstance(visible: Boolean = false, loggingOn: Boolean = false): CoSimulationSlave {
        val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
        val c = fmu.instantiate(modelDescription, lib, FmiType.CO_SIMULATION, visible, loggingOn)
        val wrapper = CoSimulationLibraryWrapper(c, lib)
        return CoSimulationSlave(fmu, wrapper).also {
            fmu.registerInstance(it)
        }
    }

}