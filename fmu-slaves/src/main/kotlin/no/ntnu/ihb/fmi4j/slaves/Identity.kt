package no.ntnu.ihb.fmi4j.slaves

import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality

class Identity(
        args: Map<String, Any>
) : Fmi2Slave(args) {


    private var real: Double = 0.0
    private var integer: Int = 0
    private var boolean: Boolean = false
    private var string: String = ""

    override fun registerVariables() {
        register(real("real") { real }
                .causality(Fmi2Causality.output))
        register(integer("integer") { integer }
                .causality(Fmi2Causality.output))
        register(boolean("boolean") { boolean }
                .causality(Fmi2Causality.output))
        register(string("string") { string }
                .causality(Fmi2Causality.output))
    }

    override fun doStep(currentTime: Double, dt: Double) {
    }

}
