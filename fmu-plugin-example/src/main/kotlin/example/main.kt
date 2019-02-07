package example

import no.ntnu.ihb.fmi4j.ControlledTemperature
import no.ntnu.ihb.fmi4j.modeldescription.variables.RealVariable

fun main() {

    ControlledTemperature.newInstance().use { slave ->

        slave.simpleSetup()

        //Variables are grouped by causality and have types!
        val tempRef: RealVariable
                = slave.outputs.temperature_Reference()

        val stop = 0.1
        val stepSize = 1E-4
        while (slave.simulationTime <= stop) {

            if (!slave.doStep(stepSize)) {
                break
            }

            tempRef.read(slave).also {
                println("t=${slave.simulationTime}, ${tempRef.name}=${it.value}")
            }

        }

    }

}