package no.mechatronics.sfi.jna


internal fun convert(fmi2Boolean: Byte): Boolean {
    return fmi2Boolean.toInt() != 0
}

internal fun convert(b: Boolean): Byte {
    return (if (b) 1 else 0).toByte()
}
