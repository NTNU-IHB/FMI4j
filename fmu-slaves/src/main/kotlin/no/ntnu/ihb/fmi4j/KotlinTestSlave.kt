package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.export.fmi2.Slave
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable
import no.ntnu.ihb.fmi4j.export.fmi2.VariableContainer

class KotlinTestSlave(
        instanceName: String
): Slave(instanceName) {

    @ScalarVariable
    private val speed: Double = 10.0

    @VariableContainer
    private val data = Data()

    override fun doStep(currentTime: Double, dt: Double) {
    }

}

class Data {

    @ScalarVariable
    val x: Double = 0.0

}
