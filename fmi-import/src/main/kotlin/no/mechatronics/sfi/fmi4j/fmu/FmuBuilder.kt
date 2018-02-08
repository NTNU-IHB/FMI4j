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

package no.mechatronics.sfi.fmi4j.fmu

import java.io.File
import java.net.URL
import com.sun.jna.Native
import com.sun.jna.Pointer
import no.mechatronics.sfi.fmi4j.misc.LibraryProvider
import no.mechatronics.sfi.fmi4j.misc.convert
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionParser
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import no.mechatronics.sfi.fmi4j.proxy.*
import no.mechatronics.sfi.fmi4j.proxy.cs.CoSimulationLibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.cs.Fmi2CoSimulationLibrary
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.proxy.me.Fmi2ModelExchangeLibrary
import no.mechatronics.sfi.fmi4j.proxy.me.ModelExchangeLibraryWrapper
import no.mechatronics.sfi.fmi4j.proxy.structs.Fmi2CallbackFunctions
import org.apache.commons.math3.ode.FirstOrderIntegrator


private const val LIBRARY_PATH = "jna.library.path"

/**
 *
 * @author Lars Ivar Hatledal
 */
class FmuBuilder(
        private val fmuFile: FmuFile
) {

    constructor(url: URL) : this(FmuFile(url))
    constructor(file: File) : this(FmuFile(file))

    private val coSimulationBuilder: CoSimulationFmuBuilder? by lazy {
        if (supportsCoSimulation) CoSimulationFmuBuilder(fmuFile) else null
    }

    private val modelExchangeBuilder: ModelExchangeFmuBuilder? by lazy {
        if (supportsModelExchange) ModelExchangeFmuBuilder(fmuFile) else null
    }

    val supportsCoSimulation: Boolean
        get() = fmuFile.modelDescription.supportsCoSimulation

    val supportsModelExchange: Boolean
        get() = fmuFile.modelDescription.supportsModelExchange

    @Throws(IllegalStateException::class)
    fun asCoSimulationFmu(): CoSimulationFmuBuilder
            = coSimulationBuilder ?: throw IllegalStateException("FMU does not support Co-Simulation!")

    @Throws(IllegalStateException::class)
    fun asModelExchangeFmu(): ModelExchangeFmuBuilder
            = modelExchangeBuilder ?: throw IllegalStateException("FMU does not support Model Exchange!")

}


class CoSimulationFmuBuilder internal constructor(
        private val fmuFile: FmuFile
) {

    private val modelDescription
        get() = fmuFile.modelDescription.asCoSimulationModelDescription()

    private val libraryCache: LibraryProvider<Fmi2CoSimulationLibrary> by lazy {
        loadLibrary()
    }

    private fun loadLibrary() = loadLibrary(fmuFile, modelDescription, Fmi2CoSimulationLibrary::class.java)

    @JvmOverloads
    fun newInstance(visible: Boolean = false, loggingOn: Boolean = false) : CoSimulationFmu {
        val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
        val c = instantiate(fmuFile, modelDescription, lib.get(), Fmi2Type.CoSimulation, visible, loggingOn)
        val wrapper = CoSimulationLibraryWrapper(c, lib)
        return CoSimulationFmu(fmuFile, wrapper)
    }

}

class ModelExchangeFmuBuilder(
        private val fmuFile: FmuFile
) {

    private val modelDescription
        get() = fmuFile.modelDescription.asModelExchangeModelDescription()

    private val libraryCache: LibraryProvider<Fmi2ModelExchangeLibrary> by lazy {
        loadLibrary()
    }

    private fun loadLibrary() = loadLibrary(fmuFile, modelDescription, Fmi2ModelExchangeLibrary::class.java)

    @JvmOverloads
    fun newInstance(visible: Boolean = false, loggingOn: Boolean = false) : ModelExchangeFmu {
        val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
        val c = instantiate(fmuFile, modelDescription, lib.get(), Fmi2Type.ModelExchange, visible, loggingOn)
        val wrapper = ModelExchangeLibraryWrapper(c, lib)
        return ModelExchangeFmu(fmuFile, wrapper)
    }

    @JvmOverloads
    fun newInstance(integrator: FirstOrderIntegrator, visible: Boolean = false, loggingOn: Boolean = false) : ModelExchangeFmuWithIntegrator {
        val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess) loadLibrary() else libraryCache
        val c = instantiate(fmuFile, modelDescription, lib.get(), Fmi2Type.ModelExchange, visible, loggingOn)
        val wrapper = ModelExchangeLibraryWrapper(c, lib)
        return ModelExchangeFmuWithIntegrator(ModelExchangeFmu(fmuFile, wrapper), integrator)
    }

}


private fun <E: Fmi2Library> loadLibrary(fmuFile: FmuFile, modelDescription: ModelDescription, type: Class<E>): LibraryProvider<E> {
    System.setProperty(LIBRARY_PATH, fmuFile.libraryFolderPath)
    return LibraryProvider(Native.loadLibrary(fmuFile.getLibraryName(modelDescription), type))
}

private fun instantiate(fmuFile: FmuFile, modelDescription: ModelDescription, library: Fmi2Library, fmiType: Fmi2Type, visible: Boolean, loggingOn: Boolean) : Pointer {
    return library.fmi2Instantiate(modelDescription.modelIdentifier,
            fmiType.code, modelDescription.guid,
            fmuFile.resourcesPath, Fmi2CallbackFunctions(),
            convert(visible), convert(loggingOn)) ?: throw AssertionError("Unable to instantiate FMU. Returned pointer is null!")
}



