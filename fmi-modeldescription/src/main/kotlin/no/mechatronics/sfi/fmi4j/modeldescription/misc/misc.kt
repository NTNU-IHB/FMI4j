package no.mechatronics.sfi.fmi4j.modeldescription.misc

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.io.Serializable

/**
 * @author Lars Ivar Hatledal
 */
data class SourceFile(

        /**
         * Name of the file including the path to the sources
         * directory, using forward slash as separator
         */
        @JacksonXmlProperty
        val name: String

) : Serializable


/**
 *
 * Defines whether the variable names in “ModelVariables /
 * ScalarVariable / name” and in “TypeDefinitions / Type /
 * name” follow a particular convention. For the details, see section 2.2.9.
 * Currently standardized are:
 * • “flat”: A list of strings (the default).
 * • “structured“: Hierarchical names with “.” as hierarchy separator,
 * and with array elements and derivative characterization.
 *
 * @author Lars Ivar Hatledal
 */
enum class VariableNamingConvention {

    FLAT,
    STRUCTURED

}