package no.mechatronics.sfi.fmi4j.common

import no.mechatronics.sfi.fmi4j.modeldescription.variables.Real

interface FmuRead<E> {

    val value: E
    val status: Fmi2Status

}

data class FmuReadImpl<E>(
        override val value: E,
        override val status: Fmi2Status
): FmuRead<E>