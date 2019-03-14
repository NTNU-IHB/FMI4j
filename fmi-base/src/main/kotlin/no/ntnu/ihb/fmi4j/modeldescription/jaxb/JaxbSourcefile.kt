package no.ntnu.ihb.fmi4j.modeldescription.jaxb

import no.ntnu.ihb.fmi4j.modeldescription.SourceFile

class JaxbSourcefile private constructor(
        override val name: String
): SourceFile {

    internal constructor(
            file: FmiModelDescription.CoSimulation.SourceFiles.File
    ) : this(file.name)

    internal constructor(
            file: FmiModelDescription.ModelExchange.SourceFiles.File
    ) : this(file.name)

}
