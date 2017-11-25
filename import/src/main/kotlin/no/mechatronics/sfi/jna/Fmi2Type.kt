package no.mechatronics.sfi.jna


enum class Fmi2Type private constructor(val id: Int) {

    ModelExchange(0),
    CoSimulation(1)

}

