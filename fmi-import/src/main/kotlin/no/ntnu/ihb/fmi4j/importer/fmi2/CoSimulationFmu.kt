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

package no.ntnu.ihb.fmi4j.importer.fmi2

import no.ntnu.ihb.fmi4j.CoSimulationModel
import no.ntnu.ihb.fmi4j.importer.fmi2.jni.CoSimulationLibraryWrapper
import no.ntnu.ihb.fmi4j.importer.fmi2.jni.Fmi2CoSimulationLibrary
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import java.io.Closeable


class CoSimulationFmu(
    private val fmu: Fmu
) : CoSimulationModel, Closeable by fmu {

    override val modelDescription: CoSimulationModelDescription by lazy {
        fmu.modelDescription.asCoSimulationModelDescription()
    }

    private val lib: Fmi2CoSimulationLibrary by lazy {
        val modelIdentifier = modelDescription.attributes.modelIdentifier
        val lib = fmu.getAbsoluteLibraryPath(modelIdentifier)
        Fmi2CoSimulationLibrary(lib).also {
            fmu.registerLibrary(it)
        }
    }

    private fun instantiate(instanceName: String, visible: Boolean, loggingOn: Boolean): Long {
        return fmu.instantiate(lib, instanceName, fmu.guid, 1, visible, loggingOn)
    }

    override fun newInstance(): CoSimulationSlave {
        return super.newInstance() as CoSimulationSlave
    }

    override fun newInstance(instanceName: String): CoSimulationSlave {
        return newInstance(instanceName, visible = false, loggingOn = false)
    }

    fun newInstance(instanceName: String, visible: Boolean = false, loggingOn: Boolean = false): CoSimulationSlave {
        val c = instantiate(instanceName, visible, loggingOn)
        val wrapper = CoSimulationLibraryWrapper(c, lib)
        return CoSimulationSlave(instanceName, wrapper, modelDescription).also {
            fmu.registerInstance(it)
        }
    }

}
