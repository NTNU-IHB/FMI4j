package no.mechatronics.sfi.fmi4j.crosscheck

internal val TEST_FMUs = System.getenv("TEST_FMUs") ?: throw IllegalStateException("TEST_FMUs not found on PATH!")