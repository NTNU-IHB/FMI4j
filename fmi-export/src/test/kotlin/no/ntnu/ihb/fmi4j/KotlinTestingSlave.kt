package no.ntnu.ihb.fmi4j

class KotlinTestingSlave: Fmi2Slave() {

    @VariableContainer
    val container = TestContainer()

    override fun doStep(currentTime: Double, dt: Double): Boolean {
        return true
    }

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

