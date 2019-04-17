package no.ntnu.ihb.fmi4j.modeldescription.jaxb

import no.ntnu.ihb.fmi4j.modeldescription.ModelStructure
import no.ntnu.ihb.fmi4j.modeldescription.Unknown

fun FmiModelDescription.ModelStructure.convert(): ModelStructure {
    return ModelStructure(
            outputs = this@convert.outputs?.unknown?.map { it.convert() } ?: emptyList(),
            derivatives = this@convert.derivatives?.unknown?.map { it.convert() } ?: emptyList(),
            initialUnknowns = this@convert.initialUnknowns?.unknown?.map { it.convert() } ?: emptyList()
    )
}
