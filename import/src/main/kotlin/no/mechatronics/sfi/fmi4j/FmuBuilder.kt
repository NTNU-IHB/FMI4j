package no.mechatronics.sfi.fmi4j

import com.sun.jna.Native
import com.sun.jna.Pointer
import no.mechatronics.sfi.fmi4j.misc.LibraryProvider
import no.mechatronics.sfi.fmi4j.misc.convert
import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.VariableBase
import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.me.ModelExchangeModelDescription
import no.mechatronics.sfi.fmi4j.proxy.*
import no.mechatronics.sfi.fmi4j.proxy.enums.Fmi2Type
import no.mechatronics.sfi.fmi4j.proxy.structs.Fmi2CallbackFunctions
import org.apache.commons.math3.ode.FirstOrderIntegrator
import java.io.File
import java.net.URL


private const val LIBRARY_PATH = "jna.library.path"

class FmuBuilder(
        private val fmuFile: FmuFile
) {

    constructor(url: URL) : this(FmuFile(url))
    constructor(file: File) : this(FmuFile(file))

    fun asCoSimulationFmu() = CoSimulationFmuBuilder(fmuFile)
    fun asModelExchangeFmu() = ModelExchangeFmuBuilder(fmuFile)
    fun asModelExchangeFmuWithIntegrator(integrator: FirstOrderIntegrator) = ModelExchangeFmuWithIntegratorBuilder(fmuFile, integrator)
}


class CoSimulationFmuBuilder(
        private val fmuFile: FmuFile
) {

    private val modelDescription: CoSimulationModelDescription

    init {
        modelDescription = CoSimulationModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())

        if (!modelDescription.isCoSimulationFmu()) {
            throw IllegalStateException("This FMU is NOT built for Co-simulation!")
        }


    }

    private val libraryProvider: LibraryProvider<Fmi2CoSimulationLibrary> by lazy {
        loadLibrary()
    }

    private fun loadLibrary() = loadLibrary(fmuFile, modelDescription, Fmi2CoSimulationLibrary::class.java)

    @JvmOverloads
    fun newInstance(visible: Boolean = false, loggingOn: Boolean = false) : CoSimulationFmu {

        val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess()) loadLibrary() else libraryProvider
        val c = instantiate(fmuFile, modelDescription, lib.get(), Fmi2Type.CoSimulation, visible, loggingOn)
        val wrapper = CoSimulationLibraryWrapper(c, lib)
        injectWrapperInVariables(modelDescription, wrapper)

        return CoSimulationFmu(fmuFile, modelDescription, wrapper)

    }

}

open class ModelExchangeFmuBuilder(
        private val fmuFile: FmuFile
) {

    private val modelDescription: ModelExchangeModelDescription

    init {
        modelDescription = ModelExchangeModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())

        if (!modelDescription.isMeSimulationFmu()) {
            throw IllegalStateException("This FMU is NOT built for Model Exchange!")
        }

    }

    private val libraryProvider: LibraryProvider<Fmi2ModelExchangeLibrary> by lazy {
        loadLibrary()
    }

    private fun loadLibrary() = loadLibrary(fmuFile, modelDescription, Fmi2ModelExchangeLibrary::class.java)

    @JvmOverloads
    fun newInstance(visible: Boolean = false, loggingOn: Boolean = false) : ModelExchangeFmu {

        val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess()) loadLibrary() else libraryProvider
        val c = instantiate(fmuFile, modelDescription, lib.get(), Fmi2Type.ModelExchange, visible, loggingOn)
        val wrapper = ModelExchangeLibraryWrapper(c, lib)
        injectWrapperInVariables(modelDescription, wrapper)

        return ModelExchangeFmu(fmuFile, modelDescription, wrapper)

    }

}

open class ModelExchangeFmuWithIntegratorBuilder(
        private val fmuFile: FmuFile,
        private val integrator: FirstOrderIntegrator
)  {

    private val modelDescription: ModelExchangeModelDescription

    init {
        modelDescription = ModelExchangeModelDescription.parseModelDescription(fmuFile.getModelDescriptionXml())

        if (!modelDescription.isMeSimulationFmu()) {
            throw IllegalStateException("This FMU is NOT built for Model Exchange!")
        }

    }

    private val library: LibraryProvider<Fmi2ModelExchangeLibrary> by lazy {
        loadLibrary()
    }

    private fun loadLibrary() = loadLibrary(fmuFile, modelDescription, Fmi2ModelExchangeLibrary::class.java)

    @JvmOverloads
    fun newInstance(visible: Boolean = false, loggingOn: Boolean = false) : ModelExchangeFmuWithIntegrator {

        val lib = if (modelDescription.canBeInstantiatedOnlyOncePerProcess()) loadLibrary() else library
        val c = instantiate(fmuFile, modelDescription, lib.get(), Fmi2Type.ModelExchange, visible, loggingOn)
        val wrapper = ModelExchangeLibraryWrapper(c, library)
        injectWrapperInVariables(modelDescription, wrapper)

        return ModelExchangeFmuWithIntegrator(ModelExchangeFmu(fmuFile, modelDescription, wrapper), integrator)

    }

}

private fun <E: Fmi2Library> loadLibrary(fmuFile: FmuFile, modelDescription: ModelDescription, type: Class<E>): LibraryProvider<E> {
    System.setProperty(LIBRARY_PATH, fmuFile.getLibraryFolderPath())
    return LibraryProvider(Native.loadLibrary(fmuFile.getLibraryName(modelDescription), type))
}

private fun instantiate(fmuFile: FmuFile, modelDescription: ModelDescription, library: Fmi2Library, fmiType: Fmi2Type, visible: Boolean, loggingOn: Boolean) : Pointer {
    return library.fmi2Instantiate(modelDescription.modelIdentifier,
            fmiType.code, modelDescription.guid,
            fmuFile.getResourcesPath(), Fmi2CallbackFunctions.ByValue(),
            convert(visible), convert(loggingOn))
}


fun injectWrapperInVariables(modelDescription: ModelDescription, wrapper: Fmi2LibraryWrapper<*>) {

    val f = VariableBase::class.java.getDeclaredField("wrapper")
    f.isAccessible = true
    modelDescription.modelVariables.forEach{
        f.set(it, wrapper)
    }

}


