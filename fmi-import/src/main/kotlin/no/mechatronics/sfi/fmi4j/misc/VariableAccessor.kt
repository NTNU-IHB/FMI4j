package no.mechatronics.sfi.fmi4j.misc

import no.mechatronics.sfi.fmi4j.proxy.Fmi2LibraryWrapper

abstract class VariableAccessor internal constructor(
        protected val wrapper: Fmi2LibraryWrapper<*>,
        protected val valueReference: Int
)
