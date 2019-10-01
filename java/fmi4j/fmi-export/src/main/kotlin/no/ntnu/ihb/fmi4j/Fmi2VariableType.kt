package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ScalarVariable

enum class Fmi2VariableType {

    REAL,
    INTEGER,
    BOOLEAN,
    STRING,
    ENUMERATION;

    companion object {

        @JvmStatic
        fun getType(v: Fmi2ScalarVariable): Fmi2VariableType {
            if (v.real != null) return Fmi2VariableType.REAL
            if (v.integer != null) return Fmi2VariableType.INTEGER
            if (v.boolean != null) return Fmi2VariableType.BOOLEAN
            if (v.string != null) return Fmi2VariableType.STRING
            if (v.enumeration != null) return Fmi2VariableType.ENUMERATION
            throw AssertionError()
        }

    }

}
