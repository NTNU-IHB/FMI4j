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

package no.ntnu.ihb.fmi4j.importer.fmi1

import no.ntnu.ihb.fmi4j.CoSimulationModel
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.CoSimulationLibraryWrapper
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.Fmi1CoSimulationLibrary
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.CommonModelDescription
import java.io.Closeable


class CoSimulationFmu(
        private val fmu: Fmu
) : CoSimulationModel, Closeable by fmu {

    override val modelDescription: CoSimulationModelDescription by lazy {
        fmu.modelDescription.asCoSimulationModelDescription()
    }

    private val lib: Fmi1CoSimulationLibrary by lazy {
        val modelIdentifier = modelDescription.attributes.modelIdentifier
        val lib = fmu.getAbsoluteLibraryPath(modelIdentifier)
        Fmi1CoSimulationLibrary(lib, modelIdentifier).also {
            fmu.registerLibrary(it)
        }
    }

    private fun instantiate(modelDescription: CommonModelDescription, loggingOn: Boolean): Long {
        return lib.instantiateSlave(modelDescription.attributes.modelIdentifier,
                modelDescription.guid, fmu.fmuPath, loggingOn)
    }

    override fun newInstance(): CoSimulationSlave {
        return newInstance(loggingOn = false)
    }

    fun newInstance(loggingOn: Boolean = false): CoSimulationSlave {
        val c = instantiate(modelDescription, loggingOn)
        val wrapper = CoSimulationLibraryWrapper(c, lib)
        return CoSimulationSlave(wrapper, modelDescription).also {
            fmu.registerInstance(it)
        }
    }

}
