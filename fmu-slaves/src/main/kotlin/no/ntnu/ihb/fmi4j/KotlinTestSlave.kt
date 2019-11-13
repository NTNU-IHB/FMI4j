package no.ntnu.ihb.fmi4j

class KotlinTestSlave: Fmi2Slave() {

    @ScalarVariable
    private val speed: Double = 10.0

    @VariableContainer
    private val data = Data()

    override fun doStep(currentTime: Double, dt: Double): Boolean {
        return true
    }

}

class Data {

    @ScalarVariable
    val x: Double = 0.0

}
