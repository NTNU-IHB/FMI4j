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

import no.ntnu.ihb.fmi4j.ModelExchangeModel
import no.ntnu.ihb.fmi4j.importer.fmi2.jni.Fmi2ModelExchangeLibrary
import no.ntnu.ihb.fmi4j.importer.fmi2.jni.ModelExchangeLibraryWrapper
import no.ntnu.ihb.fmi4j.modeldescription.ModelExchangeModelDescription
import java.io.Closeable

/**
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeFmu(
    private val fmu: Fmu
) : ModelExchangeModel, Closeable by fmu {

    override val modelDescription: ModelExchangeModelDescription by lazy {
        fmu.modelDescription.asModelExchangeModelDescription()
    }

    private val lib: Fmi2ModelExchangeLibrary by lazy {
        val modelIdentifier = modelDescription.attributes.modelIdentifier
        val lib = fmu.getAbsoluteLibraryPath(modelIdentifier)
        Fmi2ModelExchangeLibrary(lib).also {
            fmu.registerLibrary(it)
        }
    }

    private fun instantiate(instanceName: String, visible: Boolean, loggingOn: Boolean): Long {
        return fmu.instantiate(lib, instanceName, fmu.guid, 0, visible, loggingOn)
    }

    override fun newInstance(instanceName: String): ModelExchangeInstance {
        return newInstance(instanceName, visible = false, loggingOn = false)
    }

    fun newInstance(instanceName: String, visible: Boolean = false, loggingOn: Boolean = false): ModelExchangeInstance {
        val c = instantiate(instanceName, visible, loggingOn)
        val wrapper = ModelExchangeLibraryWrapper(c, lib)
        return ModelExchangeInstance(instanceName, wrapper, modelDescription).also {
            fmu.registerInstance(it)
        }
    }

}
