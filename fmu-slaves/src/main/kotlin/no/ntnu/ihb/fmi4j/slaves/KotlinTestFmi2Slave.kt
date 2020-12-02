package no.ntnu.ihb.fmi4j.slaves

import no.ntnu.ihb.fmi4j.export.BulkRead
import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave
import no.ntnu.ihb.fmi4j.export.fmi2.ScalarVariable
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality

class KotlinTestFmi2Slave(
        args: Map<String, Any>
) : Fmi2Slave(args) {

    private val data = Data()

    @ScalarVariable
    private var speed: Double = 10.0

    @ScalarVariable(causality = Fmi2Causality.local)
    private var getAllInvoked: Boolean = false

    override fun registerVariables() {
        register(real("data.x") { data.x })
    }

    override fun doStep(currentTime: Double, dt: Double) {
        speed = -1.0
    }

    override fun getAll(intVr: LongArray, realVr: LongArray, boolVr: LongArray, strVr: LongArray): BulkRead {
        return super.getAll(intVr, realVr, boolVr, strVr).also {
            getAllInvoked = true
        }
    }

    data class Data(
        val x: Double = 0.0
    )

}

