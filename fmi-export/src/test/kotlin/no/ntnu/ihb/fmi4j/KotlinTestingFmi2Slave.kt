package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable
import no.ntnu.ihb.fmi4j.export.fmi2.VariableContainer

class KotlinTestingFmi2Slave(
        instanceName: String
): Fmi2Slave(instanceName) {

    @VariableContainer
    val container = TestContainer()

    @ScalarVariable
    var str: String? = null

    override fun setupExperiment(startTime: Double) {
        str = startTime.toString()
    }

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

