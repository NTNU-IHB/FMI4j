package no.ntnu.ihb.fmu2jar

val TEST_FMUs: String
    get() = System.getenv("TEST_FMUs")
            ?: throw IllegalStateException("TEST_FMUs not found on PATH!")
