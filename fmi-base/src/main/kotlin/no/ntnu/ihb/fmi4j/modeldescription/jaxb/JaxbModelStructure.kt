package no.ntnu.ihb.fmi4j.modeldescription.jaxb

import no.ntnu.ihb.fmi4j.modeldescription.ModelStructure
import no.ntnu.ihb.fmi4j.modeldescription.Unknown

class JaxbModelStructure internal constructor(
        private val ms: FmiModelDescription.ModelStructure
): ModelStructure {

    override val outputs: List<Unknown>
        get() = ms.outputs?.unknown?.map { JaxbUnknown(it) } ?: emptyList()
    override val derivatives: List<Unknown>
        get() = ms.derivatives?.unknown?.map { JaxbUnknown(it) } ?: emptyList()
    override val initialUnknowns: List<Unknown>
        get() = ms.initialUnknowns?.unknown?.map { JaxbUnknown(it) } ?: emptyList()
}