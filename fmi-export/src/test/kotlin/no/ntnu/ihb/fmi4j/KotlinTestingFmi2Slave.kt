package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariableGetter
import no.ntnu.ihb.fmi4j.export.fmi2.VariableContainer
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Variability

open class KotlinTestingFmi2Slave(
        args: Map<String, Any>
): Fmi2Slave(args) {

    @VariableContainer
    val container = TestContainer()

    @ScalarVariable
    var str: String? = null

    @ScalarVariable(causality = Fmi2Causality.parameter, variability = Fmi2Variability.constant)
    var start: Double = 5.0

    override fun setupExperiment(startTime: Double) {
        str = startTime.toString()
    }

    override fun doStep(currentTime: Double, dt: Double) {}

    @ScalarVariableGetter(causality = Fmi2Causality.output)
    fun getSubModel_out() = 99.0

}

open class KotlinTestingExtendingFmi2Slave(
        args: Map<String, Any>
): KotlinTestingFmi2Slave(args) {

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
