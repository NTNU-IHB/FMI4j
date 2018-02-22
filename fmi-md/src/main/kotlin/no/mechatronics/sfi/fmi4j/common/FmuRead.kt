package no.mechatronics.sfi.fmi4j.common

interface FmuRead<E> {

    val value: E
    val status: FmiStatus

}

data class FmuReadImpl<E>(
        override val value: E,
        override val status: FmiStatus
): FmuRead<E>