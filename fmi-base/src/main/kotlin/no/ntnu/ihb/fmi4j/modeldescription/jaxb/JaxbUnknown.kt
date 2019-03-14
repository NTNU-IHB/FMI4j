package no.ntnu.ihb.fmi4j.modeldescription.jaxb

import no.ntnu.ihb.fmi4j.modeldescription.Unknown

class JaxbUnknown private constructor(

        override val index: Int,
        override val dependencies: List<Int>,
        override val dependenciesKind: List<String>

) : Unknown {

    internal constructor(
            unknown: Fmi2VariableDependency.Unknown
    ) : this(
            unknown.index.toInt(),
            unknown.dependencies?.map { it.toInt() } ?: emptyList(),
            unknown.dependenciesKind ?: emptyList()
    )

    internal constructor(
            unknown: FmiModelDescription.ModelStructure.InitialUnknowns.Unknown
    ) : this(
            unknown.index.toInt(),
            unknown.dependencies?.map { it.toInt() } ?: emptyList(),
            unknown.dependenciesKind ?: emptyList()
    )

}