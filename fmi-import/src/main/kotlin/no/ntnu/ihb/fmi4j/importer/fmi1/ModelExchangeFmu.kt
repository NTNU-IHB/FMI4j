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

import no.ntnu.ihb.fmi4j.ModelExchangeModel
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.Fmi1ModelExchangeLibrary
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.FmiComponent
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.ModelExchangeLibraryWrapper
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

    private val lib: Fmi1ModelExchangeLibrary by lazy {
        val modelIdentifier = modelDescription.attributes.modelIdentifier
        val lib = fmu.getAbsoluteLibraryPath(modelIdentifier)
        Fmi1ModelExchangeLibrary(lib, modelIdentifier).also {
            fmu.registerLibrary(it)
        }
    }

    private fun instantiate(instanceName: String, loggingOn: Boolean): FmiComponent {
        return lib.instantiateModel(instanceName, fmu.guid, loggingOn)
    }

    override fun newInstance(instanceName: String): ModelExchangeInstance {
        return newInstance(instanceName, false)
    }

    fun newInstance(instanceName: String, loggingOn: Boolean = false): ModelExchangeInstance {
        val c = instantiate(instanceName, loggingOn)
        val wrapper = ModelExchangeLibraryWrapper(c, lib)
        return ModelExchangeInstance(instanceName, wrapper, modelDescription).also {
            fmu.registerInstance(it)
        }
    }

}
