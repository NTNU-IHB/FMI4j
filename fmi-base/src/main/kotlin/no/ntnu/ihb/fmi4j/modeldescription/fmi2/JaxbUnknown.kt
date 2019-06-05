package no.ntnu.ihb.fmi4j.modeldescription.fmi2

import no.ntnu.ihb.fmi4j.modeldescription.Unknown


fun Fmi2VariableDependency.Unknown.convert(): Unknown {
    return Unknown(
            index =  this@convert.getIndex().toInt(),
            dependencies =  this@convert.dependencies?.map { it.toInt() } ?: emptyList(),
            dependenciesKind = this@convert.dependenciesKind ?: emptyList()
    )
}

fun FmiModelDescription.ModelStructure.InitialUnknowns.Unknown.convert(): Unknown {
    return Unknown(
            index =  this@convert.getIndex().toInt(),
            dependencies =  this@convert.dependencies?.map { it.toInt() } ?: emptyList(),
            dependenciesKind = this@convert.dependenciesKind ?: emptyList()
    )
}
