package no.ntnu.ihb.fmi4j.slaves

import no.ntnu.ihb.fmi4j.FmuBuilder
import no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2Causality
import java.io.File
import java.io.FileFilter

class Identity(
        args: Map<String, Any>
) : Fmi2Slave(args) {


    private var real: Double = 0.0
    private var integer: Int = 0
    private var boolean: Boolean = false
    private var string: String = ""

    override fun registerVariables() {
        register(real("real")
                .causality(Fmi2Causality.output)
                .getter({ real }))
        register(integer("integer")
                .causality(Fmi2Causality.output)
                .getter({ integer }))
        register(boolean("boolean")
                .causality(Fmi2Causality.output)
                .getter({ boolean }))
        register(string("string")
                .causality(Fmi2Causality.output)
                .getter { string })
    }

    override fun doStep(currentTime: Double, dt: Double) {
    }

}


fun main() {
    val group = "no.ntnu.ihb.fmi4j.slaves"
    val version = File("VERSION").readLines()[0]
    val dest = File("build/generated").absolutePath
    val jar = File("fmu-builder/build/libs").listFiles(FileFilter {
        it.name == "fmu-slaves-$version.jar"
    })?.get(0)?.absolutePath

    jar?.also {
        FmuBuilder.main(arrayOf("-f", it, "-m", "${group}.Identity", "-d", dest))
    }
}
