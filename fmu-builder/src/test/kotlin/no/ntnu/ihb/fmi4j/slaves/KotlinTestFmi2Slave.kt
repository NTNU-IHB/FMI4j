package no.ntnu.ihb.fmi4j.slaves

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable
import no.ntnu.ihb.fmi4j.export.fmi2.VariableContainer

class KotlinTestFmi2Slave(
        args: Map<String, Any>
): Fmi2Slave(args) {

    @ScalarVariable
    private var speed: Double = 10.0

    @VariableContainer
    private val data = Data()

    override fun doStep(currentTime: Double, dt: Double) {
        speed = -1.0
    }

}

class Data {

    @ScalarVariable
    val x: Double = 0.0

}
