package no.sfi.mechatronics.fmi4j.jna


enum class Fmi2Type private constructor(val id: Int) {

    ModelExchange(0),
    CoSimulation(1)

}

