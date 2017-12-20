package no.mechatronics.sfi.fmi4j.proxy.me


data class CompletedIntegratorStep(
        val enterEventMode: Boolean,
        val terminateSimulation: Boolean
)