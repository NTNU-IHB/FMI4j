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

import no.ntnu.ihb.fmi4j.importer.AbstractFmu
import no.ntnu.ihb.fmi4j.importer.fmi1.jni.Fmi1Library
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionProvider
import no.ntnu.ihb.fmi4j.util.OsUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL

/**
 * Represents an FMU
 *
 * @author Lars Ivar Hatledal
 */
class Fmu internal constructor(
    name: String,
    extractedFmu: File
) : AbstractFmu(name, extractedFmu) {

    private val libraries = mutableListOf<Fmi1Library>()
    private val instances = mutableListOf<AbstractModelInstance<*, *>>()

    private val coSimulationFmu by lazy {
        CoSimulationFmu(this)
    }

    private val modelExchangeFmu by lazy {
        ModelExchangeFmu(this)
    }

    override val modelDescription: ModelDescriptionProvider by lazy {
        ModelDescriptionParser.parse(modelDescriptionXml)
    }

    internal val fmuPath: String
        get() = "file:///${extractedFmu.absolutePath.replace("\\", "/")}"

    override fun asCoSimulationFmu(): CoSimulationFmu {
        if (!supportsCoSimulation) {
            throw IllegalStateException("FMU does not support Co-simulation!")
        }
        return coSimulationFmu
    }

    override fun asModelExchangeFmu(): ModelExchangeFmu {
        if (!supportsModelExchange) {
            throw IllegalStateException("FMU does not support Model Exchange!")
        }
        return modelExchangeFmu
    }

    /**
     * Get the absolute name of the native library on the form "C://folder/name.extension"
     */
    internal fun getAbsoluteLibraryPath(modelIdentifier: String): File {
        return File(
            extractedFmu, BINARIES_FOLDER + File.separator + OsUtil.libraryFolderName + OsUtil.platformBitness
                    + File.separator + modelIdentifier + "." + OsUtil.libExtension
        )
    }

    internal fun registerLibrary(library: Fmi1Library) {
        libraries.add(library)
    }

    internal fun registerInstance(instance: AbstractModelInstance<*, *>) {
        instances.add(instance)
    }

    override fun disposeNativeLibraries() {
        libraries.forEach {
            it.close()
        }
        libraries.clear()
        System.gc()
    }

    override fun terminateInstances() {
        instances.forEach { instance ->
            if (!instance.isTerminated) {
                instance.terminate()
            }
            if (!instance.wrapper.isInstanceFreed) {
                instance.wrapper.freeInstance()
            }
        }
        instances.clear()
    }

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Fmu::class.java)

        /**
         * Creates an FMU from the provided File
         */
        @JvmStatic
        @Throws(IOException::class, FileNotFoundException::class)
        fun from(file: File): Fmu {
            return AbstractFmu.from(file) as Fmu
        }

        /**
         * Creates an FMU from the provided URL.
         */
        @JvmStatic
        @Throws(IOException::class)
        fun from(url: URL): Fmu {
            return AbstractFmu.from(url) as Fmu
        }

        /**
         * Creates an FMU from the provided name and byte array.
         */
        @Throws(IOException::class)
        fun from(name: String, data: ByteArray): Fmu {
            return AbstractFmu.from(name, data) as Fmu
        }

    }

}
