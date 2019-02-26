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

package no.ntnu.ihb.fmi4j.importer

import no.ntnu.ihb.fmi4j.importer.jni.Fmi2ModelExchangeLibrary
import no.ntnu.ihb.fmi4j.importer.me.ModelExchangeFmuStepper
import no.ntnu.ihb.fmi4j.importer.me.ModelExchangeInstance
import no.ntnu.ihb.fmi4j.importer.me.ModelExchangeLibraryWrapper
import no.ntnu.ihb.fmi4j.importer.misc.FmiType
import no.ntnu.ihb.fmi4j.modeldescription.ModelExchangeModelDescription
import no.ntnu.ihb.fmi4j.solvers.Solver

/**
 *
 * @author Lars Ivar Hatledal
 */
class ModelExchangeFmu(
        private val fmu: Fmu
): IFmu by fmu {

    override val modelDescription: ModelExchangeModelDescription by lazy {
        fmu.modelDescription.asModelExchangeModelDescription()
    }

    private val libraryCache: Fmi2ModelExchangeLibrary by lazy {
        loadLibrary()
    }

    private fun loadLibrary(): Fmi2ModelExchangeLibrary {
        val modelIdentifier = modelDescription.attributes.modelIdentifier
        val libName = fmu.getAbsoluteLibraryPath(modelIdentifier)
        return Fmi2ModelExchangeLibrary(libName).also {
            fmu.registerLibrary(it)
        }
    }

    @JvmOverloads
    fun newInstance(visible: Boolean = false, loggingOn: Boolean = false): ModelExchangeInstance {
        val lib = if (modelDescription.attributes.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
        val c = fmu.instantiate(modelDescription, lib, FmiType.MODEL_EXCHANGE, visible, loggingOn)
        val wrapper = ModelExchangeLibraryWrapper(c, lib)
        return ModelExchangeInstance(wrapper, modelDescription).also {
            fmu.registerInstance(it)
        }
    }

    @JvmOverloads
    fun newInstance(solver: Solver, visible: Boolean = false, loggingOn: Boolean = false): ModelExchangeFmuStepper {
        return newInstance(visible, loggingOn).let {
            ModelExchangeFmuStepper(it, solver)
        }
    }

}