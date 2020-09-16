package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality

open class KotlinTestingFmi2Slave(
        args: Map<String, Any>
): Fmi2Slave(args) {


    var str: String? = null
    var start: Double = 5.0

    private val container = TestContainer()

    override fun registerVariables() {
        registerReal("start") {
            causality = Fmi2Causality.local
            getter { start }
        }
        registerReal("subModel_out") {
            getter { 99.0 }
        }
        registerReal("container.value") {
            getter {
                container.value
            }
        }
    }

    override fun setupExperiment(startTime: Double) {
        str = startTime.toString()
    }

    override fun doStep(currentTime: Double, dt: Double) {}

    fun getSubModel_out() = 99.0

}

open class KotlinTestingExtendingFmi2Slave(
        args: Map<String, Any>
): KotlinTestingFmi2Slave(args) {

    override fun doStep(currentTime: Double, dt: Double) {}

}

class TestContainer {

    val value: Double = 5.0
    val container2 = TestContainer2()

}

class TestContainer2 {

    val value2: Int = 1

}
