package no.ntnu.ihb.fmi4j.slaves

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave

class KotlinTestFmi2Slave(
        args: Map<String, Any>
) : Fmi2Slave(args) {

    private val data = Data()
    private var speed: Double = 10.0

    override fun registerVariables() {
        register(real("speed")
                .getter({ speed }))
        register(real("data.x")
                .getter({ data.x }))
    }

    override fun doStep(currentTime: Double, dt: Double) {
        speed = -1.0
    }

}

class Data {

    val x: Double = 0.0

}
