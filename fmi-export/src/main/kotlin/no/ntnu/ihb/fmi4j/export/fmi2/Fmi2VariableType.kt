package no.ntnu.ihb.fmi4j.export.fmi2

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ScalarVariable

enum class Fmi2VariableType {
    INTEGER,
    REAL,
    BOOLEAN,
    STRING,
    ENUMERATION
}

internal fun Fmi2ScalarVariable.type(): Fmi2VariableType {
    return when {
        integer != null -> Fmi2VariableType.INTEGER
        real != null -> Fmi2VariableType.REAL
        boolean != null -> Fmi2VariableType.BOOLEAN
        string != null -> Fmi2VariableType.STRING
        enumeration != null -> Fmi2VariableType.ENUMERATION
        else -> throw IllegalStateException()
    }
}
