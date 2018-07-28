package no.mechatronics.sfi.fmu2jar.util

import org.junit.jupiter.api.condition.OS

val TEST_FMUs: String
    get() = System.getenv("TEST_FMUs")
            ?: throw IllegalStateException("TEST_FMUs not found on PATH!")


val currentOS: String
get() = when {
    OS.LINUX.isCurrentOs -> "linux64"
    OS.WINDOWS.isCurrentOs -> "win64"
    OS.MAC.isCurrentOs -> "darwin64"
    else -> throw IllegalStateException("Unsupported OS")
}


