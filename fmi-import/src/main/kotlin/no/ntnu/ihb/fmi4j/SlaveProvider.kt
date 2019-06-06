package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.modeldescription.ModelDescription
import java.io.Closeable

interface SlaveProvider: Closeable {

    val guid: String
        get() = modelDescription.guid

    val modelName: String
        get() = modelDescription.modelName

    val modelDescription: ModelDescription

    fun newInstance(): FmuSlave

}