package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.export.fmi2.Slave
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable
import no.ntnu.ihb.fmi4j.export.fmi2.VariableContainer

class KotlinTestingSlave(
        instanceName: String
): Slave(instanceName) {

    @VariableContainer
    val container = TestContainer()

    override fun doStep(currentTime: Double, dt: Double) {}

}


class TestContainer {

    @ScalarVariable
    val value: Double = 5.0

    @VariableContainer
    val container2 = TestContainer2()

}

class TestContainer2 {

    @ScalarVariable
    val value2: Int = 1

}

