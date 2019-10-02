package no.ntnu.ihb.fmi4j

class KotlinTestSlave: Fmi2Slave() {

    @ScalarVariable
    private val speed: Double = 10.0

    override fun doStep(currentTime: Double, dt: Double): Boolean {
        return true
    }

}
