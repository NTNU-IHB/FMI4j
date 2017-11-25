package no.mechatronics.sfi.jna

import java.util.*
import kotlin.streams.toList

enum class Fmi2Status private constructor(val code: Int) {

    NONE(-1),
    OK(0),
    Warning(1),
    Discard(2),
    Error(3),
    Fatal(4),
    Pending(5);


    companion object {

        @JvmStatic
        fun valueOf(i: Int): Fmi2Status {
            for (status in values()) {
                if (i == status.code) {
                    return status
                }
            }
            throw IllegalArgumentException("$i not in range of ${Arrays.stream(values()).map { it.code }.toList()}")
        }
    }

}